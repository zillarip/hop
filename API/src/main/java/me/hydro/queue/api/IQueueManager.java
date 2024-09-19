package me.hydro.queue.api;

import org.bukkit.entity.Player;

public interface IQueueManager {

    boolean isQueued(PlayerData data);

    Queue getQueued(PlayerData data);

    int getPlayerPos(PlayerData data);

    void addToQueue(PlayerData data, String queueName, int position);

    void removeFromQueue(PlayerData data);

    PlayerData getPlayer(Player player);

    Queue getQueue(String queueName);
}
