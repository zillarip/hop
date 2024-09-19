package me.hydro.queue.common.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.hydro.queue.common.Redis;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Packet {

    private final Redis redis;

    public abstract void send();

}
