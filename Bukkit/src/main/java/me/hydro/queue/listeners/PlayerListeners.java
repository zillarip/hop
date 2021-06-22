package me.hydro.queue.listeners;

import lombok.SneakyThrows;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.QueueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new PlayerData(event.getPlayer());
    }

    @EventHandler
    @SneakyThrows
    public void onLeave(PlayerQuitEvent event) {
        PlayerData data = PlayerData.players.get(event.getPlayer().getUniqueId());
        if (QueueManager.isQueued(data))
            QueueManager.getQueued(data).getQueued().remove(event.getPlayer().getUniqueId());

        PlayerData.players.remove(event.getPlayer().getUniqueId());
    }
}
