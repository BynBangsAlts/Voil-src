package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.Setting;
import org.lwjgl.glfw.GLFW;

public class KeybindSetting
extends Setting {
    private int key;
    private boolean enabled;
    private boolean canToggleModule;
    private String description;

    public KeybindSetting(String name, int defaultKey, boolean canToggleModule, String description) {
        super(name);
        this.key = defaultKey;
        this.canToggleModule = canToggleModule;
        this.description = description;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void toggle() {
        if (this.canToggleModule) {
            this.enabled = !this.enabled;
        }
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public boolean isPressed() {
        return GLFW.glfwGetKey((long)ClientMain.mc.getWindow().getHandle(), (int)this.key) == 1;
    }

    public boolean canToggleModule() {
        return this.canToggleModule;
    }

    public void setCanToggleModule(boolean canToggleModule) {
        this.canToggleModule = canToggleModule;
    }
}
