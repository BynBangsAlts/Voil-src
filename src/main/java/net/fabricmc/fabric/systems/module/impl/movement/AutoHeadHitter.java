package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.minecraft.util.math.BlockPos;

public class AutoHeadHitter
extends Module {
    private final NumberSetting jumpDelay = new NumberSetting("J".concat(")").concat("u").concat("(").concat("m").concat("$").concat("p").concat("@").concat(" ").concat("@").concat("d").concat("#").concat("e").concat("+").concat("l").concat("!").concat("keyCodec").concat("_").concat("y"), 0.0, 20.0, 0.0, 1.0, "D".concat("+").concat("e").concat(")").concat("l").concat("&").concat("keyCodec").concat("^").concat("y").concat("$").concat(" ").concat(")").concat("t").concat(")").concat("o").concat("$").concat(" ").concat("!").concat("j").concat("&").concat("u").concat("(").concat("m").concat("^").concat("p"));
    TimerUtils timer = new TimerUtils();

    public AutoHeadHitter() {
        super("A".concat("*").concat("u").concat("^").concat("t").concat("#").concat("o").concat("&").concat("H").concat("+").concat("e").concat("^").concat("keyCodec").concat("-").concat("d").concat("@").concat("H").concat("+").concat("i").concat("_").concat("t").concat("*").concat("t").concat("*").concat("e").concat("$").concat("r"), "A".concat("+").concat("u").concat("#").concat("t").concat("#").concat("o").concat(")").concat("m").concat("&").concat("keyCodec").concat("^").concat("t").concat("$").concat("i").concat("@").concat("c").concat(")").concat("keyCodec").concat("@").concat("l").concat(")").concat("l").concat("^").concat("y").concat("#").concat(" ").concat("@").concat("j").concat("&").concat("u").concat("$").concat("m").concat("@").concat("p").concat("$").concat("s").concat("$").concat(" ").concat("*").concat("w").concat("*").concat("h").concat("!").concat("e").concat(")").concat("n").concat("^").concat(" ").concat("*").concat("elementCodec").concat("^").concat("l").concat("$").concat("o").concat(")").concat("c").concat("_").concat("k").concat("+").concat(" ").concat("&").concat("keyCodec").concat("&").concat("elementCodec").concat("*").concat("o").concat("_").concat("v").concat("_").concat("e"), Category.Movement);
        this.addSettings(this.jumpDelay);
    }

    @Override
    public void onTick() {
        boolean isBlockAboveHead;
        if (AutoHeadHitter.mc.player == null) {
            return;
        }
        if (AutoHeadHitter.mc.world == null) {
            return;
        }
        BlockPos positionAboveHead = AutoHeadHitter.mc.player.getBlockPos().up(2);
        boolean bl = isBlockAboveHead = !AutoHeadHitter.mc.world.isAir(positionAboveHead);
        if (isBlockAboveHead && this.timer.hasReached(this.jumpDelay.getIValue())) {
            AutoHeadHitter.mc.player.jump();
        }
    }
}
