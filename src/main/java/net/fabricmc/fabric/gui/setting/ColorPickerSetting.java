package net.fabricmc.fabric.gui.setting;

import java.awt.Color;
import net.fabricmc.fabric.gui.setting.Setting;

public class ColorPickerSetting
extends Setting {
    private Color color;
    private final String name;
    private String description;

    public ColorPickerSetting(String name, Color defaultColor, String description) {
        super(name);
        this.name = name;
        this.color = defaultColor;
        this.description = description;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    @Override
    public String getName() {
        return this.name.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }
}
