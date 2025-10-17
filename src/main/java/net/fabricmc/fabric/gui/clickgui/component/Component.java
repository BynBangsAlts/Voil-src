package net.fabricmc.fabric.gui.clickgui.component;

import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.utils.render.Bounds;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Component {
    private float height;
    private float width;
    protected Setting setting;
    protected float x;
    protected float y;

    public Component(Setting setting) {
        this.setting = setting;
    }

    public float getY() {
        return this.y;
    }

    public double getX() {
        return this.x;
    }

    void setX(double x) {
    }

    public void setY(float y, float settingsX) {
        this.y = y;
    }

    protected static boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)x2 && mouseY >= (double)y && mouseY <= (double)y2;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char chr, int modifiers) {
    }

    public abstract void mouseDragged(double var1, double var3, int var5, double var6, double var8);

    public void mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public Bounds getBounds() {
        return new Bounds(this.x, this.y, this.width, this.height);
    }
}
