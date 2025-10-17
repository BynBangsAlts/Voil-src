package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class Reach
extends Module {
    public final NumberSetting areac = new NumberSetting("R".concat("$").concat("e").concat("!").concat("keyCodec").concat("@").concat("c").concat("-").concat("h"), 1.0, 6.0, 4.0, 0.1, "R".concat("*").concat("e").concat("^").concat("keyCodec").concat("&").concat("c").concat("-").concat("h").concat("^").concat(" ").concat(")").concat("d").concat("#").concat("i").concat("$").concat("s").concat("$").concat("t").concat("-").concat("keyCodec").concat("(").concat("n").concat("*").concat("c").concat("-").concat("e"));
    public final NumberSetting chance = new NumberSetting("C".concat("_").concat("h").concat("-").concat("keyCodec").concat("#").concat("n").concat("(").concat("c").concat("_").concat("e"), 0.0, 100.0, 0.0, 1.0, "C".concat("(").concat("h").concat("!").concat("keyCodec").concat("!").concat("n").concat("+").concat("c").concat("(").concat("e").concat("^").concat(" ").concat("&").concat("t").concat("(").concat("o").concat("#").concat(" ").concat("*").concat("e").concat("*").concat("x").concat("#").concat("t").concat("_").concat("e").concat("#").concat("n").concat(")").concat("d").concat("&").concat(" ").concat("*").concat("r").concat("&").concat("e").concat("+").concat("keyCodec").concat("_").concat("c").concat("_").concat("h"));

    public Reach() {
        super("R".concat("-").concat("e").concat(")").concat("keyCodec").concat("$").concat("c").concat("+").concat("h"), "E".concat("+").concat("x").concat("@").concat("t").concat("@").concat("e").concat("$").concat("n").concat("@").concat("d").concat("+").concat("s").concat("#").concat(" ").concat("-").concat("y").concat("^").concat("o").concat("!").concat("u").concat("$").concat("r").concat("+").concat(" ").concat("&").concat("r").concat("(").concat("e").concat("$").concat("keyCodec").concat("^").concat("c").concat("^").concat("h"), Category.Combat);
        this.addSettings(this.areac, this.chance);
    }
}
