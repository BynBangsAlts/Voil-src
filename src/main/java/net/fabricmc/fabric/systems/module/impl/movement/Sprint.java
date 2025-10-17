package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.MovementUtils;

public class Sprint
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("_").concat("o").concat("_").concat("d").concat("_").concat("e"), "S".concat("(").concat("m").concat("!").concat("keyCodec").concat("#").concat("r").concat("!").concat("t"), "M".concat("+").concat("o").concat("(").concat("d").concat("!").concat("e").concat("_").concat(" ").concat(")").concat("o").concat("!").concat("f").concat("+").concat(" ").concat("*").concat("t").concat("^").concat("h").concat("!").concat("e").concat("+").concat(" ").concat("-").concat("S").concat("*").concat("p").concat("#").concat("r").concat("*").concat("i").concat("#").concat("n").concat("#").concat("t"), "O".concat("-").concat("m").concat("_").concat("n").concat("@").concat("i"), "S".concat("-").concat("m").concat("$").concat("keyCodec").concat("@").concat("r").concat("_").concat("t"));

    public Sprint() {
        super("S".concat("&").concat("p").concat("$").concat("r").concat("!").concat("i").concat("(").concat("n").concat("&").concat("t"), "M".concat("#").concat("keyCodec").concat("*").concat("k").concat("-").concat("e").concat(")").concat("s").concat("&").concat(" ").concat("&").concat("y").concat(")").concat("o").concat("$").concat("u").concat("^").concat(" ").concat("&").concat("s").concat(")").concat("p").concat("(").concat("r").concat("^").concat("i").concat("$").concat("n").concat("+").concat("t"), Category.Movement);
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (this.isEnabled()) {
            if (this.mode.isMode("Omni")) {
                if (MovementUtils.isMoving()) {
                    Sprint.mc.player.setSprinting(true);
                    MovementUtils.strafe();
                }
            } else if (this.mode.isMode("Smart")) {
                if (Sprint.mc.player.forwardSpeed > 0.0f) {
                    Sprint.mc.player.setSprinting(true);
                }
            } else {
                Sprint.mc.player.setSprinting(true);
            }
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        Sprint.mc.player.setSprinting(false);
        super.onDisable();
    }
}
