package net.fabricmc.fabric.api.scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptLoader {
    public static String loadScript(String scriptPath) throws IOException {
        return Files.readString(Paths.get(scriptPath, new String[0]));
    }
}
