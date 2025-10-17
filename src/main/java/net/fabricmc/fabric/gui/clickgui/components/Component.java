package net.fabricmc.fabric.gui.clickgui.components;

import net.minecraft.client.util.math.MatrixStack;

public abstract class Component {
    private float height;
    protected double x;
    protected double y;

    public double getY() {
        return this.y;
    }

    void setX(double x) {
    }

    public void setY(double y, float settingsX) {
        this.y = y;
    }

    protected static boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)x2 && mouseY >= (double)y && mouseY <= (double)y2;
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void charTyped(char chr, int modifiers) {
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
    }

    public abstract void mouseDragged(double var1, double var3, int var5, double var6, double var8);

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
