package me.hydro.queue.common.packet.impl;

import me.hydro.queue.common.Redis;
import me.hydro.queue.common.packet.Packet;

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
