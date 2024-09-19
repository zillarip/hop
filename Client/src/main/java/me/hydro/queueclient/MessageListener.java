package me.hydro.queueclient;

import me.hydro.queue.common.event.RedisMessageEvent;
import me.hydro.queue.common.packet.impl.AllowPacket;
import me.hydro.queue.common.packet.impl.DenyPacket;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

public class MessageListener implements Listener {

    @EventHandler
    public void onRedisMessage(RedisMessageEvent event) {
        final String channel = event.getChannel();
        final String server = event.getMessage();

        if (channel.equals("STATUS")) {
            if (server.equals(QueueClient.getInstance().getRepresenting())) {
                if (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                    new DenyPacket(QueueClient.getInstance().getRedis(), server, "MAX").send();
                    return;
                }

                if (Bukkit.hasWhitelist()) {
                    final List<String> whitelistedList = Bukkit.getWhitelistedPlayers().stream()
                            .map(p -> p.getUniqueId().toString())
                            .collect(Collectors.toList());

                    String whitelisted = String.join(",", whitelistedList);
                    new DenyPacket(QueueClient.getInstance().getRedis(), server, "WHITELISTED@@" + whitelisted).send();

                    return;
                }

                new AllowPacket(QueueClient.getInstance().getRedis(), server).send();
            }
        }
    }
}
