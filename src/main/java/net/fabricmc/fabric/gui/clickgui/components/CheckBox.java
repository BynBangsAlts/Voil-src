package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Animation;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class CheckBox
extends Component {
    private final BooleanSetting setting;
    private final ClickGUI parent;
    private final Animation animation;
    private final float x;
    private float y;

    public CheckBox(BooleanSetting setting, ClickGUI parent, float x, float y) {
        this.setting = setting;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.animation = new Animation(0.0f, 100.0f, 3.0f);
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.setting.isEnabled()) {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName(), this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 8.0f, -1);
            this.animation.setEnd(100.0f);
            Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + this.parent.width - 30.0f, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 10.0f, this.y + 12.0f, 4.0, 20.0, Theme.ENABLED);
            Render2DEngine.drawCircle(matrices, (double)(this.x + this.parent.windowX + this.parent.width - 25.0f + 10.0f * (this.animation.getValue() / 100.0f)), (double)(this.y + 7.0f), 3.5, 10.0, Color.white);
        } else {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName(), this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 8.0f, Theme.SLIDER_SETTING_BG.getRGB());
            this.animation.setEnd(0.0f);
            Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + this.parent.width - 30.0f, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 10.0f, this.y + 12.0f, 4.0, 20.0, Theme.TOGGLE_BUTTON_BG);
            Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + this.parent.width - 29.0f, this.y + 3.0f, this.x + this.parent.windowX + this.parent.width - 11.0f, this.y + 11.0f, 3.0, 20.0, Theme.MODE_SETTING_FILL);
            Render2DEngine.drawCircle(matrices, (double)(this.x + this.parent.windowX + this.parent.width - 25.0f + 10.0f * (this.animation.getValue() / 100.0f)), (double)(this.y + 7.0f), 3.5, 10.0, Theme.TOGGLE_BUTTON_BG);
        }
        this.animation.update();
        if (CheckBox.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
        if (CheckBox.isHovered(this.x + this.parent.windowX + this.parent.width - 30.0f, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 10.0f, this.y + 12.0f, mouseX, mouseY)) {
            this.setting.toggle();
        }
        return false;
    }

    public void setY(float y) {
        this.y = y;
    }
}
