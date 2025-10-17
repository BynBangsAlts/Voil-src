package net.fabricmc.fabric.command;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.command.impl.Config;
import net.fabricmc.fabric.command.impl.Friend;
import net.fabricmc.fabric.command.impl.Hclip;
import net.fabricmc.fabric.command.impl.Vclip;

public class CommandManager {
    public static CommandManager INSTANCE = new CommandManager();
    private final List<Command> cmds = new ArrayList<Command>();

    public CommandManager() {
        this.init();
    }

    private void init() {
        this.add(new Vclip());
        this.add(new Hclip());
        this.add(new Config());
        this.add(new Friend());
    }

    public void add(Command command) {
        if (!this.cmds.contains(command)) {
            this.cmds.add(command);
        }
    }

    public void remove(Command command) {
        this.cmds.remove(command);
    }

    public List<Command> getCmds() {
        return this.cmds;
    }
}
