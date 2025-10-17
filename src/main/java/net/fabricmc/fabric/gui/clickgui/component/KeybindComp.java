package net.fabricmc.fabric.gui.clickgui.component;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class KeybindComp
extends Component {
    private final Module module;
    private final KeybindSetting setting;
    private final GUI parent;
    private boolean waitingForKey = false;

    public KeybindComp(KeybindSetting setting, GUI parent, float x, float y, Module module) {
        super(setting);
        this.setting = setting;
        this.parent = parent;
        this.setPosition(0.0f, 0.0f);
        this.module = module;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 446.0f, this.y + 3.0f, this.x + (float)this.parent.x + (float)this.parent.width - 6.0f, this.y + 21.0f, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        String keyName = this.waitingForKey ? "Listening..." : "Key: " + KeyUtils.getKeyName(this.setting.getKey());
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, keyName, this.x + (float)this.parent.x + 460.0f, this.y + 10.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        if (this.setting.getKey() == 256 || KeyUtils.getKeyName(this.setting.getKey()).equals("ESCAPE")) {
            this.module.setKey(0);
        }
        if (KeybindComp.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
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
        if (this.waitingForKey) {
            int keyCode = button + 0;
            this.setting.setKey(keyCode);
            if (this.setting.canToggleModule()) {
                this.module.setKey(keyCode);
            }
            this.waitingForKey = false;
        } else if (KeybindComp.isHovered(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, this.x + (float)this.parent.x + (float)this.parent.width - 5.0f, this.y + 22.0f, mouseX, mouseY)) {
            this.waitingForKey = !this.waitingForKey;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.waitingForKey) {
            this.setting.setKey(keyCode);
            if (this.setting.canToggleModule()) {
                this.module.setKey(keyCode);
            }
            this.waitingForKey = false;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public Bounds getBounds() {
        float width = this.parent.width - 5;
        return new Bounds(this.x + (float)this.parent.x + 445.0f, this.y + 2.0f, width, 20.0f);
    }

    public void setY(float y) {
        this.y = y;
    }
}
