package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class Slider
extends Component {
    private final NumberSetting setting;
    private final ClickGUI parent;
    private final float x;
    private float y;
    private boolean dragging = false;

    public Slider(NumberSetting setting, float x, float y, ClickGUI parent) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float present = (float)((double)(this.x + this.parent.windowX + this.parent.width - 11.0f - (this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX)) * ((double)((Number)this.setting.getValue()).floatValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()));
        if (this.dragging) {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName() + " - " + this.setting.getValue(), this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 5.0f, -1);
        } else {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName() + " - " + this.setting.getValue(), this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 5.0f, Theme.MODULE_TEXT.getRGB());
        }
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX, this.y + 20.0f, this.x + this.parent.windowX + this.parent.width - 11.0f, this.y + 23.0f, 1.0, 10.0, Theme.SLIDER_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX, this.y + 20.0f, this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX + present, this.y + 23.0f, 1.0, 10.0, Theme.ENABLED);
        float circleX = this.x + this.parent.windowX + 452.0f + this.parent.settingsFieldX + present;
        float circleY = this.y + 26.0f - 4.5f;
        float circleRadius = 4.0f;
        Render2DEngine.drawCircle(matrices, circleX, circleY, circleRadius, 50.0f, Theme.TOGGLE_BUTTON_BG);
        if (this.dragging) {
            float render2 = (float)this.setting.getMin();
            double max = this.setting.getMax();
            double width = this.x + this.parent.windowX + this.parent.width - 11.0f - (this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX);
            double valAbs = (double)mouseX - (double)(this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX);
            double perc = Math.min(Math.max(0.0, valAbs / width), 1.0);
            double newValue = (double)render2 + (max - (double)render2) * perc;
            newValue = (double)Math.round(newValue * 10.0) / 10.0;
            this.setting.setValue(newValue);
        }
        if (Slider.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Slider.isHovered(this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX, this.y + 18.0f, this.x + this.parent.windowX + this.parent.width - 11.0f, this.y + 23.5f, mouseX, mouseY)) {
            this.dragging = true;
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
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
