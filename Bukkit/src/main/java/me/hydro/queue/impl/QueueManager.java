package me.hydro.queue.impl;

import me.hydro.queue.api.IQueueManager;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
import org.bukkit.entity.Player;

public class QueueManager implements IQueueManager {

    public boolean isQueued(PlayerData data) {
        return data.getQueuedFor() != null;
    }

    public Queue getQueued(PlayerData data) {
        return getQueue(data.getQueuedFor());
    }

    public int getPlayerPos(PlayerData data) {
        return getQueued(data).getQueued().indexOf(data.getPlayer().getUniqueId());
    }

    public void addToQueue(PlayerData data, String queueName, int position) {
        final Queue queue = Queue.getQueues().get(queueName);

        if (position == -1) {
            queue.getQueued().add(data.getPlayer().getUniqueId());
        } else {
            queue.getQueued().add(position, data.getPlayer().getUniqueId());
        }
    }

    public void removeFromQueue(PlayerData data) {
        final Queue queue = getQueued(data);

        queue.getQueued().remove(data.getPlayer().getUniqueId());
        data.setQueuedFor(null);
    }

    public PlayerData getPlayer(Player player) {
        return PlayerData.getPlayers().get(player.getUniqueId());
    }

    public Queue getQueue(String queueName) {
        return Queue.getQueues().get(queueName);
    }
}
