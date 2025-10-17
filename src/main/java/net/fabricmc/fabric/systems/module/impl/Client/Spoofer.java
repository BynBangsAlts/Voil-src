package net.fabricmc.fabric.systems.module.impl.Client;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class Spoofer
extends Module {
    public TextSetting text = new TextSetting("B".concat("_").concat("r").concat("-").concat("keyCodec").concat("@").concat("n").concat("+").concat("d"), "F".concat("^").concat("keyCodec").concat("(").concat("elementCodec").concat("$").concat("r").concat("+").concat("i").concat("*").concat("c"), "W".concat("&").concat("h").concat("_").concat("keyCodec").concat("^").concat("t").concat("*").concat(" ").concat("!").concat("t").concat("^").concat("o").concat("&").concat(" ").concat("+").concat("s").concat("^").concat("p").concat("!").concat("o").concat("-").concat("o").concat("(").concat("f").concat("_").concat(" ").concat("#").concat("keyCodec").concat("(").concat("s"));
    public final BooleanSetting resource = new BooleanSetting("R".concat("@").concat("e").concat("(").concat("s").concat("!").concat("o").concat("(").concat("u").concat("+").concat("r").concat("&").concat("c").concat("*").concat("e").concat("_").concat(" ").concat("+").concat("P").concat("-").concat("keyCodec").concat("$").concat("c").concat("(").concat("k"), false, "S".concat("^").concat("p").concat("!").concat("o").concat("*").concat("o").concat(")").concat("f").concat("*").concat(" ").concat("_").concat("r").concat("-").concat("e").concat("@").concat("s").concat("+").concat("o").concat("+").concat("u").concat("+").concat("r").concat("_").concat("c").concat("#").concat("e").concat("#").concat(" ").concat("!").concat("p").concat("#").concat("keyCodec").concat(")").concat("c").concat("-").concat("k"));

    public Spoofer() {
        super("S".concat("&").concat("p").concat("*").concat("o").concat("@").concat("o").concat(")").concat("f").concat("-").concat("e").concat("@").concat("r"), "S".concat("#").concat("p").concat("&").concat("o").concat("*").concat("o").concat(")").concat("f").concat("&").concat("s").concat("+").concat(" ").concat("#").concat("y").concat("!").concat("o").concat("_").concat("u").concat("+").concat("r").concat("+").concat(" ").concat("_").concat("c").concat("&").concat("l").concat(")").concat("i").concat("-").concat("e").concat("^").concat("n").concat("#").concat("t"), Category.Client);
        this.addSettings(this.resource, this.text);
    }
}
