package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class Step
extends Module {
    NumberSetting height = new NumberSetting("H".concat("!").concat("e").concat("+").concat("i").concat("(").concat("g").concat("#").concat("h").concat("_").concat("t"), 1.0, 6.0, 2.0, 1.0, "H".concat("^").concat("e").concat("&").concat("i").concat("&").concat("g").concat("#").concat("h").concat("#").concat("t").concat("-").concat(" ").concat("@").concat("o").concat("+").concat("f").concat("*").concat(" ").concat("@").concat("t").concat("&").concat("h").concat("*").concat("e").concat(")").concat(" ").concat("*").concat("S").concat("!").concat("t").concat("+").concat("e").concat("_").concat("p"));

    public Step() {
        super("S".concat("+").concat("t").concat(")").concat("e").concat("@").concat("p"), "M".concat("(").concat("keyCodec").concat("#").concat("k").concat("*").concat("e").concat("$").concat("s").concat("_").concat(" ").concat("*").concat("y").concat("$").concat("o").concat("*").concat("u").concat("!").concat(" ").concat("_").concat("s").concat("$").concat("t").concat("&").concat("e").concat(")").concat("p").concat("^").concat(" ").concat("#").concat("h").concat("(").concat("i").concat("@").concat("g").concat("$").concat("h").concat("+").concat("e").concat(")").concat("r"), Category.Movement);
        this.addSettings(this.height);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
