package me.hydro.queue.listeners;

import me.hydro.queue.api.ManagerHandle;
import me.hydro.queue.common.event.RedisMessageEvent;
import me.hydro.queue.Hop;
import me.hydro.queue.impl.QueueManager;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
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
    public void onRedisMessage(final RedisMessageEvent event) {
        final String channel = event.getChannel();
        final String message = event.getMessage();

        switch (channel) {
            case "ALLOW": {
                final Queue queue = Queue.getQueues().get(message);

                if (queue.getQueued().isEmpty()) return;

                PlayerData firstCandidate;

                do {
                    if (queue.getQueued().isEmpty()) return;

                    firstCandidate = PlayerData.getPlayers().get(queue.getQueued().getFirst());

                    if (firstCandidate.getAttempts() >= Hop.getInstance().getSettings().getConfig().getInt("max-send-attempts")) {
                        firstCandidate.getPlayer().sendMessage(Messages.failure(queue.getName()));
                        ManagerHandle.getImplementation().removeFromQueue(firstCandidate);

                        firstCandidate = null;
                    }
                } while (firstCandidate == null);

                final PlayerData first = firstCandidate;

                queue.getQueued().parallelStream().map(uuid -> PlayerData.getPlayers().get(uuid)).forEach(data -> {
                    if (data != first) {
                        int pos = ManagerHandle.getImplementation().getPlayerPos(data);
                        int size = ManagerHandle.getImplementation().getQueued(data).getQueued().size();

                        Messages.reminder(data.getPlayer(), pos + 1, size);
                    }
                });

                first.setAttempts(first.getAttempts() + 1);

                sendToServer(queue, first);
                acknowledged(message);

                break;
            }
            case "DENY": {
                final String[] split = message.split("@@");

                final String server = split[0];
                final String reason = split[1];

                final Queue queue = Queue.getQueues().get(server);

                if (queue.getQueued().isEmpty()) return;

                switch (reason) {
                    case "MAX": {
                        queue.getQueued().parallelStream().map(uuid -> PlayerData.getPlayers().get(uuid)).forEach(data -> {
                            final int pos = ManagerHandle.getImplementation().getPlayerPos(data);
                            final int size = ManagerHandle.getImplementation().getQueued(data).getQueued().size();

                            Messages.reminderFail(data.getPlayer(), "max", pos + 1, size);
                        });

                        break;
                    }
                    case "WHITELISTED": {
                        final List<String> whitelisted = Arrays.asList(split[2].split(","));

                        queue.getQueued().parallelStream().map(uuid -> PlayerData.getPlayers().get(uuid)).forEach(data -> {
                            if (whitelisted.contains(data.getPlayer().getUniqueId().toString())) {
                                // This is not the greatest fix, but it *should* work
                                // Theoretically, there won't be that many whitelisted players all
                                // in a queue.
                                sendToServer(queue, data);
                            }

                            final int pos = ManagerHandle.getImplementation().getPlayerPos(data);
                            final int size = ManagerHandle.getImplementation().getQueued(data).getQueued().size();

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

        player.getPlayer().sendPluginMessage(Hop.getInstance(), "BungeeCord", b.toByteArray());
        player.getPlayer().sendMessage(Messages.sending(queue.getName()));
    }

    private void acknowledged(final String server) {
        Hop.getInstance().getReceivedResponse().add(server);
    }
}
