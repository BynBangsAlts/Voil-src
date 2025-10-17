package net.fabricmc.fabric.systems.module.impl.movement;

import java.awt.Color;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.MultiOptionSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.fabricmc.fabric.utils.packet.backtrack.TimedPacket;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class FakeLag
extends Module {
    private final TimerUtils timer;
    private Vec3d vec3d;
    private boolean isLagging;
    private final Queue<TimedPacket> packetQueue = new ConcurrentLinkedQueue<TimedPacket>();
    private final ModeSetting mode = new ModeSetting("M".concat("$").concat("o").concat("^").concat("d").concat("#").concat("e"), "F".concat("&").concat("keyCodec").concat("$").concat("k").concat("$").concat("e").concat("*").concat("l").concat("@").concat("keyCodec").concat("#").concat("g"), "M".concat("^").concat("o").concat("_").concat("d").concat("!").concat("e").concat("#").concat(" ").concat("-").concat("o").concat("^").concat("f").concat("&").concat(" ").concat("&").concat("t").concat("-").concat("h").concat("(").concat("e").concat("-").concat(" ").concat("_").concat("f").concat("+").concat("keyCodec").concat("_").concat("k").concat("_").concat("e").concat("_").concat("l").concat("(").concat("keyCodec").concat("!").concat("g"), "L".concat("(").concat("keyCodec").concat("(").concat("g").concat("-").concat(" ").concat("+").concat("r").concat("&").concat("keyCodec").concat("-").concat("n").concat("$").concat("g").concat("#").concat("e"), "F".concat("_").concat("keyCodec").concat("*").concat("k").concat("!").concat("e").concat("*").concat("l").concat("#").concat("keyCodec").concat("@").concat("g"));
    private final NumberSetting2 delay2 = new NumberSetting2("D".concat("&").concat("e").concat("(").concat("l").concat("#").concat("keyCodec").concat("@").concat("y"), 50.0, 3000.0, 400.0, 600.0, 1.0, "D".concat("(").concat("e").concat("*").concat("l").concat("-").concat("keyCodec").concat("_").concat("y").concat("(").concat(" ").concat("+").concat("o").concat("(").concat("f").concat("#").concat(" ").concat("_").concat("h").concat(")").concat("o").concat("+").concat("l").concat("@").concat("d").concat("(").concat("i").concat("!").concat("n").concat(")").concat("g").concat("#").concat(" ").concat("&").concat("p").concat("^").concat("keyCodec").concat("-").concat("c").concat("!").concat("k").concat("(").concat("e").concat("^").concat("t").concat("(").concat("s"));
    private final BooleanSetting weaponOnly = new BooleanSetting("W".concat(")").concat("e").concat("!").concat("keyCodec").concat("(").concat("p").concat("(").concat("o").concat("$").concat("n").concat("^").concat(" ").concat("!").concat("O").concat("_").concat("n").concat("-").concat("l").concat("$").concat("y"), false, "O".concat("*").concat("n").concat(")").concat("l").concat("$").concat("y").concat("^").concat(" ").concat("#").concat("l").concat(")").concat("keyCodec").concat("#").concat("g").concat("#").concat(" ").concat(")").concat("w").concat("#").concat("h").concat("!").concat("i").concat("*").concat("l").concat("(").concat("e").concat("+").concat(" ").concat("&").concat("h").concat("_").concat("o").concat("@").concat("l").concat("#").concat("d").concat("&").concat("i").concat("!").concat("n").concat("^").concat("g").concat("(").concat(" ").concat("@").concat("keyCodec").concat("#").concat(" ").concat(")").concat("w").concat("@").concat("e").concat("_").concat("keyCodec").concat("&").concat("p").concat("_").concat("o").concat("+").concat("n"));
    private final BooleanSetting realPos = new BooleanSetting("S".concat("^").concat("e").concat(")").concat("r").concat(")").concat("v").concat("#").concat("e").concat("$").concat("r").concat("#").concat(" ").concat("_").concat("P").concat("#").concat("o").concat("^").concat("s"), false, "S".concat("_").concat("h").concat("&").concat("o").concat("+").concat("w").concat("@").concat("s").concat("-").concat(" ").concat("#").concat("s").concat("@").concat("e").concat("!").concat("r").concat("$").concat("v").concat("$").concat("e").concat("#").concat("r").concat("$").concat(" ").concat("-").concat("p").concat("-").concat("o").concat("_").concat("s").concat("-").concat("i").concat("$").concat("t").concat("&").concat("i").concat(")").concat("o").concat("$").concat("n").concat("_").concat(" ").concat("#").concat("o").concat("*").concat("f").concat("*").concat(" ").concat("_").concat("p").concat(")").concat("l").concat("_").concat("keyCodec").concat("#").concat("y").concat(")").concat("e").concat("-").concat("r"));
    private final ColorPickerSetting color = new ColorPickerSetting("C".concat("!").concat("o").concat("@").concat("l").concat("(").concat("o").concat("&").concat("r"), Color.RED, "C".concat("^").concat("o").concat("^").concat("l").concat("(").concat("o").concat("_").concat("r").concat("@").concat(" ").concat("(").concat("o").concat("(").concat("f").concat("_").concat(" ").concat("-").concat("t").concat("_").concat("h").concat("#").concat("e").concat("$").concat(" ").concat("#").concat("s").concat("&").concat("e").concat("!").concat("r").concat("@").concat("v").concat("!").concat("e").concat("@").concat("r").concat("!").concat(" ").concat("$").concat("p").concat("&").concat("o").concat("^").concat("s"));
    private final NumberSetting range = new NumberSetting("R".concat("@").concat("keyCodec").concat("^").concat("n").concat("&").concat("g").concat(")").concat("e"), 1.0, 12.0, 6.0, 1.0, "R".concat("&").concat("keyCodec").concat("^").concat("n").concat("!").concat("g").concat("(").concat("e").concat("#").concat(" ").concat("!").concat("f").concat("_").concat("o").concat("_").concat("r").concat("*").concat(" ").concat("*").concat("t").concat("^").concat("h").concat("^").concat("e").concat("@").concat(" ").concat("!").concat("l").concat("&").concat("keyCodec").concat("^").concat("g").concat("-").concat(" ").concat("+").concat("r").concat("^").concat("keyCodec").concat("!").concat("n").concat("_").concat("g").concat("$").concat("e").concat("(").concat(" ").concat("^").concat("t").concat("_").concat("o").concat("!").concat(" ").concat("^").concat("e").concat("$").concat("n").concat("+").concat("keyCodec").concat("!").concat("elementCodec").concat("(").concat("l").concat("#").concat("e"));
    private final MultiOptionSetting test = new MultiOptionSetting("T".concat("#").concat("e").concat(")").concat("s").concat("_").concat("t"), "T".concat("&").concat("e").concat("#").concat("s").concat("@").concat("t"), "T".concat(")").concat("e").concat("$").concat("s").concat(")").concat("t").concat("@").concat("1"), "T".concat(")").concat("e").concat("^").concat("s").concat("@").concat("t").concat("(").concat("2"), "T".concat("*").concat("e").concat("&").concat("s").concat("+").concat("t").concat("+").concat("3"));

    public FakeLag() {
        super("F".concat("^").concat("keyCodec").concat("$").concat("k").concat("+").concat("e").concat("_").concat("L").concat("$").concat("keyCodec").concat(")").concat("g"), "H".concat("&").concat("o").concat("!").concat("l").concat(")").concat("d").concat("!").concat("s").concat("-").concat(" ").concat("#").concat("p").concat("@").concat("keyCodec").concat("$").concat("c").concat("&").concat("k").concat("+").concat("e").concat("(").concat("t").concat("#").concat("s").concat("#").concat(" ").concat("!").concat("keyCodec").concat("!").concat("n").concat("&").concat("d").concat("#").concat(" ").concat("#").concat("s").concat("!").concat("i").concat("#").concat("m").concat("@").concat("u").concat("*").concat("l").concat("&").concat("keyCodec").concat("_").concat("t").concat(")").concat("e").concat("-").concat("s").concat("^").concat(" ").concat("@").concat("l").concat("-").concat("keyCodec").concat(")").concat("g"), Category.Movement);
        this.addSettings(this.delay2, this.range, this.weaponOnly, this.realPos, this.mode, this.color, this.test);
        this.timer = new TimerUtils();
        this.vec3d = Vec3d.ZERO;
        this.isLagging = false;
        Setting.dependSetting(this.range, "L".concat("-").concat("keyCodec").concat("(").concat("g").concat("_").concat(" ").concat("-").concat("r").concat(")").concat("keyCodec").concat("^").concat("n").concat(")").concat("g").concat("*").concat("e"), this.mode);
    }

    @Override
    public void onEnable() {
        this.packetQueue.clear();
    }

    @EventHandler(priority=200)
    public void onPacketSend(PacketEvent.Send event) {
        if (FakeLag.mc.world == null || FakeLag.mc.player == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (this.shouldSendPacket(packet)) {
            this.sendPacket(false);
            PacketUtils.sendPacketSilently(packet);
            event.cancel();
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        this.packetQueue.add(new TimedPacket(packet, System.currentTimeMillis()));
        event.cancel();
    }

    @Override
    public void onTick() {
        this.setSuffix((int)this.delay2.getMinValue() + "-" + (int)this.delay2.getMaxValue());
        if (FakeLag.mc.world == null || FakeLag.mc.player == null) {
            this.sendPacket(false);
            return;
        }
        if (this.mode.isMode("Fakelag")) {
            this.handleFakeLag();
        } else if (this.mode.isMode("Lag range")) {
            this.handleLagRange();
        }
        this.sendPacket(true);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.realPos.isEnabled() && FakeLag.mc.player != null) {
            if (FakeLag.mc.options.getPerspective() != Perspective.FIRST_PERSON && this.isLagging && this.vec3d != null) {
                Render3DEngine.drawBox(this.vec3d, ColorUtil.addAlpha(this.color.getColor(), 145), matrices);
            }
            if (this.mode.isMode("Lag range")) {
                this.renderLagRange(matrices);
            }
        }
    }

    private void handleFakeLag() {
        this.isLagging = true;
        if (this.timer.delay((float)this.delay2.getRandomValue()) && FakeLag.mc.player != null) {
            if (this.weaponOnly.isEnabled() && this.isHoldingWeapon()) {
                this.sendPacket(true);
            } else {
                this.sendPacket(false);
            }
        }
    }

    private void handleLagRange() {
        boolean nearbyPlayer;
        if (FakeLag.mc.world == null || FakeLag.mc.player == null) {
            this.sendPacket(false);
            return;
        }
        this.isLagging = nearbyPlayer = FakeLag.mc.world.getPlayers().stream().anyMatch(player -> player != FakeLag.mc.player && player.squaredDistanceTo((Entity)FakeLag.mc.player) <= (double)(this.range.getIValue() * this.range.getIValue()));
        if (!nearbyPlayer || this.weaponOnly.isEnabled() && !this.isHoldingWeapon()) {
            this.sendPacket(false);
        }
    }

    private boolean isHoldingWeapon() {
        if (FakeLag.mc.player == null) {
            return false;
        }
        Item item = FakeLag.mc.player.getMainHandStack().getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    private void renderLagRange(MatrixStack matrices) {
        int segments = 64;
        Vec3d playerPos = FakeLag.mc.player.getLerpedPos(Utils.getTick());
        double yOffset = 0.5;
        Vec3d previousPoint = null;
        for (int i = 0; i <= segments; ++i) {
            double angle = Math.PI * 2 * (double)i / (double)segments;
            double x = playerPos.x + (double)this.range.getFloatValue() * Math.cos(angle);
            double z = playerPos.z + (double)this.range.getFloatValue() * Math.sin(angle);
            Vec3d currentPoint = new Vec3d(x, playerPos.y + yOffset, z);
            if (previousPoint != null) {
                Render3DEngine.drawLine(previousPoint, currentPoint, this.color.getColor(), matrices);
            }
            previousPoint = currentPoint;
        }
    }

    private void sendPacket(boolean delay) {
        try {
            TimedPacket timedPacket;
            while (!(this.packetQueue.isEmpty() || (timedPacket = this.packetQueue.peek()) == null || delay && !timedPacket.getTimer().hasCooldownElapsed((int)this.delay2.getRandomValue()))) {
                Packet packet = this.packetQueue.remove().getPacket();
                if (packet == null) continue;
                FakeLag.getPos(packet).ifPresent(pos -> {
                    this.vec3d = pos;
                });
                PacketUtils.sendPacketSilently(packet);
            }
        }
        catch (Exception exception) {
            // empty catchblock
        }
    }

    private boolean shouldSendPacket(Packet<?> packet) {
        return packet instanceof PlayerInteractEntityC2SPacket || packet instanceof HandSwingC2SPacket || packet instanceof PlayerInteractBlockC2SPacket || packet instanceof PlayerInteractItemC2SPacket || packet instanceof ClickSlotC2SPacket;
    }

    @NotNull
    public static Optional<Vec3d> getPos(Packet<?> packet) {
        if (packet instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket p = (PlayerPositionLookS2CPacket)packet;
            return Optional.of(new Vec3d(p.getX(), p.getY(), p.getZ()));
        }
        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket p = (PlayerMoveC2SPacket)packet;
            return Optional.of(new Vec3d(p.getX(FakeLag.mc.player.getX()), p.getY(FakeLag.mc.player.getY()), p.getZ(FakeLag.mc.player.getZ())));
        }
        return Optional.empty();
    }
}
