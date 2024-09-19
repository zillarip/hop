package me.hydro.queue.common.packet.impl;

import me.hydro.queue.common.Redis;
import me.hydro.queue.common.packet.Packet;

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
