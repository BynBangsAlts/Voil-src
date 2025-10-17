package net.fabricmc.fabric.api.scripting;

import java.util.List;
import net.fabricmc.fabric.api.scripting.ScriptEngine;

interface ScriptCallable {
    public Object call(List<Object> var1, ScriptEngine var2);
}
