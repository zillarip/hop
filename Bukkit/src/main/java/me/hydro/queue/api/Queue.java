package me.hydro.queue.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class Queue {

    public static HashSet<Queue> queues = new HashSet<>();

    private final String id, name, bungee;
    private final boolean restricted;

    private LinkedList<UUID> queued = new LinkedList<>();

    public boolean hasPermission(CommandSender player) {
        if (!restricted) return true;
        return player.hasPermission("queue.join." + id);
    }
}
