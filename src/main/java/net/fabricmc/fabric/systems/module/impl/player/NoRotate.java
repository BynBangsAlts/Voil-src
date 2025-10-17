package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.mixin.IPlayerPositionLookS2CPacket;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class NoRotate
extends Module {
    ModeSetting cancelMode = new ModeSetting("C".concat("#").concat("keyCodec").concat("^").concat("n").concat("&").concat("c").concat("!").concat("e").concat("_").concat("l").concat("(").concat(" ").concat("!").concat("m").concat("#").concat("o").concat(")").concat("d").concat("^").concat("e"), "P".concat("-").concat("keyCodec").concat("-").concat("c").concat("-").concat("k").concat("$").concat("e").concat("#").concat("t"), "M".concat("-").concat("o").concat("_").concat("d").concat(")").concat("e").concat("&").concat(" ").concat("$").concat("h").concat("!").concat("o").concat("+").concat("w").concat(")").concat(" ").concat("@").concat("t").concat(")").concat("o").concat("&").concat(" ").concat("!").concat("c").concat("$").concat("keyCodec").concat("#").concat("n").concat("^").concat("c").concat("(").concat("e").concat("-").concat("l"), "M".concat("$").concat("keyCodec").concat("&").concat("n").concat("#").concat("u").concat("&").concat("keyCodec").concat("-").concat("l"), "P".concat("^").concat("keyCodec").concat("&").concat("c").concat("*").concat("k").concat("-").concat("e").concat("-").concat("t"));
    private float restoreYaw;
    private float restorePitch;
    private boolean shouldRestore;

    public NoRotate() {
        super("N".concat("$").concat("o").concat("^").concat("R").concat("-").concat("o").concat("@").concat("t").concat("$").concat("keyCodec").concat("_").concat("t").concat("(").concat("e"), "P".concat("^").concat("r").concat("@").concat("e").concat("_").concat("v").concat("(").concat("e").concat("+").concat("n").concat("+").concat("t").concat("-").concat("s").concat("#").concat(" ").concat("-").concat("s").concat("&").concat("e").concat("#").concat("r").concat("+").concat("v").concat("$").concat("e").concat("@").concat("r").concat("!").concat(" ").concat("_").concat("f").concat("!").concat("r").concat("-").concat("o").concat("@").concat("m").concat("*").concat(" ").concat("_").concat("r").concat("_").concat("o").concat("$").concat("t").concat("$").concat("keyCodec").concat("!").concat("t").concat("(").concat("i").concat("_").concat("n").concat("^").concat("g").concat(")").concat(" ").concat("-").concat("y").concat("@").concat("o").concat(")").concat("u").concat("^").concat("r").concat("@").concat(" ").concat(")").concat("h").concat("+").concat("e").concat("@").concat("keyCodec").concat("!").concat("d"), Category.Player);
        this.addSettings(this.cancelMode);
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive e) {
        Packet<?> packet = e.getPacket();
        if (packet instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket pac = (PlayerPositionLookS2CPacket)packet;
            if (NoRotate.mc.player == null) {
                return;
            }
            if (this.cancelMode.isMode("Packet")) {
                ((IPlayerPositionLookS2CPacket)pac).setYaw(NoRotate.mc.player.getYaw());
                ((IPlayerPositionLookS2CPacket)pac).setPitch(NoRotate.mc.player.getPitch());
            } else if (this.cancelMode.isMode("Manual")) {
                this.restoreYaw = NoRotate.mc.player.getYaw();
                this.restorePitch = NoRotate.mc.player.getPitch();
                NoRotate.mc.player.setYaw(pac.getYaw());
                NoRotate.mc.player.setPitch(pac.getPitch());
                this.shouldRestore = true;
            }
        }
    }

    @Override
    public void onTick() {
        if (this.shouldRestore) {
            NoRotate.mc.player.setYaw(this.restoreYaw);
            NoRotate.mc.player.setPitch(this.restorePitch);
            this.shouldRestore = false;
        }
    }
}
