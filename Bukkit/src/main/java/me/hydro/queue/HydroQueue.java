package me.hydro.queue;

import lombok.Getter;
import me.hydro.common.Redis;
import me.hydro.common.file.HydroFile;
import me.hydro.common.misc.Logger;
import me.hydro.common.packet.impl.StatusPacket;
import me.hydro.queue.command.JoinQueueCommand;
import me.hydro.queue.command.LeaveQueueCommand;
import me.hydro.queue.command.QueueCommand;
import me.hydro.queue.listeners.MessageListener;
import me.hydro.queue.listeners.PlayerListeners;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
import me.hydro.queue.api.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class HydroQueue extends JavaPlugin {

    @Getter
    private static HydroQueue instance;

    private HydroFile settings, queues;
    private Redis redis;

    private List<String> receivedResponse = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        settings = new HydroFile(this, "settings");
        queues = new HydroFile(this, "queues");

        redis = new Redis(this.settings.getConfig().getString("redis.host"),
                this.settings.getConfig().getInt("redis.port"),
                this.settings.getConfig().getBoolean("redis.auth.enabled"),
                this.settings.getConfig().getString("redis.auth.password"));

        for (String queueId : queues.getConfig().getConfigurationSection("queues").getKeys(false)) {
            final boolean restricted = queues.getConfig().getBoolean("queues." + queueId + ".restricted");

            final String display = queues.getConfig().getString("queues." + queueId + ".display");
            final String bungee = queues.getConfig().getString("queues." + queueId + ".bungee");

            final Queue queue = new Queue(queueId, display, bungee, restricted);
            Queue.queues.add(queue);
        }

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getPluginManager().registerEvents(new MessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);

        getCommand("joinqueue").setExecutor(new JoinQueueCommand());
        getCommand("joinqueue").setTabCompleter(new JoinQueueCommand());

        getCommand("leavequeue").setExecutor(new LeaveQueueCommand());
        getCommand("leavequeue").setTabCompleter(new LeaveQueueCommand());

        getCommand("queue").setExecutor(new QueueCommand());
        getCommand("queue").setTabCompleter(new QueueCommand());

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Queue queue : Queue.queues) {
                new StatusPacket(redis, queue.getId()).send();

                Bukkit.getScheduler().runTaskLater(HydroQueue.getInstance(), () -> {
                    if (!receivedResponse.contains(queue.getId())) {
                        for (final UUID uuid : queue.getQueued()) {
                            final Player player = Bukkit.getPlayer(uuid);
                            final PlayerData data = PlayerData.players.get(uuid);

                            final int pos = QueueManager.getPlayerPos(data);
                            final int size = QueueManager.getQueued(data).getQueued().size();

                            Messages.reminderFail(player, "offline", pos + 1, size);
                        }
                    } else receivedResponse.remove(queue.getId());
                }, 10L);
            }
        }, 0L, (settings.getConfig().getInt("interval") * 20L) + 15L);

        Logger.success("HydroQueue is ready to go!");
    }

    @Override
    public void onDisable() {
        instance = null;
        redis.getPool().close();

        Logger.info("Disabled HydroQueue.");
    }
}
