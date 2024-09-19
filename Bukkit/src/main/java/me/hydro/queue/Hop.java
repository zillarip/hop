package me.hydro.queue;

import lombok.Getter;
import me.hydro.queue.api.ManagerHandle;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.impl.QueueManager;
import me.hydro.queue.common.Redis;
import me.hydro.queue.common.file.HopFile;
import me.hydro.queue.common.misc.Logger;
import me.hydro.queue.common.packet.impl.StatusPacket;
import me.hydro.queue.command.JoinQueueCommand;
import me.hydro.queue.command.LeaveQueueCommand;
import me.hydro.queue.command.QueueCommand;
import me.hydro.queue.listeners.MessageListener;
import me.hydro.queue.listeners.PlayerListeners;
import me.hydro.queue.api.Queue;
import me.hydro.queue.misc.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class Hop extends JavaPlugin {

    @Getter
    private static Hop instance;

    private HopFile settings, queues;
    private Redis redis;

    private boolean send = true;

    private final List<String> receivedResponse = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        this.settings = new HopFile(this, "settings");
        this.queues = new HopFile(this, "queues");

        this.redis = new Redis(this.settings.getConfig().getString("redis.host"),
                this.settings.getConfig().getInt("redis.port"),
                this.settings.getConfig().getBoolean("redis.auth.enabled"),
                this.settings.getConfig().getString("redis.auth.password"));

        ManagerHandle.setImplementation(new QueueManager());

        for (final String queueId : this.queues.getConfig().getConfigurationSection("queues").getKeys(false)) {
            final boolean restricted = this.queues.getConfig().getBoolean("queues." + queueId + ".restricted");

            final String display = this.queues.getConfig().getString("queues." + queueId + ".display");
            final String bungee = this.queues.getConfig().getString("queues." + queueId + ".bungee");

            final Queue queue = new Queue(queueId, display, bungee, restricted);

            Queue.getQueues().put(queueId, queue);
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
            if (this.send) {
                for (final Queue queue : Queue.getQueues().values()) {
                    new StatusPacket(redis, queue.getId()).send();
                }
            } else {
                for (final Queue queue : Queue.getQueues().values()) {
                    if (!this.receivedResponse.contains(queue.getId())) {
                        for (final UUID uuid : queue.getQueued()) {
                            final Player player = Bukkit.getPlayer(uuid);
                            final PlayerData data = PlayerData.getPlayers().get(uuid);

                            final int pos = ManagerHandle.getImplementation().getPlayerPos(data);
                            final int size = ManagerHandle.getImplementation().getQueued(data).getQueued().size();

                            Messages.reminderFail(player, "offline", pos + 1, size);
                        }
                    }
                }

                this.receivedResponse.clear();
            }

            this.send = !this.send;
        }, 0L, this.settings.getConfig().getInt("interval") * 10L);

        Logger.success("Hop is ready!");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.redis.getPool().close();

        Logger.info("Disabled Hop.");
    }
}
