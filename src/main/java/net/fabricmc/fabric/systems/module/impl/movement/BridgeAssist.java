package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

public class BridgeAssist
extends Module {
    BooleanSetting autoPlace = new BooleanSetting("A".concat("@").concat("u").concat("*").concat("t").concat("^").concat("o").concat("+").concat(" ").concat("@").concat("P").concat("&").concat("l").concat("+").concat("keyCodec").concat("-").concat("c").concat("(").concat("e"), false, "A".concat("@").concat("u").concat("!").concat("t").concat(")").concat("o").concat("_").concat(" ").concat("(").concat("p").concat(")").concat("l").concat("&").concat("keyCodec").concat("*").concat("c").concat("$").concat("e").concat("#").concat(" ").concat("#").concat("elementCodec").concat("@").concat("l").concat("-").concat("o").concat("#").concat("c").concat("-").concat("k").concat(")").concat("s"));
    public boolean bridging;

    public BridgeAssist() {
        super("B".concat("-").concat("r").concat("-").concat("i").concat("-").concat("d").concat("@").concat("g").concat("!").concat("e").concat(")").concat("A").concat("-").concat("s").concat("*").concat("s").concat("!").concat("i").concat("-").concat("s").concat(")").concat("t"), "A".concat("!").concat("u").concat("+").concat("t").concat("$").concat("o").concat("(").concat("m").concat("(").concat("keyCodec").concat(")").concat("t").concat("_").concat("i").concat("$").concat("c").concat("&").concat("keyCodec").concat("&").concat("l").concat("+").concat("l").concat("&").concat("y").concat("(").concat(" ").concat("#").concat("elementCodec").concat("(").concat("r").concat("(").concat("i").concat("@").concat("d").concat("^").concat("g").concat("$").concat("e").concat("@").concat("s"), Category.Movement);
        this.addSettings(this.autoPlace);
    }

    @Override
    public void onTick() {
        if (BridgeAssist.mc.player.getMainHandStack().getItem() instanceof BlockItem || BridgeAssist.mc.player.getOffHandStack().getItem() instanceof BlockItem) {
            BlockPos blockPos;
            ClientWorld clientWorld;
            if (BridgeAssist.mc.player.getPitch() < 70.0f && this.bridging) {
                BridgeAssist.mc.options.sneakKey.setPressed(false);
                this.bridging = false;
            }
            if ((clientWorld = BridgeAssist.mc.world).getBlockState(blockPos = BlockPos.ofFloored((Position)BridgeAssist.mc.player.getPos()).down()).isReplaceable() && clientWorld.getBlockState(blockPos.down()).isReplaceable() && clientWorld.getBlockState(blockPos.down().down()).isReplaceable()) {
                BridgeAssist.mc.options.sneakKey.setPressed(true);
                this.bridging = true;
                if (this.autoPlace.isEnabled()) {
                    // empty if block
                }
            } else if (this.bridging) {
                BridgeAssist.mc.options.sneakKey.setPressed(false);
                this.bridging = false;
            }
        }
    }
}
