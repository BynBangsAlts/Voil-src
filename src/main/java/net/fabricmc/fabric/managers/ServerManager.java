package net.fabricmc.fabric.managers;

import com.mojang.brigadier.suggestion.Suggestion;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.api.astral.events.JoinWorldEvent;
import net.fabricmc.fabric.managers.NotificationManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

public class ServerManager {
    private static final int samplescount = 100;
    private static final long[] tickTimes = new long[100];
    private static int tickIndex = 0;
    private static double tps = 20.0;
    private static final List<String> plugins = new ArrayList<String>();
    private static String anticheat = "None";

    public void tick() {
        long currentTime;
        ServerManager.tickTimes[ServerManager.tickIndex % 100] = currentTime = System.nanoTime();
        if (++tickIndex < 100) {
            return;
        }
        int samples = Math.min(100, tickIndex);
        double elapsedTime = (double)(tickTimes[tickIndex % 100] - tickTimes[(tickIndex + 1) % 100]) / 1.0E9;
        tps = (double)samples / elapsedTime;
    }

    @EventHandler
    public void onJoinWorld(JoinWorldEvent e) {
        if (ClientMain.mc.world == null || ClientMain.mc.player == null) {
            return;
        }
        ClientMain.mc.player.networkHandler.sendPacket((Packet)new RequestCommandCompletionsC2SPacket(new Random().nextInt(200), "/"));
        ClientMain.getNotificationManager().sendNotification("Server Information", "AntiCheat: " + anticheat + " TPS: " + tps, NotificationManager.NotificationPosition.BOTTOM_RIGHT);
    }

    @EventHandler
    public void onCmdSuggest(CommandSuggestionsS2CPacket packet) {
        String[] knownAnticheats;
        plugins.clear();
        List<Suggestion> cmdList = packet.getSuggestions().getList();
        for (Suggestion s : cmdList) {
            String pluginName;
            String[] cmd = s.getText().split(":");
            if (cmd.length <= 1 || plugins.contains(pluginName = cmd[0].replace("/", "").toLowerCase())) continue;
            plugins.add(pluginName);
        }
        for (String ac : knownAnticheats = new String[]{"vulcan", "grim", "spartan", "ncp", "updatedncp", "aac", "matrix", "verus", "karhu", "minesecure", "negativity", "intave", "reflex", "matrix", "polar", "foxaddition", "lightanticheat", "themis", "wave", "godseye", "anticheataddition", "wraith"}) {
            for (String plugin : plugins) {
                if (!plugin.equalsIgnoreCase(ac)) continue;
                anticheat = ac;
                return;
            }
        }
        anticheat = "None";
    }

    @EventHandler
    public void onUpdate(EventUpdate e) {
        this.tick();
    }

    public String getAnticheat() {
        return anticheat;
    }

    public double getTPS() {
        return Math.min(20.0, tps);
    }
}
