package net.fabricmc.fabric.handler.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Iterator;
import net.minecraft.client.MinecraftClient;

public class BypassHandler {
    private static final Path path = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "config", "modmenu.json");
    private static FileTime lastModified;

    public static void hide(String modId) {
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        try {
            lastModified = Files.getLastModifiedTime(path, new LinkOption[0]);
            JsonObject config = BypassHandler.readConfig();
            JsonArray hiddenMods = config.has("hidden_mods") ? config.getAsJsonArray("hidden_mods") : new JsonArray();
            hiddenMods.add(modId);
            config.add("hidden_mods", (JsonElement)hiddenMods);
            BypassHandler.writeConfig(config);
            Files.setLastModifiedTime(path, lastModified);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove(String modId) {
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        try {
            lastModified = Files.getLastModifiedTime(path, new LinkOption[0]);
            JsonObject config = BypassHandler.readConfig();
            JsonArray hiddenMods = config.has("hidden_mods") ? config.getAsJsonArray("hidden_mods") : new JsonArray();
            Iterator iterator = hiddenMods.iterator();
            while (iterator.hasNext()) {
                JsonElement element = (JsonElement)iterator;
                if (!element.getAsString().equals(modId)) continue;
                iterator.remove();
            }
            config.add("hidden_mods", (JsonElement)hiddenMods);
            BypassHandler.writeConfig(config);
            Files.setLastModifiedTime(path, lastModified);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject readConfig() throws IOException {
        FileReader reader = new FileReader(path.toFile());
        JsonParser parser = new JsonParser();
        return parser.parse((Reader)reader).getAsJsonObject();
    }

    private static void writeConfig(JsonObject config) throws IOException {
        FileWriter writer = new FileWriter(path.toFile());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson((JsonElement)config, (Appendable)writer);
        writer.close();
    }
}
