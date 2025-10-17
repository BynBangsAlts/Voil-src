package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.gui.setting.Setting;

public class TextSetting
extends Setting {
    private String value;
    private String description;

    public TextSetting(String name, String defaultValue, String description) {
        super(name);
        this.value = defaultValue;
        this.description = description;
    }

    public String getValue() {
        return this.value.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void setValue(String value) {
        this.value = value;
    }
}
