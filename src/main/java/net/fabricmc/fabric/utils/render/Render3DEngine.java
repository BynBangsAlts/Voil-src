package net.fabricmc.fabric.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.fabricmc.fabric.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Render3DEngine {
    public static void drawBox(Vec3d pos, Color color, MatrixStack stack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        float boxWidth = 0.3f;
        float boxHeight = 1.8f;
        Vec3d start = pos.subtract(camPos);
        float x = (float)start.x;
        float y = (float)start.y;
        float z = (float)start.z;
        stack.push();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        GL11.glDepthFunc((int)519);
        RenderSystem.disableCull();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
        GL11.glDepthFunc((int)515);
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        stack.pop();
    }

    public static void drawOrb(MatrixStack matrices, Vec3d lastPos, Vec3d newPos, float radius, Color color) {
        double x = (lastPos.x + newPos.x) / 2.0;
        double y = (lastPos.y + newPos.y) / 2.0;
        double z = (lastPos.z + newPos.z) / 2.0;
        matrices.push();
        matrices.translate(x, y, z);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Render3DEngine.drawCircle3D(matrices, 0.0, 0.0, 0.0, radius * 1.5f, new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        Render3DEngine.drawCircle3D(matrices, 0.0, 0.0, 0.0, radius, color);
        matrices.pop();
        RenderSystem.disableBlend();
    }

    private static void drawCircle3D(MatrixStack matrices, double x, double y, double z, float radius, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        buffer.vertex(positionMatrix, (float)x, (float)y, (float)z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        int segments = 50;
        for (int i = 0; i <= segments; ++i) {
            double angle = Math.PI * 2 * (double)i / (double)segments;
            float dx = (float)(x + Math.cos(angle) * (double)radius);
            float dz = (float)(z + Math.sin(angle) * (double)radius);
            buffer.vertex(positionMatrix, dx, (float)y, dz).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        }
    }

    public static void drawBoxWithParams(Vec3d pos, Color color, MatrixStack stack, float boxWidth, float boxHeight) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        Vec3d start = pos.subtract(camPos);
        float x = (float)start.x;
        float y = (float)start.y;
        float z = (float)start.z;
        stack.push();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        GL11.glDepthFunc((int)519);
        RenderSystem.disableCull();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
        GL11.glDepthFunc((int)515);
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        stack.pop();
    }

    private static float getPlayerEyeHeight(ClientPlayerEntity player) {
        return player.getEyeHeight(EntityPose.STANDING);
    }

    public static void drawDynamicHealthBar(PlayerEntity entity, MatrixStack stack, float width, float height, int alpha) {
    }

    public static Color interpolateHealthColor(float healthRatio, int alpha) {
        int red;
        int green;
        if (healthRatio > 0.5f) {
            green = 255;
            red = (int)(510.0f * (1.0f - healthRatio));
        } else {
            red = 255;
            green = (int)(510.0f * healthRatio);
        }
        return new Color(red, green, 0, alpha);
    }

    public static void drawEntityBox(PlayerEntity entity, Color boxColor, Color lineColor, MatrixStack stack, float width, float height) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        Vec3d playerPos = entity.getLerpedPos(Utils.getTick());
        float red = (float)boxColor.getRed() / 255.0f;
        float green = (float)boxColor.getGreen() / 255.0f;
        float blue = (float)boxColor.getBlue() / 255.0f;
        float alpha = (float)boxColor.getAlpha() / 255.0f;
        float lineRed = (float)lineColor.getRed() / 255.0f;
        float lineGreen = (float)lineColor.getGreen() / 255.0f;
        float lineBlue = (float)lineColor.getBlue() / 255.0f;
        float lineAlpha = (float)lineColor.getAlpha() / 255.0f;
        float boxWidth = width;
        float boxHeight = height;
        Vec3d start = playerPos.subtract(camPos);
        float x = (float)start.x;
        float y = (float)start.y;
        float z = (float)start.z;
        stack.push();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        GL11.glDepthFunc((int)519);
        RenderSystem.disableCull();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth((float)5.0f);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y, z - boxWidth, x + boxWidth, y, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y, z - boxWidth, x + boxWidth, y, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y, z + boxWidth, x - boxWidth, y, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y, z + boxWidth, x - boxWidth, y, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y + boxHeight, z - boxWidth, x + boxWidth, y + boxHeight, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y + boxHeight, z - boxWidth, x + boxWidth, y + boxHeight, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y + boxHeight, z + boxWidth, x - boxWidth, y + boxHeight, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y + boxHeight, z + boxWidth, x - boxWidth, y + boxHeight, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y, z - boxWidth, x - boxWidth, y + boxHeight, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y, z - boxWidth, x + boxWidth, y + boxHeight, z - boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x + boxWidth, y, z + boxWidth, x + boxWidth, y + boxHeight, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        Render3DEngine.drawLine2D(buffer, matrix, x - boxWidth, y, z + boxWidth, x - boxWidth, y + boxHeight, z + boxWidth, lineRed, lineGreen, lineBlue, lineAlpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
        GL11.glDepthFunc((int)515);
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        stack.pop();
    }

    public static void drawLine(Vec3d lastPos, Vec3d pos, Color color, MatrixStack stack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        stack.push();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        GL11.glDepthFunc((int)519);
        RenderSystem.disableCull();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Vec3d start = lastPos.subtract(camPos);
        float startX = (float)start.x;
        float startY = (float)start.y;
        float startZ = (float)start.z;
        Vec3d end = pos.subtract(camPos);
        float endX = (float)end.x;
        float endY = (float)end.y;
        float endZ = (float)end.z;
        buffer.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha);
        buffer.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
        GL11.glDepthFunc((int)515);
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        stack.pop();
    }

    public static void drawLine2D(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float red, float green, float blue, float alpha) {
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
    }

    public static void drawRectangleOutline(MatrixStack matrices, BufferBuilder buffer, double x, double y, double z, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float size = 1.0f;
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)y, (float)z, (float)(x + (double)size), (float)y, (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)y, (float)z, (float)(x + (double)size), (float)y, (float)(z + (double)size), red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)y, (float)(z + (double)size), (float)x, (float)y, (float)(z + (double)size), red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)y, (float)(z + (double)size), (float)x, (float)y, (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)(y + (double)size), (float)z, (float)(x + (double)size), (float)(y + (double)size), (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)(y + (double)size), (float)z, (float)(x + (double)size), (float)(y + (double)size), (float)(z + (double)size), red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)(y + (double)size), (float)(z + (double)size), (float)x, (float)(y + (double)size), (float)(z + (double)size), red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)(y + (double)size), (float)(z + (double)size), (float)x, (float)(y + (double)size), (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)y, (float)z, (float)x, (float)(y + (double)size), (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)y, (float)z, (float)(x + (double)size), (float)(y + (double)size), (float)z, red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)(x + (double)size), (float)y, (float)(z + (double)size), (float)(x + (double)size), (float)(y + (double)size), (float)(z + (double)size), red, green, blue, alpha);
        Render3DEngine.addLine(buffer, matrix, (float)x, (float)y, (float)(z + (double)size), (float)x, (float)(y + (double)size), (float)(z + (double)size), red, green, blue, alpha);
    }

    public static void drawFilledRectangle(MatrixStack matrices, BufferBuilder buffer, float x, float y, float z, float boxWidth, float boxHeight, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z - boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z + boxWidth).color(red, green, blue, alpha);
        buffer.vertex(matrix, x - boxWidth, y + boxHeight, z - boxWidth).color(red, green, blue, alpha);
    }

    public static void renderRoundedQuad3D(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        GL11.glDepthFunc((int)519);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        buffer.vertex(matrix, x + radius, y + radius, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + radius, y + height - radius, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + width - radius, y + height - radius, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + width - radius, y + radius, 0.0f).color(red, green, blue, alpha);
        Render3DEngine.drawCorner(matrix, buffer, x + radius, y + radius, radius, 180, 270, red, green, blue, alpha);
        Render3DEngine.drawCorner(matrix, buffer, x + width - radius, y + radius, radius, 270, 360, red, green, blue, alpha);
        Render3DEngine.drawCorner(matrix, buffer, x + width - radius, y + height - radius, radius, 0, 90, red, green, blue, alpha);
        Render3DEngine.drawCorner(matrix, buffer, x + radius, y + height - radius, radius, 90, 180, red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
        GL11.glDepthFunc((int)515);
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
    }

    private static void drawCorner(Matrix4f matrix, BufferBuilder buffer, float cx, float cy, float radius, int startAngle, int endAngle, float red, float green, float blue, float alpha) {
        for (int i = startAngle; i <= endAngle; i += 11) {
            double angle = Math.toRadians(i);
            float x = (float)((double)cx + Math.cos(angle) * (double)radius);
            float y = (float)((double)cy + Math.sin(angle) * (double)radius);
            buffer.vertex(matrix, x, y, 0.0f).color(red, green, blue, alpha);
        }
    }

    private static void addQuad(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b, float alpha) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, alpha);
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, alpha);
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, alpha);
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, alpha);
        buffer.vertex(matrix, x4, y4, z4).color(r, g, b, alpha);
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, alpha);
    }

    private static void addLine(BufferBuilder buffer, Matrix4f matrix, float startX, float startY, float startZ, float endX, float endY, float endZ, float r, float g, float b, float alpha) {
        buffer.vertex(matrix, startX, startY, startZ).color(r, g, b, alpha);
        buffer.vertex(matrix, endX, endY, endZ).color(r, g, b, alpha);
    }
}
