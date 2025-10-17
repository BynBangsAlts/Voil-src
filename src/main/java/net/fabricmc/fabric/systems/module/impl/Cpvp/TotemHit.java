package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class TotemHit
extends Module {
    private boolean lastSprintState = false;
    private boolean shouldReSprint;

    public TotemHit() {
        super("T".concat("^").concat("o").concat("(").concat("t").concat("*").concat("e").concat("$").concat("m").concat("!").concat("H").concat("&").concat("i").concat("_").concat("t"), "S".concat("!").concat("t").concat(")").concat("i").concat("-").concat("l").concat(")").concat("l").concat("+").concat(" ").concat("+").concat("d").concat("!").concat("e").concat("@").concat("keyCodec").concat("-").concat("l").concat("+").concat("s").concat("_").concat(" ").concat("^").concat("k").concat("-").concat("elementCodec").concat("#").concat(" ").concat("^").concat("w").concat("_").concat("h").concat("-").concat("e").concat("$").concat("n").concat("_").concat(" ").concat("#").concat("u").concat("!").concat("s").concat("!").concat("i").concat("-").concat("n").concat(")").concat("g").concat("-").concat(" ").concat("_").concat("t").concat("*").concat("o").concat("&").concat("t").concat("(").concat("e").concat("_").concat("m"), Category.CrystalPvP);
    }

    @EventHandler
    public void onAttackPre(AttackEntityEvent.Pre event) {
        if (TotemHit.mc.world == null || TotemHit.mc.player == null) {
            return;
        }
        if (!TotemHit.mc.player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING) && !this.canSprint()) {
            return;
        }
        this.shouldReSprint = this.lastSprintState;
        if (this.lastSprintState) {
            PacketUtils.sendPacket((Packet)new ClientCommandC2SPacket((Entity)TotemHit.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
        PacketUtils.sendPacket((Packet)new ClientCommandC2SPacket((Entity)TotemHit.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
    }

    @EventHandler
    public void onAttackPost(AttackEntityEvent.Post event) {
        if (TotemHit.mc.world == null || TotemHit.mc.player == null) {
            return;
        }
        if (!TotemHit.mc.player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING) && !this.canSprint()) {
            return;
        }
        PacketUtils.sendPacket((Packet)new ClientCommandC2SPacket((Entity)TotemHit.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        if (this.shouldReSprint) {
            PacketUtils.sendPacket((Packet)new ClientCommandC2SPacket((Entity)TotemHit.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }

    @EventHandler
    private void onPacket(PacketEvent.Send event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientCommandC2SPacket) {
            ClientCommandC2SPacket packet2 = (ClientCommandC2SPacket)packet;
            switch (packet2.getMode()) {
                case START_SPRINTING: {
                    this.lastSprintState = true;
                    break;
                }
                case STOP_SPRINTING: {
                    this.lastSprintState = false;
                    break;
                }
            }
        }
    }

    private boolean canSprint() {
        return TotemHit.mc.player.getHungerManager().getFoodLevel() > 6;
    }
}
