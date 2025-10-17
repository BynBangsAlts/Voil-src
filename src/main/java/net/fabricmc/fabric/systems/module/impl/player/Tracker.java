package net.fabricmc.fabric.systems.module.impl.player;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.api.astral.events.WorldRenderEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.world.World;
import org.joml.Quaternionf;

public class Tracker
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("$").concat("o").concat("+").concat("d").concat(")").concat("e"), "A".concat("@").concat("t").concat("*").concat("t").concat("&").concat("keyCodec").concat("*").concat("c").concat("(").concat("k").concat("(").concat("e").concat("&").concat("d"), "M".concat("(").concat("o").concat(")").concat("d").concat(")").concat("e").concat("*").concat(" ").concat("$").concat("o").concat("#").concat("f").concat("!").concat(" ").concat("^").concat("t").concat("+").concat("h").concat("!").concat("e").concat(")").concat(" ").concat("@").concat("t").concat("#").concat("r").concat("^").concat("keyCodec").concat("-").concat("c").concat("#").concat("k").concat("-").concat("e").concat("&").concat("r"), "C".concat("@").concat("l").concat("_").concat("o").concat("*").concat("s").concat("*").concat("e").concat("-").concat("s").concat("#").concat("t"), "A".concat("-").concat("t").concat("!").concat("t").concat("&").concat("keyCodec").concat("!").concat("c").concat("*").concat("k").concat("(").concat("e").concat(")").concat("d"));
    private final Map<UUID, Integer> gaps = new HashMap<UUID, Integer>();
    private final Map<UUID, Integer> crystals = new HashMap<UUID, Integer>();
    private final Map<UUID, Integer> totems = new HashMap<UUID, Integer>();
    private final Map<UUID, Integer> exp = new HashMap<UUID, Integer>();
    private static PlayerEntity target;
    private static final Set<Class<?>> loggedPackets;

    public Tracker() {
        super("T".concat("#").concat("r").concat("-").concat("keyCodec").concat("#").concat("c").concat("-").concat("k").concat("&").concat("e").concat(")").concat("r"), "T".concat("(").concat("r").concat("@").concat("keyCodec").concat("+").concat("c").concat("_").concat("k").concat("@").concat("s").concat("_").concat(" ").concat("#").concat("i").concat("-").concat("t").concat("_").concat("e").concat("!").concat("m").concat("!").concat("s").concat("#").concat(" ").concat("&").concat("o").concat("!").concat("f").concat("*").concat(" ").concat("+").concat("t").concat("!").concat("h").concat("+").concat("e").concat("@").concat(" ").concat("!").concat("o").concat("(").concat("t").concat("@").concat("h").concat("^").concat("e").concat("*").concat("r").concat("!").concat(" ").concat(")").concat("p").concat("!").concat("l").concat(")").concat("keyCodec").concat("#").concat("y").concat("(").concat("e").concat("*").concat("r"), Category.Player);
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        this.clearAllMaps();
    }

    @Override
    public void onDisable() {
        this.clearAllMaps();
    }

    private void clearAllMaps() {
        this.gaps.clear();
        this.crystals.clear();
        this.totems.clear();
        this.exp.clear();
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (Tracker.mc.player == null || Tracker.mc.world == null || target == null) {
            return;
        }
        UUID targetUUID = target.getUuid();
        Render2DEngine.drawRoundedBlur(matrices, 10.0f, 10.0f, 120.0f, 80.0f, 6.0f, 14.0f, 0.0f, true);
        Render2DEngine.renderItem(matrices, Items.GOLDEN_APPLE.getDefaultStack(), 15.0f, 15.0f, 1.0f, false);
        Render2DEngine.renderItem(matrices, Items.TOTEM_OF_UNDYING.getDefaultStack(), 15.0f, 35.0f, 1.0f, false);
        Render2DEngine.renderItem(matrices, Items.EXPERIENCE_BOTTLE.getDefaultStack(), 15.0f, 55.0f, 1.0f, false);
        Render2DEngine.renderItem(matrices, Items.END_CRYSTAL.getDefaultStack(), 15.0f, 75.0f, 1.0f, false);
        ClientMain.fontRenderer.draw(matrices, "Gaps: " + String.valueOf(this.gaps.getOrDefault(targetUUID, 0)), 35.0f, 13.0f, Color.white.getRGB());
        ClientMain.fontRenderer.draw(matrices, "Totems: " + String.valueOf(this.totems.getOrDefault(targetUUID, 0)), 35.0f, 33.0f, Color.white.getRGB());
        ClientMain.fontRenderer.draw(matrices, "Exp: " + String.valueOf(this.exp.getOrDefault(targetUUID, 0)), 35.0f, 53.0f, Color.white.getRGB());
        ClientMain.fontRenderer.draw(matrices, "Crystals: " + String.valueOf(this.crystals.getOrDefault(targetUUID, 0)), 35.0f, 72.0f, Color.white.getRGB());
        this.renderEntityFullBody(matrices, target, mc.getWindow().getScaledWidth() - 900, 160, 30, -20.0f, 180.0f);
    }

    private void renderEntityFullBody(MatrixStack matrices, PlayerEntity entity, int x, int y, int scale, float mouseX, float mouseY) {
        matrices.push();
        matrices.translate((float)x, (float)y, 50.0f);
        matrices.scale((float)scale, (float)scale, (float)scale);
        Quaternionf xRotation = new Quaternionf().rotateX((float)Math.toRadians(180.0));
        matrices.multiply(xRotation);
        Quaternionf yRotation = new Quaternionf().rotateY((float)Math.toRadians(mouseY));
        matrices.multiply(yRotation);
        entity.setCustomNameVisible(false);
        mc.getEntityRenderDispatcher().render((Entity)entity, 0.0, 0.0, 0.0, 0.0f, Utils.getTick(), matrices, (VertexConsumerProvider)mc.getBufferBuilders().getEntityVertexConsumers(), 0xF000F0);
        entity.setCustomNameVisible(false);
        matrices.pop();
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive e) {
        EntityStatusS2CPacket packet;
        Entity entity;
        Packet<?> packet2;
        if (Tracker.mc.player == null || Tracker.mc.world == null || target == null) {
            return;
        }
        Class packetClass = e.getPacket().getClass();
        if (e.getPacket() instanceof EntityStatusS2CPacket) {
            ChatUtils.addChatMessage("Received packet: " + packetClass.getSimpleName() + " status: " + ((EntityStatusS2CPacket)e.getPacket()).getStatus());
        }
        if ((packet2 = e.getPacket()) instanceof EntityStatusS2CPacket && (entity = (packet = (EntityStatusS2CPacket)packet2).getEntity((World)Tracker.mc.world)) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (entity != Tracker.mc.player) {
                UUID playerUUID = player.getUuid();
                if (packet.getStatus() == 9) {
                    ItemStack mainHand = player.getMainHandStack();
                    ItemStack offHand = player.getOffHandStack();
                    if (mainHand.getItem() == Items.GOLDEN_APPLE || offHand.getItem() == Items.GOLDEN_APPLE) {
                        this.gaps.put(playerUUID, this.gaps.getOrDefault(playerUUID, 0) + 1);
                    }
                } else if (packet.getStatus() == 35) {
                    this.totems.put(playerUUID, this.totems.getOrDefault(playerUUID, 0) + 1);
                }
            }
        }
    }

    @EventHandler
    public void onRender(WorldRenderEvent e) {
        if (Tracker.mc.player == null || Tracker.mc.world == null) {
            return;
        }
        if (this.mode.getMode().equals("Closest")) {
            target = (PlayerEntity)WorldUtils.findNearestEntity((PlayerEntity)Tracker.mc.player, 3.0f, false);
        }
    }

    @EventHandler
    public void onAttack(AttackEntityEvent e) {
        if (Tracker.mc.player == null || Tracker.mc.world == null) {
            return;
        }
        Entity entity = e.getTarget();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (this.mode.getMode().equals("Attacked")) {
                target = player;
            }
        }
    }

    static {
        loggedPackets = new HashSet();
    }
}
