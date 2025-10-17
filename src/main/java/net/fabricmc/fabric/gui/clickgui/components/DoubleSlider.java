package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class DoubleSlider
extends Component {
    private final NumberSetting2 setting;
    private final ClickGUI parent;
    private final float x;
    private float y;
    private boolean draggingMin = false;
    private boolean draggingMax = false;

    public DoubleSlider(NumberSetting2 setting, float x, float y, ClickGUI parent) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float width = this.getSliderWidth();
        float minPresent = this.calculatePosition(this.setting.getMinValue());
        float maxPresent = this.calculatePosition(this.setting.getMaxValue());
        this.drawSliderBackground(matrices);
        this.drawSliderFill(matrices, minPresent, maxPresent);
        this.drawHandles(matrices, minPresent, maxPresent);
        this.drawText(matrices, minPresent, maxPresent);
        if (this.draggingMin || this.draggingMax) {
            this.updateSetting(mouseX);
        }
        if (DoubleSlider.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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

    private float getSliderWidth() {
        return this.x + this.parent.windowX + this.parent.width - 11.0f - (this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX);
    }

    private float calculatePosition(double value) {
        float width = this.getSliderWidth();
        return (float)((value - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * (double)width);
    }

    private void drawSliderBackground(MatrixStack matrices) {
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX, this.y + 20.0f, this.x + this.parent.windowX + this.parent.width - 11.0f, this.y + 23.0f, 1.0, 10.0, Theme.SLIDER_SETTING_BG);
    }

    private void drawSliderFill(MatrixStack matrices, float minPresent, float maxPresent) {
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX + minPresent, this.y + 20.0f, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX + maxPresent, this.y + 23.0f, 1.0, 10.0, Theme.ENABLED);
    }

    private void drawHandles(MatrixStack matrices, float minPresent, float maxPresent) {
        float circleY = this.y + 21.5f;
        float circleRadius = 4.0f;
        Render2DEngine.drawCircle(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX + minPresent, circleY, circleRadius, 50.0f, Theme.TOGGLE_BUTTON_BG);
        Render2DEngine.drawCircle(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX + maxPresent, circleY, circleRadius, 50.0f, Theme.TOGGLE_BUTTON_BG);
    }

    private void drawText(MatrixStack matrices, float minPresent, float maxPresent) {
        String displayText = this.setting.getName() + " - " + (int)this.setting.getMinValue() + " to " + (int)this.setting.getMaxValue();
        int color = this.draggingMin || this.draggingMax ? -1 : Theme.MODULE_TEXT.getRGB();
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, displayText, this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 5.0f, color);
    }

    private void updateSetting(double mouseX) {
        float width = this.getSliderWidth();
        double valAbs = mouseX - (double)(this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX);
        double perc = Math.min(Math.max(0.0, valAbs / (double)width), 1.0);
        double newValue = this.setting.getMin() + (this.setting.getMax() - this.setting.getMin()) * perc;
        if (this.draggingMin && newValue < this.setting.getMaxValue()) {
            this.setting.setMinValue(newValue);
        } else if (this.draggingMax && newValue > this.setting.getMinValue()) {
            this.setting.setMaxValue(newValue);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float minHandleX = this.getHandleX(this.setting.getMinValue());
        float maxHandleX = this.getHandleX(this.setting.getMaxValue());
        float hitboxSize = 6.0f;
        float handleY = this.y + 20.0f;
        if (DoubleSlider.isHovered(minHandleX - hitboxSize, handleY - hitboxSize, minHandleX + hitboxSize, handleY + hitboxSize, mouseX, mouseY)) {
            this.draggingMin = true;
            return true;
        }
        if (DoubleSlider.isHovered(maxHandleX - hitboxSize, handleY - hitboxSize, maxHandleX + hitboxSize, handleY + hitboxSize, mouseX, mouseY)) {
            this.draggingMax = true;
            return true;
        }
        return false;
    }

    private float getHandleX(double value) {
        return this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX + this.calculatePosition(value);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingMin = false;
        this.draggingMax = false;
    }

    public float getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
