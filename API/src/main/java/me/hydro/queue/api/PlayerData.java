package me.hydro.queue.api;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Data
public class PlayerData {

    @Getter
    private static HashMap<UUID, PlayerData> players = new HashMap<>();

    private final Player player;

    private String queuedFor;

    private int attempts;
}
