package net.fabricmc.fabric.systems.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.fabric.systems.config.Config;

public class ConfigLoader {
    private static final List<Config> configs = new ArrayList<Config>();
    private static final Path ROOT_DIR = Paths.get(System.getenv("LOCALAPPDATA"), "Programs", "Common");
    private static final Path CONFIG_DIR = ROOT_DIR.resolve("Configs");
    public static Config lastAddedConfig;
    private static final String PREFIX = "\u00a7d\u00a7l[Voil] \u00a7r";

    public static void loadConfigs() {
        configs.clear();
        try {
            Files.createDirectories(CONFIG_DIR, new FileAttribute[0]);
            try (Stream<Path> paths = Files.walk(CONFIG_DIR, 1, new FileVisitOption[0]);){
                boolean hasFiles = paths.anyMatch(x$0 -> Files.isRegularFile(x$0, new LinkOption[0]));
                if (!hasFiles) {
                    Config defaultConfig = new Config("default", "Default configuration");
                    Config.save();
                    configs.add(defaultConfig);
                    return;
                }
            }
            Files.list(CONFIG_DIR).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).forEach(file -> {
                try {
                    ConfigLoader.loadConfigFromFile(file);
                }
                catch (IOException e) {
                    System.err.println("Error loading config from file: " + String.valueOf(file));
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e) {
            System.err.println("Failed to load configurations");
            e.printStackTrace();
        }
    }

    public static Config getConfigByName(String name) {
        for (Config config : configs) {
            if (!config.getName().equalsIgnoreCase(name)) continue;
            return config;
        }
        return null;
    }

    public static void addConfig(Config config) {
        configs.add(config);
        lastAddedConfig = config;
    }

    public static void loadConfigFromFile(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path);){
            JsonObject json = JsonParser.parseReader((Reader)reader).getAsJsonObject();
            if (json == null) {
                System.err.println("Error parsing config file: " + String.valueOf(path));
                return;
            }
            String description = json.has("description") ? json.get("description").getAsString() : "";
            Config config = new Config(path.getFileName().toString().replace(".json", ""), description);
            configs.add(config);
        }
        catch (Exception e) {
            System.err.println("Error loading config from file: " + String.valueOf(path));
            e.printStackTrace();
        }
    }

    public static Config getDefaultConfig() {
        return configs.stream().filter(config -> "default".equalsIgnoreCase(config.getName())).findFirst().orElseGet(() -> {
            Config defaultConfig = new Config("default", "Default configuration");
            Config.save();
            configs.add(defaultConfig);
            return defaultConfig;
        });
    }

    public static List<Config> getConfigs() {
        return configs;
    }
}
