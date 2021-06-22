package me.hydro.queue.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    public static HashMap<UUID, PlayerData> players = new HashMap<>();

    private final Player player;

    private String queuedFor;
    private int errorBuffer = 0;

    public PlayerData(Player bukkitPlayer) {
        player = bukkitPlayer;
        players.put(bukkitPlayer.getUniqueId(), this);
    }
}
