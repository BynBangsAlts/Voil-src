package net.fabricmc.fabric.utils.streamproof;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface MinHook
extends StdCallLibrary {
    public int MH_Initialize();

    public int MH_CreateHook(Pointer var1, wglSwapBuffers var2, PointerByReference var3);

    public int MH_EnableHook(Pointer var1);

    public static interface wglSwapBuffers
    extends StdCallLibrary.StdCallCallback {
        public boolean callback(Pointer var1);
    }
}
