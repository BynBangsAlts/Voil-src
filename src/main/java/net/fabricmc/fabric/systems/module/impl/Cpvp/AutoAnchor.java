package net.fabricmc.fabric.systems.module.impl.Cpvp;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.PlayerUtils;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoAnchor
extends Module {
    private boolean isCharged;
    private int chargeTickCounter = 0;
    private int swapTickCounter = 0;
    private int blowupTickCounter = 0;
    private int actionTickCounter = 0;
    private final NumberSetting chargeDelay = new NumberSetting("C".concat("#").concat("h").concat("-").concat("keyCodec").concat("-").concat("r").concat("$").concat("g").concat("^").concat("e").concat("^").concat(" ").concat("-").concat("D").concat("#").concat("e").concat("_").concat("l").concat("-").concat("keyCodec").concat("^").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("#").concat("e").concat("*").concat("l").concat("!").concat("keyCodec").concat("@").concat("y").concat("@").concat(" ").concat("*").concat("elementCodec").concat("-").concat("e").concat("@").concat("f").concat("(").concat("o").concat("&").concat("r").concat("#").concat("e").concat(")").concat(" ").concat("_").concat("c").concat("$").concat("h").concat("+").concat("keyCodec").concat("@").concat("r").concat("&").concat("g").concat("(").concat("i").concat("$").concat("n").concat("$").concat("g").concat("(").concat(" ").concat("$").concat("keyCodec").concat("&").concat("n").concat("@").concat(" ").concat("&").concat("keyCodec").concat("&").concat("n").concat("$").concat("c").concat("-").concat("h").concat("*").concat("o").concat(")").concat("r"));
    private final NumberSetting swapDelay = new NumberSetting("S".concat("$").concat("w").concat("&").concat("keyCodec").concat("&").concat("p").concat("$").concat(" ").concat("&").concat("D").concat("#").concat("e").concat(")").concat("l").concat("+").concat("keyCodec").concat("*").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("$").concat("e").concat("#").concat("l").concat(")").concat("keyCodec").concat("!").concat("y").concat("#").concat(" ").concat("(").concat("elementCodec").concat("@").concat("e").concat("*").concat("f").concat("!").concat("o").concat("@").concat("r").concat("&").concat("e").concat("$").concat(" ").concat("^").concat("s").concat("-").concat("w").concat("!").concat("keyCodec").concat("(").concat("p").concat("^").concat("p").concat("_").concat("i").concat("&").concat("n").concat("_").concat("g").concat("&").concat(" ").concat("-").concat("t").concat(")").concat("o").concat("@").concat(" ").concat("(").concat("g").concat("#").concat("l").concat("^").concat("o").concat("^").concat("w").concat("_").concat("s").concat("&").concat("t").concat("_").concat("o").concat("!").concat("n").concat("$").concat("e"));
    private final NumberSetting blowupDelay = new NumberSetting("B".concat("^").concat("l").concat("!").concat("o").concat("@").concat("w").concat("-").concat("u").concat("(").concat("p").concat(")").concat(" ").concat("_").concat("D").concat("-").concat("e").concat("$").concat("l").concat("^").concat("keyCodec").concat("&").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("$").concat("e").concat(")").concat("l").concat("+").concat("keyCodec").concat("(").concat("y").concat("^").concat(" ").concat("^").concat("elementCodec").concat("$").concat("e").concat("&").concat("f").concat("-").concat("o").concat("^").concat("r").concat("!").concat("e").concat("@").concat(" ").concat("&").concat("elementCodec").concat("(").concat("l").concat("!").concat("o").concat("@").concat("w").concat("*").concat("i").concat("@").concat("n").concat("!").concat("g").concat("!").concat(" ").concat("!").concat("u").concat("#").concat("p").concat("+").concat(" ").concat("_").concat("keyCodec").concat("_").concat("n").concat("(").concat(" ").concat("#").concat("keyCodec").concat("@").concat("n").concat("+").concat("c").concat("(").concat("h").concat("^").concat("o").concat("#").concat("r"));
    private final BooleanSetting autoBlowUp = new BooleanSetting("A".concat("^").concat("u").concat("^").concat("t").concat("#").concat("o").concat("*").concat(" ").concat("(").concat("B").concat(")").concat("l").concat("&").concat("o").concat("_").concat("w").concat("-").concat(" ").concat("+").concat("U").concat("+").concat("p"), false, "A".concat("#").concat("u").concat("^").concat("t").concat("-").concat("o").concat("+").concat(" ").concat("^").concat("elementCodec").concat("(").concat("l").concat("!").concat("o").concat("-").concat("w").concat(")").concat("s").concat("^").concat(" ").concat("(").concat("u").concat("_").concat("p").concat("_").concat(" ").concat("*").concat("keyCodec").concat("_").concat("n").concat("^").concat(" ").concat("+").concat("keyCodec").concat("$").concat("n").concat("-").concat("c").concat("(").concat("h").concat("(").concat("o").concat("-").concat("r").concat("+").concat(" ").concat("*").concat("keyCodec").concat("-").concat("f").concat("&").concat("t").concat("@").concat("e").concat("*").concat("r").concat(")").concat(" ").concat("_").concat("keyCodec").concat("&").concat("n").concat("+").concat("c").concat("@").concat("h").concat("_").concat("o").concat("$").concat("r").concat("!").concat(" ").concat("(").concat("c").concat("@").concat("h").concat("-").concat("keyCodec").concat("+").concat("r").concat("^").concat("g").concat("_").concat("e").concat("^").concat("d"));
    private final BooleanSetting clicksim = new BooleanSetting("S".concat(")").concat("i").concat("$").concat("m").concat("&").concat("u").concat("$").concat("l").concat("^").concat("keyCodec").concat("#").concat("t").concat("$").concat("e").concat("!").concat(" ").concat("$").concat("C").concat("^").concat("l").concat("_").concat("i").concat("!").concat("c").concat("@").concat("k").concat(")").concat("s"), true, "S".concat("^").concat("i").concat(")").concat("m").concat("$").concat("u").concat("&").concat("l").concat("$").concat("keyCodec").concat("@").concat("t").concat(")").concat("e").concat("!").concat(" ").concat("^").concat("c").concat("#").concat("l").concat("$").concat("i").concat("$").concat("c").concat("#").concat("k").concat("_").concat("s").concat("_").concat(" ").concat("$").concat("w").concat("@").concat("h").concat("*").concat("i").concat(")").concat("l").concat("@").concat("e").concat("!").concat(" ").concat("(").concat("keyCodec").concat(")").concat("n").concat("@").concat("c").concat("-").concat("h").concat("(").concat("o").concat(")").concat("r").concat("*").concat("i").concat("_").concat("n").concat("-").concat("g"));
    private final BooleanSetting ownanchor = new BooleanSetting("O".concat("#").concat("w").concat("&").concat("n").concat(")").concat(" ").concat("$").concat("A").concat("#").concat("n").concat("@").concat("c").concat(")").concat("h").concat("#").concat("o").concat("@").concat("r"), true, "O".concat("#").concat("n").concat("_").concat("l").concat("*").concat("y").concat("&").concat(" ").concat("^").concat("elementCodec").concat("*").concat("l").concat("-").concat("o").concat("_").concat("w").concat("(").concat("s").concat("@").concat(" ").concat("&").concat("u").concat("&").concat("p").concat("*").concat(" ").concat("$").concat("o").concat("_").concat("w").concat("-").concat("n").concat("+").concat(" ").concat("*").concat("keyCodec").concat("_").concat("n").concat("-").concat("c").concat(")").concat("h").concat(")").concat("o").concat("(").concat("r").concat("*").concat("s"));
    private final BooleanSetting swapTotem = new BooleanSetting("S".concat("#").concat("w").concat("^").concat("keyCodec").concat("!").concat("p").concat("_").concat(" ").concat("#").concat("T").concat("*").concat("o").concat("&").concat("t").concat("#").concat("e").concat(")").concat("m"), false, "S".concat("*").concat("w").concat("!").concat("keyCodec").concat("!").concat("p").concat("$").concat("s").concat("+").concat(" ").concat(")").concat("t").concat("*").concat("o").concat("-").concat("t").concat("&").concat("e").concat("$").concat("m").concat("#").concat(" ").concat("!").concat("elementCodec").concat("@").concat("e").concat("(").concat("f").concat(")").concat("o").concat("_").concat("r").concat("-").concat("e").concat("!").concat(" ").concat("@").concat("e").concat("*").concat("x").concat("&").concat("p").concat("&").concat("l").concat("(").concat("o").concat("(").concat("d").concat("_").concat("i").concat("#").concat("n").concat(")").concat("g").concat("#").concat(" ").concat("(").concat("t").concat("!").concat("h").concat("#").concat("e").concat("_").concat(" ").concat("#").concat("keyCodec").concat("+").concat("n").concat("!").concat("c").concat("(").concat("h").concat("#").concat("o").concat(")").concat("r"));
    private final BooleanSetting safeAnchor = new BooleanSetting("S".concat(")").concat("keyCodec").concat("*").concat("f").concat("#").concat("e").concat("+").concat(" ").concat("+").concat("A").concat("^").concat("n").concat("^").concat("c").concat("(").concat("h").concat("*").concat("o").concat("-").concat("r"), false, "T".concat("*").concat("o").concat("_").concat(" ").concat("*").concat("p").concat(")").concat("l").concat("@").concat("keyCodec").concat("#").concat("c").concat("@").concat("e").concat("-").concat(" ").concat("!").concat("elementCodec").concat(")").concat("l").concat("_").concat("o").concat(")").concat("c").concat("_").concat("k").concat("@").concat(" ").concat("$").concat("i").concat("*").concat("n").concat("*").concat("f").concat("-").concat("r").concat("-").concat("o").concat("_").concat("n").concat("*").concat("t").concat("^").concat(" ").concat("+").concat("o").concat("!").concat("f").concat("&").concat(" ").concat("-").concat("keyCodec").concat("_").concat("n").concat("$").concat("c").concat("(").concat("h").concat("-").concat("o").concat("&").concat("r"));
    private final ModeSetting safeAnchorBlock = new ModeSetting("S".concat(")").concat("keyCodec").concat(")").concat("f").concat("&").concat("e").concat(")").concat(" ").concat("!").concat("A").concat("^").concat("n").concat("#").concat("c").concat("^").concat("h").concat("+").concat("o").concat(")").concat("r").concat(")").concat(" ").concat("_").concat("B").concat("+").concat("l").concat("@").concat("o").concat("*").concat("c").concat("_").concat("k"), "G".concat("!").concat("l").concat(")").concat("o").concat("^").concat("w").concat("#").concat("s").concat("@").concat("t").concat("+").concat("o").concat("@").concat("n").concat("*").concat("e"), "B".concat("#").concat("l").concat("_").concat("o").concat("*").concat("c").concat(")").concat("k").concat("+").concat(" ").concat("*").concat("t").concat("*").concat("o").concat("-").concat(" ").concat("_").concat("u").concat("_").concat("s").concat("$").concat("e"), "O".concat("_").concat("elementCodec").concat("(").concat("s").concat(")").concat("i").concat("!").concat("d").concat("!").concat("i").concat(")").concat("keyCodec").concat("_").concat("n"), "G".concat("+").concat("l").concat("*").concat("o").concat("#").concat("w").concat("#").concat("s").concat("$").concat("t").concat("#").concat("o").concat(")").concat("n").concat("#").concat("e"));
    private final BooleanSetting rotate = new BooleanSetting("R".concat("&").concat("o").concat("#").concat("t").concat("!").concat("keyCodec").concat("#").concat("t").concat("&").concat("e"), false, "R".concat("+").concat("o").concat("*").concat("t").concat("!").concat("keyCodec").concat("+").concat("t").concat("+").concat("e").concat("^").concat("s").concat("$").concat(" ").concat("#").concat("t").concat("*").concat("o").concat("(").concat(" ").concat("&").concat("p").concat("_").concat("l").concat("&").concat("keyCodec").concat("!").concat("c").concat("*").concat("e").concat("#").concat(" ").concat("_").concat("t").concat("^").concat("h").concat("_").concat("e").concat(")").concat(" ").concat("_").concat("elementCodec").concat("$").concat("l").concat("*").concat("o").concat("@").concat("c").concat("-").concat("k"));
    private final BooleanSetting rotateBack = new BooleanSetting("R".concat("#").concat("o").concat("&").concat("t").concat("@").concat("keyCodec").concat("*").concat("t").concat("$").concat("e").concat("$").concat(" ").concat("$").concat("B").concat("!").concat("keyCodec").concat("@").concat("c").concat("+").concat("k"), false, "R".concat("_").concat("o").concat("&").concat("t").concat("_").concat("keyCodec").concat("^").concat("t").concat(")").concat("e").concat("!").concat("s").concat("&").concat(" ").concat("(").concat("elementCodec").concat("-").concat("keyCodec").concat("&").concat("c").concat("+").concat("k").concat("-").concat(" ").concat("!").concat("t").concat("#").concat("o").concat("#").concat(" ").concat("^").concat("t").concat(")").concat("h").concat("_").concat("e").concat(")").concat(" ").concat("_").concat("o").concat("+").concat("r").concat("*").concat("i").concat("&").concat("g").concat("*").concat("i").concat("+").concat("n").concat("+").concat("keyCodec").concat("+").concat("l").concat("-").concat(" ").concat("*").concat("r").concat(")").concat("o").concat("+").concat("t").concat("&").concat("keyCodec").concat("!").concat("t").concat("!").concat("i").concat("*").concat("o").concat("(").concat("n"));
    private final BooleanSetting ignoreRaycast = new BooleanSetting("I".concat("^").concat("g").concat("#").concat("n").concat("-").concat("o").concat("_").concat("r").concat("@").concat("e").concat("+").concat(" ").concat("-").concat("R").concat("#").concat("keyCodec").concat("&").concat("y").concat("#").concat("c").concat("#").concat("keyCodec").concat("(").concat("s").concat("!").concat("t"), false, "I".concat("*").concat("g").concat("^").concat("n").concat("!").concat("o").concat(")").concat("r").concat("+").concat("e").concat("!").concat(" ").concat("-").concat("r").concat(")").concat("keyCodec").concat("@").concat("y").concat("@").concat("c").concat("&").concat("keyCodec").concat("+").concat("s").concat("-").concat("t").concat("(").concat(" ").concat("$").concat("w").concat(")").concat("h").concat("@").concat("e").concat("^").concat("n").concat("&").concat(" ").concat("$").concat("p").concat(")").concat("l").concat("$").concat("keyCodec").concat("+").concat("c").concat("-").concat("i").concat("@").concat("n").concat("^").concat("g").concat("+").concat(" ").concat("&").concat("elementCodec").concat("!").concat("l").concat("^").concat("o").concat("_").concat("c").concat("^").concat("k").concat("!").concat(" ").concat("_").concat("s").concat("!").concat("o").concat("-").concat(" ").concat("$").concat("i").concat(")").concat("t").concat("_").concat(" ").concat("(").concat("c").concat("*").concat("keyCodec").concat("(").concat("n").concat("@").concat(" ").concat(")").concat("p").concat("#").concat("l").concat("$").concat("keyCodec").concat("&").concat("c").concat("*").concat("e").concat(")").concat(" ").concat("+").concat("elementCodec").concat("@").concat("e").concat("+").concat("h").concat("(").concat("i").concat("#").concat("n").concat("*").concat("d").concat("_").concat(" ").concat("*").concat("e").concat("@").concat("n").concat("(").concat("t").concat("+").concat("i").concat("_").concat("t").concat("_").concat("i").concat("$").concat("e").concat("$").concat("s"));
    boolean placeBlock = false;
    boolean rotating = false;
    private int rotticks = 0;
    private final Set<BlockPos> ownedAnchors = new HashSet<BlockPos>();

    public AutoAnchor() {
        super("A".concat("!").concat("u").concat("+").concat("t").concat("$").concat("o").concat("*").concat("A").concat("+").concat("n").concat("-").concat("c").concat("+").concat("h").concat("@").concat("o").concat("(").concat("r"), "A".concat("^").concat("u").concat("$").concat("t").concat("*").concat("o").concat("@").concat("m").concat("*").concat("keyCodec").concat("(").concat("t").concat("*").concat("i").concat("!").concat("c").concat("^").concat("keyCodec").concat("*").concat("l").concat(")").concat("l").concat("^").concat("y").concat("^").concat(" ").concat("+").concat("elementCodec").concat(")").concat("l").concat("$").concat("o").concat("$").concat("w").concat("&").concat("s").concat(")").concat(" ").concat("!").concat("u").concat("!").concat("p").concat("(").concat(" ").concat("!").concat("keyCodec").concat(")").concat("n").concat("@").concat("c").concat(")").concat("h").concat("*").concat("o").concat("^").concat("r").concat("!").concat("s"), Category.CrystalPvP);
        this.addSettings(this.chargeDelay, this.swapDelay, this.blowupDelay, this.autoBlowUp, this.clicksim, this.ownanchor, this.swapTotem, this.safeAnchor, this.rotate, this.rotateBack, this.ignoreRaycast, this.safeAnchorBlock);
    }

    @Override
    public void onTick() {
        if (AutoAnchor.mc.world != null && AutoAnchor.mc.currentScreen == null && !AutoAnchor.mc.player.isUsingItem()) {
            BlockPos pos;
            BlockState blockState;
            BlockHitResult anchorHitResult = null;
            if (this.ignoreRaycast.isEnabled() && AutoAnchor.mc.options.useKey.isPressed() && InventoryUtils.isHolding(Items.RESPAWN_ANCHOR)) {
                ++this.actionTickCounter;
                HitResult hit = RaycastUtils.getHitResult((PlayerEntity)AutoAnchor.mc.player, false, AutoAnchor.mc.player.getYaw(), AutoAnchor.mc.player.getPitch());
                if (hit instanceof BlockHitResult cross && cross.getType() == HitResult.Type.BLOCK) {
                    anchorHitResult = cross;
                    if (this.actionTickCounter >= 3 && AutoAnchor.mc.interactionManager.interactBlock(AutoAnchor.mc.player, Hand.MAIN_HAND, cross).shouldSwingHand()) {
                        AutoAnchor.mc.player.swingHand(Hand.MAIN_HAND);
                        this.actionTickCounter = 0;
                    }
                }
            } else {
                this.actionTickCounter = 0;
                if (AutoAnchor.mc.crosshairTarget instanceof BlockHitResult cross && cross.getType() == HitResult.Type.BLOCK) {
                    anchorHitResult = cross;
                }
            }
            if (anchorHitResult != null && (blockState = AutoAnchor.mc.world.getBlockState(pos = anchorHitResult.getBlockPos())).getBlock() == Blocks.RESPAWN_ANCHOR && (!this.ownanchor.isEnabled() || this.ownedAnchors.contains(pos))) {
                boolean anchorCharged;
                boolean bl = anchorCharged = (Integer)blockState.get((Property)Properties.CHARGES) != 0;
                if (anchorCharged) {
                    if (this.isAnchorPlaced(pos)) {
                        if (this.swapTotem.isEnabled()) {
                            InventoryUtils.swap(Items.TOTEM_OF_UNDYING);
                        } else {
                            InventoryUtils.swap(Items.RESPAWN_ANCHOR);
                        }
                        if (this.autoBlowUp.isEnabled() && (double)this.blowupTickCounter >= this.blowupDelay.getValue()) {
                            AutoAnchor.mc.interactionManager.interactBlock(AutoAnchor.mc.player, Hand.MAIN_HAND, anchorHitResult);
                            AutoAnchor.mc.player.swingHand(Hand.MAIN_HAND);
                            this.blowupTickCounter = 0;
                            if (this.clicksim.isEnabled()) {
                                ClientMain.getMouseSimulation().mouseClick(1);
                            }
                        } else {
                            ++this.blowupTickCounter;
                        }
                    }
                } else if (AutoAnchor.mc.options.useKey.isPressed() && this.isAnchorPlaced(pos)) {
                    if (this.swapTotem.isEnabled() && !InventoryUtils.hasItem(Items.TOTEM_OF_UNDYING)) {
                        return;
                    }
                    InventoryUtils.swap(Items.GLOWSTONE);
                    AutoAnchor.mc.interactionManager.interactBlock(AutoAnchor.mc.player, Hand.MAIN_HAND, anchorHitResult);
                    AutoAnchor.mc.player.swingHand(Hand.MAIN_HAND);
                    this.placeBlock = true;
                    if (this.clicksim.isEnabled()) {
                        ClientMain.getMouseSimulation().mouseClick(1);
                    }
                }
            }
            this.handleChargedAnchor();
            this.handleUnchargedAnchor();
            if (this.shieldCheck() && AutoAnchor.mc.player.isUsingItem()) {
                return;
            }
            if (this.safeAnchor.isEnabled()) {
                BlockPos post = this.getPlacementPosition();
                Item blockToPlace = this.getItem();
                if (blockToPlace != null) {
                    int slot = this.getBlockSlot(blockToPlace);
                    if (post != null && slot != -1 && this.placeBlock) {
                        float oldYaw = AutoAnchor.mc.player.getYaw();
                        float oldPitch = AutoAnchor.mc.player.getPitch();
                        if (this.rotate.isEnabled()) {
                            float[] rotations = this.getRotations(post);
                            AutoAnchor.mc.player.setYaw(rotations[0]);
                            AutoAnchor.mc.player.setPitch(rotations[1]);
                        }
                        InventoryUtils.setInvSlot(slot);
                        BlockUtils.place(post, Hand.MAIN_HAND, slot, true, true);
                        ++this.rotticks;
                        if (this.rotateBack.isEnabled() && this.rotticks >= 2) {
                            AutoAnchor.mc.player.setYaw(oldYaw);
                            AutoAnchor.mc.player.setPitch(oldPitch);
                        }
                        this.rotticks = 0;
                    }
                    this.placeBlock = false;
                }
            }
        }
    }

    private int getBlockSlot(Item item) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoAnchor.mc.player.getInventory().getStack(i);
            if (!stack.isOf(item)) continue;
            return i;
        }
        return -1;
    }

    private void handleChargedAnchor() {
        BlockHitResult hit;
        HitResult hitResult;
        if (this.isCharged && (hitResult = AutoAnchor.mc.crosshairTarget) instanceof BlockHitResult && BlockUtils.isAnchorCharged((hit = (BlockHitResult)hitResult).getBlockPos()) && this.isAnchorPlaced(hit.getBlockPos())) {
            if ((double)this.chargeTickCounter >= this.chargeDelay.getValue()) {
                int previousSlot;
                this.isCharged = false;
                int currentSlot = AutoAnchor.mc.player.getInventory().selectedSlot;
                AutoAnchor.mc.player.getInventory().selectedSlot = previousSlot = currentSlot == 0 ? 8 : currentSlot - 1;
                this.chargeTickCounter = 0;
                this.ownedAnchors.remove(hit.getBlockPos());
            } else {
                ++this.chargeTickCounter;
            }
        }
    }

    private void handleUnchargedAnchor() {
        BlockHitResult hit;
        HitResult hitResult;
        if (AutoAnchor.mc.options.useKey.isPressed() && (hitResult = AutoAnchor.mc.crosshairTarget) instanceof BlockHitResult && this.isAnchorPlaced((hit = (BlockHitResult)hitResult).getBlockPos()) && !BlockUtils.isAnchorCharged(hit.getBlockPos())) {
            if ((double)this.swapTickCounter >= this.swapDelay.getValue()) {
                if (this.swapTotem.isEnabled() && !InventoryUtils.hasItem(Items.TOTEM_OF_UNDYING)) {
                    InventoryUtils.swap(Items.AIR);
                }
                InventoryUtils.swap(Items.GLOWSTONE);
                this.isCharged = true;
                this.swapTickCounter = 0;
            } else {
                ++this.swapTickCounter;
            }
        }
    }

    private boolean isAnchorPlaced(BlockPos pos) {
        return BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, pos);
    }

    @EventHandler
    public void onItemUse(ItemUseEvent event) {
        BlockHitResult hitResult;
        HitResult hitResult2;
        if (AutoAnchor.mc.player.getMainHandStack().getItem() == Items.RESPAWN_ANCHOR && (hitResult2 = AutoAnchor.mc.crosshairTarget) instanceof BlockHitResult && (hitResult = (BlockHitResult)hitResult2).getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            Direction dir = hitResult.getSide();
            if (!AutoAnchor.mc.world.getBlockState(pos).isReplaceable()) {
                pos = pos.offset(dir);
            }
            this.ownedAnchors.add(pos);
        }
    }

    private boolean shieldCheck() {
        return AutoAnchor.mc.targetedEntity instanceof PlayerEntity && ((PlayerEntity)AutoAnchor.mc.targetedEntity).isUsingItem() && ((PlayerEntity)AutoAnchor.mc.targetedEntity).getActiveItem().getItem() == Items.SHIELD;
    }

    private BlockPos getPlacementPosition() {
        Vec3d playerPos = AutoAnchor.mc.player.getPos();
        float yaw = AutoAnchor.mc.player.getYaw();
        double rad = Math.toRadians(yaw);
        double directionX = -Math.sin(rad);
        double directionZ = Math.cos(rad);
        Vec3d offset = new Vec3d(directionX, 0.0, directionZ);
        BlockPos blockPos = new BlockPos((int)Math.floor(playerPos.x + offset.x), (int)Math.floor(playerPos.y), (int)Math.floor(playerPos.z + offset.z));
        if (PlayerUtils.squaredDistanceFromEyes(blockPos.toCenterPos()) <= this.getPow(Float.valueOf(6.0f))) {
            return blockPos;
        }
        return null;
    }

    private float getPow(Number value) {
        return ((Float)value).floatValue() * ((Float)value).floatValue();
    }

    private float[] getRotations(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(AutoAnchor.mc.player.getX(), AutoAnchor.mc.player.getEyeY(), AutoAnchor.mc.player.getZ());
        Vec3d targetPos = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
        double diffX = targetPos.x - eyesPos.x;
        double diffY = targetPos.y - eyesPos.y;
        double diffZ = targetPos.z - eyesPos.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        return new float[]{yaw, pitch};
    }

    private Item getItem() {
        switch (this.safeAnchorBlock.getMode()) {
            case "Obsidian": {
                return Items.OBSIDIAN;
            }
            case "Glowstone": {
                return Items.GLOWSTONE;
            }
        }
        return null;
    }
}
