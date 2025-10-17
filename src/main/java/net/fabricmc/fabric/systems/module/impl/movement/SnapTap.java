package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class SnapTap
extends Module {
    public static long LEFT_STRAFE_LAST_PRESS_TIME = 0L;
    public static long RIGHT_STRAFE_LAST_PRESS_TIME = 0L;
    public static long FORWARD_STRAFE_LAST_PRESS_TIME = 0L;
    public static long BACKWARD_STRAFE_LAST_PRESS_TIME = 0L;

    public SnapTap() {
        super("S".concat("!").concat("n").concat("_").concat("keyCodec").concat("+").concat("p").concat("+").concat("T").concat(")").concat("keyCodec").concat("_").concat("p"), "A".concat("-").concat("l").concat("^").concat("l").concat("@").concat("o").concat("-").concat("w").concat("(").concat("s").concat("-").concat(" ").concat("-").concat("y").concat("_").concat("o").concat("^").concat("u").concat("#").concat(" ").concat("-").concat("t").concat("!").concat("o").concat("^").concat(" ").concat("*").concat("s").concat("!").concat("t").concat("+").concat("r").concat("_").concat("keyCodec").concat("$").concat("f").concat("(").concat("e").concat("+").concat(" ").concat("^").concat("w").concat("(").concat("i").concat("*").concat("t").concat("!").concat("h").concat("@").concat("o").concat("@").concat("u").concat("!").concat("t").concat("#").concat(" ").concat("+").concat("s").concat("_").concat("t").concat(")").concat("o").concat("&").concat("p").concat("$").concat("p").concat("_").concat("i").concat("$").concat("n").concat("(").concat("g"), Category.Movement);
    }
}
