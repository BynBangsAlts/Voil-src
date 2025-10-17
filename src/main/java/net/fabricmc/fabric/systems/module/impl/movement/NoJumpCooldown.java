package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.mixin.ILivingEntity;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

public class NoJumpCooldown
extends Module {
    public NoJumpCooldown() {
        super("N".concat("(").concat("o").concat("!").concat("J").concat("^").concat("u").concat("-").concat("m").concat("^").concat("p").concat("*").concat("C").concat("^").concat("o").concat("#").concat("o").concat("&").concat("l").concat("(").concat("d").concat("*").concat("o").concat("-").concat("w").concat("&").concat("n"), "R".concat("#").concat("e").concat("&").concat("m").concat("+").concat("o").concat("#").concat("v").concat("^").concat("e").concat("_").concat("s").concat("-").concat(" ").concat("&").concat("j").concat("*").concat("u").concat("(").concat("m").concat("@").concat("p").concat("(").concat(" ").concat("@").concat("c").concat(")").concat("o").concat("_").concat("o").concat("*").concat("l").concat("_").concat("d").concat(")").concat("o").concat("^").concat("w").concat("^").concat("n"), Category.Movement);
    }

    @Override
    public void onTick() {
        if (NoJumpCooldown.mc.player != null) {
            ((ILivingEntity)NoJumpCooldown.mc.player).setJumpingCooldown(0);
        }
    }
}
