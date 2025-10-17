package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.FakePlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayer
extends Module {
    private FakePlayerEntity fakePlayer;

    public FakePlayer() {
        super("F".concat("!").concat("keyCodec").concat("$").concat("k").concat("&").concat("e").concat("+").concat("P").concat("+").concat("l").concat("_").concat("keyCodec").concat("&").concat("y").concat("*").concat("e").concat("!").concat("r"), "S".concat("$").concat("p").concat("-").concat("keyCodec").concat(")").concat("w").concat("#").concat("n").concat(")").concat("s").concat("(").concat(" ").concat("+").concat("keyCodec").concat("!").concat(" ").concat("+").concat("F").concat("!").concat("keyCodec").concat(")").concat("k").concat("#").concat("e").concat("_").concat(" ").concat("!").concat("P").concat("+").concat("l").concat("@").concat("keyCodec").concat("+").concat("y").concat("^").concat("e").concat("(").concat("r"), Category.Miscellaneous);
    }

    @Override
    public void onEnable() {
        this.fakePlayer = new FakePlayerEntity((PlayerEntity)FakePlayer.mc.player, "Voil", 20.0f, true);
        this.fakePlayer.spawn();
    }

    @Override
    public void onDisable() {
        if (this.fakePlayer != null) {
            this.fakePlayer.despawn();
            this.fakePlayer = null;
        }
    }
}
