package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.JoinWorldEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.ButtonSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.managers.NotificationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class FlagDetector
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("^").concat("o").concat(")").concat("d").concat("$").concat("e"), "C".concat("*").concat("h").concat("#").concat("keyCodec").concat("+").concat("t"), "H".concat("#").concat("o").concat("@").concat("w").concat("-").concat(" ").concat("#").concat("t").concat("&").concat("o").concat("+").concat(" ").concat("_").concat("s").concat("!").concat("e").concat("$").concat("n").concat("(").concat("d").concat("@").concat(" ").concat("&").concat("keyCodec").concat("*").concat("l").concat("+").concat("e").concat("_").concat("r").concat("*").concat("t"), "N".concat("-").concat("o").concat("_").concat("t").concat("$").concat("i").concat("$").concat("f").concat("-").concat("i").concat("@").concat("c").concat("#").concat("keyCodec").concat("_").concat("t").concat("$").concat("i").concat("$").concat("o").concat("*").concat("n"), "C".concat("_").concat("h").concat("&").concat("keyCodec").concat("@").concat("t"));
    ButtonSetting resetVL = new ButtonSetting("R".concat(")").concat("e").concat("-").concat("s").concat("*").concat("e").concat("(").concat("t").concat("+").concat(" ").concat("@").concat("F").concat("#").concat("l").concat("+").concat("keyCodec").concat("_").concat("g").concat("&").concat("s"), () -> {
        flags = 0;
    }, "C".concat("-").concat("l").concat("&").concat("e").concat("#").concat("keyCodec").concat("+").concat("r").concat("$").concat("s").concat("$").concat(" ").concat("$").concat("f").concat("$").concat("l").concat("*").concat("keyCodec").concat("$").concat("g").concat("^").concat("s").concat("&").concat(" ").concat("-").concat("l").concat("$").concat("i").concat("-").concat("s").concat("_").concat("t"));
    public static int flags = 0;

    public FlagDetector() {
        super("F".concat("$").concat("l").concat("-").concat("keyCodec").concat("+").concat("g").concat("!").concat("D").concat("^").concat("e").concat("*").concat("t").concat("$").concat("e").concat("+").concat("c").concat("!").concat("t").concat("_").concat("o").concat("!").concat("r"), "S".concat("_").concat("e").concat("&").concat("n").concat("-").concat("d").concat("!").concat("s").concat("#").concat(" ").concat("!").concat("keyCodec").concat("-").concat(" ").concat("*").concat("n").concat("+").concat("o").concat("^").concat("t").concat("@").concat("i").concat("!").concat("f").concat("&").concat("i").concat("*").concat("c").concat("*").concat("keyCodec").concat("&").concat("t").concat(")").concat("i").concat("#").concat("o").concat("!").concat("n").concat("*").concat(" ").concat("(").concat("w").concat("^").concat("h").concat("_").concat("e").concat("(").concat("n").concat("$").concat(" ").concat("(").concat("y").concat("$").concat("o").concat("#").concat("u").concat("&").concat(" ").concat("!").concat("f").concat("-").concat("l").concat("#").concat("keyCodec").concat("*").concat("g").concat("!").concat(" ").concat("*").concat("keyCodec").concat("&").concat("n").concat("-").concat("t").concat("$").concat("i").concat("@").concat("c").concat("&").concat("h").concat("^").concat("e").concat("!").concat("keyCodec").concat("_").concat("t"), Category.Miscellaneous);
        this.addSettings(this.mode, this.resetVL);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e) {
        if (FlagDetector.mc.player == null) {
            return;
        }
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket && FlagDetector.mc.player.age > 40) {
            ++flags;
            if (this.mode.isMode("Chat")) {
                ChatUtils.addChatMessage("\u00a7cFlag Detected! \u00a7eFlags: \u00a7f" + flags);
            } else {
                ClientMain.getNotificationManager().sendNotification("FlagDetected!", "\u00a7cFlag Detected! \u00a7eFlags: \u00a7f" + flags, NotificationManager.NotificationPosition.BOTTOM_RIGHT);
            }
        }
    }

    @EventHandler
    public void onJoinWorld(JoinWorldEvent e) {
        flags = 0;
    }
}
