package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class ModeComp
extends Component {
    private final ModeSetting setting;
    private final ClickGUI parent;
    private final float x;
    private float y;

    public ModeComp(ModeSetting setting, ClickGUI parent, float x, float y) {
        this.setting = setting;
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 446.0f + this.parent.settingsFieldX, this.y + 3.0f, this.x + this.parent.windowX + this.parent.width - 6.0f, this.y + 21.0f, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, this.setting.getName() + ":" + this.setting.getMode(), this.x + this.parent.windowX + 455.0f + this.parent.settingsFieldX, this.y + 10.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        if (ModeComp.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
        if (ModeComp.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY) && button == 0) {
            this.setting.cycle();
        }
        return false;
    }

    public void setY(float y) {
        this.y = y;
    }
}
