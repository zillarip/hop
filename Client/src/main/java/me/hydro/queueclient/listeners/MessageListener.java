package me.hydro.queueclient.listeners;

import me.hydro.common.redis.event.RedisMessageEvent;
import me.hydro.common.redis.misc.Logger;
import me.hydro.common.redis.packet.impl.AllowPacket;
import me.hydro.common.redis.packet.impl.DenyPacket;
import me.hydro.queueclient.QueueClient;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

public class MessageListener implements Listener {

    @EventHandler
    public void onRedisMessage(RedisMessageEvent event) {
        String channel = event.getChannel();
        String message = event.getMessage();

        switch (channel) {
            case "STATUS": {
                String server = message;
                if (server.equals(QueueClient.getInstance().getRepresenting())) {
                    if (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                        new DenyPacket(QueueClient.getInstance().getRedis(), server, "MAX").send();
                        return;
                    }

                    if (Bukkit.hasWhitelist()) {
                        List<String> whitelistedList = Bukkit.getWhitelistedPlayers().stream().map(p -> p.getName())
                                .collect(Collectors.toList());

                        String whitelisted = String.join(",", whitelistedList);
                        new DenyPacket(QueueClient.getInstance().getRedis(), server, "WHITELISTED@@").send();

                        return;
                    }

                    new AllowPacket(QueueClient.getInstance().getRedis(), server).send();
                }

                break;
            }
            default: {
                Logger.warn("Received unknown Redis packet. Is QueueClient up to date?");
            }
        }
    }

}
