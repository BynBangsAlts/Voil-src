package net.fabricmc.fabric.security;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.scripting.ScriptWrapper;
import net.fabricmc.fabric.gui.theme.ThemeWrapper;
import net.fabricmc.fabric.systems.config.ConfigWrapper;

public class UserConstants {
    public static final UserConstant USERID = new UserConstant("Id", "123123");
    public static final UserConstant USERNAME = new UserConstant("Username", "BynBang");
    public static final UserConstant PLAN = new UserConstant("Plan", "voil+");
    public static final UserConstant GUIKEY = new UserConstant("Key", 344); // Right Shift
    public static final Map<String, List<String>> ircMessages = new HashMap<String, List<String>>();
    public static final Map<String, ConfigWrapper> configs = new ConcurrentHashMap<String, ConfigWrapper>();
    public static final Map<String, ScriptWrapper> scripts = new ConcurrentHashMap<String, ScriptWrapper>();
    public static final Map<String, JsonObject> settings = new ConcurrentHashMap<String, JsonObject>();
    public static final List<ThemeWrapper> themes = new ArrayList<ThemeWrapper>();
    public static final UserConstant searchBar = new UserConstant("Search Bar", true);
    public static final UserConstant glow = new UserConstant("Glow", true);

    public static class UserConstant {
        private final String key;
        private Object value;

        public UserConstant(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
