package me.hydro.common.redis.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.hydro.common.redis.Redis;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Packet {

    private final Redis redis;

    public abstract void send();

}
