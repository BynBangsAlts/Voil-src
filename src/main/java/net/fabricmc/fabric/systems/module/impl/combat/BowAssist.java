package net.fabricmc.fabric.systems.module.impl.combat;

import java.awt.Color;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BowAssist
extends Module {
    NumberSetting range = new NumberSetting("R".concat("*").concat("keyCodec").concat("^").concat("n").concat("^").concat("g").concat("@").concat("e"), 3.0, 50.0, 35.0, 1.0, "R".concat("_").concat("keyCodec").concat("*").concat("n").concat("#").concat("g").concat("_").concat("e").concat("$").concat(" ").concat(")").concat("o").concat("&").concat("f").concat("*").concat(" ").concat("&").concat("t").concat("+").concat("h").concat("!").concat("e").concat("_").concat(" ").concat("(").concat("B").concat("#").concat("o").concat("@").concat("w").concat("!").concat("A").concat("+").concat("s").concat("$").concat("s").concat("@").concat("i").concat("^").concat("s").concat("^").concat("t"));
    NumberSetting predictionTime = new NumberSetting("P".concat("^").concat("r").concat("$").concat("e").concat("^").concat("d").concat("*").concat("i").concat("*").concat("c").concat("#").concat("t").concat(")").concat("i").concat("*").concat("o").concat("!").concat("n").concat(")").concat(" ").concat("+").concat("T").concat("@").concat("i").concat("#").concat("m").concat("(").concat("e"), 0.2, 5.0, 0.2, 0.1, "T".concat("+").concat("i").concat("$").concat("m").concat("&").concat("e").concat("@").concat(" ").concat("!").concat("o").concat("-").concat("f").concat("$").concat(" ").concat("@").concat("t").concat("!").concat("h").concat(")").concat("e").concat("-").concat(" ").concat("$").concat("p").concat("+").concat("r").concat("-").concat("e").concat("+").concat("d").concat("+").concat("i").concat("(").concat("c").concat("$").concat("t").concat("@").concat("i").concat("!").concat("o").concat("@").concat("n"));
    ModeSetting mode = new ModeSetting("M".concat("#").concat("o").concat("-").concat("d").concat("@").concat("e"), "P".concat("(").concat("r").concat("@").concat("e").concat("#").concat("d").concat("!").concat("i").concat("^").concat("c").concat(")").concat("t"), "M".concat("&").concat("o").concat("^").concat("d").concat(")").concat("e").concat("+").concat(" ").concat("@").concat("o").concat("*").concat("f").concat(")").concat(" ").concat("$").concat("t").concat(")").concat("h").concat("&").concat("e").concat("^").concat(" ").concat("!").concat("B").concat("-").concat("o").concat("#").concat("w").concat("(").concat("A").concat("-").concat("s").concat(")").concat("s").concat("$").concat("i").concat("#").concat("s").concat("^").concat("t"), "S".concat("$").concat("i").concat("#").concat("l").concat("!").concat("e").concat(")").concat("n").concat("-").concat("t"), "P".concat("$").concat("r").concat("+").concat("e").concat("+").concat("d").concat("@").concat("i").concat("_").concat("c").concat("_").concat("t"));
    BooleanSetting render = new BooleanSetting("R".concat("^").concat("e").concat("(").concat("n").concat("*").concat("d").concat("#").concat("e").concat("@").concat("r"), true, "T".concat("^").concat("o").concat("(").concat(" ").concat("#").concat("r").concat("&").concat("e").concat("*").concat("n").concat("+").concat("d").concat("*").concat("e").concat("^").concat("r").concat("*").concat(" ").concat("_").concat("t").concat("$").concat("h").concat("@").concat("e").concat("-").concat(" ").concat("_").concat("v").concat("!").concat("i").concat("@").concat("s").concat("(").concat("u").concat("&").concat("keyCodec").concat("_").concat("l").concat(")").concat("s"));
    BooleanSetting yawAssist = new BooleanSetting("Y".concat("#").concat("keyCodec").concat("@").concat("w").concat("(").concat(" ").concat("-").concat("A").concat("+").concat("s").concat("_").concat("s").concat("^").concat("i").concat("-").concat("s").concat("_").concat("t"), true, "T".concat("(").concat("o").concat("!").concat(" ").concat("*").concat("keyCodec").concat("*").concat("s").concat("$").concat("s").concat("$").concat("i").concat("_").concat("s").concat("#").concat("t").concat("$").concat(" ").concat("-").concat("w").concat("(").concat("i").concat("#").concat("t").concat("@").concat("h").concat("_").concat(" ").concat("+").concat("y").concat("!").concat("keyCodec").concat("^").concat("w").concat("^").concat(" ").concat("(").concat("keyCodec").concat("$").concat("d").concat("#").concat("j").concat("&").concat("u").concat("^").concat("s").concat("_").concat("t").concat("*").concat("m").concat("*").concat("e").concat("(").concat("n").concat("_").concat("t"));
    BooleanSetting pitchAssist = new BooleanSetting("P".concat("_").concat("i").concat("$").concat("t").concat("^").concat("c").concat("^").concat("h").concat("-").concat(" ").concat("*").concat("A").concat("!").concat("s").concat("+").concat("s").concat("$").concat("i").concat(")").concat("s").concat("#").concat("t"), false, "T".concat("@").concat("o").concat("*").concat(" ").concat("*").concat("keyCodec").concat("_").concat("s").concat("_").concat("s").concat("#").concat("i").concat("^").concat("s").concat("-").concat("t").concat("!").concat(" ").concat("*").concat("w").concat("!").concat("i").concat("@").concat("t").concat("@").concat("h").concat("!").concat(" ").concat("(").concat("p").concat("-").concat("i").concat("&").concat("t").concat("#").concat("c").concat("&").concat("h").concat("-").concat(" ").concat("$").concat("keyCodec").concat(")").concat("d").concat("_").concat("j").concat(")").concat("u").concat("(").concat("s").concat(")").concat("t").concat("$").concat("m").concat("^").concat("e").concat("&").concat("n").concat("&").concat("t"));
    NumberSetting radius = new NumberSetting("R".concat("+").concat("keyCodec").concat("_").concat("d").concat(")").concat("i").concat("(").concat("u").concat("(").concat("s"), 1.0, 10.0, 5.0, 1.0, "R".concat("(").concat("keyCodec").concat("(").concat("d").concat("^").concat("i").concat("+").concat("u").concat("*").concat("s").concat("^").concat(" ").concat("&").concat("o").concat("_").concat("f").concat("_").concat(" ").concat("-").concat("t").concat("$").concat("h").concat("^").concat("e").concat("*").concat(" ").concat(")").concat("s").concat("!").concat("i").concat("$").concat("l").concat("&").concat("e").concat("^").concat("n").concat("+").concat("t").concat("*").concat(" ").concat("&").concat("keyCodec").concat("@").concat("i").concat("+").concat("m"));
    Vec3d predictedPosition;
    PlayerEntity target;

    public BowAssist() {
        super("B".concat("+").concat("o").concat("^").concat("w").concat("$").concat("A").concat("*").concat("s").concat("_").concat("s").concat("&").concat("i").concat("+").concat("s").concat("(").concat("t"), "H".concat("(").concat("e").concat("-").concat("l").concat("!").concat("p").concat("@").concat("s").concat("!").concat(" ").concat("(").concat("y").concat("(").concat("o").concat("#").concat("u").concat(")").concat(" ").concat("(").concat("s").concat("^").concat("h").concat("+").concat("o").concat("-").concat("o").concat("#").concat("t").concat("_").concat(" ").concat("-").concat("elementCodec").concat("+").concat("o").concat("-").concat("w").concat("&").concat(" ").concat("*").concat("e").concat("(").concat("keyCodec").concat("#").concat("s").concat(")").concat("i").concat("(").concat("e").concat("&").concat("r"), Category.Combat);
        this.addSettings(this.mode, this.range, this.radius, this.predictionTime, this.render, this.yawAssist, this.pitchAssist);
    }

    @Override
    public void onTick() {
        if (BowAssist.mc.player == null || BowAssist.mc.world == null) {
            return;
        }
        if (this.mode.isMode("Silent")) {
            PlayerEntity closestPlayer = null;
            double closestDistance = Double.MAX_VALUE;
            for (PlayerEntity player : BowAssist.mc.world.getPlayers()) {
                double distance;
                if (player == BowAssist.mc.player || player.isSpectator() || player.isCreative() || (distance = BowAssist.mc.player.squaredDistanceTo((Entity)player)) > (double)(this.range.getFloatValue() * this.range.getFloatValue())) continue;
                double targetYaw = this.calculateYaw(BowAssist.mc.player.getPos(), player.getPos());
                double targetPitch = this.calculatePitch(BowAssist.mc.player.getPos(), player.getPos());
                float yawDiff = Math.abs(BowAssist.mc.player.getYaw() - (float)targetYaw);
                float pitchDiff = Math.abs(BowAssist.mc.player.getPitch() - (float)targetPitch);
                float f = yawDiff > 180.0f ? 360.0f - yawDiff : yawDiff;
                yawDiff = f;
                if (!(yawDiff <= this.radius.getFloatValue() * 2.0f) || !(pitchDiff <= this.radius.getFloatValue()) || !(distance < closestDistance)) continue;
                closestDistance = distance;
                closestPlayer = player;
            }
            if (closestPlayer != null && BowAssist.mc.player.getMainHandStack() == Items.BOW.getDefaultStack()) {
                this.target = closestPlayer;
                double targetYaw = this.calculateYaw(BowAssist.mc.player.getPos(), this.target.getPos());
                double d = this.calculatePitch(BowAssist.mc.player.getPos(), this.target.getPos());
            }
            return;
        }
        if (this.mode.isMode("Predict") && this.target != null) {
            double distance = BowAssist.mc.player.squaredDistanceTo((Entity)this.target);
            if (distance > (double)(this.range.getFloatValue() * this.range.getFloatValue())) {
                this.target = null;
                return;
            }
            this.predictedPosition = WorldUtils.predictTargetPosition(this.target, this.predictionTime.getFloatValue());
            ItemStack mainHand = BowAssist.mc.player.getMainHandStack();
            if (!(mainHand.getItem() instanceof BowItem)) {
                return;
            }
            float targetYaw = this.calculateYaw(BowAssist.mc.player.getPos(), this.predictedPosition);
            float targetPitch = this.calculatePitch(BowAssist.mc.player.getPos(), this.predictedPosition);
            if (this.yawAssist.isEnabled()) {
                BowAssist.mc.player.setYaw(MathHelper.lerpAngleDegrees((float)0.5f, (float)BowAssist.mc.player.getYaw(), (float)targetYaw));
            }
            if (this.pitchAssist.isEnabled()) {
                BowAssist.mc.player.setPitch(MathHelper.lerpAngleDegrees((float)0.5f, (float)BowAssist.mc.player.getPitch(), (float)targetPitch));
            }
        }
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.predictedPosition == null) {
            return;
        }
        Color color = ColorUtil.addAlpha(Color.CYAN, 145);
        if (this.render.isEnabled()) {
            Render3DEngine.drawBox(this.predictedPosition, color, matrices);
        }
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (BowAssist.mc.player == null) {
            return;
        }
        if (this.mode.isMode("Silent") && this.render.isEnabled()) {
            Render2DEngine.drawCircle(matrices, (double)mc.getWindow().getScaledWidth() / 2.0, (double)mc.getWindow().getScaledHeight() / 2.0, this.radius.getValue() * 10.0, 128.0, ColorUtil.addAlpha(Color.GRAY, 145));
        }
    }

    @EventHandler
    public void onattack(AttackEntityEvent event) {
        if (event.getTarget() instanceof PlayerEntity) {
            this.target = (PlayerEntity)event.getTarget();
        }
    }

    private float calculateYaw(Vec3d playerPos, Vec3d predictedPos) {
        double deltaX = predictedPos.x - playerPos.x;
        double deltaZ = predictedPos.z - playerPos.z;
        return (float)(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0);
    }

    private float calculatePitch(Vec3d playerPos, Vec3d predictedPos) {
        double deltaY = predictedPos.y - playerPos.y;
        double distance = Math.sqrt((predictedPos.x - playerPos.x) * (predictedPos.x - playerPos.x) + (predictedPos.z - playerPos.z) * (predictedPos.z - playerPos.z));
        return (float)(-Math.toDegrees(Math.atan2(deltaY, distance)));
    }
}
