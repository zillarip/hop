package me.hydro.queue.api;

import org.bukkit.entity.Player;

import java.util.UUID;

public class QueueManager {

    public static boolean isQueued(PlayerData data) {
        UUID uuid = data.getPlayer().getUniqueId();
        final boolean[] queued = { false };

        Queue.queues.forEach(queue -> {
            if (queue.getQueued().contains(uuid)) queued[0] = true;
        });

        return queued[0];
    }

    public static Queue getQueued(PlayerData data) {
        UUID uuid = data.getPlayer().getUniqueId();
        final Queue[] queued = {null};

        Queue.queues.forEach(queue -> {
            if (queue.getQueued().contains(uuid)) queued[0] = queue;
        });

        return queued[0];
    }

    public static int getPlayerPos(PlayerData data) {
        return getQueued(data).getQueued().indexOf(data.getPlayer().getUniqueId());
    }

    public static void addToQueue(PlayerData data, String queueName, int position) {
        Queue queue = Queue.queues.stream().filter(q -> q.getId().equals(queueName)).findFirst().get();
        if (position == -1) {
            queue.getQueued().add(data.getPlayer().getUniqueId());
        } else queue.getQueued().add(position, data.getPlayer().getUniqueId());
    }

    public static void removeFromQueue(PlayerData data) {
        Queue queue = getQueued(data);
        queue.getQueued().remove(data.getPlayer().getUniqueId());
    }

    public static PlayerData getPlayer(Player player) {
        return PlayerData.players.get(player.getUniqueId());
    }

    public static Queue getQueue(String queueName) {
        return Queue.queues.stream().filter(q -> q.getId().equals(queueName)).findFirst().get();
    }
}
