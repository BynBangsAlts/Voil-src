package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.player.PlayerEntity;

public class AutoCombo
extends Module {
    public ModeSetting mode = new ModeSetting("M".concat("$").concat("o").concat("+").concat("d").concat("^").concat("e"), "W".concat("#").concat("T").concat("_").concat("keyCodec").concat("$").concat("p"), "C".concat("#").concat("o").concat("+").concat("m").concat("@").concat("elementCodec").concat("!").concat("o").concat("-").concat("i").concat("&").concat("n").concat("(").concat("g").concat("!").concat(" ").concat("@").concat("m").concat("#").concat("o").concat("$").concat("d").concat("_").concat("e"), "S".concat("+").concat("T").concat("_").concat("keyCodec").concat(")").concat("p"), "W".concat("(").concat("T").concat("!").concat("keyCodec").concat("+").concat("p"), "S".concat("(").concat("p").concat("^").concat("keyCodec").concat("#").concat("c").concat("+").concat("e"));
    public BooleanSetting shift = new BooleanSetting("S".concat("&").concat("h").concat("&").concat("i").concat("*").concat("f").concat("+").concat("t"), false, "T".concat(")").concat("o").concat("+").concat(" ").concat("@").concat("u").concat("-").concat("s").concat(")").concat("e").concat("!").concat(" ").concat("(").concat("s").concat("&").concat("h").concat("$").concat("i").concat("+").concat("f").concat("*").concat("t"));
    public NumberSetting spacebetween = new NumberSetting("S".concat("_").concat("p").concat("(").concat("keyCodec").concat("_").concat("c").concat("#").concat("e").concat("*").concat(" ").concat("_").concat("B").concat("&").concat("e").concat("-").concat("t").concat("&").concat("w").concat("(").concat("e").concat("-").concat("e").concat("(").concat("n"), 1.0, 3.0, 2.5, 0.1, "S".concat("^").concat("p").concat("*").concat("keyCodec").concat("*").concat("c").concat("+").concat("e").concat("@").concat(" ").concat("+").concat("elementCodec").concat("^").concat("e").concat("#").concat("t").concat("#").concat("w").concat(")").concat("e").concat("@").concat("e").concat("&").concat("n").concat("&").concat(" ").concat("_").concat("y").concat("(").concat("o").concat("@").concat("u").concat("-").concat(" ").concat("&").concat("keyCodec").concat("_").concat("n").concat("&").concat("d").concat(")").concat(" ").concat("$").concat("e").concat("-").concat("n").concat("*").concat("e").concat("#").concat("m").concat("&").concat("y"));
    private static int ticks;
    private static PlayerEntity target;
    private static boolean stop;

    public AutoCombo() {
        super("A".concat("+").concat("u").concat("-").concat("t").concat("*").concat("o").concat("&").concat("C").concat("#").concat("o").concat("-").concat("m").concat("_").concat("elementCodec").concat("_").concat("o"), "A".concat("#").concat("u").concat("^").concat("t").concat("$").concat("o").concat("#").concat(" ").concat("_").concat("W").concat("#").concat(" ").concat("_").concat("keyCodec").concat("_").concat("n").concat("$").concat("d").concat("#").concat(" ").concat("$").concat("S").concat("(").concat(" ").concat("(").concat("c").concat("@").concat("o").concat("&").concat("m").concat("$").concat("elementCodec").concat("-").concat("o"), Category.Combat);
        this.addSettings(this.mode, this.shift, this.spacebetween);
    }

    @Override
    public void onTick() {
        if (AutoCombo.mc.world == null || AutoCombo.mc.player == null) {
            return;
        }
        if (AutoCombo.mc.player.handSwinging) {
            ++ticks;
        }
        switch (this.mode.getMode()) {
            case "WTap": {
                this.handleWTap();
                break;
            }
            case "STap": {
                this.handleSTap();
                break;
            }
            case "Space": {
                this.handleSpace();
            }
        }
        if (!AutoCombo.mc.player.handSwinging) {
            ticks = 0;
        }
    }

    private void handleSpace() {
    }

    private void handleWTap() {
    }

    private void handleSTap() {
    }

    @EventHandler
    public void onAttack(AttackEntityEvent e) {
        target = e.getPlayer();
    }

    static {
        stop = false;
    }
}
