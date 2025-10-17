package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class EventUpdate
extends Cancellable {
    private static final EventUpdate instance = new EventUpdate();

    public static EventUpdate get() {
        instance.setCancelled(false);
        return instance;
    }
}
