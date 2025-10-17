package net.fabricmc.fabric.api.scripting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.scripting.ScriptEngine;
import net.fabricmc.fabric.api.scripting.ScriptEventManager;
import net.minecraft.client.MinecraftClient;

public class ScriptAPI {
    private static MinecraftClient mc;
    private static ScriptEngine scriptEngine;
    private static ScriptEventManager scriptHooks;
    public static ScriptAPI instance;
    private final Map<String, String> scripts = new HashMap<String, String>();
    private final Set<String> enabledScripts = new HashSet<String>();

    public static void init() {
        mc = MinecraftClient.getInstance();
        instance = new ScriptAPI();
        scriptEngine = new ScriptEngine();
        scriptHooks = new ScriptEventManager();
    }

    public static ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public static ScriptEventManager getScriptHooks() {
        return scriptHooks;
    }

    public void registerScript(String name, String content) {
        this.scripts.put(name, content);
        this.enabledScripts.add(name);
    }

    public void toggleScript(String name) {
        if (!this.scripts.containsKey(name)) {
            return;
        }
        if (this.enabledScripts.contains(name)) {
            this.enabledScripts.remove(name);
        } else {
            this.enabledScripts.add(name);
        }
    }

    public boolean isScriptEnabled(String name) {
        return this.enabledScripts.contains(name);
    }

    public static ScriptAPI getInstance() {
        return instance;
    }
}
