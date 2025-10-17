package net.fabricmc.fabric.gui.theme;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ThemeWrapper {
    private String name;
    private JsonObject theme;

    public ThemeWrapper(String name, JsonObject theme) {
        this.name = name;
        this.theme = theme;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getTheme() {
        return this.theme;
    }

    public void setTheme(JsonObject config) {
        this.theme = config;
    }

    public static ThemeWrapper fromJson(String json) {
        return (ThemeWrapper)new Gson().fromJson(json, ThemeWrapper.class);
    }

    public String toJson() {
        return new Gson().toJson((Object)this);
    }
}
