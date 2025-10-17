package net.fabricmc.fabric.gui.screens;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudGuiScreen
extends Screen {
    private Module draggingModule = null;
    private int dragOffsetX;
    private int dragOffsetY;
    private static final int grid = 10;

    public HudGuiScreen() {
        super(Text.of((String)"HUD Editor"));
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        if (ClientMain.getHudModuleManager() != null) {
            for (Module module : ClientMain.getHudModuleManager().getModules()) {
                int x = ClientMain.getHudModuleManager().getX(module);
                int y = ClientMain.getHudModuleManager().getY(module);
                boolean isModuleEnabled = ClientMain.getHudModuleManager().isEnabled(module);
                boolean isModuleHud = ClientMain.getHudModuleManager().isHud(module);
                if (!isModuleHud) continue;
                int rectWidth = 140;
                int rectHeight = 60;
                int rectTopLeftX = x;
                int rectTopLeftY = y;
                boolean isHovering = mouseX >= rectTopLeftX && mouseX <= rectTopLeftX + rectWidth && mouseY >= rectTopLeftY && mouseY <= rectTopLeftY + rectHeight;
                Color outlineColor = isHovering ? Color.GREEN : Color.WHITE;
                Render2DEngine.drawHollowRect(matrices.getMatrices(), rectTopLeftX, rectTopLeftY, rectWidth, rectHeight, 1, outlineColor);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Module module : ClientMain.getHudModuleManager().getModules()) {
                boolean isHovering;
                int x = ClientMain.getHudModuleManager().getX(module);
                int y = ClientMain.getHudModuleManager().getY(module);
                boolean isModuleHud = ClientMain.getHudModuleManager().isHud(module);
                boolean isModuleEnabled = ClientMain.getHudModuleManager().isEnabled(module);
                int rectWidth = 100;
                int rectHeight = 20;
                int rectTopLeftX = x;
                int rectTopLeftY = y;
                boolean bl = isHovering = mouseX >= (double)rectTopLeftX && mouseX <= (double)(rectTopLeftX + rectWidth) && mouseY >= (double)rectTopLeftY && mouseY <= (double)(rectTopLeftY + rectHeight);
                if (!isModuleHud || !isModuleEnabled || !isHovering) continue;
                this.draggingModule = module;
                this.dragOffsetX = (int)(mouseX - (double)x);
                this.dragOffsetY = (int)(mouseY - (double)y);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.draggingModule != null) {
            ClientMain.getHudModuleManager().setX(this.draggingModule, (int)mouseX - this.dragOffsetX);
            ClientMain.getHudModuleManager().setY(this.draggingModule, (int)mouseY - this.dragOffsetY);
            this.draggingModule = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 && this.draggingModule != null) {
            int newX = (int)mouseX - this.dragOffsetX;
            int newY = (int)mouseY - this.dragOffsetY;
            newX = Math.max(0, newX);
            newY = Math.max(0, newY);
            ClientMain.getHudModuleManager().setX(this.draggingModule, newX);
            ClientMain.getHudModuleManager().setY(this.draggingModule, newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
