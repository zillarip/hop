package me.hydro.common.redis.packet.impl;

import me.hydro.common.redis.Redis;
import me.hydro.common.redis.packet.Packet;

public class StatusPacket extends Packet {

    private String server;

    public StatusPacket(Redis redis, String server) {
        super(redis);
        this.server = server;
    }

    @Override
    public void send() {
        getRedis().message("STATUS", server);
    }
}
