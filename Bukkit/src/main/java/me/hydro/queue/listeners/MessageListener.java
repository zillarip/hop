package me.hydro.queue.listeners;

import me.hydro.common.event.RedisMessageEvent;
import me.hydro.queue.HydroQueue;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
import me.hydro.queue.api.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MessageListener implements Listener {

    @EventHandler
    public void onRedisMessage(RedisMessageEvent event) {
        final String channel = event.getChannel();
        final String message = event.getMessage();

        switch (channel) {
            case "ALLOW": {
                final String server = message;
                final Queue queue = Queue.queues.stream().filter(q -> q.getId().equals(server)).findFirst().get();

                if (queue.getQueued().size() == 0) return;

                final PlayerData first = PlayerData.players.get(queue.getQueued().getFirst());

                queue.getQueued().parallelStream().map(uuid -> PlayerData.players.get(uuid)).forEach(data -> {
                    if (data != first) {
                        int pos = QueueManager.getPlayerPos(data);
                        int size = QueueManager.getQueued(data).getQueued().size();

                        Messages.reminder(data.getPlayer(), pos + 1, size);
                    }
                });

                sendToServer(queue, first);
                acknowledged(server);

                break;
            }
            case "DENY": {
                final String[] split = message.split("@@");

                final String server = split[0];
                final String reason = split[1];

                final Queue queue = Queue.queues.stream().filter(q -> q.getId().equals(server)).findFirst().get();
                if (queue.getQueued().size() == 0) return;

                switch (reason) {
                    case "MAX": {
                        queue.getQueued().parallelStream().map(uuid -> PlayerData.players.get(uuid)).forEach(data -> {
                            int pos = QueueManager.getPlayerPos(data);
                            int size = QueueManager.getQueued(data).getQueued().size();

                            Messages.reminderFail(data.getPlayer(), "max", pos + 1, size);
                        });

                        break;
                    }
                    case "WHITELISTED": {
                        final List<String> whitelisted = Arrays.asList(split[2].split(","));

                        queue.getQueued().parallelStream().map(uuid -> PlayerData.players.get(uuid)).forEach(data -> {
                            if (whitelisted.contains(data.getPlayer().getUniqueId().toString())) {
                                // This is not the greatest fix, but it *should* work
                                // Theoretically, there won't be that many whitelisted players all
                                // in a queue.
                                sendToServer(queue, data);
                            }

                            final int pos = QueueManager.getPlayerPos(data);
                            final int size = QueueManager.getQueued(data).getQueued().size();

                            Messages.reminderFail(data.getPlayer(), "whitelisted", pos + 1, size);
                        });

                        break;
                    }
                }

                acknowledged(server);

                break;
            }
        }
    }

    private void sendToServer(final Queue queue, final PlayerData player) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(queue.getBungee());
        } catch (IOException eee) {
            Bukkit.getLogger().info("You'll never see me!");
        }

        player.getPlayer().sendPluginMessage(HydroQueue.getInstance(), "BungeeCord", b.toByteArray());
        player.getPlayer().sendMessage(Messages.sending(queue.getName()));
    }

    private void acknowledged(String server) {
        HydroQueue.getInstance().getReceivedResponse().add(server);
    }
}
