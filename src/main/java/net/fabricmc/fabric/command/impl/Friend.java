package net.fabricmc.fabric.command.impl;

import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Friend
extends Command {
    public Friend() {
        super("friend", "add/remove", "fr");
    }

    @Override
    public void onCmd(String message, String[] args) {
        if (args.length < 2) {
            ChatUtils.addChatMessage("Usage: .friend add/remove");
            return;
        }
        String action = args[0].toLowerCase();
        PlayerEntity targetPlayer = this.getTargetedPlayer();
        if (targetPlayer == null) {
            ChatUtils.addChatMessage("No player targeted.");
            return;
        }
        String playerName = String.valueOf(targetPlayer.getName());
        switch (action) {
            case "add": {
                SocialManager.addFriend(targetPlayer.getUuid());
                ChatUtils.addChatMessage(playerName + " has been added to your friends list.");
                break;
            }
            case "remove": {
                SocialManager.removeFriend(targetPlayer.getUuid());
                ChatUtils.addChatMessage(playerName + " has been removed from your friends list.");
                break;
            }
            default: {
                ChatUtils.addChatMessage("Invalid. Use 'add' or 'remove'.");
            }
        }
    }

    private PlayerEntity getTargetedPlayer() {
        HitResult hit = Friend.mc.crosshairTarget;
        if (hit instanceof EntityHitResult ehr) {
            if (ehr.getEntity() instanceof PlayerEntity player) {
                return player;
            }
        }
        return null;
    }
}
