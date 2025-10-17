package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class AutoPot
extends Module {
    TimerUtils timer = new TimerUtils();
    NumberSetting health = new NumberSetting("H".concat("-").concat("e").concat("(").concat("keyCodec").concat("-").concat("l").concat("^").concat("t").concat(")").concat("h"), 1.0, 19.0, 8.0, 1.0, "M".concat("$").concat("i").concat(")").concat("n").concat("!").concat("i").concat("!").concat("m").concat("(").concat("u").concat("_").concat("m").concat("+").concat(" ").concat("*").concat("h").concat("-").concat("e").concat("^").concat("keyCodec").concat("$").concat("l").concat("^").concat("t").concat("_").concat("h").concat("!").concat(" ").concat("^").concat("t").concat("*").concat("o").concat("^").concat(" ").concat("&").concat("u").concat("#").concat("s").concat("^").concat("e").concat("#").concat(" ").concat("#").concat("p").concat("@").concat("o").concat("&").concat("t").concat("+").concat("i").concat("#").concat("o").concat(")").concat("n"));
    NumberSetting delay = new NumberSetting("D".concat("*").concat("e").concat("_").concat("l").concat("&").concat("keyCodec").concat("-").concat("y"), 2.0, 20.0, 10.0, 1.0, "D".concat("$").concat("e").concat("$").concat("l").concat("*").concat("keyCodec").concat("^").concat("y").concat("(").concat(" ").concat("@").concat("elementCodec").concat("@").concat("e").concat("&").concat("f").concat("(").concat("o").concat("+").concat("r").concat("-").concat("e").concat("$").concat(" ").concat("_").concat("u").concat("-").concat("s").concat("_").concat("i").concat("^").concat("n").concat(")").concat("g").concat(")").concat(" ").concat(")").concat("p").concat("$").concat("o").concat("#").concat("t").concat("!").concat("i").concat("_").concat("o").concat("*").concat("n"));
    BooleanSetting autobuff = new BooleanSetting("A".concat(")").concat("u").concat("#").concat("t").concat("$").concat("o").concat("*").concat("B").concat("!").concat("u").concat("@").concat("f").concat("&").concat("f"), false, "A".concat("(").concat("u").concat("^").concat("t").concat("$").concat("o").concat("-").concat("m").concat("@").concat("keyCodec").concat("*").concat("t").concat("#").concat("i").concat("^").concat("c").concat("-").concat("keyCodec").concat("-").concat("l").concat("(").concat("l").concat("!").concat("y").concat("+").concat(" ").concat("!").concat("elementCodec").concat("+").concat("u").concat("^").concat("f").concat("&").concat("f").concat("^").concat(" ").concat("&").concat("w").concat("#").concat("i").concat("^").concat("t").concat("^").concat("h").concat("(").concat(" ").concat("@").concat("p").concat("$").concat("o").concat("+").concat("t").concat("+").concat("i").concat("#").concat("o").concat("^").concat("n"));
    BooleanSetting spoof = new BooleanSetting("S".concat("!").concat("p").concat("_").concat("o").concat("+").concat("o").concat("&").concat("f").concat("*").concat(" ").concat(")").concat("S").concat("(").concat("l").concat("#").concat("o").concat("^").concat("t"), false, "D".concat("^").concat("o").concat("-").concat("e").concat("-").concat("s").concat("!").concat("n").concat("!").concat("t").concat("&").concat(" ").concat("+").concat("s").concat("@").concat("w").concat("_").concat("i").concat("$").concat("t").concat("^").concat("c").concat("!").concat("h").concat("$").concat(" ").concat("(").concat("t").concat("-").concat("o").concat("!").concat(" ").concat("-").concat("p").concat("_").concat("o").concat("_").concat("t").concat("#").concat("i").concat("#").concat("o").concat("^").concat("n").concat("!").concat(" ").concat(")").concat("s").concat("-").concat("l").concat("$").concat("o").concat("(").concat("t").concat("@").concat(" ").concat("$").concat("w").concat("$").concat("h").concat("*").concat("e").concat("*").concat("n").concat("*").concat(" ").concat("#").concat("t").concat("+").concat("h").concat("_").concat("r").concat(")").concat("o").concat("$").concat("w").concat("+").concat("i").concat(")").concat("n").concat("(").concat("g"));
    ModeSetting rotations = new ModeSetting("R".concat("*").concat("o").concat("-").concat("t").concat("@").concat("keyCodec").concat("_").concat("t").concat("@").concat("i").concat("&").concat("o").concat("@").concat("n").concat("-").concat("s"), "L".concat("_").concat("e").concat("+").concat("g").concat("&").concat("i").concat("$").concat("t"), "R".concat("_").concat("o").concat("(").concat("t").concat("$").concat("keyCodec").concat("@").concat("t").concat("@").concat("i").concat("@").concat("o").concat("+").concat("n").concat("-").concat("s").concat("!").concat(" ").concat("!").concat("m").concat("#").concat("o").concat("*").concat("d").concat("+").concat("e"), "S".concat("@").concat("i").concat("@").concat("l").concat("-").concat("e").concat("@").concat("n").concat("*").concat("t"), "L".concat("*").concat("e").concat("^").concat("g").concat("-").concat("i").concat("&").concat("t"));
    Float prevPitch;
    int potSlot = -1;
    int prevSlot;
    int ticksAfterPotion = 0;

    public AutoPot() {
        super("A".concat("+").concat("u").concat("(").concat("t").concat("+").concat("o").concat("-").concat("P").concat("!").concat("o").concat("_").concat("t"), "A".concat("&").concat("u").concat("+").concat("t").concat(")").concat("o").concat(")").concat("m").concat("+").concat("keyCodec").concat(")").concat("t").concat("@").concat("i").concat("-").concat("c").concat("@").concat("keyCodec").concat("(").concat("l").concat("(").concat("l").concat("#").concat("y").concat("_").concat(" ").concat("*").concat("t").concat("!").concat("h").concat("&").concat("r").concat("*").concat("o").concat("*").concat("w").concat("_").concat("s").concat("^").concat(" ").concat("^").concat("p").concat("-").concat("o").concat("-").concat("t").concat("#").concat("i").concat("@").concat("o").concat("+").concat("n"), Category.Combat);
        this.addSettings(this.health, this.delay, this.autobuff, this.spoof, this.rotations);
    }

    @Override
    public void onTick() {
        super.onTick();
        assert (AutoPot.mc.player != null);
        if (AutoPot.mc.player.getHealth() <= this.health.getFloatValue() && this.ticksAfterPotion == 0 && this.findInstantHealthPotion()) {
            this.usePotion();
        }
        if (this.autobuff.isEnabled() && this.ticksAfterPotion == 0) {
            if (!AutoPot.mc.player.hasStatusEffect(StatusEffects.REGENERATION) && this.findBuffPotion((StatusEffects)StatusEffects.REGENERATION)) {
                this.usePotion();
            } else if (!AutoPot.mc.player.hasStatusEffect(StatusEffects.STRENGTH) && this.findBuffPotion((StatusEffects)StatusEffects.STRENGTH)) {
                this.usePotion();
            } else if (!AutoPot.mc.player.hasStatusEffect(StatusEffects.SPEED) && this.findBuffPotion((StatusEffects)StatusEffects.SPEED)) {
                this.usePotion();
            } else if (!AutoPot.mc.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && this.findBuffPotion((StatusEffects)StatusEffects.FIRE_RESISTANCE)) {
                this.usePotion();
            }
        }
        if (this.ticksAfterPotion > 0) {
            ++this.ticksAfterPotion;
            if ((double)this.ticksAfterPotion > this.delay.getValue()) {
                this.ticksAfterPotion = 0;
            }
        }
    }

    public boolean findInstantHealthPotion() {
        Integer slot = InventoryUtils.findPotion(0, 9, (StatusEffects)StatusEffects.INSTANT_HEALTH);
        if (slot != null) {
            this.potSlot = slot;
            return true;
        }
        return false;
    }

    public boolean findBuffPotion(StatusEffects effect) {
        Integer slot = InventoryUtils.findPotion(0, 9, effect);
        if (slot != null) {
            this.potSlot = slot;
            return true;
        }
        return false;
    }

    private void usePotion() {
        this.prevPitch = Float.valueOf(AutoPot.mc.player.getPitch());
        this.prevSlot = AutoPot.mc.player.getInventory().selectedSlot;
        AutoPot.mc.player.setPitch(90.0f);
        AutoPot.mc.player.getInventory().selectedSlot = this.potSlot;
        assert (AutoPot.mc.interactionManager != null);
        AutoPot.mc.interactionManager.interactItem((PlayerEntity)AutoPot.mc.player, Hand.MAIN_HAND);
        ++this.ticksAfterPotion;
        if (this.spoof.isEnabled()) {
            AutoPot.mc.player.getInventory().selectedSlot = this.prevSlot;
        } else if (this.timer.hasReached(1000.0)) {
            AutoPot.mc.player.getInventory().selectedSlot = this.potSlot;
            this.timer.reset();
        }
        if (this.rotations.getMode().equals("Silent")) {
            AutoPot.mc.player.setPitch(this.prevPitch.floatValue());
        } else if (this.rotations.getMode().equals("Legit") && this.timer.hasReached(1000.0)) {
            AutoPot.mc.player.setPitch(this.prevPitch.floatValue());
            this.timer.reset();
        }
    }
}
