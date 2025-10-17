package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class OptimalEating
extends Module {
    private final NumberSetting health = new NumberSetting("H".concat("^").concat("e").concat("&").concat("keyCodec").concat("^").concat("l").concat("@").concat("t").concat("+").concat("h"), 1.0, 20.0, 10.0, 1.0, "M".concat("*").concat("i").concat("(").concat("n").concat("!").concat("i").concat("_").concat("m").concat("^").concat("u").concat("#").concat("m").concat("!").concat(" ").concat("(").concat("h").concat("_").concat("e").concat("&").concat("keyCodec").concat("+").concat("l").concat("$").concat("t").concat(")").concat("h").concat("@").concat(" ").concat("$").concat("t").concat("(").concat("o").concat("#").concat(" ").concat("+").concat("f").concat("!").concat("keyCodec").concat("!").concat("s").concat("@").concat("t").concat("_").concat(" ").concat("#").concat("e").concat("&").concat("keyCodec").concat("$").concat("t"));
    private final BooleanSetting workonhealth = new BooleanSetting("W".concat("$").concat("o").concat("+").concat("r").concat("$").concat("k").concat("_").concat(" ").concat("_").concat("o").concat(")").concat("n").concat("^").concat(" ").concat("_").concat("H").concat("$").concat("e").concat("&").concat("keyCodec").concat("&").concat("l").concat("&").concat("t").concat("&").concat("h"), true, "O".concat("_").concat("n").concat("^").concat("l").concat("*").concat("y").concat("(").concat(" ").concat("*").concat("f").concat("-").concat("keyCodec").concat(")").concat("s").concat("*").concat("t").concat("*").concat(" ").concat("_").concat("e").concat("@").concat("keyCodec").concat("_").concat("t").concat("*").concat(" ").concat(")").concat("w").concat("-").concat("h").concat("$").concat("e").concat("-").concat("n").concat("(").concat(" ").concat("+").concat("h").concat("&").concat("e").concat("(").concat("keyCodec").concat("(").concat("l").concat("-").concat("t").concat(")").concat("h").concat("_").concat(" ").concat(")").concat("i").concat("-").concat("s").concat("$").concat(" ").concat(")").concat("i").concat("+").concat("n").concat("&").concat(" ").concat("^").concat("keyCodec").concat("*").concat(" ").concat("@").concat("c").concat("@").concat("e").concat("-").concat("r").concat(")").concat("t").concat("&").concat("keyCodec").concat("(").concat("i").concat("$").concat("n").concat("_").concat(" ").concat(")").concat("r").concat(")").concat("keyCodec").concat(")").concat("n").concat("$").concat("g").concat("#").concat("e"));
    private final NumberSetting speed = new NumberSetting("S".concat("^").concat("p").concat("(").concat("e").concat("@").concat("e").concat("#").concat("d"), 0.0, 3.0, 1.0, 1.0, "S".concat("!").concat("p").concat("$").concat("e").concat("+").concat("e").concat("@").concat("d").concat(")").concat(" ").concat("_").concat("t").concat("!").concat("o").concat("^").concat(" ").concat("^").concat("e").concat("-").concat("keyCodec").concat("^").concat("t"));

    public OptimalEating() {
        super("F".concat("(").concat("keyCodec").concat("(").concat("s").concat("-").concat("t").concat("@").concat("E").concat("#").concat("keyCodec").concat("(").concat("t"), "L".concat("^").concat("e").concat("#").concat("t").concat("_").concat("s").concat("$").concat(" ").concat("#").concat("y").concat("*").concat("o").concat("+").concat("u").concat("-").concat(" ").concat("$").concat("e").concat("@").concat("keyCodec").concat("#").concat("t").concat("-").concat(" ").concat("&").concat("f").concat("@").concat("keyCodec").concat(")").concat("s").concat("#").concat("t").concat("!").concat("e").concat("$").concat("r"), Category.Combat);
        this.addSettings(this.health, this.workonhealth, this.speed);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || OptimalEating.mc.player == null) {
            return;
        }
        ClientPlayerEntity player = OptimalEating.mc.player;
        if (player.isUsingItem()) {
            ItemStack itemStack = player.getActiveItem();
            boolean shouldFastEat = true;
            if (this.workonhealth.isEnabled()) {
                boolean bl = shouldFastEat = (double)player.getHealth() <= this.health.getValue();
            }
            if (shouldFastEat) {
                int multiplier = this.speed.getIValue();
                for (int i = 0; i < multiplier; ++i) {
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, OptimalEating.mc.player.getYaw(), OptimalEating.mc.player.getPitch()));
                }
                OptimalEating.mc.gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.MAIN_HAND);
            }
        }
    }
}
