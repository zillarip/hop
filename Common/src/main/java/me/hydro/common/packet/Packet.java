package me.hydro.common.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.hydro.common.Redis;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Packet {

    private final Redis redis;

    public abstract void send();

}
