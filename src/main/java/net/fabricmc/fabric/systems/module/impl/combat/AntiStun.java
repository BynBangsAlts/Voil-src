package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AntiStun
extends Module {
    BooleanSetting healthCheck = new BooleanSetting("H".concat("&").concat("e").concat("_").concat("keyCodec").concat("+").concat("l").concat("^").concat("t").concat("+").concat("h").concat("(").concat(" ").concat("@").concat("C").concat("$").concat("h").concat("*").concat("e").concat(")").concat("c").concat("+").concat("k"), true, "I".concat("@").concat("f").concat("#").concat(" ").concat("$").concat("h").concat("+").concat("e").concat("(").concat("keyCodec").concat("#").concat("l").concat("!").concat("t").concat("@").concat("h").concat(")").concat(" ").concat("_").concat("i").concat("&").concat("s").concat("$").concat(" ").concat("$").concat("elementCodec").concat("(").concat("e").concat("$").concat("l").concat(")").concat("o").concat("-").concat("w").concat("$").concat(" ").concat("+").concat("keyCodec").concat(")").concat(" ").concat(")").concat("c").concat("(").concat("e").concat("&").concat("r").concat("*").concat("t").concat("-").concat("keyCodec").concat("+").concat("i").concat("@").concat("n").concat("@").concat(" ").concat("(").concat("v").concat("@").concat("keyCodec").concat("(").concat("l").concat("_").concat("u").concat("_").concat("e").concat("!").concat(" ").concat("#").concat("i").concat("!").concat("t").concat("^").concat(" ").concat("-").concat("w").concat("-").concat("o").concat("@").concat("n").concat("@").concat("t").concat("#").concat(" ").concat(")").concat("u").concat("#").concat("n").concat("&").concat("s").concat("^").concat("h").concat("&").concat("i").concat("(").concat("e").concat("#").concat("l").concat("^").concat("d"));
    NumberSetting health = new NumberSetting("H".concat("^").concat("e").concat("+").concat("keyCodec").concat("^").concat("l").concat("*").concat("t").concat("-").concat("h"), 1.0, 19.0, 10.0, 1.0, "I".concat("$").concat("f").concat("*").concat(" ").concat("*").concat("h").concat("$").concat("e").concat("(").concat("keyCodec").concat("_").concat("l").concat(")").concat("t").concat("@").concat("h").concat("@").concat(" ").concat("+").concat("i").concat("+").concat("s").concat("$").concat(" ").concat("!").concat("elementCodec").concat("^").concat("e").concat("&").concat("l").concat("$").concat("o").concat("@").concat("w").concat("!").concat(" ").concat("(").concat("t").concat("!").concat("h").concat("-").concat("i").concat("_").concat("s").concat("$").concat(" ").concat("^").concat("v").concat("$").concat("keyCodec").concat("&").concat("l").concat(")").concat("u").concat("&").concat("e").concat("^").concat(" ").concat("#").concat("i").concat("$").concat("t").concat("&").concat(" ").concat("!").concat("w").concat("_").concat("o").concat("#").concat("n").concat("&").concat("t").concat("#").concat(" ").concat("^").concat("u").concat("!").concat("n").concat("&").concat("s").concat("^").concat("h").concat("_").concat("i").concat("+").concat("e").concat("@").concat("l").concat("&").concat("d"));
    boolean unShield;

    public AntiStun() {
        super("A".concat("-").concat("n").concat("*").concat("t").concat("_").concat("i").concat(")").concat("S").concat("$").concat("t").concat("-").concat("u").concat("!").concat("n"), "U".concat("^").concat("n").concat("!").concat("-").concat("(").concat("s").concat("@").concat("h").concat("^").concat("i").concat("&").concat("e").concat("&").concat("l").concat("+").concat("d").concat("#").concat("s").concat("-").concat(" ").concat(")").concat("w").concat("!").concat("h").concat("(").concat("e").concat("#").concat("n").concat("$").concat(" ").concat("@").concat("e").concat("_").concat("n").concat("+").concat("e").concat("-").concat("m").concat("+").concat("y").concat("+").concat(" ").concat("#").concat("t").concat("_").concat("r").concat(")").concat("i").concat(")").concat("e").concat("#").concat("s").concat("(").concat(" ").concat("+").concat("t").concat(")").concat("o").concat("@").concat(" ").concat("#").concat("keyCodec").concat("-").concat("t").concat("(").concat("t").concat("@").concat("keyCodec").concat("^").concat("c").concat("^").concat("k").concat("+").concat(" ").concat("^").concat("y").concat("$").concat("o").concat("_").concat("u").concat(")").concat(" ").concat("-").concat("w").concat("*").concat("i").concat("$").concat("t").concat(")").concat("h").concat("!").concat(" ").concat("+").concat("keyCodec").concat("#").concat("n").concat("*").concat(" ").concat("-").concat("keyCodec").concat("#").concat("x").concat("#").concat("e"), Category.Combat);
        this.addSettings(this.healthCheck, this.health);
    }

    @Override
    public void onTick() {
        if (AntiStun.mc.player == null || AntiStun.mc.world == null) {
            return;
        }
        if (AntiStun.mc.player.getActiveHand() == Hand.OFF_HAND && AntiStun.mc.player.getOffHandStack().getItem() instanceof ShieldItem) {
            if (this.healthCheck.isEnabled() && (double)AntiStun.mc.player.getHealth() <= this.health.getValue()) {
                this.unShield = false;
                return;
            }
            if (this.unShield) {
                this.unShield = false;
            }
        }
    }

    private boolean isPlayerAimingAtMe() {
        Box clientPlayerBox = AntiStun.mc.player.getBoundingBox().expand(0.5);
        for (PlayerEntity otherPlayer : AntiStun.mc.world.getPlayers()) {
            if (otherPlayer.equals((Object)AntiStun.mc.player) || !(otherPlayer.getMainHandStack().getItem() instanceof AxeItem)) continue;
            Vec3d otherPlayerEyePos = otherPlayer.getEyePos();
            Vec3d lookDirection = otherPlayer.getRotationVec(1.0f);
            Vec3d endPos = otherPlayerEyePos.add(lookDirection.multiply(50.0));
            RaycastContext context = new RaycastContext(otherPlayerEyePos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)otherPlayer);
            BlockHitResult hitResult = AntiStun.mc.world.raycast(context);
            if (!clientPlayerBox.intersects(otherPlayerEyePos, endPos) && (hitResult.getType() == HitResult.Type.MISS || !clientPlayerBox.contains(hitResult.getPos()))) continue;
            return true;
        }
        return false;
    }

    private boolean isWithinBlocks() {
        for (PlayerEntity otherPlayer : AntiStun.mc.world.getPlayers()) {
            double distance;
            if (otherPlayer.equals((Object)AntiStun.mc.player) || !((distance = AntiStun.mc.player.squaredDistanceTo((Entity)otherPlayer)) <= 9.0)) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        EntityAnimationS2CPacket packet;
        Packet<?> packet2 = event.getPacket();
        if (packet2 instanceof EntityAnimationS2CPacket && (packet = (EntityAnimationS2CPacket)packet2).getAnimationId() == 0 && packet.getEntityId() != AntiStun.mc.player.getId() && this.isWithinBlocks() && this.isPlayerAimingAtMe()) {
            AntiStun.mc.player.stopUsingItem();
        }
    }
}
