package net.fabricmc.fabric.systems.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;

public class SettingsManager {
    private static final Gson gson = new Gson();

    public static void loadsettings(String jsonString) {
        try {
            JsonObject configJson;
            JsonObject rootJson = (JsonObject)gson.fromJson(jsonString, JsonObject.class);
            if (rootJson == null) {
                return;
            }
            JsonObject jsonObject = configJson = rootJson.has("settings") ? rootJson.getAsJsonObject("settings") : rootJson;
            if (configJson == null) {
                return;
            }
            configJson.entrySet().forEach(entry -> {
                String moduleName = (String)entry.getKey();
                try {
                    JsonObject moduleJson = ((JsonElement)entry.getValue()).getAsJsonObject();
                    SettingsManager.applyModuleSettings(moduleName, moduleJson);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void applyModuleSettings(String moduleName, JsonObject moduleJson) {
        for (Module module : ModuleManager.INSTANCE.getModules()) {
            if (!module.getName().equals(moduleName)) continue;
            boolean enabled = moduleJson.has("enabled") && moduleJson.get("enabled").getAsBoolean();
            module.setEnabled(enabled);
            if (moduleJson.has("key")) {
                module.setKey(moduleJson.get("key").getAsInt());
            }
            module.getSettings().forEach(setting -> {
                if (moduleJson.has(setting.getName())) {
                    if (setting instanceof BooleanSetting) {
                        BooleanSetting booleanSetting = (BooleanSetting)setting;
                        booleanSetting.setEnabled(moduleJson.get(setting.getName()).getAsBoolean());
                    } else if (setting instanceof NumberSetting) {
                        NumberSetting numberSetting = (NumberSetting)setting;
                        numberSetting.setValue(moduleJson.get(setting.getName()).getAsDouble());
                    } else if (setting instanceof ModeSetting) {
                        ModeSetting modeSetting = (ModeSetting)setting;
                        modeSetting.setMode(moduleJson.get(setting.getName()).getAsString());
                    } else if (setting instanceof TextSetting) {
                        TextSetting textSetting = (TextSetting)setting;
                        textSetting.setValue(moduleJson.get(setting.getName()).getAsString());
                    } else if (setting instanceof KeybindSetting) {
                        JsonObject keybindJson;
                        KeybindSetting keybindSetting = (KeybindSetting)setting;
                        if (moduleJson.has(setting.getName()) && (keybindJson = moduleJson.getAsJsonObject(setting.getName())).has("key")) {
                            keybindSetting.setKey(keybindJson.get("key").getAsInt());
                        }
                    } else if (setting instanceof ColorPickerSetting) {
                        ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                        colorPickerSetting.setColor(Color.decode(moduleJson.get(setting.getName()).getAsString()));
                    } else if (setting instanceof NumberSetting2) {
                        String[] range;
                        NumberSetting2 numberSetting2 = (NumberSetting2)setting;
                        JsonElement element = moduleJson.get(setting.getName());
                        if (element != null && element.isJsonPrimitive() && (range = element.getAsString().split("-")).length == 2) {
                            try {
                                double minValue = Double.parseDouble(range[0]);
                                double maxValue = Double.parseDouble(range[1]);
                                numberSetting2.setMinValue(minValue);
                                numberSetting2.setMaxValue(maxValue);
                            }
                            catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    public static JsonObject getSettings() {
        JsonObject settingsJson = new JsonObject();
        ModuleManager.INSTANCE.getModules().forEach(module -> {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
            moduleJson.addProperty("key", (Number)module.getKey());
            module.getSettings().forEach(setting -> {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting)setting;
                    moduleJson.addProperty(booleanSetting.getName(), Boolean.valueOf(booleanSetting.isEnabled()));
                } else if (setting instanceof NumberSetting) {
                    NumberSetting numberSetting = (NumberSetting)setting;
                    moduleJson.addProperty(numberSetting.getName(), (Number)numberSetting.getValue());
                } else if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)setting;
                    moduleJson.addProperty(modeSetting.getName(), modeSetting.getMode());
                } else if (setting instanceof TextSetting) {
                    TextSetting textSetting = (TextSetting)setting;
                    moduleJson.addProperty(textSetting.getName(), textSetting.getValue());
                } else if (setting instanceof ColorPickerSetting) {
                    ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                    moduleJson.addProperty(colorPickerSetting.getName(), "#" + Integer.toHexString(colorPickerSetting.getColor().getRGB()).substring(2));
                } else if (setting instanceof NumberSetting2) {
                    NumberSetting2 numberSetting2 = (NumberSetting2)setting;
                    moduleJson.addProperty(numberSetting2.getName(), numberSetting2.getMinValue() + "-" + numberSetting2.getMaxValue());
                } else if (setting instanceof KeybindSetting) {
                    KeybindSetting keybindSetting = (KeybindSetting)setting;
                    JsonObject keybindJson = new JsonObject();
                    keybindJson.addProperty("name", keybindSetting.getName());
                    keybindJson.addProperty("key", (Number)keybindSetting.getKey());
                    keybindJson.addProperty("toggleable", Boolean.valueOf(keybindSetting.canToggleModule()));
                    moduleJson.add(keybindSetting.getName(), (JsonElement)keybindJson);
                }
            });
            settingsJson.add(module.getName(), (JsonElement)moduleJson);
        });
        return settingsJson;
    }
}
