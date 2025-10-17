package net.fabricmc.fabric.systems.module.core;

import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.gui.DrawContext;

public abstract class HudModule
extends Module {
    private int x;
    private int y;
    private int width;
    private int height;

    public HudModule(String name, String description, Category category, int x, int y, int width, int height) {
        super(name, description, category);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(DrawContext var1);

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
