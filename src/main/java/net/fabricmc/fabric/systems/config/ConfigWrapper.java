package net.fabricmc.fabric.systems.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Color;

public class ConfigWrapper {
    private String user;
    private String name;
    private String description;
    private String id;
    private String plan;
    private JsonObject config;
    final int VOIL_PLUS_COLOR = -10496;
    final int VOIL_COLOR = -4144960;
    final int VOIL_MONTHLY_COLOR = -3309774;
    final int OWNER_COLOR = -65536;
    final int BETA_TESTER_COLOR = -8388480;

    public ConfigWrapper(String user, String name, String description, String id, String plan, JsonObject config) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.id = id;
        this.plan = plan;
        this.config = config;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlan() {
        return this.plan;
    }

    public Color getColor() {
        switch (this.plan) {
            case "owner": {
                return new Color(-65536);
            }
            case "voil": {
                return new Color(-4144960);
            }
            case "voil+": {
                return new Color(-10496);
            }
            case "staff": {
                return new Color(-3309774);
            }
            case "beta": {
                return new Color(-8388480);
            }
        }
        return Color.WHITE;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(JsonObject config) {
        this.config = config;
    }

    public static ConfigWrapper fromJson(String json) {
        return (ConfigWrapper)new Gson().fromJson(json, ConfigWrapper.class);
    }

    public String toJson() {
        return new Gson().toJson((Object)this);
    }
}
