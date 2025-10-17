package net.fabricmc.fabric.command.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.config.ConfigLoader;
import net.fabricmc.fabric.systems.config.JsonUtils;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.ChatUtils;

public class Config
extends Command {
    public static final Path configsDirectory = Paths.get(System.getenv("LOCALAPPDATA"), "Programs", "Common", "Configs");
    public static final String PREFIX = "\u00a7d\u00a7l[Voil] \u00a7r";

    public Config() {
        super("config", "load/create/list configs", "cfg", "cf");
        try {
            Files.createDirectories(configsDirectory, new FileAttribute[0]);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create configs directory", e);
        }
    }

    @Override
    public void onCmd(String message, String[] args) {
        String subCommand;
        if (args.length < 2) {
            ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rNot enough arguments. Use 'load', 'create', 'list', or 'delete'.");
            return;
        }
        switch (subCommand = args[1].toLowerCase()) {
            case "load": {
                if (args.length < 3) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rSpecify the config name to load.");
                    break;
                }
                String configName = args[2];
                Path configPath = configsDirectory.resolve(configName + ".json");
                if (Files.exists(configPath, new LinkOption[0])) {
                    try {
                        Config.loadConfigFromFile(configPath);
                        ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " loaded successfully.");
                        break;
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " not found.");
                break;
            }
            case "create": {
                if (args.length < 4) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rSpecify the config name and description to create.");
                    break;
                }
                String configName = args[2];
                String description = args[3];
                Path configPath = configsDirectory.resolve(configName + ".json");
                if (Files.exists(configPath, new LinkOption[0])) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " already exists.");
                    break;
                }
                try {
                    Config.save(configName, description, false);
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " created successfully.");
                }
                catch (IOException e) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError creating config " + configName + ": " + e.getMessage());
                }
                break;
            }
            case "delete": {
                if (args.length < 3) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rSpecify the config name to delete.");
                    break;
                }
                String configName = args[2];
                Path configPath = configsDirectory.resolve(configName + ".json");
                if (Files.exists(configPath, new LinkOption[0])) {
                    try {
                        Files.delete(configPath);
                        this.removeConfigByName(configName);
                        ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " deleted successfully.");
                    }
                    catch (IOException e) {
                        ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError deleting config " + configName + ": " + e.getMessage());
                    }
                    break;
                }
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " not found.");
                break;
            }
            case "list": {
                try {
                    this.listConfigs();
                }
                catch (IOException e) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError listing configs: " + e.getMessage());
                }
                break;
            }
            case "save": {
                if (args.length < 3) {
                    ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rSpecify the config name to save.");
                    break;
                }
                String configName = args[2];
                Path configPath = configsDirectory.resolve(configName + ".json");
                if (Files.exists(configPath, new LinkOption[0])) {
                    try {
                        Config.save(configName, null, true);
                        ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " updated successfully.");
                    }
                    catch (IOException e) {
                        ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError updating config " + configName + ": " + e.getMessage());
                    }
                    break;
                }
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rConfig " + configName + " not found.");
                break;
            }
            default: {
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rUnknown command. Use 'load', 'create', 'list', or 'save'.");
            }
        }
    }

    public static void save(String configName, String description, boolean overwrite) throws IOException {
        Path configPath = configsDirectory.resolve(configName + ".json");
        if (!overwrite && Files.exists(configPath, new LinkOption[0])) {
            throw new IOException("Config " + configName + " already exists. Use config save to update.");
        }
        JsonObject json = new JsonObject();
        if (description != null) {
            json.addProperty("description", description);
        }
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
                } else if (setting instanceof ColorPickerSetting) {
                    ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                    String hexColor = "#" + Integer.toHexString(colorPickerSetting.getColor().getRGB()).substring(2);
                    moduleJson.addProperty(colorPickerSetting.getName(), hexColor);
                } else if (setting instanceof NumberSetting2) {
                    NumberSetting2 numberSetting2 = (NumberSetting2)setting;
                    JsonObject numberSetting2Json = new JsonObject();
                    numberSetting2Json.addProperty("minValue", (Number)numberSetting2.getMinValue());
                    numberSetting2Json.addProperty("maxValue", (Number)numberSetting2.getMaxValue());
                    numberSetting2Json.addProperty("increment", (Number)numberSetting2.getIncrement());
                    moduleJson.add(numberSetting2.getName(), (JsonElement)numberSetting2Json);
                }
            });
            json.add(module.getName().toString(), (JsonElement)moduleJson);
        });
        try (BufferedWriter writer = Files.newBufferedWriter(configPath, new OpenOption[0]);){
            JsonUtils.gson.toJson((JsonElement)json, (Appendable)writer);
        }
        catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
            throw e;
        }
        List<net.fabricmc.fabric.systems.config.Config> configs = ConfigLoader.getConfigs();
        boolean found = false;
        for (net.fabricmc.fabric.systems.config.Config config : configs) {
            if (!config.getName().equalsIgnoreCase(configName)) continue;
            if (description != null) {
                config.setDescription(description);
            }
            found = true;
            break;
        }
        if (!found) {
            configs.add(new net.fabricmc.fabric.systems.config.Config(configName, description));
        }
    }

    private void listConfigs() throws IOException {
        if (Config.mc.world == null) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(configsDirectory, "*.json");){
            StringBuilder builder = new StringBuilder();
            builder.append("\u00a7d\u00a7l[Voil] \u00a7rYour configs: \n");
            for (Path entry : stream) {
                if (!Files.isRegularFile(entry, new LinkOption[0])) continue;
                String fileName = entry.getFileName().toString();
                builder.append("- ").append(fileName, 0, fileName.lastIndexOf(46)).append("\n");
            }
            ChatUtils.addChatMessage(builder.toString());
        }
    }

    public static void loadConfigFromFile(Path path) throws IOException {
        if (Config.mc.world == null) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path);){
            JsonObject json = (JsonObject)JsonUtils.gson.fromJson((Reader)reader, JsonObject.class);
            json.entrySet().forEach(entry -> Config.applyModuleSettings((String)entry.getKey(), (JsonElement)entry.getValue()));
        }
        catch (Exception e) {
            ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError loading config: " + e.getMessage());
        }
    }

    private void removeConfigByName(String configName) {
        List<net.fabricmc.fabric.systems.config.Config> configs = ConfigLoader.getConfigs();
        configs.removeIf(config -> config.getName().equalsIgnoreCase(configName));
    }

    private static void applyModuleSettings(String moduleName, JsonElement moduleJson) {
        if (Config.mc.world == null || "description".equals(moduleName)) {
            return;
        }
        Module module = ModuleManager.INSTANCE.getModuleByName(moduleName);
        if (module == null && !"description".equals(moduleName)) {
            ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rModule not found: " + moduleName);
            return;
        }
        try {
            JsonObject moduleJsonObj = moduleJson.getAsJsonObject();
            module.setEnabled(moduleJsonObj.get("enabled").getAsBoolean());
            module.setKey(moduleJsonObj.get("key").getAsInt());
            module.getSettings().forEach(setting -> {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting)setting;
                    booleanSetting.setEnabled(moduleJsonObj.get(booleanSetting.getName()).getAsBoolean());
                } else if (setting instanceof NumberSetting) {
                    NumberSetting numberSetting = (NumberSetting)setting;
                    numberSetting.setValue(moduleJsonObj.get(numberSetting.getName()).getAsDouble());
                } else if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)setting;
                    modeSetting.setMode(moduleJsonObj.get(modeSetting.getName()).getAsString());
                } else if (setting instanceof KeybindSetting) {
                    KeybindSetting keybindSetting = (KeybindSetting)setting;
                    keybindSetting.setKey(moduleJsonObj.get(keybindSetting.getName()).getAsInt());
                } else if (setting instanceof TextSetting) {
                    TextSetting textSetting = (TextSetting)setting;
                    textSetting.setValue(moduleJsonObj.get(textSetting.getName()).getAsString());
                } else if (setting instanceof ColorPickerSetting) {
                    ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                    String hexColor = moduleJsonObj.get(colorPickerSetting.getName()).getAsString();
                    Color color = Color.decode(hexColor);
                    colorPickerSetting.setColor(color);
                } else if (setting instanceof NumberSetting2) {
                    NumberSetting2 numberSetting2 = (NumberSetting2)setting;
                    JsonObject numberSetting2Json = moduleJson.getAsJsonObject();
                    if (numberSetting2Json != null) {
                        double minValue = numberSetting2Json.get("minValue").getAsDouble();
                        double maxValue = numberSetting2Json.get("maxValue").getAsDouble();
                        numberSetting2.setMinValue(minValue);
                        numberSetting2.setMaxValue(maxValue);
                    }
                }
            });
        }
        catch (Exception e) {
            ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rError applying settings for module: " + moduleName + " - " + e.getMessage());
        }
    }
}
