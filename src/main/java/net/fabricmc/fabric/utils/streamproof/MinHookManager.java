package net.fabricmc.fabric.utils.streamproof;

import com.sun.jna.Native;
import net.fabricmc.fabric.utils.streamproof.MinHook;

public class MinHookManager {
    private static MinHook instance;

    static MinHook GetInstance() {
        if (instance == null) {
            instance = (MinHook)Native.load((String)"MinHook", MinHook.class);
            return instance;
        }
        return instance;
    }
}
