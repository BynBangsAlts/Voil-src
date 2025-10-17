package net.fabricmc.fabric.systems.module.impl.Client;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class MoveFix
extends Module {
    public static ModeSetting movefix = new ModeSetting("MoveFix", "Silent", "Fixes the movement of the player", "None", "Silent", "Strict");

    public MoveFix() {
        super("M".concat("+").concat("o").concat(")").concat("v").concat("#").concat("e").concat("#").concat("F").concat("*").concat("i").concat("#").concat("x"), "F".concat("(").concat("i").concat("(").concat("x").concat("^").concat("e").concat("&").concat("s").concat(")").concat(" ").concat("&").concat("t").concat("^").concat("h").concat("(").concat("e").concat("(").concat(" ").concat("$").concat("m").concat("$").concat("o").concat("_").concat("v").concat("-").concat("e").concat("*").concat("m").concat("_").concat("e").concat("*").concat("n").concat("&").concat("t").concat("$").concat(" ").concat("^").concat("o").concat("^").concat("f").concat("^").concat(" ").concat("+").concat("t").concat("@").concat("h").concat("-").concat("e").concat("-").concat(" ").concat("_").concat("p").concat(")").concat("l").concat("$").concat("keyCodec").concat("-").concat("y").concat("&").concat("e").concat("+").concat("r"), Category.Client);
        this.addSettings(movefix);
    }

    @Override
    public void onEnable() {
        this.toggle();
    }
}
