package net.fabricmc.fabric.gui.setting;

import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;

public class Setting {
    private final String name;
    private BooleanSupplier dependencyCondition;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void dependSetting(BooleanSupplier condition) {
        this.dependencyCondition = condition;
    }

    public boolean isVisible() {
        return this.dependencyCondition == null || this.dependencyCondition.getAsBoolean();
    }

    public static void dependSetting(Setting setting, String modeName, ModeSetting mode) {
        setting.dependSetting(() -> mode.isMode(modeName));
    }

    public static void dependSetting(Setting setting, BooleanSetting booleanSetting) {
        setting.dependSetting(booleanSetting::isEnabled);
    }

    public static void dependSetting(Setting setting, boolean condition) {
        setting.dependSetting(() -> condition);
    }

    public static void dependSettingBoolFalse(Setting setting, BooleanSetting booleanSetting) {
        setting.dependSetting(() -> !booleanSetting.isEnabled());
    }
}
