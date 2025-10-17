package net.fabricmc.fabric.api.scripting;

import java.util.List;
import net.fabricmc.fabric.api.scripting.ScriptCallable;
import net.fabricmc.fabric.api.scripting.ScriptEngine;

public class ScriptFunction
implements ScriptCallable {
    private String name;
    private String block;

    public ScriptFunction(String name, String block) {
        this.name = name;
        this.block = block;
    }

    @Override
    public Object call(List<Object> args, ScriptEngine engine) {
        engine.executeBlock(this.block);
        return null;
    }
}
