package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class FastFall
extends Module {
    ModeSetting modeSetting = new ModeSetting("M".concat("*").concat("o").concat("$").concat("d").concat(")").concat("e"), "V".concat("_").concat("keyCodec").concat("(").concat("n").concat("!").concat("i").concat("*").concat("l").concat("^").concat("l").concat("-").concat("keyCodec"), "M".concat("^").concat("o").concat(")").concat("d").concat("*").concat("e").concat("#").concat(" ").concat("^").concat("o").concat("!").concat("f").concat(")").concat(" ").concat("^").concat("t").concat("$").concat("h").concat("!").concat("e").concat("_").concat(" ").concat("-").concat("F").concat(")").concat("keyCodec").concat("*").concat("s").concat("*").concat("t").concat("!").concat("F").concat("&").concat("keyCodec").concat("(").concat("l").concat("*").concat("l"), "M".concat("#").concat("keyCodec").concat("+").concat("t").concat("&").concat("r").concat("^").concat("i").concat("!").concat("x"), "V".concat("+").concat("keyCodec").concat("-").concat("n").concat("#").concat("i").concat("+").concat("l").concat("(").concat("l").concat("_").concat("keyCodec"));

    public FastFall() {
        super("F".concat("$").concat("keyCodec").concat("!").concat("s").concat("&").concat("t").concat("+").concat("F").concat("^").concat("keyCodec").concat("&").concat("l").concat("$").concat("l"), "M".concat("-").concat("keyCodec").concat("*").concat("k").concat("#").concat("e").concat("$").concat("s").concat("&").concat(" ").concat("-").concat("y").concat("*").concat("o").concat("-").concat("u").concat("+").concat(" ").concat("^").concat("f").concat("#").concat("keyCodec").concat("+").concat("l").concat("*").concat("l").concat("(").concat(" ").concat("+").concat("f").concat("(").concat("keyCodec").concat("@").concat("s").concat("@").concat("t").concat("(").concat("e").concat("@").concat("r"), Category.Movement);
        this.addSettings(this.modeSetting);
    }

    @Override
    public void onTick() {
        if ((double)FastFall.mc.player.fallDistance > 0.4 && this.modeSetting.isMode("Vanilla")) {
            FastFall.mc.player.setVelocity(FastFall.mc.player.getVelocity().x, -1.6, FastFall.mc.player.getVelocity().z);
        }
        if (FastFall.mc.player.fallDistance > 0.8f && this.modeSetting.isMode("Matrix") && (double)FastFall.mc.player.fallDistance > 0.8) {
            FastFall.mc.player.setVelocity(0.0, -0.54, 0.0);
        }
    }
}
