package net.fabricmc.fabric.systems.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.MultiOptionSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.backtrack.TimedPacket;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Backtrack
extends Module {
    NumberSetting2 delay2 = new NumberSetting2("D".concat("*").concat("e").concat("!").concat("l").concat("^").concat("keyCodec").concat("#").concat("y"), 1.0, 10000.0, 500.0, 1200.0, 1.0, "D".concat(")").concat("e").concat("@").concat("l").concat("^").concat("keyCodec").concat("^").concat("y").concat("*").concat(" ").concat("^").concat("f").concat("&").concat("o").concat("+").concat("r").concat("*").concat(" ").concat("*").concat("t").concat("^").concat("h").concat("*").concat("e").concat("(").concat(" ").concat("*").concat("elementCodec").concat("$").concat("keyCodec").concat(")").concat("c").concat("#").concat("k").concat("-").concat("t").concat("*").concat("r").concat(")").concat("keyCodec").concat("_").concat("c").concat("^").concat("k"));
    ColorPickerSetting boxcolor = new ColorPickerSetting("B".concat("&").concat("o").concat("!").concat("x").concat("&").concat(" ").concat(")").concat("C").concat("^").concat("o").concat("#").concat("l").concat("*").concat("o").concat("_").concat("r"), new Color(130, 120, 255, 145), "C".concat("@").concat("o").concat("&").concat("l").concat("_").concat("o").concat("!").concat("r").concat("#").concat(" ").concat("$").concat("o").concat("!").concat("f").concat("&").concat(" ").concat("@").concat("t").concat("-").concat("h").concat("_").concat("e").concat("#").concat(" ").concat("#").concat("r").concat(")").concat("e").concat("$").concat("keyCodec").concat("!").concat("l").concat("+").concat(" ").concat("&").concat("p").concat("(").concat("o").concat("#").concat("s").concat("!").concat("i").concat("+").concat("t").concat("$").concat("i").concat("(").concat("o").concat("-").concat("n").concat("#").concat(" ").concat("^").concat("elementCodec").concat("*").concat("o").concat(")").concat("x"));
    NumberSetting range = new NumberSetting("R".concat("_").concat("keyCodec").concat("-").concat("n").concat("&").concat("g").concat("(").concat("e"), 1.0, 6.0, 4.5, 0.1, "R".concat("!").concat("keyCodec").concat("*").concat("n").concat("!").concat("g").concat("#").concat("e").concat("(").concat(" ").concat("!").concat("f").concat("#").concat("o").concat("-").concat("r").concat("@").concat(" ").concat("&").concat("t").concat("*").concat("h").concat("$").concat("e").concat("_").concat(" ").concat("$").concat("elementCodec").concat("+").concat("keyCodec").concat("-").concat("c").concat("$").concat("k").concat("_").concat("t").concat("&").concat("r").concat("_").concat("keyCodec").concat("$").concat("c").concat("#").concat("k").concat("-").concat(" ").concat("*").concat("t").concat("^").concat("o").concat(")").concat(" ").concat("&").concat("d").concat("-").concat("i").concat("*").concat("s").concat("#").concat("keyCodec").concat("!").concat("elementCodec").concat("!").concat("l").concat("&").concat("e"));
    BooleanSetting render = new BooleanSetting("R".concat("@").concat("e").concat("$").concat("n").concat("@").concat("d").concat(")").concat("e").concat("(").concat("r"), true, "R".concat("(").concat("e").concat("+").concat("n").concat("#").concat("d").concat("(").concat("e").concat("_").concat("r").concat("+").concat(" ").concat("$").concat("t").concat("-").concat("h").concat("^").concat("e").concat(")").concat(" ").concat("!").concat("r").concat("$").concat("e").concat("^").concat("keyCodec").concat("*").concat("l").concat("_").concat(" ").concat("&").concat("p").concat("(").concat("o").concat("@").concat("s").concat("$").concat("i").concat("*").concat("t").concat("&").concat("i").concat("!").concat("o").concat("!").concat("n"));
    BooleanSetting sendPacketsWithDelay = new BooleanSetting("S".concat("_").concat("e").concat("^").concat("n").concat("&").concat("d").concat("+").concat(" ").concat("!").concat("P").concat("$").concat("keyCodec").concat("&").concat("c").concat("&").concat("k").concat("&").concat("e").concat(")").concat("t").concat("+").concat("s").concat("#").concat(" ").concat(")").concat("D").concat("#").concat("e").concat("_").concat("l").concat("_").concat("keyCodec").concat("#").concat("y").concat("#").concat("e").concat(")").concat("d"), true, "S".concat("+").concat("e").concat("#").concat("n").concat(")").concat("d").concat("+").concat(" ").concat("*").concat("p").concat("(").concat("keyCodec").concat("@").concat("c").concat("&").concat("k").concat("!").concat("e").concat("*").concat("t").concat(")").concat("s").concat("&").concat(" ").concat("!").concat("w").concat("$").concat("i").concat("-").concat("t").concat("+").concat("h").concat("+").concat(" ").concat("-").concat("d").concat("&").concat("e").concat(")").concat("l").concat("&").concat("keyCodec").concat("+").concat("y"));
    MultiOptionSetting options = new MultiOptionSetting("O".concat("@").concat("p").concat("&").concat("t").concat("*").concat("i").concat("&").concat("o").concat(")").concat("n").concat("@").concat("s"), "S".concat(")").concat("t").concat("*").concat("u").concat("@").concat("f").concat("*").concat("f").concat("$").concat(" ").concat("@").concat("t").concat("_").concat("o").concat("!").concat(" ").concat("_").concat("f").concat("+").concat("l").concat("$").concat("u").concat("-").concat("s").concat("-").concat("h").concat("^").concat(" ").concat("!").concat("p").concat("&").concat("keyCodec").concat("(").concat("c").concat("+").concat("k").concat("&").concat("e").concat("+").concat("t").concat("!").concat("s").concat("^").concat(" ").concat("-").concat("o").concat("#").concat("n"), "U".concat("$").concat("p").concat(")").concat("d").concat("(").concat("keyCodec").concat("$").concat("t").concat("!").concat("e").concat("*").concat("H").concat("&").concat("e").concat("_").concat("keyCodec").concat("$").concat("l").concat("^").concat("t").concat("(").concat("h"), "U".concat("^").concat("p").concat("$").concat("d").concat("&").concat("keyCodec").concat("#").concat("t").concat("^").concat("e").concat(")").concat("S").concat("$").concat("h").concat("#").concat("i").concat("+").concat("e").concat("*").concat("l").concat("*").concat("d"), "K".concat("+").concat("e").concat("+").concat("e").concat("!").concat("p").concat(")").concat("S").concat("#").concat("o").concat("*").concat("u").concat("&").concat("n").concat("(").concat("d"), "S".concat("!").concat("t").concat("+").concat("o").concat("^").concat("p").concat("&").concat("R").concat("&").concat("keyCodec").concat("+").concat("n").concat("*").concat("g").concat("+").concat("e"));
    private final Queue<TimedPacket> packetQueue = new ConcurrentLinkedQueue<TimedPacket>();
    private final List<Packet<?>> skipPackets = new ArrayList();
    private Entity target;
    private Vec3d pos;
    private int currentLatency = 0;
    private boolean dumpNextTick = false;

    public Backtrack() {
        super("B".concat("!").concat("keyCodec").concat(")").concat("c").concat(")").concat("k").concat("$").concat("t").concat("!").concat("r").concat("@").concat("keyCodec").concat("+").concat("c").concat("_").concat("k"), "L".concat("+").concat("keyCodec").concat("@").concat("g").concat("#").concat("s").concat("^").concat(" ").concat(")").concat("e").concat("$").concat("n").concat("@").concat("e").concat("!").concat("m").concat(")").concat("i").concat("#").concat("e").concat("*").concat("s").concat("-").concat(" ").concat("*").concat("i").concat("*").concat("n").concat("+").concat("t").concat("+").concat("o").concat("^").concat(" ").concat("&").concat("p").concat("!").concat("r").concat("-").concat("e").concat("&").concat("v").concat("^").concat("i").concat("$").concat("o").concat("*").concat("u").concat("^").concat("s").concat("+").concat(" ").concat("&").concat("p").concat("*").concat("o").concat("@").concat("s").concat("_").concat("i").concat("&").concat("t").concat("*").concat("i").concat("@").concat("o").concat("&").concat("n").concat(")").concat("s"), Category.Combat);
        this.addSettings(this.render, this.sendPacketsWithDelay, this.range, this.delay2, this.options, this.boxcolor);
    }

    @Override
    public void onEnable() {
        this.packetQueue.clear();
        this.skipPackets.clear();
        this.target = null;
        this.pos = null;
    }

    @Override
    public void onDisable() {
        this.dumpPackets();
        this.target = null;
        this.pos = null;
    }

    @EventHandler(priority=200)
    public void onPacketReceive(PacketEvent.Receive e) {
        if (this.target == null || Backtrack.mc.world == null) {
            return;
        }
        if (!this.cancelPackets(this.target)) {
            return;
        }
        Packet<?> p = e.getPacket();
        try {
            EntityPositionS2CPacket packet;
            EntityTrackerUpdateS2CPacket etu;
            if (this.target == null || !this.target.isAlive()) {
                this.dumpNextTick = true;
                return;
            }
            if (e.isCancelled()) {
                return;
            }
            if (p instanceof PlaySoundS2CPacket && this.options.getOption("KeepSound").isEnabled()) {
                return;
            }
            if (p instanceof HealthUpdateS2CPacket && this.options.getOption("UpdateHealth").isEnabled()) {
                return;
            }
            if (p instanceof EntityTrackerUpdateS2CPacket && (etu = (EntityTrackerUpdateS2CPacket)p).id() != Backtrack.mc.player.getId() && this.options.getOption("UpdateShield").isEnabled()) {
                return;
            }
            if (p instanceof EntityStatusS2CPacket || p instanceof ChatMessageS2CPacket || p instanceof EntityAnimationS2CPacket || p instanceof TeamS2CPacket) {
                return;
            }
            if (e.getPacket() instanceof EntityPositionS2CPacket && (packet = (EntityPositionS2CPacket)e.packet).getEntityId() == this.target.getId()) {
                this.pos = new Vec3d(packet.getX(), packet.getY(), packet.getZ());
            }
            if (e.packet instanceof EntityS2CPacket && ((EntityS2CPacket)e.packet).getEntity((World)Backtrack.mc.world) == this.target || e.packet instanceof EntityPositionS2CPacket && ((EntityPositionS2CPacket)e.packet).getEntityId() == this.target.getId()) {
                this.pos = e.packet instanceof EntityS2CPacket ? new Vec3d(this.pos.x + (double)((EntityS2CPacket)e.packet).getDeltaX() / 4096.0, this.pos.y + (double)((EntityS2CPacket)e.packet).getDeltaY() / 4096.0, this.pos.z + (double)((EntityS2CPacket)e.packet).getDeltaZ() / 4096.0) : new Vec3d(((EntityPositionS2CPacket)e.packet).getX(), ((EntityPositionS2CPacket)e.packet).getY(), ((EntityPositionS2CPacket)e.packet).getZ());
                if ((double)MathHelper.sqrt((float)((float)Backtrack.mc.player.squaredDistanceTo(this.pos))) > this.range.getValue() && this.options.getOption("StopRange").isEnabled()) {
                    this.dumpNextTick = true;
                    return;
                }
            }
            if (p instanceof PlayerPositionLookS2CPacket || p instanceof DisconnectS2CPacket) {
                this.dumpNextTick = true;
                this.target = null;
                this.pos = null;
                return;
            }
            long delay = this.sendPacketsWithDelay.isEnabled() ? (long)((double)System.currentTimeMillis() + this.delay2.getRandomValue()) : 0L;
            this.packetQueue.add(new TimedPacket(p, delay));
            e.cancel();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.render.isEnabled() && this.target != null && this.pos != null) {
            Color boxcolor2 = ColorUtil.addAlpha(this.boxcolor.getColor(), 145);
            Render3DEngine.drawBox(this.pos, boxcolor2, matrices);
        }
    }

    @Override
    public void onTick() {
        this.setSuffix(this.currentLatency);
        if (this.dumpNextTick) {
            this.dumpPackets();
            this.dumpNextTick = false;
            return;
        }
        if (Backtrack.mc.currentScreen instanceof LevelLoadingScreen) {
            this.dumpPackets();
        }
        if (!this.packetQueue.isEmpty()) {
            TimedPacket timedPacket = this.packetQueue.peek();
            if (timedPacket != null && timedPacket.getTimer().hasCooldownElapsed(this.currentLatency)) {
                this.dumpPackets();
            } else if (this.sendPacketsWithDelay.isEnabled() && timedPacket != null && System.currentTimeMillis() >= timedPacket.getMillis()) {
                this.dumpPackets();
            }
        }
    }

    @EventHandler
    public void onAttack(AttackEntityEvent.Pre event) {
        int newLatency;
        if (!this.cancelPackets(event.target)) {
            return;
        }
        if (this.target != event.target) {
            this.target = event.target;
            this.pos = this.target.getPos();
        }
        if ((newLatency = (int)this.delay2.getRandomValue()) != this.currentLatency) {
            this.currentLatency = newLatency;
        }
    }

    private boolean cancelPackets(Entity entity) {
        boolean result = entity != null && entity.isAlive();
        return result;
    }

    private void dumpPackets() {
        if (mc.getNetworkHandler() == null) {
            return;
        }
        while (!this.packetQueue.isEmpty()) {
            TimedPacket timedPacket = this.packetQueue.poll();
            if (timedPacket == null) continue;
            Packet packet = timedPacket.getPacket();
            this.skipPackets.add(packet);
            mc.execute(() -> packet.apply((PacketListener)mc.getNetworkHandler()));
            this.currentLatency = 0;
        }
        this.target = null;
        this.pos = null;
    }
}
