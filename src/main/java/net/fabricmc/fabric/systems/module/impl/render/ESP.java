package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4d;

public class ESP
extends Module {
    public ModeSetting modes = new ModeSetting("m".concat("+").concat("o").concat("$").concat("d").concat(")").concat("e").concat("&").concat("s"), "2".concat("&").concat("D"), "M".concat("+").concat("o").concat("&").concat("d").concat("*").concat("e").concat("$").concat(" ").concat("^").concat("f").concat("-").concat("o").concat("(").concat("r").concat("@").concat(" ").concat("#").concat("t").concat("@").concat("h").concat("@").concat("e").concat("(").concat(" ").concat("!").concat("e").concat("&").concat("s").concat("$").concat("p"), "B".concat("^").concat("o").concat(")").concat("x"), "2".concat("$").concat("D"), "2".concat("-").concat("D").concat("@").concat(" ").concat("*").concat("C").concat("(").concat("o").concat("!").concat("r").concat("_").concat("n").concat("^").concat("e").concat(")").concat("r").concat("@").concat("s"), "2".concat("-").concat("D").concat("!").concat(" ").concat("!").concat("R").concat("@").concat("o").concat("-").concat("u").concat("#").concat("n").concat("!").concat("d"));
    public ModeSetting tracerY = new ModeSetting("T".concat("$").concat("r").concat("+").concat("keyCodec").concat("$").concat("c").concat("!").concat("e").concat("&").concat("r").concat("^").concat(" ").concat("#").concat("Y"), "F".concat("$").concat("e").concat("#").concat("e").concat("(").concat("t"), "F".concat("&").concat("r").concat("$").concat("o").concat("^").concat("m").concat("+").concat(" ").concat("@").concat("w").concat("&").concat("h").concat("_").concat("e").concat("-").concat("r").concat("+").concat("e").concat("^").concat(" ").concat(")").concat("l").concat("$").concat("i").concat("(").concat("n").concat(")").concat("e").concat("#").concat(" ").concat("#").concat("t").concat("!").concat("o").concat("@").concat(" ").concat("&").concat("g").concat("*").concat("o").concat(")").concat(" ").concat("*").concat("t").concat("(").concat("o").concat("_").concat(" ").concat("$").concat("e").concat("#").concat("n").concat("#").concat("e").concat("@").concat("m").concat(")").concat("y"), "F".concat("(").concat("e").concat("-").concat("e").concat("@").concat("t"), "C".concat("-").concat("r").concat("(").concat("o").concat("&").concat("s").concat("#").concat("s").concat("$").concat("h").concat("+").concat("keyCodec").concat("(").concat("i").concat("!").concat("r"));
    public BooleanSetting healthbar = new BooleanSetting("H".concat("!").concat("e").concat("_").concat("keyCodec").concat("^").concat("l").concat("@").concat("t").concat("!").concat("h").concat("^").concat("elementCodec").concat("*").concat("keyCodec").concat("&").concat("r"), true, "S".concat("^").concat("h").concat("$").concat("o").concat("^").concat("w").concat("$").concat(" ").concat("*").concat("h").concat("$").concat("e").concat("&").concat("keyCodec").concat("(").concat("l").concat("#").concat("t").concat("+").concat("h").concat("!").concat("elementCodec").concat("-").concat("keyCodec").concat("@").concat("r"));
    public ColorPickerSetting color = new ColorPickerSetting("L".concat("$").concat("i").concat("_").concat("n").concat("+").concat("e").concat("(").concat(" ").concat("(").concat("C").concat("&").concat("o").concat("*").concat("l").concat("&").concat("o").concat("@").concat("r"), new Color(130, 120, 255), "L".concat("+").concat("i").concat("&").concat("n").concat("$").concat("e").concat("$").concat(" ").concat("+").concat("c").concat("+").concat("o").concat("+").concat("l").concat("!").concat("o").concat("$").concat("r").concat("&").concat(" ").concat("*").concat("f").concat("#").concat("o").concat("_").concat("r").concat("^").concat(" ").concat("^").concat("t").concat(")").concat("h").concat("@").concat("e").concat("@").concat(" ").concat("&").concat("e").concat("-").concat("s").concat("#").concat("p"));
    public BooleanSetting fill = new BooleanSetting("F".concat("#").concat("i").concat("-").concat("l").concat("$").concat("l"), true, "D".concat("#").concat("r").concat("-").concat("keyCodec").concat("-").concat("w").concat("*").concat("s").concat("-").concat(" ").concat("(").concat("keyCodec").concat("&").concat(" ").concat("$").concat("f").concat("(").concat("i").concat("_").concat("l").concat("_").concat("l").concat(")").concat("e").concat("(").concat("d").concat("-").concat(" ").concat("-").concat("keyCodec").concat("(").concat("r").concat("^").concat("e").concat("(").concat("keyCodec"));
    public BooleanSetting tracers = new BooleanSetting("T".concat("$").concat("r").concat("$").concat("keyCodec").concat(")").concat("c").concat("+").concat("e").concat("@").concat("r").concat(")").concat("s"), true, "D".concat("*").concat("r").concat("$").concat("keyCodec").concat("^").concat("w").concat("^").concat("s").concat("(").concat(" ").concat("&").concat("keyCodec").concat(")").concat(" ").concat("+").concat("l").concat("(").concat("i").concat("$").concat("n").concat("_").concat("e").concat("*").concat(" ").concat("$").concat("t").concat("$").concat("o").concat("&").concat(" ").concat(")").concat("e").concat("#").concat("n").concat("^").concat("e").concat("&").concat("m").concat("#").concat("y"));
    public BooleanSetting flash = new BooleanSetting("F".concat("$").concat("l").concat("@").concat("keyCodec").concat("#").concat("s").concat("(").concat("h").concat("#").concat(" ").concat("$").concat("o").concat("+").concat("n").concat("-").concat(" ").concat("$").concat("d").concat("(").concat("keyCodec").concat("(").concat("m").concat("+").concat("keyCodec").concat("+").concat("g").concat("!").concat("e"), false, "C".concat("-").concat("h").concat("&").concat("keyCodec").concat("@").concat("n").concat("$").concat("g").concat("#").concat("e").concat("$").concat("s").concat("$").concat(" ").concat("@").concat("t").concat("$").concat("r").concat("+").concat("keyCodec").concat("-").concat("c").concat(")").concat("e").concat("(").concat("r").concat("@").concat("s").concat("-").concat(" ").concat("-").concat("l").concat("^").concat("i").concat("_").concat("n").concat("@").concat("e").concat("*").concat(" ").concat("!").concat("c").concat("*").concat("o").concat("+").concat("l").concat("-").concat("o").concat("&").concat("r").concat("#").concat(" ").concat("+").concat("t").concat("!").concat("o").concat("#").concat(" ").concat("#").concat("r").concat("+").concat("e").concat(")").concat("d").concat("&").concat(" ").concat(")").concat("w").concat("^").concat("h").concat("_").concat("e").concat("!").concat("n").concat("$").concat(" ").concat("_").concat("e").concat("+").concat("n").concat("*").concat("e").concat("+").concat("m").concat("(").concat("y").concat("!").concat(" ").concat("^").concat("d").concat("_").concat("keyCodec").concat("^").concat("m").concat("_").concat("keyCodec").concat("*").concat("g").concat("*").concat("e").concat("!").concat("d"));
    public BooleanSetting armor = new BooleanSetting("A".concat("&").concat("r").concat("_").concat("m").concat("+").concat("o").concat("!").concat("r"), false, "D".concat("@").concat("r").concat("@").concat("keyCodec").concat("&").concat("w").concat("+").concat("s").concat("@").concat(" ").concat("#").concat("p").concat("-").concat("l").concat("(").concat("keyCodec").concat(")").concat("y").concat("*").concat("e").concat("&").concat("r").concat("$").concat("s").concat("(").concat(" ").concat("+").concat("keyCodec").concat("+").concat("r").concat("*").concat("m").concat("(").concat("o").concat(")").concat("r"));

    public ESP() {
        super("E".concat("(").concat("S").concat("@").concat("P"), "H".concat("_").concat("i").concat("!").concat("g").concat("_").concat("h").concat("&").concat("l").concat("^").concat("i").concat("_").concat("g").concat("^").concat("h").concat("@").concat("t").concat("@").concat("s").concat("#").concat(" ").concat("@").concat("p").concat("+").concat("l").concat("@").concat("keyCodec").concat("#").concat("y").concat("#").concat("e").concat("^").concat("r").concat("^").concat("s"), Category.Render);
        this.addSettings(this.modes, this.healthbar, this.fill, this.tracers, this.flash, this.armor, this.tracerY, this.color);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.isEnabled()) {
            for (Entity entity : ESP.mc.world.getEntities()) {
                PlayerEntity player;
                if (!(entity instanceof PlayerEntity) || entity == ESP.mc.player || SocialManager.isBot(player = (PlayerEntity)entity)) continue;
                if (this.armor.isEnabled()) {
                    this.renderPlayerArmor(player, matrices);
                }
                if (this.tracers.isEnabled()) {
                    Vec3d start;
                    Color tracer = this.flash.isEnabled() && player.hurtTime > 0 ? Color.RED : this.color.getColor();
                    if (this.tracerY.getMode().equals("Crosshair")) {
                        Vec3d cameraPos = ESP.mc.player.getCameraPosVec(Utils.getTick());
                        Vec3d forward = ESP.mc.player.getRotationVec(Utils.getTick()).multiply(0.1);
                        start = cameraPos.add(forward);
                    } else {
                        start = ESP.mc.player.getLerpedPos(Utils.getTick());
                    }
                    Vec3d end = this.tracerY.getMode().equals("Crosshair") ? player.getLerpedPos(Utils.getTick()).add(0.0, (double)(player.getEyeHeight(player.getPose()) / 2.0f), 0.0) : player.getLerpedPos(Utils.getTick());
                    Render3DEngine.drawLine(start, end, tracer, matrices);
                }
                if (!this.modes.isMode("Box")) continue;
                int alpha = this.fill.isEnabled() ? 79 : 0;
                Color boxColor = new Color(0, 0, 0, alpha);
                Render3DEngine.drawEntityBox(player, boxColor, this.color.getColor(), matrices, 0.3f, player.getHeight());
                if (!this.healthbar.isEnabled()) continue;
                Render3DEngine.drawDynamicHealthBar(player, matrices, 0.3f, player.getHeight(), 255);
            }
        }
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (ESP.mc.world == null || ESP.mc.player == null) {
            return;
        }
        for (PlayerEntity player : ESP.mc.world.getPlayers()) {
            if (player == ESP.mc.player) continue;
            Vec3d[] vectors = ESP.getVectors((Entity)player);
            Vector4d position = null;
            for (Vec3d vector : vectors) {
                Vec3d screenPos = WorldUtils.worldToScreen(vector);
                if (!(screenPos.z > 0.0) || !(screenPos.z < 1.0)) continue;
                if (position == null) {
                    position = new Vector4d(screenPos.x, screenPos.y, screenPos.x, screenPos.y);
                    continue;
                }
                position.x = Math.min(screenPos.x, position.x);
                position.y = Math.min(screenPos.y, position.y);
                position.z = Math.max(screenPos.x, position.z);
                position.w = Math.max(screenPos.y, position.w);
            }
            if (position == null) continue;
            double distance = player.squaredDistanceTo((Entity)ESP.mc.player);
            float scaledThickness = Math.max(0.5f, (float)(1.0 / (Math.sqrt(distance) / 10.0 + 1.0)));
            int x = (int)position.x;
            int y = (int)position.y;
            int width = (int)(position.z - position.x);
            int height = (int)(position.w - position.y);
            int boxColor = this.color.getColor().getRGB();
            if (this.modes.isMode("2D")) {
                this.drawPlayerOutline(matrices, x, y, width, height, boxColor, scaledThickness);
            } else if (this.modes.isMode("2D Corners")) {
                this.drawCornerBox(matrices, x, y, width, height, boxColor, scaledThickness);
            } else if (this.modes.isMode("2D Round")) {
                Render2DEngine.drawGlow(matrices, x, y, width, height, 10.0f, 13.0f, this.color.getColor());
            }
            if (this.healthbar.isEnabled()) {
                this.drawHealthBar(matrices, player, x, y, width, height);
            }
            if (!this.armor.isEnabled()) continue;
            this.renderPlayerArmor(player, matrices);
        }
    }

    private void drawHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int width, int height) {
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercentage = health / maxHealth;
        Color hpColor = health > 18.0f ? new Color(57, 180, 57) : (health > 14.0f ? new Color(100, 200, 100) : (health > 10.0f ? new Color(180, 220, 120) : (health > 8.0f ? new Color(255, 165, 80) : (health > 5.0f ? new Color(200, 200, 100) : (health > 3.0f ? new Color(200, 100, 100) : new Color(255, 100, 80))))));
        int barWidth = 1;
        int fullHeight = height;
        int barHeight = (int)((float)fullHeight * healthPercentage);
        int barX = x - barWidth - 2;
        int barYBottom = y + height;
        int barYStart = barYBottom - barHeight;
        Render2DEngine.fill(matrices, barX, y, barX + barWidth, barYBottom, new Color(0, 0, 0, 150).getRGB());
        Render2DEngine.fill(matrices, barX, barYStart, barX + barWidth, barYBottom, hpColor.getRGB());
        this.drawOutline(matrices, barX - 1, y - 1, barWidth + 2, height + 2, Color.BLACK.getRGB(), 0.8f);
    }

    private void drawCornerBox(MatrixStack matrices, int x, int y, int width, int height, int color, float thickness) {
        int outlineColor = -16777216;
        float outlineThickness = thickness + 1.0f;
        int cornerSize = Math.min(width, height) / 3;
        Render2DEngine.fill(matrices, x - 1, y - 1, x + cornerSize + 1, (float)y + outlineThickness, outlineColor);
        Render2DEngine.fill(matrices, x - 1, y - 1, (float)x + outlineThickness, y + cornerSize + 1, outlineColor);
        Render2DEngine.fill(matrices, x, y, x + cornerSize, (float)y + thickness, color);
        Render2DEngine.fill(matrices, x, y, (float)x + thickness, y + cornerSize, color);
        Render2DEngine.fill(matrices, x + width - cornerSize - 1, y - 1, x + width + 1, (float)y + outlineThickness, outlineColor);
        Render2DEngine.fill(matrices, (float)(x + width) - outlineThickness, y - 1, x + width + 1, y + cornerSize + 1, outlineColor);
        Render2DEngine.fill(matrices, x + width - cornerSize, y, x + width, (float)y + thickness, color);
        Render2DEngine.fill(matrices, (float)(x + width) - thickness, y, x + width, y + cornerSize, color);
        Render2DEngine.fill(matrices, x - 1, (float)(y + height) - outlineThickness, x + cornerSize + 1, y + height + 1, outlineColor);
        Render2DEngine.fill(matrices, x - 1, y + height - cornerSize - 1, (float)x + outlineThickness, y + height + 1, outlineColor);
        Render2DEngine.fill(matrices, x, (float)(y + height) - thickness, x + cornerSize, y + height, color);
        Render2DEngine.fill(matrices, x, y + height - cornerSize, (float)x + thickness, y + height, color);
        Render2DEngine.fill(matrices, x + width - cornerSize - 1, (float)(y + height) - outlineThickness, x + width + 1, y + height + 1, outlineColor);
        Render2DEngine.fill(matrices, (float)(x + width) - outlineThickness, y + height - cornerSize - 1, x + width + 1, y + height + 1, outlineColor);
        Render2DEngine.fill(matrices, x + width - cornerSize, (float)(y + height) - thickness, x + width, y + height, color);
        Render2DEngine.fill(matrices, (float)(x + width) - thickness, y + height - cornerSize, x + width, y + height, color);
    }

    private void drawCorner(MatrixStack matrices, int x, int y, int length, float thickness, float outerOutlineThickness, float innerOutlineThickness, int color, int outerColor, int innerColor) {
    }

    @NotNull
    private static Vec3d[] getVectors(@NotNull Entity ent) {
        double x = ent.prevX + (ent.getX() - ent.prevX) * (double)Utils.getTick();
        double y = ent.prevY + (ent.getY() - ent.prevY) * (double)Utils.getTick();
        double z = ent.prevZ + (ent.getZ() - ent.prevZ) * (double)Utils.getTick();
        Box box = ent.getBoundingBox().offset(-ent.getX(), -ent.getY(), -ent.getZ()).offset(x, y, z);
        return new Vec3d[]{new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.minX, box.maxY, box.minZ), new Vec3d(box.maxX, box.minY, box.minZ), new Vec3d(box.maxX, box.maxY, box.minZ), new Vec3d(box.minX, box.minY, box.maxZ), new Vec3d(box.minX, box.maxY, box.maxZ), new Vec3d(box.maxX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.maxZ)};
    }

    public void drawPlayerOutline(MatrixStack matrices, int x, int y, int width, int height, int color, float thickness) {
        Render2DEngine.fill(matrices, (float)x - thickness - 1.0f, (float)y - thickness - 1.0f, (float)(x + width) + thickness + 1.0f, y, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, (float)x - thickness - 1.0f, y + height, (float)(x + width) + thickness + 1.0f, (float)(y + height) + thickness + 1.0f, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, (float)x - thickness - 1.0f, y - 1, x, y + height + 1, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, x + width, y - 1, (float)(x + width) + thickness + 1.0f, y + height + 1, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, x, y, x + width, (float)y + thickness, color);
        Render2DEngine.fill(matrices, x, (float)(y + height) - thickness, x + width, y + height, color);
        Render2DEngine.fill(matrices, x, y, (float)x + thickness, y + height, color);
        Render2DEngine.fill(matrices, (float)(x + width) - thickness, y, x + width, y + height, color);
        Render2DEngine.fill(matrices, (float)x + thickness, (float)y + thickness, (float)(x + width) - thickness, (float)y + thickness + 2.0f, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, (float)x + thickness, (float)(y + height) - thickness - 2.0f, (float)(x + width) - thickness, (float)(y + height) - thickness, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, (float)x + thickness, (float)y + thickness, (float)x + thickness + 2.0f, (float)(y + height) - thickness, Color.BLACK.getRGB());
        Render2DEngine.fill(matrices, (float)(x + width) - thickness - 2.0f, (float)y + thickness, (float)(x + width) - thickness, (float)(y + height) - thickness, Color.BLACK.getRGB());
        if (this.fill.isEnabled()) {
            Render2DEngine.fill(matrices, (float)x + thickness, (float)y + thickness, (float)(x + width) - thickness, (float)(y + height) - thickness, ColorUtil.addAlpha(Color.black, 145).getRGB());
        }
    }

    public void drawOutline(MatrixStack matrices, int x, int y, int width, int height, int color, float thickness) {
        Render2DEngine.fill(matrices, x, y, x + width, (float)y + thickness, color);
        Render2DEngine.fill(matrices, x, (float)(y + height) - thickness, x + width, y + height, color);
        Render2DEngine.fill(matrices, x, y, (float)x + thickness, y + height, color);
        Render2DEngine.fill(matrices, (float)(x + width) - thickness, y, x + width, y + height, color);
    }

    private void renderPlayerArmor(PlayerEntity player, MatrixStack matrices) {
    }
}
