package me.hydro.common.redis.packet.impl;

import me.hydro.common.redis.Redis;
import me.hydro.common.redis.packet.Packet;

public class AllowPacket extends Packet {

    private String server;

    public AllowPacket(Redis redis, String server) {
        super(redis);
        this.server = server;
    }

    @Override
    public void send() {
        getRedis().message("ALLOW", server);
    }
}
