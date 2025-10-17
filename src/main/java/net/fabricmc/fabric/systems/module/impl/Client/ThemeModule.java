package net.fabricmc.fabric.systems.module.impl.Client;

import java.util.Objects;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class ThemeModule
extends Module {
    ModeSetting theme = new ModeSetting("t".concat("(").concat("h").concat("#").concat("e").concat("(").concat("m").concat("!").concat("e"), "D".concat("$").concat("keyCodec").concat("^").concat("r").concat("&").concat("k"), "G".concat("(").concat("u").concat(")").concat("i").concat(")").concat(" ").concat("+").concat("T").concat("#").concat("h").concat(")").concat("e").concat("*").concat("m").concat("-").concat("e"), "D".concat("+").concat("keyCodec").concat("(").concat("r").concat("-").concat("k"), "M".concat(")").concat("o").concat("!").concat("o").concat("+").concat("n").concat("&").concat("l").concat("$").concat("i").concat("*").concat("g").concat("!").concat("h").concat("@").concat("t"), "V".concat(")").concat("o").concat("(").concat("l").concat("_").concat("c").concat("-").concat("keyCodec").concat("#").concat("n").concat("#").concat("i").concat("-").concat("c"), "C".concat(")").concat("h").concat("(").concat("e").concat("^").concat("r").concat("+").concat("r").concat("*").concat("y"), "C".concat("-").concat("u").concat("_").concat("s").concat("^").concat("t").concat("_").concat("o").concat("$").concat("m"), "L".concat("!").concat("i").concat("*").concat("g").concat("-").concat("h").concat("_").concat("t"));

    public ThemeModule() {
        super("T".concat("&").concat("h").concat("$").concat("e").concat("(").concat("m").concat("+").concat("e"), "C".concat("#").concat("h").concat("@").concat("keyCodec").concat("^").concat("n").concat("$").concat("g").concat("*").concat("e").concat(")").concat("s").concat("@").concat(" ").concat("+").concat("t").concat("_").concat("h").concat("@").concat("e").concat("*").concat(" ").concat("&").concat("g").concat("_").concat("u").concat("+").concat("i").concat("&").concat(" ").concat("@").concat("t").concat("-").concat("h").concat("&").concat("e").concat("&").concat("m").concat("$").concat("e"), Category.Client);
        this.addSettings(this.theme);
    }

    @Override
    public void onTick() {
        Objects.requireNonNull(this.theme.getMode());
    }
}
