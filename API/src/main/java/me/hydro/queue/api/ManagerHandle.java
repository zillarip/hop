package me.hydro.queue.api;

import lombok.Getter;
import lombok.Setter;

public class ManagerHandle {

    @Getter
    @Setter
    private static IQueueManager implementation;
}
