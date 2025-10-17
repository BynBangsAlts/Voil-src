package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class KeyPearl
extends Module {
    private KeybindSetting key = new KeybindSetting("K".concat("+").concat("e").concat("_").concat("y"), 0, false, "K".concat("$").concat("e").concat("*").concat("y").concat("&").concat(" ").concat(")").concat("t").concat("#").concat("o").concat("_").concat(" ").concat("#").concat("u").concat("&").concat("s").concat("*").concat("e"));
    private BooleanSetting switchBack = new BooleanSetting("S".concat("(").concat("w").concat("(").concat("i").concat("*").concat("t").concat("+").concat("c").concat("_").concat("h").concat("@").concat(" ").concat("+").concat("B").concat("^").concat("keyCodec").concat("(").concat("c").concat("-").concat("k"), false, "S".concat("#").concat("w").concat(")").concat("i").concat("(").concat("t").concat("#").concat("c").concat("^").concat("h").concat("_").concat("e").concat("!").concat("s").concat("*").concat(" ").concat("!").concat("elementCodec").concat("+").concat("keyCodec").concat(")").concat("c").concat(")").concat("k").concat("#").concat(" ").concat("$").concat("t").concat("-").concat("o").concat("@").concat(" ").concat("^").concat("o").concat("-").concat("l").concat("&").concat("d").concat("@").concat(" ").concat("^").concat("s").concat("^").concat("l").concat("(").concat("o").concat("_").concat("t").concat("&").concat(" ").concat("(").concat("keyCodec").concat("(").concat("f").concat("@").concat("t").concat("#").concat("e").concat("-").concat("r").concat("!").concat(" ").concat("#").concat("t").concat("^").concat("h").concat("!").concat("r").concat("&").concat("o").concat(")").concat("w").concat("^").concat("i").concat("_").concat("n").concat("!").concat("g"));
    private BooleanSetting swing = new BooleanSetting("S".concat("#").concat("w").concat("!").concat("i").concat("_").concat("n").concat("_").concat("g"), false, "S".concat("&").concat("w").concat("(").concat("i").concat("&").concat("n").concat("!").concat("g").concat("-").concat(" ").concat("^").concat("t").concat("@").concat("h").concat("&").concat("e").concat("&").concat(" ").concat("+").concat("keyCodec").concat("&").concat("r").concat("&").concat("m"));
    private BooleanSetting usePitch = new BooleanSetting("U".concat("(").concat("s").concat("+").concat("e").concat("+").concat(" ").concat("-").concat("P").concat("*").concat("i").concat("$").concat("t").concat(")").concat("c").concat("^").concat("h"), false, "T".concat("&").concat("o").concat("(").concat(" ").concat("^").concat("u").concat("&").concat("s").concat("_").concat("e").concat("+").concat(" ").concat("$").concat("p").concat(")").concat("r").concat("_").concat("e").concat("_").concat("-").concat("&").concat("s").concat("#").concat("e").concat("!").concat("t").concat("#").concat(" ").concat("!").concat("p").concat(")").concat("i").concat("_").concat("t").concat("*").concat("c").concat("^").concat("h"));
    private BooleanSetting onHurt = new BooleanSetting("O".concat("#").concat("n").concat("+").concat(" ").concat("_").concat("H").concat(")").concat("u").concat("-").concat("r").concat("_").concat("t"), false, "T".concat("_").concat("h").concat("$").concat("r").concat(")").concat("o").concat("!").concat("w").concat("(").concat("s").concat("&").concat(" ").concat("&").concat("p").concat(")").concat("e").concat("$").concat("keyCodec").concat("&").concat("r").concat("+").concat("l").concat("@").concat("s").concat(")").concat(" ").concat("^").concat("w").concat("(").concat("h").concat("@").concat("e").concat("!").concat("n").concat("@").concat(" ").concat("#").concat("p").concat("^").concat("l").concat("_").concat("keyCodec").concat("-").concat("y").concat("(").concat("e").concat("(").concat("r").concat("(").concat(" ").concat("&").concat("g").concat("@").concat("e").concat("+").concat("t").concat("#").concat("s").concat("-").concat(" ").concat("@").concat("h").concat("^").concat("u").concat("_").concat("r").concat("+").concat("t"));
    private NumberSetting delay = new NumberSetting("D".concat("$").concat("e").concat("(").concat("l").concat("$").concat("keyCodec").concat("+").concat("y"), 0.0, 10.0, 0.0, 1.0, "D".concat("!").concat("e").concat("*").concat("l").concat("-").concat("keyCodec").concat("+").concat("y").concat("&").concat(" ").concat("$").concat("elementCodec").concat("#").concat("e").concat("(").concat("f").concat("!").concat("o").concat("_").concat("r").concat("^").concat("e").concat("#").concat(" ").concat("*").concat("s").concat("+").concat("w").concat("^").concat("i").concat("$").concat("t").concat("-").concat("c").concat("(").concat("h").concat("*").concat("i").concat("#").concat("n").concat("_").concat("g").concat("*").concat(" ").concat("&").concat("elementCodec").concat("!").concat("keyCodec").concat("(").concat("c").concat("$").concat("k"));
    private NumberSetting pitch = new NumberSetting("P".concat("*").concat("i").concat("(").concat("t").concat(")").concat("c").concat("*").concat("h"), 10.0, 90.0, 45.0, 1.0, "P".concat("^").concat("i").concat("#").concat("t").concat(")").concat("c").concat("!").concat("h").concat("#").concat(" ").concat("$").concat("o").concat("&").concat("f").concat("@").concat(" ").concat("-").concat("t").concat("*").concat("h").concat("^").concat("e").concat("_").concat(" ").concat(")").concat("t").concat("$").concat("h").concat("&").concat("r").concat("*").concat("o").concat("(").concat("w"));
    private int ticks = 0;
    private int oldslot = -1;
    private boolean pearld = false;
    private boolean rotated = false;

    public KeyPearl() {
        super("K".concat("_").concat("e").concat("*").concat("y").concat(")").concat("P").concat("@").concat("e").concat("_").concat("keyCodec").concat("&").concat("r").concat("#").concat("l"), "T".concat("(").concat("h").concat("+").concat("r").concat("@").concat("o").concat("&").concat("w").concat("@").concat("s").concat("!").concat(" ").concat("@").concat("keyCodec").concat("^").concat("n").concat("(").concat(" ").concat("-").concat("E").concat("(").concat("n").concat("#").concat("d").concat(")").concat("e").concat("*").concat("r").concat("-").concat(" ").concat("^").concat("P").concat("#").concat("e").concat(")").concat("keyCodec").concat("$").concat("r").concat("!").concat("l").concat("*").concat(" ").concat("!").concat("w").concat("&").concat("i").concat("!").concat("t").concat("#").concat("h").concat("_").concat(" ").concat("#").concat("t").concat("#").concat("h").concat("*").concat("e").concat("*").concat(" ").concat("(").concat("k").concat("+").concat("e").concat(")").concat("y"), Category.CrystalPvP);
        this.addSettings(this.key, this.switchBack, this.usePitch, this.swing, this.onHurt, this.delay, this.pitch);
    }

    @Override
    public void onTick() {
        if ((this.key.isPressed() || this.onHurt.isEnabled() && KeyPearl.mc.player.hurtTime > 0) && !this.pearld) {
            this.oldslot = KeyPearl.mc.player.getInventory().selectedSlot;
            if (this.usePitch.isEnabled()) {
                KeyPearl.mc.player.setPitch((float)this.pitch.getValue());
                this.rotated = true;
            }
            InventoryUtils.swap(Items.ENDER_PEARL);
            KeyPearl.mc.interactionManager.interactItem((PlayerEntity)KeyPearl.mc.player, Hand.MAIN_HAND);
            if (this.swing.isEnabled()) {
                KeyPearl.mc.player.swingHand(Hand.MAIN_HAND);
            }
            this.pearld = true;
            this.rotated = false;
            this.ticks = 0;
        }
        if (this.pearld) {
            ++this.ticks;
            if ((double)this.ticks >= this.delay.getValue()) {
                if (this.switchBack.isEnabled()) {
                    KeyPearl.mc.player.getInventory().selectedSlot = this.oldslot;
                }
                this.pearld = false;
            }
        }
    }
}
