package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextComp
extends Component {
    private final TextSetting setting;
    private final ClickGUI parent;
    private String text;
    private boolean focused;
    private float x;
    private float y;
    private float height;

    public TextComp(ClickGUI parent, TextSetting setting, float x, float y, Module module) {
        this.parent = parent;
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.text = setting.getValue();
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.height = 20.0f;
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + this.parent.windowX + 446.0f + this.parent.settingsFieldX, this.y + 3.0f, this.x + this.parent.windowX + this.parent.width - 6.0f, this.y + 21.0f, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        ClientMain.fontRenderer.drawString(matrices, this.text, (double)(this.x + this.parent.windowX + 450.0f + this.parent.settingsFieldX), (double)(this.y + 6.0f), -1, false);
        if (TextComp.isHovered(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX, this.y + 2.0f, this.x + this.parent.windowX + this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.focused) {
            return false;
        }

        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (modifiers & 8) != 0 : (modifiers & 2) != 0;

        if (control && keyCode == 67) {
            ClientMain.mc.keyboard.setClipboard(this.text);
            return true;
        }
        if (control && keyCode == 88) {
            ClientMain.mc.keyboard.setClipboard(this.text);
            this.clearSelection();
            return true;
        }
        if (keyCode == 257 || keyCode == 335) {
            this.focused = false;
            this.setting.setValue(this.text);
            return true;
        }
        if (control && keyCode == 86) {
            String clipboard = ClientMain.mc.keyboard.getClipboard();
            this.text = this.text + clipboard;
            return true;
        }
        if (keyCode == 259 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.text.length() - 1);
        }

        return false;
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= (double)(this.x + this.parent.windowX + 445.0f + this.parent.settingsFieldX) && mouseX <= (double)(this.x + this.parent.windowX + this.parent.width - 5.0f) && mouseY >= (double)(this.y + 2.0f) && mouseY <= (double)(this.y + 22.0f) && button == 0) {
            this.focused = !this.focused;
            return true;
        }
        return false;
    }

    public void clearSelection() {
        this.text = "";
    }

    public boolean isHovered(double mouseX, double mouseY, double boundX1, double boundY1, double boundX2, double boundY2) {
        return mouseX >= boundX1 && mouseX <= boundX2 && mouseY >= boundY1 && mouseY <= boundY2;
    }

    private boolean isValid(char chr) {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"$%&/()=? _-:.,;+*/#'";
        return validChars.indexOf(chr) >= 0;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getText() {
        return this.text;
    }
}
