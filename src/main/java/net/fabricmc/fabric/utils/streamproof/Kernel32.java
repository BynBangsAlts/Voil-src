package net.fabricmc.fabric.utils.streamproof;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32
extends StdCallLibrary {
    public static final Kernel32 INSTANCE = (Kernel32)Native.load((String)"Kernel32", Kernel32.class);

    public Pointer GetModuleHandleA(String var1);

    public Pointer GetProcAddress(Pointer var1, String var2);
}
