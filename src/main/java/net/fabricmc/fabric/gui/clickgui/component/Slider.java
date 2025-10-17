package net.fabricmc.fabric.gui.clickgui.component;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Slider
extends Component {
    private final NumberSetting setting;
    private final GUI parent;
    private boolean dragging = false;

    public Slider(NumberSetting setting, float x, float y, GUI parent) {
        super(setting);
        this.setting = setting;
        this.parent = parent;
        this.setPosition(0.0f, 0.0f);
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float present = (float)((double)(this.x + (float)this.parent.x + (float)this.parent.width - 11.0f - (this.x + (float)this.parent.x + 450.0f)) * ((double)((Number)this.setting.getValue()).floatValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()));
        if (this.dragging) {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName() + " - " + this.setting.getValue(), this.x + (float)this.parent.x + 445.0f, this.y + 5.0f, -1);
        } else {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName() + " - " + this.setting.getValue(), this.x + (float)this.parent.x + 445.0f, this.y + 5.0f, Theme.MODULE_TEXT.getRGB());
        }
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 450.0f, this.y + 20.0f, this.x + (float)this.parent.x + (float)this.parent.width - 11.0f, this.y + 23.0f, 1.0, 10.0, Theme.SLIDER_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 450.0f, this.y + 20.0f, this.x + (float)this.parent.x + 455.0f + present, this.y + 23.0f, 1.0, 10.0, Theme.ENABLED);
        float circleX = this.x + (float)this.parent.x + 452.0f + present;
        float circleY = this.y + 26.0f - 4.5f;
        float circleRadius = 3.0f;
        Render2DEngine.drawCircle(matrices, circleX, circleY - 3.0f, circleRadius, 1.0f, Theme.TOGGLE_BUTTON_BG);
        if (this.dragging) {
            float render2 = (float)this.setting.getMin();
            double max = this.setting.getMax();
            double width = this.x + (float)this.parent.x + (float)this.parent.width - 11.0f - (this.x + (float)this.parent.x + 450.0f);
            double valAbs = (double)mouseX - (double)(this.x + (float)this.parent.x + 450.0f);
            double perc = Math.min(Math.max(0.0, valAbs / width), 1.0);
            double newValue = (double)render2 + (max - (double)render2) * perc;
            newValue = (double)Math.round(newValue * 10.0) / 10.0;
            this.setting.setValue(newValue);
        }
        if (Slider.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (Slider.isHovered(this.x + (float)this.parent.x + 450.0f, this.y + 18.0f, this.x + (float)this.parent.x + (float)this.parent.width - 11.0f, this.y + 23.5f, mouseX, mouseY)) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
    }

    @Override
    public Bounds getBounds() {
        float width = this.parent.width - 11 - 450;
        float height = 35.0f;
        return new Bounds(this.x + (float)this.parent.x + 450.0f, this.y + 20.0f, width, height);
    }
}
