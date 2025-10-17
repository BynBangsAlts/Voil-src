package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.gui.setting.Setting;

public class BooleanSetting
extends Setting {
    private boolean enabled;
    private String description;

    public BooleanSetting(String name, boolean defaultValue, String description) {
        super(name);
        this.enabled = defaultValue;
        this.description = description;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
