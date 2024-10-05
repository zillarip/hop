package me.hydro.queue.api;

import lombok.Data;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

@Data
public class Queue {

    @Getter
    private static HashMap<String, Queue> queues = new HashMap<>();

    private final String id, name, bungee;

    private final boolean restricted;

    private LinkedList<UUID> queued = new LinkedList<>();

    public boolean hasPermission(final CommandSender player) {
        if (!this.restricted) {
            return true;
        }

        return player.hasPermission("queue.join." + id);
    }
}
