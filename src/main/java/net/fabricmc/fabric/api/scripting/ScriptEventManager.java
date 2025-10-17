package net.fabricmc.fabric.api.scripting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.scripting.ScriptCallable;
import net.fabricmc.fabric.api.scripting.ScriptEngine;
import net.fabricmc.fabric.api.scripting.ScriptFunction;

public class ScriptEventManager {
    private Map<String, ScriptCallable> events = new HashMap<String, ScriptCallable>();

    public void registerEvent(String eventName, String block) {
        this.events.put(eventName, new ScriptFunction(eventName, block));
    }

    public void triggerEvent(String eventName, ScriptEngine engine) {
        ScriptCallable function = this.events.get(eventName);
        if (function == null) {
            return;
        }
        function.call(List.of(), engine);
    }
}
