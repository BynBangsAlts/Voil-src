package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class Prevent
extends Module {
    private static final BooleanSetting preventAnchor = new BooleanSetting("Prevent Anchor", true, "Prevents placing anchor on another empty anchor");
    private static final BooleanSetting preventGlowstone = new BooleanSetting("Prevent Glowstone", true, "Prevents placing glowstone on ground");
    private static final BooleanSetting preventEchest = new BooleanSetting("Prevent Echest", true, "Prevents opening an echest with sword or totem");
    private static final BooleanSetting preventDoubleGlowstone = new BooleanSetting("Prevent DoubleGlowstone", true, "Prevents placing double glowstone on anchor");
    private static final BooleanSetting preventShield = new BooleanSetting("Prevent Shield", true, "Prevents blocking with shield when holding blocks");

    public Prevent() {
        super("P".concat("!").concat("r").concat(")").concat("e").concat("-").concat("v").concat(")").concat("e").concat("$").concat("n").concat("!").concat("t"), "P".concat(")").concat("r").concat("_").concat("e").concat("^").concat("v").concat("&").concat("e").concat("_").concat("n").concat("*").concat("t").concat("!").concat("s").concat("^").concat(" ").concat(")").concat("c").concat("$").concat("e").concat("#").concat("r").concat("^").concat("t").concat("+").concat("keyCodec").concat("-").concat("i").concat("+").concat("n").concat("-").concat(" ").concat("_").concat("keyCodec").concat("$").concat("c").concat("$").concat("t").concat("+").concat("i").concat(")").concat("o").concat("!").concat("n").concat("(").concat("s"), Category.Miscellaneous);
        this.addSettings(preventAnchor, preventGlowstone, preventEchest, preventDoubleGlowstone, preventShield);
    }

    @EventHandler
    public void onItemUse(ItemUseEvent.Pre event) {
        if (Prevent.mc.player == null || Prevent.mc.world == null) {
            return;
        }
        HitResult hitResult = Prevent.mc.crosshairTarget;
        if (hitResult instanceof BlockHitResult) {
            BlockHitResult hit = (BlockHitResult)hitResult;
            if (BlockUtils.isAnchorCharged(hit.getBlockPos()) && preventDoubleGlowstone.isEnabled() && Prevent.mc.player.isHolding(Items.GLOWSTONE)) {
                event.cancel();
            }
            if (!BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, hit.getBlockPos()) && preventGlowstone.isEnabled() && Prevent.mc.player.isHolding(Items.GLOWSTONE)) {
                event.cancel();
            }
            if (BlockUtils.isAnchorUncharged(hit.getBlockPos()) && preventAnchor.isEnabled() && Prevent.mc.player.isHolding(Items.RESPAWN_ANCHOR)) {
                event.cancel();
            }
            if (BlockUtils.isBlock(Blocks.ENDER_CHEST, hit.getBlockPos()) && preventEchest.isEnabled() && (Prevent.mc.player.getMainHandStack().getItem() instanceof SwordItem || Prevent.mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL || Prevent.mc.player.getMainHandStack().getItem() == Items.OBSIDIAN || Prevent.mc.player.getMainHandStack().getItem() == Items.RESPAWN_ANCHOR || Prevent.mc.player.getMainHandStack().getItem() == Items.GLOWSTONE)) {
                event.cancel();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
