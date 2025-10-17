package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.gui.setting.Setting;

public class ButtonSetting
extends Setting {
    private String label;
    private Runnable action;
    private String description;

    public ButtonSetting(String label, Runnable action, String description) {
        super(label);
        this.label = label;
        this.action = action;
        this.description = description;
    }

    public void onClick() {
        if (this.action != null) {
            this.action.run();
        }
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public String getLabel() {
        return this.label.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
