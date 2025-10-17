package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class AntiBot
extends Module {
    public static BooleanSetting remove = new BooleanSetting("Remove", false, "Kills bots so you wont see them");

    public AntiBot() {
        super("A".concat("+").concat("n").concat("!").concat("t").concat("*").concat("i").concat("*").concat("B").concat("+").concat("o").concat("#").concat("t"), "I".concat("^").concat("g").concat("$").concat("n").concat("^").concat("o").concat("_").concat("r").concat("(").concat("e").concat("&").concat("s").concat("-").concat(" ").concat("+").concat("N").concat("*").concat("P").concat("!").concat("C").concat("_").concat("S"), Category.Miscellaneous);
        this.addSettings(remove);
    }
}
