package net.fabricmc.fabric.command.impl;

import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.client.network.ClientPlayerEntity;

public class Hclip
extends Command {
    public Hclip() {
        super("Hclip", "clip horizontally", "hclip");
    }

    @Override
    public void onCmd(String message, String[] args) {
        if (args.length < 1) {
            ChatUtils.addChatMessage("Not enough arguments.");
            return;
        }
        int horiz = Integer.parseInt(args[1]);
        ClientPlayerEntity player = Hclip.mc.player;
        assert (player != null);
        double yaw = Math.toRadians(player.getYaw());
        double forwardX = -Math.sin(yaw) * (double)horiz;
        double forwardZ = Math.cos(yaw) * (double)horiz;
        player.updatePosition(player.getX() + forwardX, player.getY(), player.getZ() + forwardZ);
    }
}
