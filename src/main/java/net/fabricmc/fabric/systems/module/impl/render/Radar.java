package net.fabricmc.fabric.systems.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Radar
extends Module {
    private final int radarSize = 80;
    private final int radarX = 2;
    private final int radarY = 30;

    public Radar() {
        super("R".concat("@").concat("keyCodec").concat("#").concat("d").concat("*").concat("keyCodec").concat("-").concat("r"), "s".concat("^").concat("h").concat("$").concat("o").concat("^").concat("w").concat("#").concat("s").concat("!").concat(" ").concat(")").concat("w").concat("&").concat("h").concat("#").concat("e").concat("@").concat("r").concat("$").concat("e").concat("_").concat(" ").concat("+").concat("o").concat("*").concat("t").concat("-").concat("h").concat("&").concat("e").concat("$").concat("r").concat(")").concat(" ").concat("$").concat("p").concat("_").concat("l").concat("-").concat("keyCodec").concat("^").concat("y").concat(")").concat("e").concat("$").concat("r").concat("&").concat("s").concat("*").concat(" ").concat("+").concat("keyCodec").concat("$").concat("r").concat("$").concat("e"), Category.Render);
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (Radar.mc.player == null || Radar.mc.world == null) {
            return;
        }
        Render2DEngine.drawRoundedBlur(matrices, 2.0f, 30.0f, 80.0f, 80.0f, 6.0f, 14.0f, 0.0f, true);
        Render2DEngine.drawCircle(matrices, Color.WHITE, 42.0, 70.0, 3.0, 20);
        this.drawFOVCone(matrices, 42.0, 70.0, 20.0, 90.0, new Color(255, 255, 255, 200));
        for (Entity entity : Radar.mc.world.getEntities()) {
            if (entity == Radar.mc.player || !(entity instanceof PlayerEntity)) continue;
            this.drawEntityOnRadar(matrices, entity, Color.RED);
        }
    }

    public void drawFOVCone(MatrixStack matrices, double centerX, double centerY, double radius, double fovAngle, Color color) {
        double playerLook = Math.toRadians(Radar.mc.player.getYaw());
        double leftX = centerX + radius * Math.cos(playerLook + Math.toRadians(fovAngle));
        double leftY = centerY + radius * Math.sin(playerLook + Math.toRadians(fovAngle));
        double rightX = centerX + radius * Math.cos(playerLook - Math.toRadians(fovAngle));
        double rightY = centerY + radius * Math.sin(playerLook - Math.toRadians(fovAngle));
        Radar.drawTriangle(matrices, centerX, centerY, leftX, leftY, rightX, rightY, color);
    }

    public static void drawTriangle(MatrixStack matrices, double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        float a = (float)color.getAlpha() / 255.0f;
        buffer.vertex((float)x1, (float)y1, 0.0f).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, 0.0f).color(r, g, b, a);
        buffer.vertex((float)x3, (float)y3, 0.0f).color(r, g, b, a);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
    }

    private void drawEntityOnRadar(MatrixStack matrices, Entity entity, Color color) {
        if (Radar.mc.player == null) {
            return;
        }
        Vec3d playerPos = Radar.mc.player.getPos();
        Vec3d entityPos = entity.getPos();
        double radarHalf = 40.0;
        double radarCenterX = 2.0 + radarHalf;
        double radarCenterY = 30.0 + radarHalf;
        double scale = radarHalf / 50.0;
        double offsetX = (entityPos.x - playerPos.x) * scale;
        double offsetZ = (entityPos.z - playerPos.z) * scale;
        double posX = radarCenterX + offsetX;
        double posY = radarCenterY + offsetZ;
        posX = Math.max(2.0, Math.min(82.0, posX));
        posY = Math.max(30.0, Math.min(110.0, posY));
        Render2DEngine.drawCircle(matrices, color, posX, posY, 3.0, 20);
    }
}
