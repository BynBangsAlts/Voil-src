package net.fabricmc.fabric.systems.module.impl.misc;

import java.util.UUID;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MiddleClickFriend
extends Module {
    private final BooleanSetting render = new BooleanSetting("R".concat("_").concat("e").concat("&").concat("n").concat("-").concat("d").concat("_").concat("e").concat("(").concat("r"), true, "S".concat("+").concat("h").concat("+").concat("o").concat("_").concat("w").concat(")").concat("s").concat("^").concat(" ").concat("+").concat("m").concat("!").concat("e").concat(")").concat("s").concat("(").concat("s").concat("@").concat("keyCodec").concat("$").concat("g").concat("&").concat("e").concat("!").concat("s").concat("*").concat(" ").concat("^").concat("w").concat("&").concat("h").concat("$").concat("e").concat("+").concat("n").concat("*").concat(" ").concat("*").concat("f").concat("*").concat("r").concat("&").concat("i").concat("-").concat("e").concat("*").concat("n").concat("^").concat("d").concat("+").concat("s").concat("&").concat(" ").concat("(").concat("keyCodec").concat("*").concat("r").concat("*").concat("e").concat(")").concat(" ").concat("!").concat("keyCodec").concat("@").concat("d").concat("@").concat("d").concat("_").concat("e").concat("-").concat("d").concat("_").concat(" ").concat("(").concat("o").concat("@").concat("r").concat("+").concat(" ").concat("_").concat("r").concat("$").concat("e").concat("-").concat("m").concat("*").concat("o").concat("#").concat("v").concat("@").concat("e").concat("*").concat("d"));
    public static final String PREFIX = "\u00a7d\u00a7l[Voil] \u00a7r";
    private int clickTickCounter = 0;

    public MiddleClickFriend() {
        super("M".concat("*").concat("i").concat("!").concat("d").concat("&").concat("d").concat(")").concat("l").concat("^").concat("e").concat("-").concat("C").concat("_").concat("l").concat("!").concat("i").concat("^").concat("c").concat("*").concat("k").concat("$").concat("F").concat("*").concat("r").concat("(").concat("i").concat("#").concat("e").concat("+").concat("n").concat("^").concat("d"), "A".concat(")").concat("d").concat("*").concat("d").concat("@").concat("s").concat("^").concat(" ").concat("&").concat("f").concat("#").concat("r").concat("$").concat("i").concat("^").concat("e").concat("$").concat("n").concat("&").concat("d").concat(")").concat("s").concat("!").concat(" ").concat("#").concat("w").concat("#").concat("i").concat("+").concat("t").concat("^").concat("h").concat("_").concat(" ").concat("-").concat("m").concat("#").concat("i").concat("#").concat("d").concat("-").concat("d").concat("^").concat("l").concat("_").concat("e").concat("&").concat(" ").concat("#").concat("c").concat("#").concat("l").concat("&").concat("i").concat("_").concat("c").concat("^").concat("k"), Category.Miscellaneous);
        this.addSettings(this.render);
    }

    @Override
    public void onTick() {
        EntityHitResult entityHit;
        Entity entity;
        HitResult hitResult;
        if (MiddleClickFriend.mc.currentScreen != null) {
            return;
        }
        if (KeyUtils.isKeyPressed(2) && (hitResult = MiddleClickFriend.mc.crosshairTarget) instanceof EntityHitResult && (entity = (entityHit = (EntityHitResult)hitResult).getEntity()) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            this.toggleFriend(player);
        }
    }

    private void toggleFriend(PlayerEntity player) {
        ++this.clickTickCounter;
        if (this.clickTickCounter < 5) {
            return;
        }
        this.clickTickCounter = 0;
        UUID playerUuid = player.getUuid();
        if (SocialManager.isFriend(playerUuid)) {
            SocialManager.removeFriend(playerUuid);
            if (this.render.isEnabled()) {
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rYou are no longer friends with " + player.getName().getString());
            }
        } else {
            SocialManager.addFriend(playerUuid);
            if (this.render.isEnabled()) {
                ChatUtils.addChatMessage("\u00a7d\u00a7l[Voil] \u00a7rYou are now friends with " + player.getName().getString());
            }
        }
    }
}
