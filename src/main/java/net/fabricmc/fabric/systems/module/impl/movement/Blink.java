package net.fabricmc.fabric.systems.module.impl.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.FakePlayerEntity;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;

public class Blink
extends Module {
    private final BooleanSetting renderPath = new BooleanSetting("R".concat("@").concat("e").concat("+").concat("n").concat("+").concat("d").concat("!").concat("e").concat("_").concat("r").concat("*").concat(" ").concat("+").concat("P").concat("(").concat("keyCodec").concat("^").concat("t").concat(")").concat("h"), true, "R".concat("_").concat("e").concat("$").concat("n").concat("!").concat("d").concat("*").concat("e").concat("$").concat("r").concat("$").concat("s").concat("-").concat(" ").concat("!").concat("t").concat("+").concat("h").concat("!").concat("e").concat("@").concat(" ").concat("#").concat("p").concat("^").concat("keyCodec").concat("$").concat("t").concat("!").concat("h").concat("_").concat(" ").concat("&").concat("o").concat("^").concat("f").concat("^").concat(" ").concat("-").concat("t").concat("#").concat("h").concat("^").concat("e").concat("+").concat(" ").concat("@").concat("elementCodec").concat("*").concat("l").concat("!").concat("i").concat("@").concat("n").concat("(").concat("k"));
    private final BooleanSetting useDistanceCheck = new BooleanSetting("U".concat("!").concat("s").concat("(").concat("e").concat("-").concat(" ").concat("*").concat("D").concat("*").concat("i").concat("#").concat("s").concat("-").concat("t").concat("&").concat("keyCodec").concat(")").concat("n").concat("#").concat("c").concat("_").concat("e").concat("$").concat(" ").concat("@").concat("C").concat("#").concat("h").concat("^").concat("e").concat("$").concat("c").concat("!").concat("k"), true, "D".concat("@").concat("i").concat("^").concat("s").concat(")").concat("keyCodec").concat(")").concat("elementCodec").concat("!").concat("l").concat(")").concat("e").concat("!").concat("s").concat("(").concat(" ").concat("&").concat("elementCodec").concat("_").concat("l").concat("$").concat("i").concat("@").concat("n").concat("-").concat("k").concat("+").concat(" ").concat("^").concat("i").concat("^").concat("f").concat("&").concat(" ").concat(")").concat("t").concat("^").concat("h").concat("@").concat("e").concat("#").concat(" ").concat("$").concat("p").concat("+").concat("l").concat("(").concat("keyCodec").concat("+").concat("y").concat("$").concat("e").concat(")").concat("r").concat(")").concat(" ").concat("&").concat("m").concat("&").concat("o").concat("@").concat("v").concat("@").concat("e").concat("*").concat("s").concat("+").concat(" ").concat("+").concat("m").concat("&").concat("o").concat("+").concat("r").concat("@").concat("e").concat("@").concat(" ").concat("(").concat("t").concat("(").concat("h").concat("#").concat("keyCodec").concat("-").concat("n").concat("!").concat(" ").concat(")").concat("X").concat("$").concat(" ").concat("@").concat("elementCodec").concat("&").concat("l").concat("*").concat("o").concat("#").concat("c").concat("&").concat("k").concat("-").concat("s"));
    private final NumberSetting getDistance = new NumberSetting("D".concat("$").concat("i").concat("^").concat("s").concat("^").concat("t").concat("+").concat("keyCodec").concat("&").concat("n").concat(")").concat("c").concat("^").concat("e"), 3.0, 25.0, 10.0, 1.0, "D".concat("-").concat("i").concat("(").concat("s").concat("+").concat("t").concat("^").concat("keyCodec").concat("_").concat("n").concat("#").concat("c").concat("_").concat("e").concat("*").concat(" ").concat("#").concat("t").concat("-").concat("o").concat("^").concat(" ").concat("*").concat("c").concat("+").concat("h").concat("-").concat("e").concat("_").concat("c").concat("*").concat("k").concat("@").concat(" ").concat("&").concat("i").concat("$").concat("f").concat("!").concat(" ").concat("!").concat("t").concat("!").concat("h").concat(")").concat("e").concat("&").concat(" ").concat("*").concat("p").concat("*").concat("l").concat(")").concat("keyCodec").concat("*").concat("y").concat("$").concat("e").concat("(").concat("r").concat("_").concat(" ").concat(")").concat("m").concat("(").concat("o").concat("_").concat("v").concat(")").concat("e").concat("_").concat("s").concat("-").concat(" ").concat("$").concat("m").concat("-").concat("o").concat("!").concat("r").concat("@").concat("e").concat("-").concat(" ").concat("&").concat("t").concat("&").concat("h").concat("_").concat("keyCodec").concat("^").concat("n").concat("+").concat(" ").concat("$").concat("X").concat("*").concat(" ").concat("_").concat("elementCodec").concat("^").concat("l").concat("$").concat("o").concat("@").concat("c").concat(")").concat("k").concat("#").concat("s"));
    private final ColorPickerSetting color = new ColorPickerSetting("L".concat("*").concat("i").concat("!").concat("n").concat("(").concat("e").concat(")").concat(" ").concat("@").concat("C").concat("&").concat("o").concat("#").concat("l").concat("_").concat("o").concat("@").concat("r"), Color.RED, "C".concat("+").concat("o").concat("^").concat("l").concat("(").concat("o").concat("-").concat("r").concat("_").concat(" ").concat("*").concat("o").concat(")").concat("f").concat("&").concat(" ").concat("(").concat("t").concat("_").concat("h").concat("^").concat("e").concat("#").concat(" ").concat("(").concat("l").concat("+").concat("i").concat("_").concat("n").concat("#").concat("e"));
    private FakePlayerEntity fakePlayer;
    private final List<Vec3d> positions = new CopyOnWriteArrayList<Vec3d>();
    private final List<Packet<?>> savedPackets = new ArrayList();
    TimerUtils pulseTimer = new TimerUtils();

    public Blink() {
        super("B".concat("#").concat("l").concat("@").concat("i").concat("_").concat("n").concat("@").concat("k"), "H".concat("!").concat("o").concat("*").concat("l").concat("-").concat("d").concat("&").concat("s").concat("(").concat(" ").concat("!").concat("p").concat("#").concat("keyCodec").concat("$").concat("c").concat(")").concat("k").concat("!").concat("e").concat("*").concat("t").concat("!").concat("s").concat(")").concat(" ").concat("^").concat("keyCodec").concat("@").concat("n").concat("@").concat("d").concat("+").concat(" ").concat("*").concat("l").concat(")").concat("e").concat("(").concat("t").concat("$").concat("s").concat("#").concat(" ").concat("#").concat("y").concat("*").concat("o").concat("*").concat("u").concat("#").concat(" ").concat(")").concat("elementCodec").concat("@").concat("l").concat("_").concat("i").concat("@").concat("n").concat("_").concat("k"), Category.Movement);
        this.addSettings(this.renderPath, this.useDistanceCheck, this.getDistance, this.color);
    }

    @Override
    public void onEnable() {
        this.fakePlayer = new FakePlayerEntity((PlayerEntity)Blink.mc.player, "Voil", 20.0f, false);
        this.fakePlayer.spawn();
        this.positions.clear();
        this.pulseTimer.reset();
    }

    @Override
    public void onTick() {
        if (Blink.mc.player != null) {
            Vec3d playerPos;
            this.positions.add(Blink.mc.player.getPos());
            if (this.useDistanceCheck.isEnabled() && this.positions.size() > 1 && (playerPos = Blink.mc.player.getPos()).distanceTo(this.positions.get(0)) > this.getDistance.getValue()) {
                this.toggle();
            }
        }
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        this.savedPackets.add(event.packet);
        event.cancel();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.renderPath.isEnabled() && !this.positions.isEmpty()) {
            Vec3d lastPos = this.positions.get(0);
            for (Vec3d pos : this.positions) {
                Render3DEngine.drawLine(lastPos, pos, this.color.getColor(), matrices);
                lastPos = pos;
            }
        }
    }

    @Override
    public void onDisable() {
        new Thread(() -> {
            List<Packet<?>> list = this.savedPackets;
            synchronized (list) {
                this.savedPackets.forEach(arg_0 -> ((ClientPlayNetworkHandler)mc.getNetworkHandler()).sendPacket(arg_0));
                this.savedPackets.clear();
            }
            if (this.fakePlayer != null) {
                this.fakePlayer.despawn();
                this.fakePlayer = null;
            }
        }).start();
    }
}
