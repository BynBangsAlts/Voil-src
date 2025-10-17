package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class AutoDoubleHand
extends Module {
    private final NumberSetting health = new NumberSetting("H".concat("^").concat("e").concat("(").concat("keyCodec").concat("*").concat("l").concat("+").concat("t").concat(")").concat("h"), 1.0, 20.0, 10.0, 1.0, "M".concat("^").concat("i").concat(")").concat("n").concat(")").concat("i").concat("+").concat("m").concat("+").concat("u").concat("$").concat("m").concat("&").concat(" ").concat("^").concat("h").concat("*").concat("e").concat("_").concat("keyCodec").concat("+").concat("l").concat("*").concat("t").concat("#").concat("h").concat("!").concat(" ").concat("_").concat("t").concat(")").concat("o").concat(")").concat(" ").concat("#").concat("s").concat("-").concat("w").concat("+").concat("keyCodec").concat("!").concat("p").concat("-").concat(" ").concat("#").concat("t").concat("&").concat("o").concat("!").concat(" ").concat("^").concat("t").concat("*").concat("o").concat("^").concat("t").concat("@").concat("e").concat("#").concat("m"));
    private final BooleanSetting dhandAir = new BooleanSetting("D".concat("@").concat("o").concat("@").concat("u").concat("^").concat("elementCodec").concat("(").concat("l").concat("*").concat("e").concat("!").concat(" ").concat("*").concat("h").concat("!").concat("keyCodec").concat("#").concat("n").concat("@").concat("d").concat(")").concat(" ").concat("-").concat("o").concat(")").concat("n").concat("!").concat(" ").concat("-").concat("keyCodec").concat("#").concat("i").concat("*").concat("r"), true, "D".concat("+").concat("o").concat("(").concat("u").concat("!").concat("elementCodec").concat("&").concat("l").concat("-").concat("e").concat("$").concat(" ").concat("(").concat("h").concat("_").concat("keyCodec").concat("-").concat("n").concat("+").concat("d").concat("#").concat(" ").concat("#").concat("o").concat("^").concat("n").concat("^").concat(" ").concat(")").concat("keyCodec").concat("!").concat("i").concat("*").concat("r"));
    private final BooleanSetting dhandAfterPop = new BooleanSetting("D".concat(")").concat("h").concat("!").concat("keyCodec").concat(")").concat("n").concat("+").concat("d").concat("&").concat(" ").concat("-").concat("keyCodec").concat("(").concat("f").concat("(").concat("t").concat("+").concat("e").concat("&").concat("r").concat("-").concat(" ").concat("*").concat("p").concat("$").concat("o").concat("&").concat("p"), true, "D".concat("#").concat("o").concat("&").concat("u").concat(")").concat("elementCodec").concat("-").concat("l").concat("$").concat("e").concat("*").concat(" ").concat("+").concat("h").concat("!").concat("keyCodec").concat("&").concat("n").concat("@").concat("d").concat("&").concat(" ").concat("@").concat("keyCodec").concat(")").concat("f").concat(")").concat("t").concat("@").concat("e").concat("!").concat("r").concat("_").concat(" ").concat("_").concat("p").concat("@").concat("o").concat("@").concat("p").concat("+").concat("p").concat("_").concat("i").concat("*").concat("n").concat("@").concat("g").concat("&").concat(" ").concat(")").concat("keyCodec").concat("&").concat(" ").concat("#").concat("t").concat("$").concat("o").concat("$").concat("t").concat("#").concat("e").concat("*").concat("m"));
    private final BooleanSetting predictCrystal = new BooleanSetting("C".concat("_").concat("h").concat("$").concat("e").concat("&").concat("c").concat("(").concat("k").concat("@").concat(" ").concat("$").concat("P").concat(")").concat("l").concat("#").concat("keyCodec").concat("-").concat("y").concat("*").concat("e").concat("_").concat("r").concat("_").concat("s").concat("@").concat(" ").concat("-").concat("L").concat("$").concat("o").concat("#").concat("o").concat("^").concat("k"), true, "D".concat("@").concat("o").concat("_").concat("u").concat("@").concat("elementCodec").concat("#").concat("l").concat("+").concat("e").concat("(").concat(" ").concat("^").concat("h").concat("@").concat("keyCodec").concat("#").concat("n").concat("@").concat("d").concat("-").concat(" ").concat("_").concat("i").concat(")").concat("f").concat("(").concat(" ").concat(")").concat("p").concat("&").concat("l").concat("$").concat("keyCodec").concat("$").concat("y").concat("^").concat("e").concat(")").concat("r").concat("#").concat(" ").concat("^").concat("i").concat("^").concat("s").concat("#").concat(" ").concat("-").concat("l").concat("@").concat("o").concat("#").concat("o").concat("_").concat("k").concat("^").concat("i").concat("(").concat("n").concat("$").concat("g").concat("*").concat(" ").concat("$").concat("keyCodec").concat("!").concat("t").concat("*").concat(" ").concat("@").concat("m").concat("#").concat("e"));
    private final BooleanSetting predictSword = new BooleanSetting("P".concat("@").concat("r").concat("#").concat("e").concat("!").concat("d").concat("$").concat("i").concat("+").concat("c").concat("-").concat("t").concat("+").concat(" ").concat("-").concat("S").concat("-").concat("w").concat("#").concat("o").concat("!").concat("r").concat("#").concat("d"), true, "D".concat("-").concat("o").concat("_").concat("u").concat("^").concat("elementCodec").concat(")").concat("l").concat("@").concat("e").concat(")").concat(" ").concat("$").concat("h").concat("-").concat("keyCodec").concat("(").concat("n").concat("+").concat("d").concat("(").concat(" ").concat("-").concat("i").concat("!").concat("f").concat("(").concat(" ").concat("-").concat("p").concat("(").concat("l").concat("-").concat("keyCodec").concat("_").concat("y").concat("^").concat("e").concat("$").concat("r").concat("*").concat(" ").concat("+").concat("i").concat("$").concat("s").concat("^").concat(" ").concat("!").concat("keyCodec").concat("(").concat("i").concat("-").concat("m").concat("&").concat("i").concat("#").concat("n").concat("#").concat("g").concat("_").concat(" ").concat("*").concat("keyCodec").concat("(").concat("t").concat("_").concat(" ").concat("_").concat("m").concat("-").concat("e").concat("(").concat(" ").concat("+").concat("w").concat("*").concat("i").concat("*").concat("t").concat("&").concat("h").concat("(").concat(" ").concat("$").concat("keyCodec").concat("#").concat(" ").concat("-").concat("s").concat("#").concat("w").concat(")").concat("o").concat("@").concat("r").concat("*").concat("d"));
    private final BooleanSetting predictObsidian = new BooleanSetting("P".concat("#").concat("r").concat("^").concat("e").concat("_").concat("d").concat("_").concat("i").concat("#").concat("c").concat("^").concat("t").concat("#").concat(" ").concat("#").concat("O").concat("+").concat("elementCodec").concat("*").concat("s").concat("&").concat("i").concat("&").concat("d").concat("@").concat("i").concat("+").concat("keyCodec").concat("_").concat("n"), true, "D".concat("^").concat("o").concat("@").concat("u").concat("^").concat("elementCodec").concat("#").concat("l").concat("-").concat("e").concat(")").concat(" ").concat("-").concat("h").concat("_").concat("keyCodec").concat("_").concat("n").concat("*").concat("d").concat(")").concat(" ").concat("-").concat("i").concat("#").concat("f").concat("-").concat(" ").concat("*").concat("o").concat("#").concat("elementCodec").concat("+").concat("s").concat(")").concat("i").concat("&").concat("d").concat("$").concat("i").concat("@").concat("keyCodec").concat("-").concat("n").concat("^").concat(" ").concat("&").concat("n").concat(")").concat("e").concat("$").concat("keyCodec").concat("$").concat("r").concat("#").concat("elementCodec").concat("*").concat("y"));
    private final BooleanSetting dontstick = new BooleanSetting("D".concat("&").concat("o").concat("!").concat("n").concat("&").concat("t").concat("!").concat(" ").concat("$").concat("s").concat("(").concat("t").concat("$").concat("i").concat("@").concat("c").concat("-").concat("k"), false, "O".concat("$").concat("n").concat("*").concat("l").concat("!").concat("y").concat("*").concat(" ").concat("_").concat("s").concat("+").concat("w").concat("+").concat("i").concat("#").concat("t").concat(")").concat("c").concat(")").concat("h").concat("$").concat("e").concat("!").concat("s").concat("@").concat(" ").concat("#").concat("t").concat("(").concat("o").concat("@").concat(" ").concat("!").concat("t").concat(")").concat("o").concat(")").concat("t").concat("+").concat("e").concat("#").concat("m").concat("-").concat(" ").concat("*").concat("o").concat("#").concat("n").concat("&").concat("c").concat("$").concat("e"));
    boolean switched = false;

    public AutoDoubleHand() {
        super("A".concat("&").concat("u").concat("-").concat("t").concat("*").concat("o").concat("*").concat("D").concat("(").concat("o").concat("_").concat("u").concat("@").concat("elementCodec").concat("*").concat("l").concat("&").concat("e").concat("!").concat("H").concat("&").concat("keyCodec").concat(")").concat("n").concat("*").concat("d"), "A".concat("$").concat("u").concat(")").concat("t").concat("_").concat("o").concat(")").concat("m").concat("$").concat("keyCodec").concat("^").concat("t").concat("@").concat("i").concat("_").concat("c").concat("&").concat("keyCodec").concat("!").concat("l").concat("&").concat("l").concat("$").concat("y").concat("@").concat(" ").concat("#").concat("s").concat("*").concat("w").concat("*").concat("keyCodec").concat("-").concat("p").concat("#").concat("s").concat("!").concat(" ").concat("^").concat("t").concat("&").concat("o").concat("^").concat("t").concat("@").concat("e").concat("!").concat("m").concat("*").concat(" ").concat("*").concat("o").concat("(").concat("n").concat("#").concat(" ").concat("+").concat("c").concat("#").concat("e").concat("(").concat("r").concat(")").concat("t").concat("@").concat("keyCodec").concat("#").concat("i").concat("_").concat("n").concat("_").concat(" ").concat("_").concat("c").concat("^").concat("o").concat("^").concat("n").concat(")").concat("d").concat("-").concat("i").concat("!").concat("t").concat("#").concat("i").concat(")").concat("o").concat("!").concat("n").concat("#").concat("s"), Category.CrystalPvP);
        this.addSettings(this.health, this.dhandAir, this.dhandAfterPop, this.predictCrystal, this.predictSword, this.dontstick);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.switched = false;
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || AutoDoubleHand.mc.player == null) {
            return;
        }
        if (this.shouldSwapToTotem()) {
            this.swapToTotem();
        }
    }

    private boolean shouldSwapToTotem() {
        double currentHealth = AutoDoubleHand.mc.player.getHealth() + AutoDoubleHand.mc.player.getAbsorptionAmount();
        if (currentHealth <= (double)this.health.getFloatValue()) {
            return true;
        }
        if (this.dhandAir.isEnabled() && AutoDoubleHand.mc.player.fallDistance > 0.0f) {
            return true;
        }
        if (this.predictSword.isEnabled() && this.isPlayerAimingAtMe()) {
            return true;
        }
        if (this.dhandAfterPop.isEnabled()) {
            return true;
        }
        if (this.predictObsidian.isEnabled() && this.arePlayersAimingAtBlock()) {
            return true;
        }
        if (this.predictCrystal.isEnabled()) {
            return AutoDoubleHand.mc.world.getEntitiesByClass(EndCrystalEntity.class, AutoDoubleHand.mc.player.getBoundingBox().expand(10.0), endCrystal -> true).stream().anyMatch(this::arePlayersAimingAtCrystal);
        }
        return false;
    }

    private void swapToTotem() {
        if (AutoDoubleHand.mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            if (this.dontstick.isEnabled() && this.switched) {
                return;
            }
            InventoryUtils.swap(Items.TOTEM_OF_UNDYING);
            this.switched = true;
            if (!this.dontstick.isEnabled()) {
                this.switched = false;
            }
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        EntityStatusS2CPacket packet;
        if (event.getPacket() instanceof EntityStatusS2CPacket && (packet = (EntityStatusS2CPacket)event.getPacket()).getStatus() == 35 && packet.getEntity((World)AutoDoubleHand.mc.world) == AutoDoubleHand.mc.player) {
            this.switched = false;
        }
    }

    private boolean isPlayerAimingAtMe() {
        Box clientPlayerBox = AutoDoubleHand.mc.player.getBoundingBox().expand(0.5);
        for (PlayerEntity otherPlayer : AutoDoubleHand.mc.world.getPlayers()) {
            if (otherPlayer.equals((Object)AutoDoubleHand.mc.player)) continue;
            Vec3d otherPlayerEyePos = otherPlayer.getEyePos();
            Vec3d lookDirection = otherPlayer.getRotationVec(1.0f);
            Vec3d endPos = otherPlayerEyePos.add(lookDirection.multiply(50.0));
            RaycastContext context = new RaycastContext(otherPlayerEyePos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)otherPlayer);
            BlockHitResult hitResult = AutoDoubleHand.mc.world.raycast(context);
            if (!clientPlayerBox.intersects(otherPlayerEyePos, endPos) && (hitResult.getType() == HitResult.Type.MISS || !clientPlayerBox.contains(hitResult.getPos()))) continue;
            return true;
        }
        return false;
    }

    private boolean arePlayersAimingAtBlock() {
        for (PlayerEntity player : AutoDoubleHand.mc.world.getPlayers()) {
            Block hitBlock;
            Vec3d lookDirection;
            Vec3d endVec;
            Vec3d playerEyePosition;
            RaycastContext raycastContext;
            BlockHitResult hitResult;
            if (player.equals((Object)AutoDoubleHand.mc.player) || (hitResult = AutoDoubleHand.mc.world.raycast(raycastContext = new RaycastContext(playerEyePosition = player.getEyePos(), endVec = playerEyePosition.add((lookDirection = player.getRotationVec(1.0f)).multiply(100.0)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)player))).getType() != HitResult.Type.BLOCK || hitResult.getBlockPos() == null || (hitBlock = AutoDoubleHand.mc.world.getBlockState(hitResult.getBlockPos()).getBlock()) != Blocks.OBSIDIAN && hitBlock != Blocks.BEDROCK && hitBlock != Blocks.RESPAWN_ANCHOR) continue;
            return true;
        }
        return false;
    }

    private boolean arePlayersAimingAtCrystal(EndCrystalEntity crystal) {
        for (PlayerEntity player : AutoDoubleHand.mc.world.getPlayers()) {
            Vec3d lookVec;
            Vec3d end;
            if (player.equals((Object)AutoDoubleHand.mc.player)) continue;
            Vec3d start = player.getEyePos();
            Box visionBox = new Box(start, end = start.add(lookVec = RaycastUtils.getPlayerLookVec(player)));
            return AutoDoubleHand.mc.world.getEntitiesByClass(EndCrystalEntity.class, visionBox, endCrystal -> endCrystal.equals((Object)crystal)).size() > 0;
        }
        return false;
    }
}
