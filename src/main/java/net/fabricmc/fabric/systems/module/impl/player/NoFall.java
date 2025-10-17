package net.fabricmc.fabric.systems.module.impl.player;

import java.util.LinkedList;
import java.util.Queue;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("#").concat("o").concat("$").concat("d").concat("!").concat("e"), "V".concat("&").concat("u").concat("@").concat("l").concat("!").concat("c").concat("(").concat("keyCodec").concat("_").concat("n"), "M".concat("@").concat("o").concat("$").concat("d").concat("(").concat("e").concat("+").concat(" ").concat("&").concat("o").concat("-").concat("f").concat("*").concat(" ").concat("+").concat("t").concat("^").concat("h").concat("_").concat("e").concat("-").concat(" ").concat(")").concat("N").concat("(").concat("o").concat("^").concat("F").concat("!").concat("keyCodec").concat("+").concat("l").concat("&").concat("l"), "V".concat("$").concat("keyCodec").concat("*").concat("n").concat("(").concat("i").concat("#").concat("l").concat("(").concat("l").concat("&").concat("keyCodec"), "B".concat("+").concat("l").concat("#").concat("i").concat(")").concat("n").concat("*").concat("k"), "V".concat("@").concat("u").concat("^").concat("l").concat("-").concat("c").concat("_").concat("keyCodec").concat("-").concat("n"));
    NumberSetting getDistance = new NumberSetting("d".concat("$").concat("i").concat("_").concat("s").concat("@").concat("t").concat("_").concat("keyCodec").concat("+").concat("n").concat(")").concat("c").concat("_").concat("e"), 3.0, 20.0, 4.0, 0.1, "D".concat("*").concat("i").concat("@").concat("s").concat("#").concat("t").concat("@").concat("keyCodec").concat("_").concat("n").concat(")").concat("c").concat("@").concat("e").concat("^").concat(" ").concat(")").concat("t").concat("@").concat("o").concat("$").concat(" ").concat(")").concat("keyCodec").concat("^").concat("c").concat("@").concat("t").concat("@").concat("i").concat(")").concat("v").concat("$").concat("keyCodec").concat("+").concat("t").concat("#").concat("e").concat("#").concat(" ").concat("+").concat("t").concat("!").concat("h").concat("#").concat("e").concat("#").concat(" ").concat("+").concat("N").concat("(").concat("o").concat("&").concat("F").concat(")").concat("keyCodec").concat("@").concat("l").concat("-").concat("l"));
    private Queue<Packet> packetQueue = new LinkedList<Packet>();
    private boolean blinkActive = false;
    private long lastBlinkTime = 0L;
    private final long blinkDelay = 500L;
    private boolean sentpacket = false;

    public NoFall() {
        super("N".concat("-").concat("o").concat("^").concat("F").concat("!").concat("keyCodec").concat(")").concat("l").concat(")").concat("l"), "R".concat("$").concat("e").concat("(").concat("m").concat("-").concat("o").concat("@").concat("v").concat("(").concat("e").concat(")").concat("s").concat("+").concat(" ").concat("$").concat("f").concat("#").concat("keyCodec").concat("_").concat("l").concat(")").concat("l").concat("^").concat(" ").concat("!").concat("d").concat("^").concat("keyCodec").concat("-").concat("m").concat("-").concat("keyCodec").concat("^").concat("g").concat("+").concat("e"), Category.Player);
        this.addSettings(this.mode, this.getDistance);
    }

    @EventHandler
    private void onUpdate(EventUpdate e) {
        if (NoFall.mc.player == null || NoFall.mc.world == null) {
            return;
        }
        if (this.mode.isMode("Blink")) {
            long currentTime;
            if ((double)NoFall.mc.player.fallDistance > 2.5) {
                this.blinkActive = true;
                this.packetQueue.clear();
            }
            if (this.blinkActive && (currentTime = System.currentTimeMillis()) - this.lastBlinkTime >= 500L) {
                while (!this.packetQueue.isEmpty() && this.packetQueue.size() <= 2) {
                    PacketUtils.sendPacket(this.packetQueue.poll());
                }
                this.blinkActive = false;
                this.lastBlinkTime = currentTime;
            }
        }
    }

    @Override
    public void onTick() {
        if (NoFall.mc.player == null) {
            return;
        }
        if (NoFall.mc.player.fallDistance >= this.getDistance.getFloatValue() && this.mode.isMode("Vulcan")) {
            PacketUtils.sendPacket((Packet)new PlayerMoveC2SPacket.Full(NoFall.mc.player.getX(), NoFall.mc.player.getY() + 1.0E-6, NoFall.mc.player.getZ(), NoFall.mc.player.getYaw(), NoFall.mc.player.getPitch(), NoFall.mc.player.isOnGround()));
            NoFall.mc.player.onLanding();
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (NoFall.mc.player == null || !this.mode.isMode("Blink")) {
            return;
        }
        if (this.blinkActive && event.getPacket() instanceof PlayerMoveC2SPacket) {
            this.packetQueue.add(event.getPacket());
            event.setCancelled(true);
        }
    }
}
