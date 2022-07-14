package me.hydro.common.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class RedisMessageEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final String channel, message;

    public RedisMessageEvent(String channel, String message) {
        super(true);

        this.channel = channel;
        this.message = message;
    }
}
