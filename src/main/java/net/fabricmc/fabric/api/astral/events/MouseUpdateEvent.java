package net.fabricmc.fabric.api.astral.events;

public class MouseUpdateEvent {
    private static final MouseUpdateEvent INSTANCE = new MouseUpdateEvent();

    public static MouseUpdateEvent get() {
        return INSTANCE;
    }
}
