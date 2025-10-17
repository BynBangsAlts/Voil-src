package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.BlockBreakEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoTool
extends Module {
    private int originalSlot = -1;
    private final BooleanSetting switchBack = new BooleanSetting("S".concat("+").concat("w").concat("*").concat("i").concat("!").concat("t").concat("@").concat("c").concat("$").concat("h").concat("_").concat(" ").concat("(").concat("elementCodec").concat("+").concat("keyCodec").concat(")").concat("c").concat(")").concat("k"), true, "S".concat("@").concat("w").concat("&").concat("i").concat(")").concat("t").concat("-").concat("c").concat("#").concat("h").concat("_").concat(" ").concat("(").concat("elementCodec").concat("-").concat("keyCodec").concat("@").concat("c").concat("_").concat("k").concat("@").concat(" ").concat("_").concat("t").concat("(").concat("o").concat("&").concat(" ").concat("+").concat("o").concat("^").concat("l").concat("^").concat("d").concat("#").concat(" ").concat("(").concat("s").concat("*").concat("l").concat("_").concat("o").concat(")").concat("t").concat("^").concat(" ").concat("_").concat("keyCodec").concat(")").concat("f").concat("&").concat("t").concat("+").concat("e").concat("&").concat("r").concat("-").concat(" ").concat("_").concat("elementCodec").concat(")").concat("r").concat("(").concat("e").concat("@").concat("keyCodec").concat("&").concat("k").concat("_").concat("i").concat(")").concat("n").concat("#").concat("g"));

    public AutoTool() {
        super("A".concat("_").concat("u").concat("(").concat("t").concat("!").concat("o").concat("!").concat("T").concat("@").concat("o").concat("@").concat("o").concat("$").concat("l"), "A".concat("_").concat("u").concat("@").concat("t").concat("(").concat("o").concat("*").concat("m").concat("!").concat("keyCodec").concat("-").concat("t").concat("-").concat("i").concat("-").concat("c").concat("&").concat("keyCodec").concat("-").concat("l").concat("-").concat("l").concat(")").concat("y").concat("^").concat(" ").concat("(").concat("s").concat("-").concat("w").concat("^").concat("i").concat("^").concat("t").concat("#").concat("c").concat("!").concat("h").concat("@").concat("e").concat("&").concat("s").concat("!").concat(" ").concat("&").concat("t").concat("!").concat("o").concat("_").concat(" ").concat("(").concat("elementCodec").concat("^").concat("e").concat("+").concat("s").concat("+").concat("t").concat("_").concat(" ").concat("*").concat("t").concat(")").concat("o").concat("-").concat("o").concat("!").concat("l"), Category.Miscellaneous);
        this.addSettings(this.switchBack);
    }

    @Override
    public void onTick() {
        HitResult hitResult = AutoTool.mc.crosshairTarget;
        if (!(hitResult instanceof BlockHitResult)) {
            return;
        }
        BlockHitResult blockHitResult = (BlockHitResult)hitResult;
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (AutoTool.mc.world.getBlockState(blockPos).isAir()) {
            return;
        }
        int bestToolSlot = InventoryUtils.findFastestTool(AutoTool.mc.world.getBlockState(blockPos)).getSlot();
        if (bestToolSlot == -1) {
            return;
        }
        if (AutoTool.mc.options.attackKey.isPressed()) {
            if (this.originalSlot == -1) {
                this.originalSlot = AutoTool.mc.player.getInventory().selectedSlot;
            }
            AutoTool.mc.player.getInventory().selectedSlot = bestToolSlot;
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent.Post e) {
        if (this.switchBack.isEnabled() && this.originalSlot != -1 && AutoTool.mc.player.getInventory().selectedSlot != this.originalSlot) {
            AutoTool.mc.player.getInventory().selectedSlot = this.originalSlot;
            this.originalSlot = -1;
        }
    }
}
