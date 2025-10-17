package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEvent;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoCrystal;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.PlayerUtils;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutoHitCrystal
extends Module {
    private final BooleanSetting clickSimulation = new BooleanSetting("C".concat("#").concat("l").concat("$").concat("i").concat("(").concat("c").concat("!").concat("k").concat("*").concat(" ").concat("*").concat("s").concat("!").concat("i").concat("^").concat("m").concat("-").concat("u").concat(")").concat("l").concat("_").concat("keyCodec").concat("#").concat("t").concat("(").concat("i").concat("#").concat("o").concat("$").concat("n"), true, "S".concat("!").concat("i").concat("$").concat("m").concat("(").concat("u").concat("(").concat("l").concat("#").concat("keyCodec").concat("@").concat("t").concat("&").concat("e").concat("$").concat(" ").concat("!").concat("c").concat("+").concat("l").concat("#").concat("i").concat("#").concat("c").concat("#").concat("k").concat("@").concat("s").concat("#").concat(" ").concat("&").concat("w").concat("*").concat("h").concat("_").concat("i").concat("&").concat("l").concat("@").concat("e").concat("^").concat(" ").concat("@").concat("p").concat("&").concat("l").concat("+").concat("keyCodec").concat("_").concat("c").concat("&").concat("i").concat(")").concat("n").concat("-").concat("g"));
    private final BooleanSetting workWithTotem = new BooleanSetting("W".concat("^").concat("o").concat("!").concat("r").concat("_").concat("k").concat("@").concat(" ").concat("-").concat("w").concat("_").concat("i").concat("!").concat("t").concat("_").concat("h").concat("(").concat(" ").concat("_").concat("t").concat(")").concat("o").concat("#").concat("t").concat("@").concat("e").concat("#").concat("m"), true, "L".concat("(").concat("e").concat("$").concat("t").concat("^").concat("s").concat("(").concat(" ").concat("#").concat("y").concat("@").concat("o").concat("^").concat("u").concat("^").concat(" ").concat("!").concat("keyCodec").concat(")").concat("u").concat("!").concat("t").concat("(").concat("o").concat("!").concat(" ").concat("$").concat("h").concat("+").concat("i").concat("&").concat("t").concat("!").concat(" ").concat("$").concat("c").concat("#").concat("r").concat("+").concat("y").concat("$").concat("s").concat(")").concat("t").concat("-").concat("keyCodec").concat("-").concat("l").concat("@").concat(" ").concat("&").concat("w").concat("!").concat("i").concat("#").concat("t").concat("$").concat("h").concat("(").concat(" ").concat("-").concat("t").concat("$").concat("o").concat("*").concat("t").concat("(").concat("e").concat(")").concat("m"));
    private final BooleanSetting preferBiggestCrystalStack = new BooleanSetting("P".concat("$").concat("r").concat("^").concat("e").concat("(").concat("f").concat("!").concat("e").concat("+").concat("r").concat("-").concat(" ").concat("&").concat("elementCodec").concat("@").concat("i").concat("+").concat("g").concat("(").concat("g").concat(")").concat("e").concat("$").concat("s").concat("#").concat("t").concat("(").concat(" ").concat("+").concat("s").concat("+").concat("t").concat("_").concat("keyCodec").concat("-").concat("c").concat("$").concat("k"), true, "P".concat("#").concat("r").concat("!").concat("e").concat("-").concat("f").concat("(").concat("e").concat("_").concat("r").concat("^").concat("s").concat("#").concat(" ").concat("&").concat("t").concat("-").concat("h").concat("#").concat("e").concat("#").concat(" ").concat("!").concat("elementCodec").concat("-").concat("i").concat("&").concat("g").concat("-").concat("g").concat("!").concat("e").concat("!").concat("s").concat("!").concat("t").concat("&").concat(" ").concat("+").concat("c").concat(")").concat("r").concat("^").concat("y").concat("@").concat("s").concat("-").concat("t").concat("_").concat("keyCodec").concat("*").concat("l").concat("@").concat(" ").concat("+").concat("s").concat("+").concat("t").concat("(").concat("keyCodec").concat(")").concat("c").concat("!").concat("k").concat("$").concat(" ").concat("^").concat("i").concat("(").concat("n").concat("+").concat(" ").concat("@").concat("h").concat("*").concat("o").concat("_").concat("t").concat("*").concat("elementCodec").concat("$").concat("keyCodec").concat("@").concat("r"));
    private final BooleanSetting placeOnObsidian = new BooleanSetting("P".concat("&").concat("l").concat("*").concat("keyCodec").concat("!").concat("c").concat("+").concat("e").concat("#").concat(" ").concat("$").concat("o").concat("^").concat("n").concat("$").concat(" ").concat("@").concat("o").concat("_").concat("elementCodec").concat(")").concat("s").concat(")").concat("i").concat("#").concat("d").concat("!").concat("i").concat("#").concat("keyCodec").concat(")").concat("n"), false, "A".concat("$").concat("l").concat("^").concat("w").concat("-").concat("keyCodec").concat(")").concat("y").concat("(").concat("s").concat("&").concat(" ").concat("*").concat("p").concat("#").concat("l").concat("(").concat("keyCodec").concat("+").concat("c").concat("@").concat("e").concat("@").concat(" ").concat("!").concat("o").concat(")").concat("elementCodec").concat("$").concat("s").concat("^").concat("i").concat("+").concat("d").concat("_").concat("i").concat("!").concat("keyCodec").concat("&").concat("n").concat("_").concat(" ").concat("@").concat("w").concat("*").concat("h").concat("#").concat("e").concat("_").concat("n").concat("#").concat(" ").concat("!").concat("keyCodec").concat("#").concat("i").concat("!").concat("m").concat("^").concat("i").concat("(").concat("n").concat("-").concat("g").concat("_").concat(" ").concat("@").concat("keyCodec").concat("+").concat("t").concat("-").concat(" ").concat("*").concat("o").concat("@").concat("elementCodec").concat("&").concat("s").concat("+").concat("i").concat("#").concat("d").concat("!").concat("i").concat("!").concat("keyCodec").concat("^").concat("n"));
    private final BooleanSetting silentRotate = new BooleanSetting("S".concat("+").concat("i").concat("^").concat("l").concat(")").concat("e").concat("^").concat("n").concat("!").concat("t").concat("(").concat(" ").concat("#").concat("r").concat("(").concat("o").concat("#").concat("t").concat(")").concat("keyCodec").concat("(").concat("t").concat(")").concat("e"), false, "R".concat("(").concat("o").concat(")").concat("t").concat(")").concat("keyCodec").concat("!").concat("t").concat("@").concat("e").concat("(").concat(" ").concat("(").concat("i").concat("_").concat("n").concat("*").concat("s").concat("#").concat("t").concat("+").concat("keyCodec").concat("(").concat("n").concat("$").concat("t").concat("@").concat("l").concat("(").concat("y").concat("*").concat(" ").concat("_").concat("w").concat("+").concat("h").concat(")").concat("i").concat("+").concat("l").concat("*").concat("e").concat("*").concat(" ").concat("*").concat("h").concat("#").concat("i").concat("(").concat("t").concat("&").concat("t").concat("_").concat("i").concat("*").concat("n").concat(")").concat("g").concat("$").concat(" ").concat("(").concat("keyCodec").concat("(").concat(" ").concat("-").concat("n").concat("-").concat("e").concat("&").concat("keyCodec").concat("+").concat("r").concat(")").concat("elementCodec").concat("-").concat("y").concat("!").concat(" ").concat("!").concat("p").concat("#").concat("e").concat("+").concat("r").concat("#").concat("s").concat("!").concat("o").concat("+").concat("n"));
    private final NumberSetting placeDelay = new NumberSetting("O".concat(")").concat("elementCodec").concat("#").concat("s").concat("(").concat("i").concat("*").concat("d").concat("#").concat("i").concat("$").concat("keyCodec").concat(")").concat("n").concat("(").concat(" ").concat("!").concat("d").concat("_").concat("e").concat(")").concat("l").concat("*").concat("keyCodec").concat("!").concat("y"), 0.0, 10.0, 0.0, 0.1, "D".concat("-").concat("e").concat("(").concat("l").concat("$").concat("keyCodec").concat("_").concat("y").concat("#").concat(" ").concat("-").concat("elementCodec").concat("_").concat("e").concat("(").concat("f").concat("^").concat("o").concat("_").concat("r").concat("@").concat("e").concat("@").concat(" ").concat("#").concat("p").concat("$").concat("l").concat("(").concat("keyCodec").concat("#").concat("c").concat("-").concat("i").concat("-").concat("n").concat("-").concat("g").concat("*").concat(" ").concat("@").concat("o").concat("*").concat("elementCodec").concat(")").concat("s").concat("&").concat("i").concat("#").concat("d").concat("+").concat("i").concat("@").concat("keyCodec").concat("#").concat("n"));
    private final NumberSetting switchDelay = new NumberSetting("S".concat("_").concat("w").concat("_").concat("i").concat("*").concat("t").concat("#").concat("c").concat("&").concat("h").concat(")").concat(" ").concat("_").concat("d").concat(")").concat("e").concat("(").concat("l").concat("@").concat("keyCodec").concat("!").concat("y"), 0.0, 10.0, 0.0, 0.1, "D".concat("@").concat("e").concat("#").concat("l").concat("_").concat("keyCodec").concat("-").concat("y").concat("+").concat(" ").concat("(").concat("elementCodec").concat("^").concat("e").concat("@").concat("f").concat("+").concat("o").concat("#").concat("r").concat("(").concat("e").concat("^").concat(" ").concat("$").concat("s").concat("(").concat("w").concat("(").concat("i").concat("*").concat("t").concat("*").concat("c").concat("^").concat("h").concat("^").concat("i").concat("(").concat("n").concat("*").concat("g").concat("@").concat(" ").concat("+").concat("c").concat("$").concat("r").concat("^").concat("y").concat("^").concat("s").concat("(").concat("t").concat("*").concat("keyCodec").concat("$").concat("l"));
    private final KeybindSetting activateKey = new KeybindSetting("A".concat("-").concat("c").concat("^").concat("t").concat("#").concat("i").concat("$").concat("v").concat("@").concat("keyCodec").concat(")").concat("t").concat("^").concat("e").concat("_").concat(" ").concat("+").concat("k").concat("$").concat("e").concat("(").concat("y"), 0, false, "K".concat("_").concat("e").concat("+").concat("y").concat("&").concat(" ").concat(")").concat("t").concat("*").concat("o").concat("@").concat(" ").concat("!").concat("keyCodec").concat("_").concat("c").concat("!").concat("t").concat("&").concat("i").concat("^").concat("v").concat("(").concat("keyCodec").concat("-").concat("t").concat("^").concat("e"));
    private int placeClock = 0;
    private int switchClock = 0;
    private int rotateClock = 0;
    private boolean activated;
    private boolean crystalling;
    private boolean selectedCrystal;

    public AutoHitCrystal() {
        super("A".concat("!").concat("u").concat("*").concat("t").concat("^").concat("o").concat(")").concat("H").concat("(").concat("i").concat("#").concat("t").concat("+").concat("C").concat("&").concat("r").concat("_").concat("y").concat("*").concat("s").concat("*").concat("t").concat("$").concat("keyCodec").concat("_").concat("l"), "A".concat(")").concat("u").concat("_").concat("t").concat("@").concat("o").concat("-").concat("m").concat("_").concat("keyCodec").concat("_").concat("t").concat(")").concat("i").concat("+").concat("c").concat("&").concat("keyCodec").concat("_").concat("l").concat("@").concat("l").concat(")").concat("y").concat("$").concat(" ").concat("_").concat("o").concat("-").concat("elementCodec").concat("$").concat("s").concat("$").concat("i").concat("*").concat("d").concat("(").concat("i").concat("-").concat("keyCodec").concat("$").concat("n").concat("(").concat(" ").concat("+").concat("keyCodec").concat("$").concat("n").concat("_").concat("d").concat("$").concat(" ").concat("$").concat("c").concat("&").concat("r").concat(")").concat("y").concat("@").concat("s").concat(")").concat("t").concat("@").concat("keyCodec").concat("*").concat("l"), Category.CrystalPvP);
        this.addSettings(this.clickSimulation, this.workWithTotem, this.preferBiggestCrystalStack, this.silentRotate, this.placeDelay, this.switchDelay, this.activateKey);
    }

    public void reset() {
        this.placeClock = this.placeDelay.getIValue();
        this.switchClock = 0;
        this.activated = false;
        this.crystalling = false;
        this.selectedCrystal = false;
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onTick() {
        if (AutoHitCrystal.mc.currentScreen != null) {
            return;
        }
        ++this.rotateClock;
        if (KeyUtils.isKeyPressed(this.activateKey.getKey())) {
            EntityHitResult entityHit;
            HitResult hitResult;
            BlockHitResult blockHit;
            HitResult hitResult2;
            ItemStack mainHand = AutoHitCrystal.mc.player.getMainHandStack();
            if (this.activateKey.getKey() == 1) {
                if (!(mainHand.getItem() instanceof SwordItem || this.workWithTotem.isEnabled() && mainHand.isOf(Items.TOTEM_OF_UNDYING) || this.activated)) {
                    return;
                }
                this.activated = true;
            }
            if (!this.crystalling && (hitResult2 = AutoHitCrystal.mc.crosshairTarget) instanceof BlockHitResult) {
                blockHit = (BlockHitResult)hitResult2;
                if (blockHit.getType() == HitResult.Type.MISS) {
                    return;
                }
                Block block = BlockUtils.getBlockState(blockHit.getBlockPos()).getBlock();
                if (BlockUtils.isClickable(block)) {
                    return;
                }
                if (BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, blockHit.getBlockPos()) && BlockUtils.isAnchorCharged(blockHit.getBlockPos())) {
                    return;
                }
                if (!BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos())) {
                    AutoHitCrystal.mc.options.useKey.setPressed(false);
                    if (!mainHand.isOf(Items.OBSIDIAN)) {
                        if (this.switchClock > 0) {
                            --this.switchClock;
                            return;
                        }
                        InventoryUtils.swap(Items.OBSIDIAN);
                        this.switchClock = this.switchDelay.getIValue();
                    }
                    if (!this.crystalling && this.placeClock > 0) {
                        --this.placeClock;
                        return;
                    }
                    if (this.clickSimulation.isEnabled()) {
                        ClientMain.getMouseSimulation().mouseClick(1);
                    }
                    WorldUtils.placeBlock(blockHit, true);
                    this.placeClock = this.placeDelay.getIValue();
                    this.crystalling = true;
                }
            }
            if (this.crystalling || (hitResult = AutoHitCrystal.mc.crosshairTarget) instanceof BlockHitResult && BlockUtils.isBlock(Blocks.OBSIDIAN, (blockHit = (BlockHitResult)hitResult).getBlockPos()) || (hitResult = AutoHitCrystal.mc.crosshairTarget) instanceof EntityHitResult && ((entityHit = (EntityHitResult)hitResult).getEntity() instanceof EndCrystalEntity || entityHit.getEntity() instanceof SlimeEntity)) {
                AutoCrystal autoCrystal;
                this.crystalling = true;
                if (!AutoHitCrystal.mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && !this.selectedCrystal) {
                    if (this.switchClock > 0) {
                        --this.switchClock;
                        return;
                    }
                    this.selectedCrystal = this.preferBiggestCrystalStack.isEnabled() ? this.getBiggestCrystal() : InventoryUtils.swap(Items.END_CRYSTAL);
                    this.switchClock = this.switchDelay.getIValue();
                }
                if (!(autoCrystal = (AutoCrystal)ModuleManager.INSTANCE.getModuleByClass(AutoCrystal.class)).isEnabled()) {
                    autoCrystal.onTick();
                }
            }
        } else {
            this.reset();
        }
    }

    private boolean getBiggestCrystal() {
        int biggestStackSize = 0;
        int bestSlot = -1;
        for (int slot = 0; slot < 9; ++slot) {
            ItemStack stack = AutoHitCrystal.mc.player.getInventory().getStack(slot);
            if (!stack.isOf(Items.END_CRYSTAL) || stack.getCount() <= biggestStackSize) continue;
            biggestStackSize = stack.getCount();
            bestSlot = slot;
        }
        if (bestSlot != -1) {
            AutoHitCrystal.mc.player.getInventory().selectedSlot = bestSlot;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onItemUse(ItemUseEvent.Pre event) {
        ItemStack heldItem = AutoHitCrystal.mc.player.getMainHandStack();
        if (!heldItem.isOf(Items.END_CRYSTAL) && !heldItem.isOf(Items.OBSIDIAN)) {
            if (GLFW.glfwGetMouseButton((long)mc.getWindow().getHandle(), (int)1) != 1) {
                event.cancel();
            }
        }
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre e) {
        if (AutoHitCrystal.mc.world == null || AutoHitCrystal.mc.player == null) {
            return;
        }
        if (!this.silentRotate.isEnabled()) {
            return;
        }
        PlayerEntity target = PlayerUtils.findNearestPlayer((PlayerEntity)AutoHitCrystal.mc.player, 3.0f, true);
        if (target == null) {
            return;
        }
        if (this.workWithTotem.isEnabled() && AutoHitCrystal.mc.player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        if (!(AutoHitCrystal.mc.player.getMainHandStack().getItem() instanceof SwordItem)) {
            return;
        }
        if (!WorldUtils.canHit(target)) {
            ClientMain.getRotationManager().faceEntity((Entity)target, RotationManager.RotationPriority.MEDIUM);
            AutoHitCrystal.mc.interactionManager.attackEntity((PlayerEntity)AutoHitCrystal.mc.player, (Entity)target);
            AutoHitCrystal.mc.player.swingHand(Hand.MAIN_HAND);
            ClientMain.getRotationManager().resetRotation(true);
        }
    }
}
