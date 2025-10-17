package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AirAnchor
extends Module {
    private final NumberSetting chance = new NumberSetting("C".concat("+").concat("h").concat("*").concat("keyCodec").concat("^").concat("n").concat("@").concat("c").concat("(").concat("e"), 10.0, 100.0, 75.0, 1.0, "C".concat("^").concat("h").concat("(").concat("keyCodec").concat("#").concat("n").concat("_").concat("c").concat("^").concat("e").concat("#").concat(" ").concat("_").concat("t").concat("&").concat("o").concat("^").concat(" ").concat("&").concat("keyCodec").concat("^").concat("i").concat("&").concat("r").concat(")").concat(" ").concat("_").concat("p").concat("#").concat("l").concat("(").concat("keyCodec").concat("-").concat("c").concat("@").concat("e").concat("$").concat(" ").concat("&").concat("keyCodec").concat("#").concat("n").concat("@").concat(" ").concat("!").concat("keyCodec").concat("#").concat("n").concat("-").concat("c").concat("#").concat("h").concat("$").concat("o").concat("!").concat("r"));
    private final BooleanSetting holdingkeyonly = new BooleanSetting("H".concat("+").concat("o").concat("(").concat("l").concat("_").concat("d").concat("@").concat("i").concat("@").concat("n").concat("*").concat("g").concat("+").concat(" ").concat("!").concat("k").concat("+").concat("e").concat("_").concat("y").concat("&").concat(" ").concat("_").concat("o").concat(")").concat("n").concat("-").concat("l").concat("(").concat("y"), false, "O".concat("@").concat("n").concat("$").concat("l").concat("(").concat("y").concat("^").concat(" ").concat("^").concat("p").concat(")").concat("l").concat("@").concat("keyCodec").concat("#").concat("c").concat("-").concat("e").concat(")").concat(" ").concat("+").concat("keyCodec").concat("^").concat("n").concat("$").concat(" ").concat("-").concat("keyCodec").concat("@").concat("i").concat("@").concat("r").concat("_").concat(" ").concat("^").concat("keyCodec").concat("^").concat("n").concat("_").concat("c").concat("&").concat("h").concat("*").concat("o").concat("$").concat("r").concat("^").concat(" ").concat("!").concat("w").concat("-").concat("h").concat("$").concat("e").concat("(").concat("n").concat("_").concat(" ").concat("@").concat("h").concat("+").concat("o").concat(")").concat("l").concat("#").concat("d").concat("$").concat("i").concat("!").concat("n").concat("(").concat("g").concat("$").concat(" ").concat("+").concat("t").concat("!").concat("h").concat("#").concat("e").concat("_").concat(" ").concat("(").concat("k").concat("&").concat("e").concat("*").concat("y"));
    private final KeybindSetting holdingkey = new KeybindSetting("H".concat("@").concat("o").concat("(").concat("l").concat("#").concat("d").concat("*").concat("i").concat("$").concat("n").concat("&").concat("g").concat("+").concat(" ").concat("+").concat("k").concat("#").concat("e").concat("*").concat("y"), 0, false, "K".concat("!").concat("e").concat("#").concat("y").concat("!").concat(" ").concat("_").concat("t").concat("*").concat("o").concat("#").concat(" ").concat("#").concat("h").concat("+").concat("o").concat("&").concat("l").concat("*").concat("d").concat("&").concat(" ").concat("*").concat("t").concat("-").concat("o").concat("&").concat(" ").concat("_").concat("p").concat("^").concat("l").concat("^").concat("keyCodec").concat(")").concat("c").concat("-").concat("e").concat(")").concat(" ").concat("-").concat("keyCodec").concat("(").concat("n").concat(")").concat(" ").concat("#").concat("keyCodec").concat("#").concat("i").concat("-").concat("r").concat(")").concat(" ").concat("^").concat("keyCodec").concat("^").concat("n").concat("&").concat("c").concat("&").concat("h").concat("+").concat("o").concat("#").concat("r"));
    private BlockPos bp;
    private int count;

    public AirAnchor() {
        super("A".concat("(").concat("i").concat("#").concat("r").concat("-").concat("A").concat(")").concat("n").concat("$").concat("c").concat(")").concat("h").concat(")").concat("o").concat("-").concat("r"), "A".concat("#").concat("u").concat("*").concat("t").concat("^").concat("o").concat("^").concat("m").concat("@").concat("keyCodec").concat("&").concat("t").concat("#").concat("i").concat("#").concat("c").concat("&").concat("keyCodec").concat("&").concat("l").concat("#").concat("l").concat("#").concat("y").concat("&").concat(" ").concat(")").concat("p").concat("$").concat("l").concat("!").concat("keyCodec").concat(")").concat("c").concat("^").concat("e").concat("*").concat("s").concat("+").concat(" ").concat("_").concat("keyCodec").concat("&").concat("n").concat("&").concat(" ").concat("$").concat("keyCodec").concat(")").concat("i").concat("$").concat("r").concat("*").concat(" ").concat("+").concat("keyCodec").concat("!").concat("n").concat("@").concat("c").concat(")").concat("h").concat("#").concat("o").concat(")").concat("r"), Category.CrystalPvP);
        this.addSettings(this.chance, this.holdingkeyonly, this.holdingkey);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.count = 0;
    }

    @Override
    public void onTick() {
        try {
            if (AirAnchor.mc.currentScreen == null) {
                assert (AirAnchor.mc.player != null);
                if (AirAnchor.mc.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)) {
                    BlockHitResult h;
                    assert (AirAnchor.mc.world != null);
                    HitResult hitResult = AirAnchor.mc.crosshairTarget;
                    if (hitResult instanceof BlockHitResult && (Integer)AirAnchor.mc.world.getBlockState((h = (BlockHitResult)hitResult).getBlockPos()).get((Property)RespawnAnchorBlock.CHARGES) != 0) {
                        if (h.getBlockPos().equals((Object)this.bp)) {
                            if (this.count >= 1) {
                                return;
                            }
                        } else {
                            this.bp = h.getBlockPos();
                            this.count = 0;
                        }
                        if (Math.random() * 100.0 <= this.chance.getValue()) {
                            if (this.holdingkeyonly.isEnabled()) {
                                if (this.holdingkey.isPressed()) {
                                    mc.getNetworkHandler().sendPacket((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, h, 0));
                                }
                            } else {
                                mc.getNetworkHandler().sendPacket((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, h, 0));
                            }
                        }
                        ++this.count;
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
