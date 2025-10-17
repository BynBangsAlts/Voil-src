package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.PlayerUtils;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoCart
extends Module {
    private final NumberSetting placeDelay = new NumberSetting("R".concat("$").concat("keyCodec").concat("*").concat("i").concat("+").concat("l").concat("@").concat(" ").concat("$").concat("d").concat("!").concat("e").concat("&").concat("l").concat("_").concat("keyCodec").concat("*").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("+").concat("e").concat("-").concat("l").concat("(").concat("keyCodec").concat("&").concat("y").concat("(").concat(" ").concat("_").concat("t").concat("!").concat("o").concat(")").concat(" ").concat("+").concat("p").concat("-").concat("l").concat(")").concat("keyCodec").concat("$").concat("c").concat("_").concat("e").concat("!").concat(" ").concat("*").concat("r").concat("&").concat("keyCodec").concat("-").concat("i").concat("(").concat("l"));
    private final NumberSetting switchDelay = new NumberSetting("C".concat("+").concat("keyCodec").concat("#").concat("r").concat("&").concat("t").concat("(").concat(" ").concat("&").concat("d").concat(")").concat("e").concat(")").concat("l").concat("*").concat("keyCodec").concat("$").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("#").concat("e").concat("-").concat("l").concat("*").concat("keyCodec").concat("*").concat("y").concat("^").concat(" ").concat("(").concat("t").concat("*").concat("o").concat(")").concat(" ").concat("*").concat("s").concat("!").concat("w").concat("#").concat("i").concat("!").concat("t").concat("#").concat("c").concat("_").concat("h").concat("+").concat(" ").concat("!").concat("c").concat("&").concat("keyCodec").concat("!").concat("r").concat("#").concat("t"));
    private final NumberSetting bowSwitchBackDelay = new NumberSetting("B".concat("_").concat("o").concat("!").concat("w").concat("^").concat(" ").concat(")").concat("S").concat("_").concat("w").concat("@").concat("i").concat("#").concat("t").concat("_").concat("c").concat(")").concat("h").concat("(").concat(" ").concat("$").concat("B").concat("@").concat("keyCodec").concat("-").concat("c").concat("#").concat("k").concat("+").concat(" ").concat("_").concat("D").concat("_").concat("e").concat("$").concat("l").concat("*").concat("keyCodec").concat("^").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("_").concat("e").concat("#").concat("l").concat("$").concat("keyCodec").concat("-").concat("y").concat("_").concat(" ").concat("+").concat("t").concat("!").concat("o").concat("*").concat(" ").concat("$").concat("s").concat("+").concat("w").concat("*").concat("i").concat("*").concat("t").concat(")").concat("c").concat("+").concat("h").concat("*").concat(" ").concat("$").concat("elementCodec").concat(")").concat("keyCodec").concat("#").concat("c").concat("-").concat("k").concat("(").concat(" ").concat("#").concat("t").concat("_").concat("o").concat("#").concat(" ").concat(")").concat("t").concat(")").concat("h").concat(")").concat("e").concat(")").concat(" ").concat("+").concat("elementCodec").concat("@").concat("o").concat("+").concat("w").concat("*").concat(" ").concat("+").concat("keyCodec").concat("*").concat("f").concat("!").concat("t").concat("@").concat("e").concat("!").concat("r").concat("_").concat(" ").concat("#").concat("p").concat("^").concat("l").concat("@").concat("keyCodec").concat("#").concat("c").concat("&").concat("i").concat("$").concat("n").concat("-").concat("g").concat("+").concat(" ").concat("@").concat("T").concat(")").concat("N").concat("(").concat("T").concat("#").concat(" ").concat("#").concat("c").concat(")").concat("keyCodec").concat("#").concat("r").concat("_").concat("t"));
    private final BooleanSetting switchBackToBow = new BooleanSetting("S".concat(")").concat("w").concat("-").concat("i").concat("$").concat("t").concat("*").concat("c").concat("#").concat("h").concat("(").concat(" ").concat("@").concat("B").concat("!").concat("keyCodec").concat("+").concat("c").concat(")").concat("k").concat("+").concat(" ").concat("(").concat("t").concat("_").concat("o").concat("!").concat(" ").concat("+").concat("B").concat("!").concat("o").concat("&").concat("w"), true, "S".concat("!").concat("w").concat("#").concat("i").concat("*").concat("t").concat("#").concat("c").concat("#").concat("h").concat("-").concat(" ").concat("&").concat("elementCodec").concat("(").concat("keyCodec").concat("@").concat("c").concat("#").concat("k").concat("+").concat(" ").concat(")").concat("t").concat("^").concat("o").concat("@").concat(" ").concat("^").concat("t").concat("-").concat("h").concat("@").concat("e").concat("_").concat(" ").concat("@").concat("elementCodec").concat("^").concat("o").concat("+").concat("w").concat("-").concat(" ").concat("+").concat("keyCodec").concat("#").concat("f").concat("#").concat("t").concat("+").concat("e").concat("#").concat("r").concat("*").concat(" ").concat("_").concat("p").concat("@").concat("l").concat("*").concat("keyCodec").concat("-").concat("c").concat(")").concat("i").concat("$").concat("n").concat("^").concat("g").concat("*").concat(" ").concat("(").concat("T").concat("-").concat("N").concat("_").concat("T").concat(")").concat(" ").concat("-").concat("c").concat(")").concat("keyCodec").concat("@").concat("r").concat("$").concat("t"));
    private final BooleanSetting safeBlocks = new BooleanSetting("S".concat("!").concat("keyCodec").concat(")").concat("f").concat(")").concat("e").concat("@").concat(" ").concat("!").concat("B").concat("@").concat("l").concat("^").concat("o").concat(")").concat("c").concat("-").concat("k"), true, "P".concat("@").concat("l").concat("&").concat("keyCodec").concat("$").concat("c").concat("_").concat("e").concat("^").concat("s").concat("$").concat(" ").concat("&").concat("keyCodec").concat("^").concat(" ").concat("+").concat("elementCodec").concat("+").concat("l").concat("!").concat("o").concat(")").concat("c").concat("^").concat("k").concat("_").concat(" ").concat("^").concat("elementCodec").concat("&").concat("e").concat("_").concat("t").concat("@").concat("w").concat("+").concat("e").concat("*").concat("e").concat("&").concat("n").concat("(").concat(" ").concat("^").concat("y").concat("*").concat("o").concat("#").concat("u").concat("_").concat(" ").concat("&").concat("keyCodec").concat("+").concat("n").concat("&").concat("d").concat("_").concat(" ").concat("(").concat("t").concat("&").concat("h").concat(")").concat("e").concat(")").concat(" ").concat("*").concat("t").concat("-").concat("n").concat(")").concat("t").concat("&").concat(" ").concat("^").concat("c").concat("(").concat("keyCodec").concat("@").concat("r").concat("^").concat("t"));
    private Vec3d rotationVec = Vec3d.ZERO;
    private int savedslot = -1;
    private BlockPos bp;
    private int placeDelayTicks = 0;
    private int switchDelayTicks = 0;
    private int bowSwitchBackTicks = -1;
    private int railDelayTicks = 0;
    private boolean switchRail = false;
    boolean placeBlock = false;

    public AutoCart() {
        super("C".concat("#").concat("keyCodec").concat("*").concat("r").concat("!").concat("t").concat("$").concat("M").concat("!").concat("keyCodec").concat("#").concat("c").concat("@").concat("r").concat("+").concat("o"), "H".concat("+").concat("e").concat("_").concat("l").concat("-").concat("p").concat("#").concat("s").concat("&").concat(" ").concat("*").concat("y").concat("@").concat("o").concat("!").concat("u").concat("!").concat(" ").concat("+").concat("w").concat("#").concat("i").concat("^").concat("t").concat("*").concat("h").concat("(").concat(" ").concat("*").concat("c").concat("#").concat("keyCodec").concat("$").concat("r").concat("^").concat("t").concat("_").concat(" ").concat("#").concat("p").concat("#").concat("v").concat("-").concat("p"), Category.Combat);
        this.addSettings(this.placeDelay, this.switchDelay, this.bowSwitchBackDelay, this.switchBackToBow, this.safeBlocks);
    }

    @EventHandler
    public void onPacketSendPost(PacketEvent.SendPost event) {
        PlayerActionC2SPacket action;
        Packet<?> packet = event.getPacket();
        if (packet instanceof PlayerActionC2SPacket && (action = (PlayerActionC2SPacket)packet).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM && AutoCart.mc.player.getMainHandStack().getItem() == Items.BOW) {
            this.bp = WorldUtils.calcTrajectory(AutoCart.mc.player.getYaw());
            if (this.bp != null && PlayerUtils.squaredDistanceFromEyes(this.bp.toCenterPos()) <= this.getPow(Float.valueOf(6.0f)) && PlayerUtils.squaredDistanceFromEyes(this.bp.toCenterPos()) > 3.0f && this.hasRail() && InventoryUtils.hasItem(Items.TNT_MINECART)) {
                this.savedslot = AutoCart.mc.player.getInventory().selectedSlot;
                int slot = this.getRail();
                if (this.switchRail) {
                    InventoryUtils.setInvSlot(slot);
                }
                PacketUtils.sendSequencedPacket(s -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d((double)this.bp.getX() + 0.5, (double)this.bp.up().getY(), (double)this.bp.getZ() + 0.5), Direction.UP, this.bp, false), s));
                this.rotationVec = this.bp.up().toCenterPos();
                this.switchRail = false;
                this.railDelayTicks = this.placeDelay.getIValue();
                this.switchDelayTicks = this.switchDelay.getIValue();
            }
        }
    }

    @Override
    public void onTick() {
        BlockPos blockToPlace;
        int cartSlot = InventoryUtils.findItemSlot(Items.TNT_MINECART);
        if (this.placeDelayTicks > 0) {
            --this.placeDelayTicks;
        }
        if (this.railDelayTicks > 0) {
            --this.railDelayTicks;
        }
        if (this.switchDelayTicks > 0) {
            --this.switchDelayTicks;
        }
        if (this.railDelayTicks == 0) {
            this.switchRail = true;
        }
        if (this.switchDelayTicks == 0 && this.savedslot != -1) {
            InventoryUtils.setInvSlot(cartSlot);
            PacketUtils.sendSequencedPacket(s -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d((double)this.bp.getX() + 0.5, (double)this.bp.up().getY() + 0.125, (double)this.bp.getZ() + 0.5), Direction.UP, this.bp.up(), false), s));
            this.switchDelayTicks = -1;
            this.placeBlock = true;
            if (this.switchBackToBow.isEnabled()) {
                this.bowSwitchBackTicks = this.bowSwitchBackDelay.getIValue();
            }
        }
        if (this.bowSwitchBackTicks > 0) {
            --this.bowSwitchBackTicks;
        } else if (this.bowSwitchBackTicks == 0 && this.switchBackToBow.isEnabled()) {
            InventoryUtils.swap(Items.BOW);
            this.bowSwitchBackTicks = -1;
        }
        if (this.safeBlocks.isEnabled() && this.placeBlock && (blockToPlace = this.getPlacementPosition()) != null) {
            int slot = this.getBlockSlot();
            InventoryUtils.setInvSlot(slot);
            BlockUtils.place(blockToPlace, Hand.MAIN_HAND, slot, true, true);
            this.placeBlock = false;
        }
    }

    private float getPow(Number value) {
        return ((Float)value).floatValue() * ((Float)value).floatValue();
    }

    private boolean hasRail() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoCart.mc.player.getInventory().getStack(i);
            if (stack.getItem() != Items.RAIL && stack.getItem() != Items.POWERED_RAIL && stack.getItem() != Items.DETECTOR_RAIL && stack.getItem() != Items.ACTIVATOR_RAIL) continue;
            return true;
        }
        return false;
    }

    private int getRail() {
        PlayerInventory inv = AutoCart.mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = inv.getStack(i);
            if (!this.isRail(stack.getItem())) continue;
            return i;
        }
        return -1;
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = AutoCart.mc.player.getInventory().getStack(i);
            if (!(item.getItem() instanceof BlockItem) || this.isRail(item.getItem())) continue;
            return i;
        }
        return -1;
    }

    private boolean isRail(Item item) {
        return item == Items.RAIL || item == Items.POWERED_RAIL || item == Items.DETECTOR_RAIL || item == Items.ACTIVATOR_RAIL;
    }

    private BlockPos getPlacementPosition() {
        Vec3d playerPos = AutoCart.mc.player.getPos();
        float yaw = AutoCart.mc.player.getYaw();
        double rad = Math.toRadians(yaw);
        double directionX = -Math.sin(rad);
        double directionZ = Math.cos(rad);
        Vec3d offset = new Vec3d(directionX, 0.0, directionZ);
        BlockPos blockPos = new BlockPos((int)Math.floor(playerPos.x + offset.x), (int)Math.floor(playerPos.y), (int)Math.floor(playerPos.z + offset.z));
        if (PlayerUtils.squaredDistanceFromEyes(blockPos.toCenterPos()) <= this.getPow(Float.valueOf(6.0f)) && !(AutoCart.mc.world.getBlockState(blockPos).getBlock() instanceof RailBlock)) {
            return blockPos;
        }
        return null;
    }
}
