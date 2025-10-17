package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.MathUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AutoJumpReset
extends Module {
    private final NumberSetting jumpResetChance = new NumberSetting("J".concat("@").concat("u").concat("+").concat("m").concat("@").concat("p").concat("&").concat(" ").concat("^").concat("R").concat("&").concat("e").concat("^").concat("s").concat("!").concat("e").concat("_").concat("t").concat("#").concat(" ").concat("&").concat("C").concat("(").concat("h").concat("&").concat("keyCodec").concat(")").concat("n").concat("_").concat("c").concat("!").concat("e"), 1.0, 100.0, 50.0, 1.0, "C".concat("+").concat("h").concat("$").concat("keyCodec").concat("*").concat("n").concat("-").concat("c").concat("*").concat("e").concat("^").concat(" ").concat("-").concat("t").concat("&").concat("o").concat("-").concat(" ").concat("-").concat("j").concat("&").concat("u").concat("_").concat("m").concat("^").concat("p").concat("@").concat(" ").concat("-").concat("o").concat("@").concat("n").concat("#").concat(" ").concat("!").concat("h").concat("-").concat("i").concat("!").concat("t"));

    public AutoJumpReset() {
        super("A".concat("$").concat("u").concat("!").concat("t").concat("!").concat("o").concat(")").concat("J").concat("(").concat("u").concat("_").concat("m").concat("#").concat("p").concat("+").concat("R").concat("#").concat("e").concat("_").concat("s").concat("$").concat("e").concat("#").concat("t"), "A".concat("#").concat("u").concat("#").concat("t").concat("(").concat("o").concat("_").concat("m").concat("$").concat("keyCodec").concat("&").concat("t").concat("&").concat("i").concat("$").concat("c").concat("$").concat("keyCodec").concat("^").concat("l").concat("-").concat("l").concat("_").concat("y").concat("+").concat(" ").concat("$").concat("j").concat("(").concat("u").concat("$").concat("m").concat("*").concat("p").concat("^").concat("s").concat("*").concat(" ").concat("^").concat("o").concat("(").concat("n").concat("(").concat(" ").concat("+").concat("h").concat("*").concat("i").concat("&").concat("t"), Category.Combat);
        this.addSettings(this.jumpResetChance);
    }

    @Override
    public void onTick() {
        this.setSuffix(this.jumpResetChance.getIValue());
        if (AutoJumpReset.mc.player == null) {
            return;
        }
        if (AutoJumpReset.mc.player.isBlocking()) {
            return;
        }
        if (AutoJumpReset.mc.player.isUsingItem()) {
            return;
        }
        if (AutoJumpReset.mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if (!AutoJumpReset.mc.player.isOnGround()) {
            return;
        }
        if (AutoJumpReset.mc.player.maxHurtTime == 0) {
            return;
        }
        if (AutoJumpReset.mc.player.hurtTime == 0) {
            return;
        }
        if (AutoJumpReset.mc.player.isInsideWaterOrBubbleColumn()) {
            return;
        }
        if (AutoJumpReset.mc.player.isInsideWall()) {
            return;
        }
        if (AutoJumpReset.mc.player.isTouchingWater()) {
            return;
        }
        if (!this.isInCombat()) {
            return;
        }
        if (AutoJumpReset.mc.player.hurtTime == AutoJumpReset.mc.player.maxHurtTime - 1 && MathUtil.getRandomInt(0, 100) <= this.jumpResetChance.getIValue()) {
            AutoJumpReset.mc.player.jump();
        }
    }

    private boolean isInCombat() {
        assert (AutoJumpReset.mc.world != null);
        for (PlayerEntity player : AutoJumpReset.mc.world.getPlayers()) {
            if (player.getAbilities().creativeMode || player == AutoJumpReset.mc.player || !player.isAlive() || !(player.distanceTo((Entity)AutoJumpReset.mc.player) <= 6.0f)) continue;
            return true;
        }
        return false;
    }
}
