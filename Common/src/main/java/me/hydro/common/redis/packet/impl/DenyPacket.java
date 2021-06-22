package me.hydro.common.redis.packet.impl;

import me.hydro.common.redis.Redis;
import me.hydro.common.redis.packet.Packet;

public class DenyPacket extends Packet {

    private String server, reason;

    public DenyPacket(Redis redis, String server, String reason) {
        super(redis);
        this.server = server;
        this.reason = reason;
    }

    @Override
    public void send() {
        getRedis().message("DENY", server + "@@" + reason);
    }
}
