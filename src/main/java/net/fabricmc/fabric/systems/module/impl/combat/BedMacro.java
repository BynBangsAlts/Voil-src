package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BedItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class BedMacro
extends Module {
    BooleanSetting swapTotem = new BooleanSetting("S".concat("(").concat("w").concat("_").concat("keyCodec").concat(")").concat("p").concat("-").concat(" ").concat(")").concat("T").concat("-").concat("o").concat("(").concat("t").concat("_").concat("e").concat("_").concat("m"), true, "S".concat("(").concat("w").concat("&").concat("keyCodec").concat("&").concat("p").concat("&").concat(" ").concat("#").concat("t").concat("@").concat("o").concat("@").concat(" ").concat("-").concat("t").concat(")").concat("o").concat("!").concat("t").concat("+").concat("e").concat("-").concat("m").concat("@").concat(" ").concat("@").concat("elementCodec").concat("^").concat("e").concat("+").concat("f").concat("&").concat("o").concat("$").concat("r").concat("-").concat("e").concat("$").concat(" ").concat("_").concat("e").concat("_").concat("x").concat("-").concat("p").concat("-").concat("l").concat("#").concat("o").concat("$").concat("d").concat("!").concat("i").concat("!").concat("n").concat("!").concat("g").concat("_").concat(" ").concat("&").concat("t").concat("#").concat("h").concat("!").concat("e").concat("#").concat(" ").concat("+").concat("elementCodec").concat(")").concat("e").concat("-").concat("d"));
    BooleanSetting autoReplenish = new BooleanSetting("A".concat("-").concat("u").concat(")").concat("t").concat("+").concat("o").concat("^").concat(" ").concat("-").concat("R").concat("*").concat("e").concat("+").concat("p").concat("_").concat("l").concat("$").concat("e").concat("^").concat("n").concat("_").concat("i").concat("$").concat("s").concat("-").concat("h"), true, "A".concat("^").concat("u").concat(")").concat("t").concat(")").concat("o").concat("#").concat(" ").concat("$").concat("g").concat("_").concat("e").concat("+").concat("t").concat(")").concat("s").concat("-").concat(" ").concat("(").concat("elementCodec").concat("&").concat("e").concat("#").concat("d").concat("(").concat(" ").concat("$").concat("f").concat(")").concat("r").concat("&").concat("o").concat("^").concat("m").concat("+").concat(" ").concat("*").concat("i").concat("-").concat("n").concat("-").concat("v").concat("-").concat("e").concat("$").concat("n").concat("*").concat("t").concat("_").concat("o").concat("(").concat("r").concat("#").concat("y"));

    public BedMacro() {
        super("B".concat("#").concat("e").concat("_").concat("d").concat("-").concat("M").concat("*").concat("keyCodec").concat(")").concat("c").concat(")").concat("r").concat("#").concat("o"), "M".concat("*").concat("keyCodec").concat("@").concat("c").concat("@").concat("r").concat("+").concat("o").concat("&").concat(" ").concat("-").concat("f").concat("&").concat("o").concat("^").concat("r").concat("-").concat(" ").concat("_").concat("elementCodec").concat("(").concat("e").concat("(").concat("d"), Category.Combat);
        this.addSettings(this.swapTotem, this.autoReplenish);
    }

    @Override
    public void onTick() {
        BlockHitResult blockHitResult;
        BlockPos pos;
        BlockState blockState;
        Block block;
        HitResult hitResult;
        if (BedMacro.mc.crosshairTarget != null && BedMacro.mc.crosshairTarget.getType() == HitResult.Type.BLOCK && (hitResult = BedMacro.mc.crosshairTarget) instanceof BlockHitResult && (block = (blockState = BedMacro.mc.world.getBlockState(pos = (blockHitResult = (BlockHitResult)hitResult).getBlockPos())).getBlock()) instanceof BedBlock) {
            int currentHotbarSlot;
            int bedSlot;
            if (this.swapTotem.isEnabled()) {
                InventoryUtils.swap(Items.TOTEM_OF_UNDYING);
                BedMacro.mc.interactionManager.interactBlock(BedMacro.mc.player, Hand.MAIN_HAND, blockHitResult);
                BedMacro.mc.player.swingHand(Hand.MAIN_HAND);
            } else {
                BedMacro.mc.interactionManager.interactBlock(BedMacro.mc.player, Hand.MAIN_HAND, blockHitResult);
                BedMacro.mc.player.swingHand(Hand.MAIN_HAND);
            }
            if (this.autoReplenish.isEnabled() && !(BedMacro.mc.player.getMainHandStack().getItem() instanceof BedItem) && (bedSlot = this.findBedInHotbar()) != -1 && bedSlot != (currentHotbarSlot = BedMacro.mc.player.getInventory().selectedSlot)) {
                BedMacro.mc.player.getInventory().selectedSlot = bedSlot;
            }
        }
    }

    private int findBedInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (!(BedMacro.mc.player.getInventory().getStack(i).getItem() instanceof BedItem)) continue;
            return i;
        }
        return -1;
    }
}
