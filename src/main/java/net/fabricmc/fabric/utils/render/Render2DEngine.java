package net.fabricmc.fabric.utils.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.fabricmc.fabric.utils.shader.BlurProgram;
import net.fabricmc.fabric.utils.shader.CircleProgram;
import net.fabricmc.fabric.utils.shader.GlowProgram;
import net.fabricmc.fabric.utils.shader.OutlineProgram;
import net.fabricmc.fabric.utils.shader.RectangleProgram;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Render2DEngine {
    private static Vec3d eyesPos;
    public static VertexSorter vertexSorter;
    public static boolean rendering3D;
    public static BlurProgram BLUR_PROGRAM;
    public static RectangleProgram RECT_PROGRAM;
    public static CircleProgram CIRCLE_PROGRAM;
    public static GlowProgram GLOW_PROGRAM;
    public static OutlineProgram OUTLINE_PROGRAM;

    public static void unscaledProjection() {
        vertexSorter = RenderSystem.getVertexSorting();
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)ClientMain.mc.getWindow().getFramebufferWidth(), (float)ClientMain.mc.getWindow().getFramebufferHeight(), 0.0f, 1000.0f, 21000.0f), (VertexSorter)VertexSorter.BY_Z);
        rendering3D = false;
    }

    public static void scaledProjection() {
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)((double)ClientMain.mc.getWindow().getFramebufferWidth() / ClientMain.mc.getWindow().getScaleFactor()), (float)((double)ClientMain.mc.getWindow().getFramebufferHeight() / ClientMain.mc.getWindow().getScaleFactor()), 0.0f, 1000.0f, 21000.0f), (VertexSorter)vertexSorter);
        rendering3D = true;
    }

    public static void fill(@NotNull MatrixStack matrices, double x1, double y1, double x2, double y2, int color) {
        double i;
        Render2DEngine.setupRender();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float j = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0f).color(g, h, j, f);
        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0f).color(g, h, j, f);
        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0f).color(g, h, j, f);
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0f).color(g, h, j, f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.disableBlend();
        Render2DEngine.endRender();
    }

    public static void drawLine(@NotNull MatrixStack matrices, double x1, double y1, double x2, double y2, float thickness, Color color) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0.0) {
            return;
        }
        double controlX = (x1 + x2) / 2.0 + (dy /= length) * (double)thickness;
        double controlY = (y1 + y2) / 2.0 - (dx /= length) * (double)thickness;
        int segments = 10;
        for (int i = 0; i <= segments; ++i) {
            double nextT = (double)(i + 1) / (double)segments;
            double nextX = (1.0 - nextT) * (1.0 - nextT) * x1 + 2.0 * (1.0 - nextT) * nextT * controlX + nextT * nextT * x2;
            double t = (double)i / (double)segments;
            double x = (1.0 - t) * (1.0 - t) * x1 + 2.0 * (1.0 - t) * t * controlX + t * t * x2;
            double dx2 = nextX - x;
            double nextY = (1.0 - nextT) * (1.0 - nextT) * y1 + 2.0 * (1.0 - nextT) * nextT * controlY + nextT * nextT * y2;
            double y = (1.0 - t) * (1.0 - t) * y1 + 2.0 * (1.0 - t) * t * controlY + t * t * y2;
            double dy2 = nextY - y;
            double len = Math.sqrt(dx2 * dx2 + dy2 * dy2);
            if (len == 0.0) continue;
            double px = -(dy2 /= len) * ((double)thickness / 2.0);
            double py = (dx2 /= len) * ((double)thickness / 2.0);
            double x1a = x + px;
            double y1a = y + py;
            double x1b = x - px;
            double y1b = y - py;
            double x2a = nextX + px;
            double y2a = nextY + py;
            double x2b = nextX - px;
            double y2b = nextY - py;
            int rgba = (color.getAlpha() & 0xFF) << 24 | (color.getRed() & 0xFF) << 16 | (color.getGreen() & 0xFF) << 8 | color.getBlue() & 0xFF;
            Render2DEngine.setupRender();
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionProgram);
            float r = (float)(rgba >> 16 & 0xFF) / 255.0f;
            float g = (float)(rgba >> 8 & 0xFF) / 255.0f;
            float b = (float)(rgba & 0xFF) / 255.0f;
            float a = (float)(rgba >> 24 & 0xFF) / 255.0f;
            bufferBuilder.vertex(matrix, (float)x1a, (float)y1a, 0.0f).color(r, g, b, a * 0.8f);
            bufferBuilder.vertex(matrix, (float)x2a, (float)y2a, 0.0f).color(r, g, b, a * 0.8f);
            bufferBuilder.vertex(matrix, (float)x2b, (float)y2b, 0.0f).color(r, g, b, a * 0.8f);
            bufferBuilder.vertex(matrix, (float)x1b, (float)y1b, 0.0f).color(r, g, b, a * 0.8f);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            RenderSystem.disableBlend();
            Render2DEngine.endRender();
        }
    }

    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    public static void drawPlayerHead(DrawContext context, PlayerEntity player, float x, float y, int size) {
        Render2DEngine.setupRender();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && player != null) {
            GameProfile gameProfile = new GameProfile(player.getUuid(), player.getName().getString());
            PlayerListEntry playerListEntry = mc.player.networkHandler.getPlayerListEntry(gameProfile.getId());
            boolean bl3 = player.isPartVisible(PlayerModelPart.HAT);
            if (playerListEntry == null) {
                return;
            }
            SkinTextures playerSkinIdentifier = playerListEntry.getSkinTextures();
            if (playerListEntry != null) {
                RenderSystem.setShaderTexture((int)0, (Identifier)playerListEntry.getSkinTextures().texture());
            }
            PlayerSkinDrawer.draw((DrawContext)context, (Identifier)playerSkinIdentifier.texture(), (int)((int)x), (int)((int)y), (int)size, (boolean)bl3, (boolean)false);
        }
        Render2DEngine.endRender();
    }

    public static void drawCircle(MatrixStack matrices, float cx, float cy, float r, Color color) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.vertex(matrix, cx, cy, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        for (int i = 0; i <= 360; ++i) {
            double angle = Math.toRadians(i);
            float x = (float)((double)cx + Math.cos(angle) * (double)r);
            float y = (float)((double)cy + Math.sin(angle) * (double)r);
            buffer.vertex(matrix, x, y, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
    }

    public static Identifier getTexture(PlayerEntity player) {
        if (player == null) {
            return null;
        }
        GameProfile gameProfile = new GameProfile(player.getUuid(), player.getName().getString());
        assert (MinecraftClient.getInstance().player != null);
        PlayerListEntry playerListEntry = MinecraftClient.getInstance().player.networkHandler.getPlayerListEntry(gameProfile.getId());
        if (playerListEntry == null) {
            return null;
        }
        return playerListEntry.getSkinTextures().texture();
    }

    public static void drawRoundTexture(DrawContext context, Identifier texture, int x, int y, int w, int h, int r) {
        BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_TEXTURE);
        Matrix4f mat = context.getMatrices().peek().getPositionMatrix();
        buf.vertex(mat, (float)x + (float)w / 2.0f, (float)y + (float)h / 2.0f, 0.0f).texture(0.5f, 0.5f);
        int[][] corners = new int[][]{{x + w - r, y + r}, {x + w - r, y + h - r}, {x + r, y + h - r}, {x + r, y + r}};
        for (int corner = 0; corner < 4; ++corner) {
            int cornerStart = (corner - 1) * 90;
            int cornerEnd = cornerStart + 90;
            for (int i = cornerStart; i <= cornerEnd; i += 10) {
                float angle = (float)Math.toRadians(i);
                float rx = (float)corners[corner][0] + (float)(Math.cos(angle) * (double)r);
                float ry = (float)corners[corner][1] + (float)(Math.sin(angle) * (double)r);
                float u = (rx - (float)x) / (float)w;
                float v = (ry - (float)y) / (float)h;
                buf.vertex(mat, rx, ry, 0.0f).texture(u, v);
            }
        }
        buf.vertex(mat, (float)corners[0][0], (float)y, 0.0f).texture(((float)corners[0][0] - (float)x) / (float)w, 0.0f);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture((int)0, (Identifier)texture);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buf.end());
        RenderSystem.enableCull();
    }

    public static void drawHollowRect(MatrixStack matrixStack, int x, int y, int width, int height, int thickness, Color color) {
        Render2DEngine.setupRender();
        Render2DEngine.fill(matrixStack, x, y - thickness, x - thickness, y + height + thickness, color.getRGB());
        Render2DEngine.fill(matrixStack, x + width, y - thickness, x + width + thickness, y + height + thickness, color.getRGB());
        Render2DEngine.fill(matrixStack, x, y, x + width, y - thickness, color.getRGB());
        Render2DEngine.fill(matrixStack, x, y + height, x + width, y + height + thickness, color.getRGB());
        Render2DEngine.endRender();
    }

    public static void drawTexturedRectangle(DrawContext matrices, float x, float y, int width, int height, Identifier path) {
        matrices.drawTexture(path, (int)x, (int)y, 0.0f, 0.0f, width, height, width, height);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        Render2DEngine.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static void renderRoundedQuad(@NotNull MatrixStack matrices, double fromX, double fromY, double toX, double toY, double rad, double samples, Color c) {
        Render2DEngine.setupRender();
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        GL11.glEnable((int)32925);
        Render2DEngine.renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples);
        GL11.glDisable((int)32925);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Render2DEngine.endRender();
    }

    private static void drawRoundedRectangleOutline(Matrix4f matrix, double x, double y, double width, double height, double radius, double samples, float red, float green, float blue, float alpha, float thickness) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        int i = 0;
        while ((double)i <= samples) {
            double angle = (double)i / samples * 1.5707963267948966;
            float xOffset = (float)(Math.cos(angle) * radius);
            float yOffset = (float)(Math.sin(angle) * radius);
            Render2DEngine.vertex(bufferBuilder, matrix, (float)(x + radius - (double)xOffset), (float)(y + radius - (double)yOffset), red, green, blue, alpha);
            Render2DEngine.vertex(bufferBuilder, matrix, (float)(width - radius + (double)xOffset), (float)(y + radius - (double)yOffset), red, green, blue, alpha);
            Render2DEngine.vertex(bufferBuilder, matrix, (float)(width - radius + (double)xOffset), (float)(height - radius + (double)yOffset), red, green, blue, alpha);
            Render2DEngine.vertex(bufferBuilder, matrix, (float)(x + radius - (double)xOffset), (float)(height - radius + (double)yOffset), red, green, blue, alpha);
            ++i;
        }
        bufferBuilder.end();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private static void vertex(BufferBuilder bufferBuilder, Matrix4f matrix, float x, float y, float r, float g, float b, float a) {
        bufferBuilder.vertex(matrix, x, y, 0.0f).color(r, g, b, a);
    }

    public static void renderRoundedShadow(DrawContext poses, Color innerColor, double fromX, double fromY, double toX, double toY, double rad, double samples, double shadowWidth) {
        int color = innerColor.getRGB();
        Matrix4f matrix = poses.getMatrices().peek().getPositionMatrix();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        Render2DEngine.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Render2DEngine.renderRoundedShadowInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples, shadowWidth);
        Render2DEngine.endRender();
    }

    public static void renderRoundedShadowInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples, double wid) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{{toX1, toY1}, {toX1, fromY1}, {fromX1, fromY1}, {fromX1, toY1}};
        for (int i = 0; i < map.length; ++i) {
            double[] current = map[i];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                float sin = (float)(Math.sin(rad1) * rad);
                float cos = (float)(Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca);
                float sin1 = (float)((double)sin + Math.sin(rad1) * wid);
                float cos1 = (float)((double)cos + Math.cos(rad1) * wid);
                bufferBuilder.vertex(matrix, (float)current[0] + sin1, (float)current[1] + cos1, 0.0f).color(cr, cg, cb, 0.0f);
            }
        }
        double[] current = map[0];
        float rad1 = (float)Math.toRadians(0.0);
        float sin = (float)(Math.sin(rad1) * rad);
        float cos = (float)(Math.cos(rad1) * rad);
        bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca);
        float sin1 = (float)((double)sin + Math.sin(rad1) * wid);
        float cos1 = (float)((double)cos + Math.cos(rad1) * wid);
        bufferBuilder.vertex(matrix, (float)current[0] + sin1, (float)current[1] + cos1, 0.0f).color(cr, cg, cb, 0.0f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{{toX1, toY1}, {toX1, fromY1}, {fromX1, fromY1}, {fromX1, toY1}};
        for (int i = 0; i < 4; ++i) {
            double[] current = map[i];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                float sin = (float)(Math.sin(rad1) * rad);
                float cos = (float)(Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca);
            }
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    public static void drawCircle(MatrixStack matrices, double centerX, double centerY, double radius, double samples, Color color) {
        Render2DEngine.drawCircle(matrices, color, centerX, centerY, radius, (int)samples);
    }

    public static void drawCircle(MatrixStack matrices, Color c, double originX, double originY, double rad, int segments) {
        int segments1 = MathHelper.clamp((int)segments, (int)4, (int)360);
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        Render2DEngine.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 360; i += Math.min(360 / segments1, 360 - i)) {
            double radians = Math.toRadians(i);
            double sin = Math.sin(radians) * rad;
            double cos = Math.cos(radians) * rad;
            bufferBuilder.vertex(matrix, (float)(originX + sin), (float)(originY + cos), 0.0f).color(g, h, k, f);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        Render2DEngine.endRender();
    }

    public static void renderItem(MatrixStack matrixStack, ItemStack itemStack, float x, float y, float scale, boolean renderOverlay) {
        RenderSystem.disableDepthTest();
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0f);
        RenderHelper.getContext().drawItem(itemStack, (int)(x / scale), (int)(y / scale));
        if (renderOverlay) {
            RenderHelper.getContext().drawItemInSlot(MinecraftClient.getInstance().textRenderer, itemStack, (int)(x / scale), (int)(y / scale));
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();
    }

    public static void drawRoundedBlur(MatrixStack matrices, float x, float y, float width, float height, float radius, float blurStrength, float blurOpacity, boolean vignette) {
        if (vignette) {
            Render2DEngine.renderRoundedShadow(RenderHelper.getContext(), new Color(0, 0, 0, 80), x, y, x + width, y + height, radius, 16.0, 6.0);
        }
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x, y, width, height);
        BLUR_PROGRAM.setParameters(x, y, width, height, radius, blurStrength, blurOpacity);
        BLUR_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void drawRectangle(MatrixStack matrices, float x, float y, float width, float height, float radius, float smoothness, Color c1) {
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x - 10.0f, y - 10.0f, width + 20.0f, height + 20.0f);
        RECT_PROGRAM.setParameters(x, y, width, height, radius, smoothness, c1, c1, c1, c1);
        RECT_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void drawGradientRectangle(MatrixStack matrices, float x, float y, float width, float height, float radius, float smoothness, Color c1, Color c2, Color c3, Color c4) {
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x - 10.0f, y - 10.0f, width + 20.0f, height + 20.0f);
        RECT_PROGRAM.setParameters(x, y, width, height, radius, smoothness, c1, c2, c3, c4);
        RECT_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void drawCircle(MatrixStack matrices, float x, float y, float radius, float smoothness, Color c1) {
        Render2DEngine.drawRectangle(matrices, x, y, radius * 2.0f, radius * 2.0f, radius, smoothness, c1);
    }

    public static void drawGlow(MatrixStack matrices, float x, float y, float width, float height, float radius, float smoothness, Color c1) {
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x - 10.0f, y - 10.0f, width + 20.0f, height + 20.0f);
        GLOW_PROGRAM.setParameters(x, y, width, height, radius, smoothness, c1, c1, c1, c1);
        GLOW_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void drawGradientGlow(MatrixStack matrices, float x, float y, float width, float height, float radius, float smoothness, Color c1, Color c2, Color c3, Color c4) {
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x - 10.0f, y - 10.0f, width + 20.0f, height + 20.0f);
        GLOW_PROGRAM.setParameters(x, y, width, height, radius, smoothness, c1, c2, c3, c4);
        GLOW_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void drawOutline(MatrixStack matrices, float x, float y, float width, float height, float radius, float smoothness, float thickness, Color c1) {
        BufferBuilder bb = Render2DEngine.preShaderDraw(matrices, x - 10.0f, y - 10.0f, width + 20.0f, height + 20.0f);
        OUTLINE_PROGRAM.setParameters(x, y, width, height, radius, smoothness, thickness, c1, c1, c1, c1);
        OUTLINE_PROGRAM.use();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bb.end());
        Render2DEngine.endRender();
    }

    public static void betterScissor(double x, double y, double x2, double y2) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int xPercent = (int)(x / (double)mc.getWindow().getScaledWidth());
        int yPercent = (int)(y / (double)mc.getWindow().getHeight());
        int widthPercent = (int)(x2 / (double)mc.getWindow().getWidth());
        int heightPercent = (int)(y2 / (double)mc.getWindow().getHeight());
        RenderSystem.enableScissor((int)xPercent, (int)yPercent, (int)widthPercent, (int)heightPercent);
    }

    public static void initShaders() {
        BLUR_PROGRAM = new BlurProgram();
        RECT_PROGRAM = new RectangleProgram();
        CIRCLE_PROGRAM = new CircleProgram();
        GLOW_PROGRAM = new GlowProgram();
        OUTLINE_PROGRAM = new OutlineProgram();
    }

    public static void setRectanglePoints(BufferBuilder buffer, Matrix4f matrix, float x, float y, float x1, float y1) {
        buffer.vertex(matrix, x, y, 0.0f);
        buffer.vertex(matrix, x, y1, 0.0f);
        buffer.vertex(matrix, x1, y1, 0.0f);
        buffer.vertex(matrix, x1, y, 0.0f);
    }

    public static BufferBuilder preShaderDraw(MatrixStack matrices, float x, float y, float width, float height) {
        Render2DEngine.setupRender();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        Render2DEngine.setRectanglePoints(buffer, matrix, x, y, x + width, y + height);
        return buffer;
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void endRender() {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    static {
        rendering3D = true;
    }
}
