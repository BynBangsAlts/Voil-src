package net.fabricmc.fabric.api.scripting;

import net.fabricmc.fabric.api.scripting.ScriptAPI;
import net.fabricmc.fabric.api.scripting.ScriptEngine;
import net.fabricmc.fabric.api.scripting.ScriptEventManager;

public class ScriptParser {
    public static void parse(String script, String scriptName) {
        ScriptEngine engine = ScriptAPI.getScriptEngine();
        ScriptEventManager eventManager = ScriptAPI.getScriptHooks();
        ScriptAPI.getInstance().registerScript(scriptName, script);
        String[] lines = script.split("\\n");
        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#")) {
                ++i;
                continue;
            }
            if (line.startsWith("def")) {
                int startName = "def".length();
                int braceIndex = line.indexOf("{");
                if (braceIndex == -1) {
                    System.err.println("Syntax error in function definition: " + line);
                    ++i;
                    continue;
                }
                String functionName = line.substring(startName, braceIndex).trim();
                StringBuilder blockBuilder = new StringBuilder();
                ++i;
                int braceCount = 1;
                while (i < lines.length && braceCount > 0) {
                    String current = lines[i];
                    if (current.contains("{")) {
                        ++braceCount;
                    }
                    if (current.contains("}")) {
                        --braceCount;
                    }
                    if (braceCount > 0) {
                        blockBuilder.append(current).append("\n");
                    }
                    ++i;
                }
                String block = blockBuilder.toString();
                engine.registerUserFunction(functionName, block);
                continue;
            }
            if (line.startsWith("on")) {
                int braceIndex = line.indexOf("{");
                if (braceIndex == -1) {
                    System.err.println("Syntax error in event definition: " + line);
                    ++i;
                    continue;
                }
                String eventName = line.substring(0, braceIndex).trim();
                StringBuilder blockBuilder = new StringBuilder();
                ++i;
                int braceCount = 1;
                while (i < lines.length && braceCount > 0) {
                    String current = lines[i];
                    if (current.contains("{")) {
                        ++braceCount;
                    }
                    if (current.contains("}")) {
                        --braceCount;
                    }
                    if (braceCount > 0) {
                        blockBuilder.append(current).append("\n");
                    }
                    ++i;
                }
                String block = blockBuilder.toString();
                eventManager.registerEvent(eventName, block);
                continue;
            }
            if (ScriptAPI.getInstance().isScriptEnabled(scriptName)) {
                engine.executeLine(line);
            }
            ++i;
        }
    }
}
