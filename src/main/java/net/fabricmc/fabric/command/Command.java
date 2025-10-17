package net.fabricmc.fabric.command;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public abstract class Command {
    public static MinecraftClient mc = MinecraftClient.getInstance();
    private String name;
    private String description;
    private List<String> aliases;

    public Command(String name, String description, String ... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void onCmd(String var1, String[] var2);

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}
