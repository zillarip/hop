package me.hydro.queue.listeners;

import lombok.SneakyThrows;
import me.hydro.queue.api.ManagerHandle;
import me.hydro.queue.api.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final PlayerData data = new PlayerData(event.getPlayer());

        PlayerData.getPlayers().put(event.getPlayer().getUniqueId(), data);
    }

    @EventHandler
    @SneakyThrows
    public void onLeave(PlayerQuitEvent event) {
        final PlayerData data = PlayerData.getPlayers().get(event.getPlayer().getUniqueId());

        if (ManagerHandle.getImplementation().isQueued(data)) {
            ManagerHandle.getImplementation().getQueued(data).getQueued().remove(event.getPlayer().getUniqueId());
        }

        PlayerData.getPlayers().remove(event.getPlayer().getUniqueId());
    }
}
