package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.player.NoSlow;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class AutoWater
extends Module {
    private BooleanSetting waterInWeb = new BooleanSetting("W".concat("$").concat("keyCodec").concat("!").concat("t").concat("@").concat("e").concat("&").concat("r").concat("+").concat(" ").concat("#").concat("i").concat("_").concat("n").concat("@").concat("s").concat("!").concat("i").concat("#").concat("d").concat("_").concat("e").concat("@").concat(" ").concat("+").concat("w").concat("@").concat("e").concat("^").concat("elementCodec"), false, "A".concat("+").concat("u").concat("+").concat("t").concat(")").concat("o").concat("&").concat("m").concat("#").concat("keyCodec").concat("&").concat("t").concat("(").concat("i").concat("$").concat("c").concat("&").concat("keyCodec").concat(")").concat("l").concat("-").concat("l").concat("!").concat("y").concat("@").concat(" ").concat("+").concat("p").concat("(").concat("l").concat("#").concat("keyCodec").concat(")").concat("c").concat("!").concat("e").concat("$").concat("s").concat("^").concat(" ").concat("_").concat("w").concat("&").concat("keyCodec").concat("#").concat("t").concat("@").concat("e").concat("@").concat("r").concat("_").concat(" ").concat("^").concat("i").concat("-").concat("n").concat("-").concat("s").concat("_").concat("i").concat("$").concat("d").concat("@").concat("e").concat("^").concat(" ").concat("(").concat("w").concat("@").concat("e").concat("^").concat("elementCodec").concat("@").concat("s"));
    private int tickTimer = 0;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public AutoWater() {
        super("A".concat(")").concat("u").concat("&").concat("t").concat("&").concat("o").concat(")").concat("W").concat("^").concat("keyCodec").concat("*").concat("t").concat(")").concat("e").concat("$").concat("r"), "A".concat("-").concat("u").concat("+").concat("t").concat("_").concat("o").concat("*").concat("m").concat("*").concat("keyCodec").concat("(").concat("t").concat("(").concat("i").concat("(").concat("c").concat("!").concat("keyCodec").concat(")").concat("l").concat("^").concat("l").concat(")").concat("y").concat("&").concat(" ").concat(")").concat("p").concat(")").concat("l").concat("(").concat("keyCodec").concat("(").concat("c").concat("@").concat("e").concat("$").concat("s").concat("-").concat(" ").concat("^").concat("w").concat("*").concat("keyCodec").concat("$").concat("t").concat("&").concat("e").concat("+").concat("r").concat("$").concat(" ").concat("-").concat("w").concat("+").concat("h").concat("_").concat("e").concat("&").concat("n").concat("$").concat(" ").concat("^").concat("y").concat("-").concat("o").concat("^").concat("u").concat("!").concat("r").concat("_").concat(" ").concat("!").concat("o").concat("+").concat("n").concat("!").concat(" ").concat("(").concat("f").concat("-").concat("i").concat("^").concat("r").concat("$").concat("e"), Category.Combat);
        this.addSettings(this.waterInWeb);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onTick() {
        boolean shouldPlaceWater;
        if (!this.isEnabled()) {
            return;
        }
        if (this.mc.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return;
        }
        boolean bl = shouldPlaceWater = this.mc.player.isOnFire() || this.waterInWeb.isEnabled() && NoSlow.doesBoxTouchBlock(this.mc.player.getBoundingBox(), Blocks.COBWEB);
        if (shouldPlaceWater) {
            ++this.tickTimer;
            try {
                this.mc.player.setPitch(90.0f);
                if (!InventoryUtils.swap(Items.WATER_BUCKET)) return;
                this.mc.getNetworkHandler().sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, this.mc.player.getYaw(), this.mc.player.getPitch()));
                return;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.tickTimer = 0;
        }
    }
}
