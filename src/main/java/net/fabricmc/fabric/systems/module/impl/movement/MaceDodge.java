package net.fabricmc.fabric.systems.module.impl.movement;

import java.util.LinkedList;
import java.util.Queue;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class MaceDodge
extends Module {
    private final ModeSetting moveDirection = new ModeSetting("M".concat("+").concat("o").concat("#").concat("v").concat("$").concat("e").concat("+").concat(" ").concat("$").concat("D").concat("^").concat("i").concat(")").concat("r").concat("$").concat("e").concat(")").concat("c").concat("*").concat("t").concat("+").concat("i").concat("^").concat("o").concat("$").concat("n"), "F".concat("*").concat("o").concat("$").concat("r").concat("-").concat("w").concat("#").concat("keyCodec").concat(")").concat("r").concat("-").concat("d"), "W".concat("_").concat("h").concat(")").concat("i").concat("-").concat("c").concat("^").concat("h").concat("(").concat(" ").concat("!").concat("w").concat("-").concat("keyCodec").concat("#").concat("y").concat("*").concat(" ").concat("_").concat("t").concat("&").concat("o").concat("$").concat(" ").concat("$").concat("m").concat("^").concat("o").concat("-").concat("v").concat("@").concat("e"), "F".concat("-").concat("o").concat("*").concat("r").concat("&").concat("w").concat("@").concat("keyCodec").concat("!").concat("r").concat("_").concat("d"), "B".concat("$").concat("keyCodec").concat("(").concat("c").concat("$").concat("k").concat("@").concat("w").concat("*").concat("keyCodec").concat(")").concat("r").concat("#").concat("d"), "L".concat(")").concat("e").concat(")").concat("f").concat("@").concat("t"), "R".concat("-").concat("i").concat("*").concat("g").concat("#").concat("h").concat("@").concat("t"));
    private final NumberSetting lagDelay = new NumberSetting("L".concat("&").concat("keyCodec").concat("+").concat("g").concat(")").concat(" ").concat("*").concat("D").concat("&").concat("e").concat("^").concat("l").concat("(").concat("keyCodec").concat("!").concat("y"), 100.0, 1000.0, 500.0, 1.0, "D".concat("&").concat("e").concat("_").concat("l").concat("(").concat("keyCodec").concat("+").concat("y").concat("(").concat(" ").concat("(").concat("i").concat("@").concat("n").concat("_").concat(" ").concat("^").concat("m").concat("+").concat("i").concat("^").concat("l").concat("-").concat("l").concat("(").concat("i").concat("^").concat("s").concat("@").concat("e").concat("$").concat("c").concat("@").concat("o").concat("#").concat("n").concat("@").concat("d").concat("@").concat("s").concat("+").concat(" ").concat(")").concat("f").concat("+").concat("o").concat("!").concat("r").concat("!").concat(" ").concat("!").concat("l").concat("@").concat("keyCodec").concat("&").concat("g").concat(")").concat("g").concat("!").concat("i").concat("#").concat("n").concat("+").concat("g"));
    private long lastMoveTime = 0L;
    private long unpressStartTime = 0L;
    private final Queue<PlayerMoveC2SPacket> packetQueue = new LinkedList<PlayerMoveC2SPacket>();
    TimerUtils lagTimer = new TimerUtils();
    boolean isLagging = false;

    public MaceDodge() {
        super("M".concat("-").concat("keyCodec").concat("@").concat("c").concat("*").concat("e").concat("@").concat("D").concat("&").concat("o").concat("&").concat("d").concat("(").concat("g").concat("*").concat("e"), "C".concat("@").concat("r").concat("(").concat("e").concat("&").concat("keyCodec").concat("(").concat("t").concat("*").concat("e").concat("^").concat("s").concat(")").concat(" ").concat("@").concat("l").concat(")").concat("keyCodec").concat("_").concat("g").concat("(").concat(" ").concat("^").concat("t").concat("#").concat("o").concat("@").concat(" ").concat("(").concat("g").concat("-").concat("e").concat("#").concat("t").concat("(").concat(" ").concat("$").concat("keyCodec").concat("&").concat("w").concat("$").concat("keyCodec").concat("!").concat("y").concat(")").concat(" ").concat("@").concat("f").concat("&").concat("r").concat("-").concat("o").concat("$").concat("m").concat(")").concat(" ").concat("_").concat("m").concat("_").concat("keyCodec").concat("#").concat("c").concat("-").concat("e").concat("_").concat(" ").concat("-").concat("keyCodec").concat("+").concat("t").concat("#").concat("t").concat("^").concat("keyCodec").concat("#").concat("c").concat("#").concat("k").concat("-").concat("s"), Category.Movement);
        this.addSettings(this.moveDirection, this.lagDelay);
    }

    @Override
    public void onTick() {
        if (MaceDodge.mc.player == null || MaceDodge.mc.currentScreen != null) {
            return;
        }
        Box clientPlayerBox = MaceDodge.mc.player.getBoundingBox().expand(0.5);
        for (PlayerEntity otherPlayer : MaceDodge.mc.world.getPlayers()) {
            if (otherPlayer.equals((Object)MaceDodge.mc.player)) continue;
            double distance = MaceDodge.mc.player.squaredDistanceTo((Entity)otherPlayer);
            if (otherPlayer.getEquippedStack(EquipmentSlot.MAINHAND).getItem() != Items.NETHERITE_AXE || !"1.21 Mace".equals(otherPlayer.getEquippedStack(EquipmentSlot.MAINHAND).getName().getString()) || !(otherPlayer.getY() - MaceDodge.mc.player.getY() >= 3.0) || !(distance <= 9.0)) continue;
            Vec3d otherPlayerEyePos = otherPlayer.getEyePos();
            Vec3d lookDirection = otherPlayer.getRotationVec(1.0f);
            Vec3d endPos = otherPlayerEyePos.add(lookDirection.multiply(50.0));
            RaycastContext context = new RaycastContext(otherPlayerEyePos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)otherPlayer);
            BlockHitResult hitResult = MaceDodge.mc.world.raycast(context);
            if (!clientPlayerBox.intersects(otherPlayerEyePos, endPos) && (hitResult.getType() == HitResult.Type.MISS || !clientPlayerBox.contains(hitResult.getPos()))) continue;
            ChatUtils.addChatMessage("Player: " + otherPlayer.getName().getString() + " is looking at me with mace");
            this.isLagging = true;
            this.lastMoveTime = System.currentTimeMillis();
            break;
        }
        if (this.isLagging || (double)(System.currentTimeMillis() - this.lastMoveTime) <= this.lagDelay.getValue()) {
            switch (this.moveDirection.getMode()) {
                case "Forward": {
                    MaceDodge.mc.options.forwardKey.setPressed(true);
                    break;
                }
                case "Backward": {
                    MaceDodge.mc.options.backKey.setPressed(true);
                    break;
                }
                case "Left": {
                    MaceDodge.mc.options.leftKey.setPressed(true);
                    break;
                }
                case "Right": {
                    MaceDodge.mc.options.rightKey.setPressed(true);
                }
            }
        } else if (System.currentTimeMillis() - this.unpressStartTime <= 50L) {
            MaceDodge.mc.options.forwardKey.setPressed(false);
            MaceDodge.mc.options.backKey.setPressed(false);
            MaceDodge.mc.options.leftKey.setPressed(false);
            MaceDodge.mc.options.rightKey.setPressed(false);
            if (this.isLagging) {
                this.isLagging = false;
                while (!this.packetQueue.isEmpty()) {
                    mc.getNetworkHandler().sendPacket((Packet)this.packetQueue.poll());
                }
            }
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket && this.isLagging) {
            this.packetQueue.add((PlayerMoveC2SPacket)event.getPacket());
            event.setCancelled(true);
        }
    }
}
