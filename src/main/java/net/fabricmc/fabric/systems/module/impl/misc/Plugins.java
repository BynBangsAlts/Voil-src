package net.fabricmc.fabric.systems.module.impl.misc;

import com.mojang.brigadier.suggestion.Suggestion;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.CommandSuggest;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

public class Plugins
extends Module {
    public Plugins() {
        super("P".concat(")").concat("l").concat(")").concat("u").concat("$").concat("g").concat("(").concat("i").concat("*").concat("n").concat("*").concat("s"), "G".concat("&").concat("e").concat("-").concat("t").concat("!").concat("s").concat("^").concat(" ").concat(")").concat("t").concat(")").concat("h").concat("*").concat("e").concat("_").concat(" ").concat("&").concat("p").concat("&").concat("l").concat("-").concat("u").concat("-").concat("g").concat("$").concat("i").concat("#").concat("n").concat("-").concat("s"), Category.Miscellaneous);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Plugins.mc.player != null) {
            PacketUtils.sendPacket((Packet)new RequestCommandCompletionsC2SPacket(new Random().nextInt(200), "/"));
        }
    }

    @EventHandler
    public void onCmdSuggest(CommandSuggest event) {
        CommandSuggestionsS2CPacket packet = event.getPacket();
        ArrayList<String> plugins = new ArrayList<String>();
        List<Suggestion> cmdList = packet.getSuggestions().getList();
        for (Suggestion s : cmdList) {
            String pluginName;
            String[] cmd = s.getText().split(":");
            if (cmd.length <= 1 || plugins.contains(pluginName = cmd[0].replace("/", ""))) continue;
            plugins.add(pluginName);
        }
        if (!plugins.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : plugins) {
                if (s.equalsIgnoreCase("itemeditor")) {
                    sb.append("\u00a7c").append(s).append(", \u00a7a");
                    continue;
                }
                if (s.equalsIgnoreCase("bettershulkerboxes")) {
                    sb.append("\u00a7c").append(s).append(", \u00a7a");
                    continue;
                }
                if (s.equalsIgnoreCase("vulcan") || s.equalsIgnoreCase("ncp") || s.equalsIgnoreCase("grim") || s.equalsIgnoreCase("spartan")) {
                    sb.append("\u00a7b").append(s).append(", \u00a7a");
                    continue;
                }
                sb.append(s).append(", ");
            }
            this.sendMsg("&7Plugins: &a" + String.valueOf(sb));
            this.toggle();
        } else {
            this.sendMsg("&cNo plugins found!");
            this.toggle();
        }
        this.toggle();
    }
}
