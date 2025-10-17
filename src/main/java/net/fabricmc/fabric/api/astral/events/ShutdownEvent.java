package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class ShutdownEvent
extends Cancellable {
    private static final ShutdownEvent INSTANCE = new ShutdownEvent();

    public static ShutdownEvent get() {
        return INSTANCE;
    }

    private ShutdownEvent() {
    }
}
