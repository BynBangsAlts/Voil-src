package net.fabricmc.fabric.gui.clickgui.component;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextComp
extends Component {
    private final TextSetting setting;
    private final GUI parent;
    private String text;
    private int height;
    private boolean focused;

    public TextComp(GUI parent, TextSetting setting, float x, float y, Module module) {
        super(setting);
        this.parent = parent;
        this.setting = setting;
        this.setPosition(0.0f, 0.0f);
        this.text = setting.getValue();
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.height = 20;
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 446.0f, this.y + 3.0f, this.x + (float)this.parent.x + (float)this.parent.width - 6.0f, this.y + 21.0f, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        ClientMain.fontRenderer.drawString(matrices, this.text, (double)(this.x + (float)this.parent.x + 450.0f), (double)(this.y + 2.0f), -1, false);
        if (TextComp.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.focused) {
            return;
        }

        boolean control;
        if (MinecraftClient.IS_SYSTEM_MAC) {
            control = (modifiers & 8) != 0;
        } else {
            control = (modifiers & 2) != 0;
        }

        if (control && keyCode == 67) {
            ClientMain.mc.keyboard.setClipboard(this.text);
            return;
        }

        if (control && keyCode == 88) {
            ClientMain.mc.keyboard.setClipboard(this.text);
            this.clearSelection();
            return;
        }

        if (keyCode == 257 || keyCode == 335) {
            this.focused = false;
            this.setting.setValue(this.text);
            return;
        }

        if (control && keyCode == 86) {
            String clipboard = ClientMain.mc.keyboard.getClipboard();
            this.text = this.text + clipboard;
            return;
        }

        if (keyCode == 259 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.text.length() - 1);
        }
    }


    @Override
    public void charTyped(char chr, int modifiers) {
        boolean shift;
        if (!this.focused) {
            return;
        }
        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (modifiers & 8) != 0 : (modifiers & 2) != 0;
        boolean bl = shift = (modifiers & 1) != 0;
        if (!control && this.isValid(chr)) {
            if (shift) {
                chr = Character.toUpperCase(chr);
            }
            this.text = this.text + chr;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (TextComp.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
            this.focused = !this.focused;
        }
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

    @Override
    public Bounds getBounds() {
        float width = this.parent.width - 11 - 450;
        float height = 23.0f;
        return new Bounds(this.x + (float)this.parent.x + 450.0f, this.y + 20.0f, width, height);
    }
}
