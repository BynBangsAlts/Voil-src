package net.fabricmc.fabric.gui.clickgui.components;

import java.util.ArrayList;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class TextComponent
extends Component {
    private String setting;
    private ClickGUI parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private String text;
    private boolean focused;
    private float x;
    private float y;
    private float height;

    public TextComponent(ClickGUI parent, String setting) {
        this.parent = parent;
        this.setting = setting;
    }

    public void draw(MatrixStack matrices, float x, float y, float height) {
        this.x = x;
        this.y = y;
        this.height = height;
        Render2DEngine.renderRoundedQuad(matrices, x, y, x + (float)this.mc.textRenderer.getWidth(this.text) + 5.0f, y + height, 5.0, 20.0, Theme.SETTINGS_HEADER);
        ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.text, x + 2.0f, y + 2.0f, -1);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (!this.focused) {
            return false;
        }

        boolean control;
        if (MinecraftClient.IS_SYSTEM_MAC) {
            control = (modifiers & 8) != 0;
        } else {
            control = (modifiers & 2) != 0;
        }

        if (control && key == 67) {
            this.mc.keyboard.setClipboard(this.text);
            return true;
        }

        if (control && key == 88) {
            this.mc.keyboard.setClipboard(this.text);
            this.clearSelection();
            return true;
        }

        if (key == 257 || key == 335) {
            this.focused = false;
            return true;
        }

        if (control && key == 86) {
            String clipboard = this.mc.keyboard.getClipboard();
            this.text += clipboard;
            return true;
        }

        if (key == 259) {
            if (this.text.length() > 0) {
                this.text = this.text.substring(0, this.text.length() - 1);
                return true;
            }
        }

        return false;
    }


    @Override
    public void charTyped(char letter, int modifiers) {
        boolean shift;
        if (!this.focused) {
            return;
        }
        boolean control = MinecraftClient.IS_SYSTEM_MAC ? modifiers == 8 : modifiers == 2;
        boolean bl = shift = modifiers == 1;
        if (!control && this.isValid(letter)) {
            if (shift) {
                letter = Character.valueOf(letter).toString().toUpperCase().charAt(0);
            }
            this.text = this.text + letter;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.focused = Component.isHovered(this.x, this.y, this.x + (float)this.mc.textRenderer.getWidth(this.text) + 5.0f, this.y + this.height, mouseX, mouseY) && button == 0;
        return false;
    }

    public void clearSelection() {
        this.text = "";
    }

    private boolean isValid(char letter) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"$%&/()=? _-:.,;+*/#'";
        ArrayList<Character> characters = new ArrayList<Character>();
        for (char ch : chars.toCharArray()) {
            characters.add(Character.valueOf(ch));
        }
        return characters.contains(Character.valueOf(letter));
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
