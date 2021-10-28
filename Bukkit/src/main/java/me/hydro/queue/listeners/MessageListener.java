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

public class MessageListener implements Listener {

    @EventHandler
    public void onRedisMessage(RedisMessageEvent event) {
        String channel = event.getChannel();
        String message = event.getMessage();

        switch (channel) {
            case "ALLOW": {
                String server = message;
                Queue queue = Queue.queues.stream().filter(q -> q.getId().equals(server)).findFirst().get();

                if (queue.getQueued().size() == 0) return;
                PlayerData first = PlayerData.players.get(queue.getQueued().getFirst());

                queue.getQueued().parallelStream().map(uuid -> PlayerData.players.get(uuid)).forEach(data -> {
                    if (data != first) {
                        int pos = QueueManager.getPlayerPos(data);
                        int size = QueueManager.getQueued(data).getQueued().size();

                        Messages.reminder(data.getPlayer(), pos + 1, size);
                    }
                });

                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);

                try {
                    out.writeUTF("Connect");
                    out.writeUTF(queue.getBungee());
                } catch (IOException eee) {
                    Bukkit.getLogger().info("You'll never see me!");
                }

                first.getPlayer().sendPluginMessage(HydroQueue.getInstance(), "BungeeCord", b.toByteArray());
                first.getPlayer().sendMessage(Messages.sending(queue.getName()));

                acknowledged(server);
                break;
            }
            case "DENY": {
                String[] split = message.split("@@");
                String server = split[0];
                String reason = split[1];

                Queue queue = Queue.queues.stream().filter(q -> q.getId().equals(server)).findFirst().get();
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
                        queue.getQueued().parallelStream().map(uuid -> PlayerData.players.get(uuid)).forEach(data -> {
                            int pos = QueueManager.getPlayerPos(data);
                            int size = QueueManager.getQueued(data).getQueued().size();

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

    private void acknowledged(String server) {
        HydroQueue.getInstance().getReceivedResponse().add(server);
    }
}
