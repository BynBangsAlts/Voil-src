package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import java.util.function.Consumer;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ColorPickerComponent
extends Component {
    private final ColorPickerSetting setting;
    private final ClickGUI parent;
    private final float x;
    private float y;
    private final Module module;
    private boolean isPicking;
    private boolean isDraggingRed;
    private boolean isDraggingGreen;
    private boolean isDraggingBlue;
    private boolean isExpanded = false;
    private float redHandlePos;
    private float greenHandlePos;
    private float blueHandlePos;

    public ColorPickerComponent(ColorPickerSetting setting, ClickGUI parent, float x, float y, Module module) {
        this.setting = setting;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.module = module;
        this.isPicking = false;
        this.isDraggingRed = false;
        this.isDraggingGreen = false;
        this.isDraggingBlue = false;
        this.redHandlePos = 0.0f;
        this.greenHandlePos = 0.0f;
        this.blueHandlePos = 0.0f;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color color = this.setting.getColor();
        float adjustedY = this.y;
        if (this.isExpanded) {
            adjustedY = this.y + 5.0f;
        }
        Color outlineColor = new Color(-8355712);
        float outlineThickness = 1.5f;
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX - outlineThickness, adjustedY + 2.0f - outlineThickness, this.x + this.parent.windowX + this.parent.width - 220.0f + outlineThickness, adjustedY + 22.0f + outlineThickness, 2.0 + (double)outlineThickness, 20.0, outlineColor);
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX, adjustedY + 2.0f, this.x + this.parent.windowX + this.parent.width - 220.0f, adjustedY + 22.0f, 2.0, 20.0, color);
        if (this.isPicking) {
            this.drawColorSliders(matrices, mouseX, mouseY);
        } else {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName(), this.x + this.parent.windowX + 485.0f + this.parent.settingsFieldX, adjustedY + 10.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        }
        if (ColorPickerComponent.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, adjustedY + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, adjustedY + 22.0f, mouseX, mouseY)) {
            this.renderTooltip(matrices, this.setting.getDescription().toString(), mouseX, mouseY);
        }
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

    private void drawColorSliders(MatrixStack matrices, int mouseX, int mouseY) {
        float sliderX = this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX;
        float sliderWidth = this.parent.width - 460.0f;
        float sliderHeight = 10.0f;
        this.drawColorSlider(matrices, "Red", this.setting.getColor().getRed(), 255, sliderX, this.y + 30.0f, sliderWidth, sliderHeight, mouseX, mouseY, value -> this.setting.setColor(new Color((int)value, this.setting.getColor().getGreen(), this.setting.getColor().getBlue())), this.isDraggingRed);
        this.drawColorSlider(matrices, "Green", this.setting.getColor().getGreen(), 255, sliderX, this.y + 45.0f, sliderWidth, sliderHeight, mouseX, mouseY, value -> this.setting.setColor(new Color(this.setting.getColor().getRed(), (int)value, this.setting.getColor().getBlue())), this.isDraggingGreen);
        this.drawColorSlider(matrices, "Blue", this.setting.getColor().getBlue(), 255, sliderX, this.y + 60.0f, sliderWidth, sliderHeight, mouseX, mouseY, value -> this.setting.setColor(new Color(this.setting.getColor().getRed(), this.setting.getColor().getGreen(), (int)value)), this.isDraggingBlue);
    }

    private void drawColorSlider(MatrixStack matrices, String label, int value, int maxValue, float x, float y, float width, float height, int mouseX, int mouseY, Consumer<Integer> onValueChanged, boolean isDragging) {
        Render2DEngine.renderRoundedQuad(matrices, x, y, x + width, y + height, 2.0, 20.0, new Color(100, 100, 100));
        float filledWidth = (float)value / (float)maxValue * width;
        Render2DEngine.renderRoundedQuad(matrices, x, y, x + filledWidth, y + height, 2.0, 20.0, new Color(141, 140, 140));
        float handlePos = x + (float)value / (float)maxValue * width;
        Render2DEngine.renderRoundedQuad(matrices, handlePos - 2.0f, y - 2.0f, handlePos + 2.0f, y + height + 2.0f, 2.0, 20.0, new Color(255, 255, 255));
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, label + ": " + value, x + width + 5.0f, y + 2.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        if (ColorPickerComponent.isHovered(x, y, x + width, y + height, mouseX, mouseY) && ClientMain.mc.mouse.wasLeftButtonClicked()) {
            isDragging = true;
        }
        if (isDragging) {
            int newValue = (int)(((float)mouseX - x) / width * (float)maxValue);
            newValue = Math.max(0, Math.min(maxValue, newValue));
            onValueChanged.accept(newValue);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ColorPickerComponent.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY) && button == 0) {
            this.isPicking = !this.isPicking;
            this.isExpanded = true;
            return true;
        }
        if (this.isPicking) {
            this.isDraggingRed = ColorPickerComponent.isHovered(this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX, this.y + 30.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 40.0f, mouseX, mouseY);
            this.isDraggingGreen = ColorPickerComponent.isHovered(this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX, this.y + 45.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 55.0f, mouseX, mouseY);
            this.isDraggingBlue = ColorPickerComponent.isHovered(this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX, this.y + 60.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 70.0f, mouseX, mouseY);
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.isDraggingRed = false;
        this.isDraggingGreen = false;
        this.isDraggingBlue = false;
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isPicking) {
            this.drawColorSliders(null, (int)mouseX, (int)mouseY);
        }
    }

    public void setY(float y) {
        this.y = y;
    }

    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
}
