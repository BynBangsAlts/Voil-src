package net.fabricmc.fabric.gui.clickgui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public class ColorPickerComponent
extends Component {
    private final ColorPickerSetting setting;
    private final GUI parent;
    private final Module module;
    private boolean expanded = false;
    private float selectedHue = 0.0f;
    private int selectedX = 0;
    private int selectedY = 0;
    private int selectedHueX = 0;
    private boolean draggingColor = false;
    private boolean draggingHue = false;

    public ColorPickerComponent(ColorPickerSetting setting, GUI parent, float x, float y, Module module) {
        super(setting);
        this.setting = setting;
        this.parent = parent;
        this.setPosition(0.0f, 0.0f);
        this.module = module;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color color = this.setting.getColor();
        ClientMain.fontRenderer.draw(matrices, this.setting.getName(), this.x + (float)this.parent.x + 445.0f, this.y - 10.0f, -1);
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 445.0f, this.y + 10.0f, this.x + (float)this.parent.x + (float)this.parent.width - 50.0f, this.y + 40.0f, 2.0, 20.0, color);
        if (this.expanded) {
            this.drawColor(matrices, (int)(this.x + (float)this.parent.x + 500.0f), (int)(this.y + 5.0f));
            this.drawColorSlider(matrices, (int)(this.x + (float)this.parent.x + 500.0f), (int)this.y);
        }
        if (ColorPickerComponent.isHovered(this.x, this.y + 2.0f, this.x + 20.0f, this.y + 2.0f + 20.0f, mouseX, mouseY)) {
            this.renderTooltip(matrices, this.setting.getDescription().toString(), mouseX, mouseY);
        }
    }

    private void drawColor(MatrixStack matrices, int x, int y) {
        int size = 100;
        ColorPickerComponent.drawColorBox(matrices, x, y, size + 50, size, Color.getHSBColor(this.selectedHue, 1.0f, 1.0f));
        Render2DEngine.drawCircle(matrices, x + this.selectedX, y + this.selectedY, 6.0f, 1.0f, Color.WHITE);
    }

    private void drawColorSlider(MatrixStack matrices, int x, int y) {
        int width = 150;
        int height = 15;
        ColorPickerComponent.drawHueSlider(matrices, x, y, width, height);
        Render2DEngine.drawCircle(matrices, x + this.selectedHueX, y + height / 2, 6.0f, 1.0f, Color.WHITE);
    }

    public static void drawColorBox(MatrixStack matrices, int x, int y, int width, int height, Color hue) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ColorPickerComponent.drawGradientRect(matrices, x, y, x + width, y + height, Color.WHITE.getRGB(), hue.getRGB());
        ColorPickerComponent.drawGradientRect(matrices, x, y, x + width, y + height, new Color(0, 0, 0, 0).getRGB(), hue.getRGB());
        RenderSystem.disableBlend();
    }

    public static void drawHueSlider(MatrixStack matrices, int x, int y, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int i = 0; i < width; ++i) {
            float hue = (float)i / (float)width;
            int color = Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
            ColorPickerComponent.drawGradientRect(matrices, x + i, y, x + i + 1, y + height, color, color);
        }
        RenderSystem.disableBlend();
    }

    private static void drawGradientRect(MatrixStack matrices, int left, int top, int right, int bottom, int startColor, int endColor) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        buffer.vertex(matrix, (float)left, (float)top, 0.0f).color(startColor >> 16 & 0xFF, startColor >> 8 & 0xFF, startColor & 0xFF, startColor >> 24 & 0xFF);
        buffer.vertex(matrix, (float)right, (float)top, 0.0f).color(endColor >> 16 & 0xFF, endColor >> 8 & 0xFF, endColor & 0xFF, endColor >> 24 & 0xFF);
        buffer.vertex(matrix, (float)right, (float)bottom, 0.0f).color(endColor >> 16 & 0xFF, endColor >> 8 & 0xFF, endColor & 0xFF, endColor >> 24 & 0xFF);
        buffer.vertex(matrix, (float)left, (float)bottom, 0.0f).color(startColor >> 16 & 0xFF, startColor >> 8 & 0xFF, startColor & 0xFF, startColor >> 24 & 0xFF);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.disableBlend();
    }

    private void renderTooltip(MatrixStack matrices, String description, int mouseX, int mouseY) {
        int maxLineLength = 40;
        String[] lines = description.split("(?<=\\G.{" + maxLineLength + "})");
        int tooltipWidth = 0;
        for (String line : lines) {
            int lineWidth = (int)(ClientMain.fontRenderer.getStringWidth(Text.of((String)line)) + 6.0f);
            tooltipWidth = Math.max(tooltipWidth, lineWidth);
        }
        int tooltipHeight = lines.length * 12 + 3;
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY - tooltipHeight - 3;
        Render2DEngine.renderRoundedQuad(matrices, tooltipX, tooltipY, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 2.0, 10.0, Color.darkGray);
        for (int i = 0; i < lines.length; ++i) {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, lines[i], (float)(tooltipX + 3), (float)(tooltipY + 6 + i * 12), Theme.NORMAL_TEXT_COLOR.getRGB());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (ColorPickerComponent.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 10.0f, this.x + (float)this.parent.x + (float)this.parent.width - 50.0f, this.y + 22.0f, (int)mouseX, (int)mouseY)) {
            this.expanded = !this.expanded;
        }
        int colorBoxX = (int)(this.x + (float)this.parent.x + 500.0f);
        int colorBoxY = (int)(this.y + 5.0f);
        int size = 150;
        if (this.expanded) {
            int height;
            int width;
            int hueSliderY;
            int hueSliderX;
            if (ColorPickerComponent.isHovered(colorBoxX, colorBoxY, colorBoxX + size, colorBoxY + size, (int)mouseX, (int)mouseY)) {
                this.selectedX = (int)(mouseX - (double)colorBoxX);
                this.selectedY = (int)(mouseY - (double)colorBoxY);
                this.updateColor();
                this.draggingColor = true;
            }
            if (ColorPickerComponent.isHovered(hueSliderX = (int)(this.x + (float)this.parent.x + 500.0f), hueSliderY = (int)this.y, hueSliderX + (width = 150), hueSliderY + (height = 15), (int)mouseX, (int)mouseY)) {
                this.selectedHueX = (int)(mouseX - (double)hueSliderX);
                this.selectedHue = (float)this.selectedHueX / (float)width;
                this.updateColor();
                this.draggingHue = true;
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.draggingColor) {
            int colorBoxX = (int)(this.x + (float)this.parent.x + 500.0f);
            int colorBoxY = (int)(this.y + 5.0f);
            int size = 100;
            int minY = 15;
            int maxY = size;
            this.selectedX = (int)Math.max(0.0, Math.min(mouseX - (double)colorBoxX, (double)(size + 50)));
            this.selectedY = (int)Math.max((double)minY, Math.min(mouseY - (double)colorBoxY, (double)maxY));
            this.updateColor();
        }
        if (this.draggingHue) {
            int hueSliderX = (int)(this.x + (float)this.parent.x + 500.0f);
            int width = 150;
            this.selectedHueX = (int)Math.max(0.0, Math.min(mouseX - (double)hueSliderX, (double)width));
            this.selectedHue = (float)this.selectedHueX / (float)width;
            this.updateColor();
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingColor = false;
        this.draggingHue = false;
    }

    private void updateColor() {
        this.setting.setColor(Color.getHSBColor(this.selectedHue, (float)this.selectedX / 150.0f, 1.0f - (float)this.selectedY / 150.0f));
    }

    @Override
    public Bounds getBounds() {
        int width = this.parent.width - 445 - 50;
        int height = 20;
        int xPos = (int)(this.x + (float)this.parent.x + 445.0f);
        int yPos = (int)(this.y + 10.0f);
        if (this.expanded) {
            int expandedHeight = 120;
            return new Bounds(xPos, yPos, width, expandedHeight);
        }
        return new Bounds(xPos, yPos, width, height);
    }
}
