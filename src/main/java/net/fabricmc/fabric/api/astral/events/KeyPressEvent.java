package net.fabricmc.fabric.api.astral.events;

public class KeyPressEvent {
    private static final KeyPressEvent INSTANCE = new KeyPressEvent();
    public int key;
    public int scanCode;
    public int action;
    public long window;

    public static KeyPressEvent get(int key, int scanCode, int action, long window) {
        KeyPressEvent.INSTANCE.key = key;
        KeyPressEvent.INSTANCE.scanCode = scanCode;
        KeyPressEvent.INSTANCE.action = action;
        KeyPressEvent.INSTANCE.window = window;
        return INSTANCE;
    }
}
