package net.fabricmc.fabric.utils.key;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.mixin.IKeyboardAccessorMixin;

public class KeyboardSimulation {
    private final HashMap<Integer, Integer> keyStates = new HashMap();

    public boolean isFakeKeyPressed(int keyCode) {
        return this.keyStates.containsKey(keyCode);
    }

    public IKeyboardAccessorMixin getKeyboard() {
        return (IKeyboardAccessorMixin)ClientMain.mc.keyboard;
    }

    public void keyPress(int keyCode, int frames) {
        if (!this.isFakeKeyPressed(keyCode)) {
            this.keyStates.put(keyCode, frames);
            this.getKeyboard().invokeOnKey(ClientMain.mc.getWindow().getHandle(), keyCode, 0, 1, 0);
        }
    }

    public void keyPress(int keyCode) {
        this.keyPress(keyCode, 10);
    }

    public void keyRelease(int keyCode) {
        if (this.isFakeKeyPressed(keyCode)) {
            this.getKeyboard().invokeOnKey(ClientMain.mc.getWindow().getHandle(), keyCode, 0, keyCode, 0);
            this.keyStates.remove(keyCode);
        }
    }

    private void checkKeys() {
        Iterator<Map.Entry<Integer, Integer>> iterator = this.keyStates.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int keyCode = entry.getKey();
            int framesLeft = entry.getValue();
            if (framesLeft > 0) {
                entry.setValue(framesLeft - 1);
                continue;
            }
            this.keyRelease(keyCode);
            iterator.remove();
        }
    }

    @EventHandler(priority=200)
    private void onWorldRender(EventUpdate event) {
        this.checkKeys();
    }
}
