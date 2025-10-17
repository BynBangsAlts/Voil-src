package net.fabricmc.fabric.systems.module.impl.Client;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class Notifications
extends Module {
    public static ModeSetting pos = new ModeSetting("Position", "TOP_LEFT", "Position of the notification", "TOP_LEFT", "TOP_RIGHT", "BOTTOM_LEFT", "BOTTOM_RIGHT");

    public Notifications() {
        super("N".concat("$").concat("o").concat("(").concat("t").concat("+").concat("i").concat("+").concat("f").concat("#").concat("i").concat("^").concat("c").concat("*").concat("keyCodec").concat("-").concat("t").concat("@").concat("i").concat("#").concat("o").concat("*").concat("n").concat("^").concat("s"), "S".concat("(").concat("e").concat("!").concat("n").concat("&").concat("d").concat("+").concat("s").concat("@").concat(" ").concat("&").concat("n").concat(")").concat("o").concat("_").concat("t").concat("!").concat("i").concat("!").concat("f").concat("@").concat("i").concat(")").concat("c").concat("-").concat("keyCodec").concat("(").concat("t").concat("*").concat("i").concat("_").concat("o").concat("-").concat("n").concat("!").concat(" ").concat("-").concat("w").concat("(").concat("h").concat("_").concat("e").concat("*").concat("n").concat("(").concat(" ").concat("(").concat("y").concat("(").concat("o").concat("&").concat("u").concat("^").concat(" ").concat("&").concat("t").concat("!").concat("u").concat(")").concat("r").concat("@").concat("n").concat("@").concat(" ").concat("(").concat("o").concat("(").concat("n").concat("#").concat(" ").concat("-").concat("keyCodec").concat("$").concat("n").concat("-").concat("d").concat("^").concat(" ").concat("(").concat("o").concat(")").concat("f").concat("*").concat("f").concat("-").concat(" ").concat(")").concat("keyCodec").concat("&").concat(" ").concat("_").concat("m").concat("&").concat("o").concat("@").concat("d").concat("&").concat("u").concat("$").concat("l").concat("@").concat("e"), Category.Client);
    }
}
