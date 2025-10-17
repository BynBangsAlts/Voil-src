package net.fabricmc.fabric.utils.player;

import net.fabricmc.fabric.ClientMain;
import net.minecraft.text.Text;

public class ChatUtils {
    private static final String paragraph = "\u00a7";

    public static void addChatMessage(String message) {
        ClientMain.mc.inGameHud.getChatHud().addMessage((Text)Text.literal((String)message));
    }

    public void sendMsg(String text) {
        if (ClientMain.mc.player != null) {
            ClientMain.mc.player.sendMessage(Text.of((String)ChatUtils.translate(text)));
        }
    }

    public void sendMsg(Text text) {
        if (ClientMain.mc.player != null) {
            ClientMain.mc.player.sendMessage(text);
        }
    }

    public static String translate(String text) {
        return text.replace("&", paragraph);
    }

    public static void runCommand(String command) {
        if (ClientMain.mc.world != null && ClientMain.mc.player != null) {
            ClientMain.mc.player.networkHandler.sendCommand(command);
        }
    }

    public static void message(String player, String msg) {
        if (player != null) {
            ClientMain.mc.player.networkHandler.sendCommand("msg " + player + " " + msg);
        }
    }

    public static void pmessage(String msg) {
        if (ClientMain.mc.player != null) {
            ClientMain.mc.player.networkHandler.sendChatMessage(msg);
        }
    }
}
