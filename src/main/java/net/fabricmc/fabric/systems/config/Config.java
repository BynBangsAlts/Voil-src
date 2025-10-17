package net.fabricmc.fabric.systems.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;

public class Config {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Path configPath;
    private String name;
    private static String description;

    public Config(String name, String description) {
        this.name = name;
        Config.description = description;
        Path rootDir = Paths.get(System.getenv("LOCALAPPDATA"), "Programs", "Common");
        Path configFolder = rootDir.resolve("Configs");
        try {
            Files.createDirectories(configFolder, new FileAttribute[0]);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create config directories", e);
        }
        configPath = configFolder.resolve(name + ".json");
    }

    public static void save() {
        JsonObject json = new JsonObject();
        json.addProperty("description", description);
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
                } else if (setting instanceof KeybindSetting) {
                    KeybindSetting keybindSetting = (KeybindSetting)setting;
                    moduleJson.addProperty(keybindSetting.getName(), (Number)keybindSetting.getKey());
                } else if (setting instanceof TextSetting) {
                    TextSetting textSetting = (TextSetting)setting;
                    moduleJson.addProperty(textSetting.getName(), textSetting.getValue());
                }
            });
            json.add(module.getName().toString(), (JsonElement)moduleJson);
        });
        try (BufferedWriter writer = Files.newBufferedWriter(configPath, new OpenOption[0]);){
            gson.toJson((JsonElement)json, (Appendable)writer);
        }
        catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public void load() {
        if (!Files.exists(configPath, new LinkOption[0])) {
            System.err.println("Config file does not exist: " + String.valueOf(configPath));
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(configPath);){
            JsonObject json = (JsonObject)gson.fromJson((Reader)reader, JsonObject.class);
            description = json.getAsJsonPrimitive("description").getAsString();
            json.entrySet().forEach(entry -> {
                Module module = ModuleManager.INSTANCE.getModuleByName((String)entry.getKey());
                if (module == null) {
                    return;
                }
                JsonObject moduleJson = ((JsonElement)entry.getValue()).getAsJsonObject();
                module.setEnabled(moduleJson.get("enabled").getAsBoolean());
                module.setKey(moduleJson.get("key").getAsInt());
                module.getSettings().forEach(setting -> {
                    JsonElement settingElement = moduleJson.get(setting.getName());
                    if (setting instanceof BooleanSetting) {
                        ((BooleanSetting)setting).setEnabled(settingElement.getAsBoolean());
                    } else if (setting instanceof NumberSetting) {
                        ((NumberSetting)setting).setValue(settingElement.getAsDouble());
                    } else if (setting instanceof ModeSetting) {
                        ((ModeSetting)setting).setMode(settingElement.getAsString());
                    } else if (setting instanceof KeybindSetting) {
                        ((KeybindSetting)setting).setKey(settingElement.getAsInt());
                    } else if (setting instanceof TextSetting) {
                        ((TextSetting)setting).setValue(settingElement.getAsString());
                    }
                });
            });
        }
        catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    public void delete() {
        try {
            Files.deleteIfExists(configPath);
        }
        catch (IOException e) {
            System.err.println("Failed to delete config: " + e.getMessage());
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Config.description = description;
        Config.save();
    }
}
