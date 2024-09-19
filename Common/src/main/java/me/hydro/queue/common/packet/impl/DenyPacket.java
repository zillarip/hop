package me.hydro.queue.common.packet.impl;

import me.hydro.queue.common.Redis;
import me.hydro.queue.common.packet.Packet;

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
