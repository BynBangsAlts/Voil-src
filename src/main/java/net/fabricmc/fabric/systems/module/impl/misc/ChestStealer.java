package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.mixin.IHandledScreenAccessor;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealer
extends Module {
    private final ModeSetting mode = new ModeSetting("M".concat(")").concat("o").concat(")").concat("d").concat(")").concat("e"), "P".concat("-").concat("keyCodec").concat("&").concat("c").concat("_").concat("k").concat(")").concat("e").concat("+").concat("t"), "M".concat("&").concat("o").concat(")").concat("d").concat("#").concat("e").concat("+").concat(" ").concat(")").concat("f").concat("!").concat("o").concat(")").concat("r").concat("@").concat(" ").concat("!").concat("s").concat(")").concat("t").concat("!").concat("e").concat("@").concat("keyCodec").concat("*").concat("l").concat("(").concat("i").concat("@").concat("n").concat("#").concat("g"), "M".concat("_").concat("keyCodec").concat("@").concat("n").concat("!").concat("u").concat("(").concat("keyCodec").concat("$").concat("l"), "P".concat(")").concat("keyCodec").concat("@").concat("c").concat(")").concat("k").concat("*").concat("e").concat("-").concat("t"));
    private final NumberSetting delay = new NumberSetting("D".concat("!").concat("e").concat("&").concat("l").concat("@").concat("keyCodec").concat("-").concat("y"), 0.0, 5.0, 2.0, 1.0, "D".concat("!").concat("e").concat("*").concat("l").concat(")").concat("keyCodec").concat("^").concat("y").concat("^").concat(" ").concat("+").concat("elementCodec").concat("&").concat("e").concat("#").concat("f").concat("*").concat("o").concat("$").concat("r").concat("(").concat("e").concat("+").concat(" ").concat("!").concat("g").concat("_").concat("e").concat("*").concat("t").concat(")").concat("t").concat(")").concat("i").concat("$").concat("n").concat("_").concat("g").concat("^").concat(" ").concat("&").concat("s").concat("$").concat("t").concat("$").concat("u").concat(")").concat("f").concat("#").concat("f").concat("-").concat(" ").concat("*").concat("f").concat("(").concat("r").concat("_").concat("o").concat("&").concat("m").concat(")").concat(" ").concat("_").concat("c").concat("&").concat("h").concat("&").concat("e").concat("-").concat("s").concat("#").concat("t"));
    private final BooleanSetting autoClose = new BooleanSetting("A".concat("-").concat("u").concat(")").concat("t").concat("_").concat("o").concat("-").concat(" ").concat("+").concat("C").concat("-").concat("l").concat("+").concat("o").concat("&").concat("s").concat("!").concat("e"), true, "A".concat("+").concat("u").concat("!").concat("t").concat("_").concat("o").concat("#").concat("m").concat("_").concat("keyCodec").concat("_").concat("t").concat("#").concat("i").concat(")").concat("c").concat("*").concat("keyCodec").concat("_").concat("l").concat("&").concat("l").concat("*").concat("y").concat("!").concat(" ").concat("_").concat("c").concat("_").concat("l").concat("*").concat("o").concat("!").concat("s").concat("!").concat("e").concat("&").concat("s").concat("*").concat(" ").concat("@").concat("c").concat(")").concat("h").concat("@").concat("e").concat("^").concat("s").concat("#").concat("t").concat("#").concat(" ").concat("#").concat("keyCodec").concat("+").concat("f").concat("!").concat("t").concat("_").concat("e").concat("+").concat("r").concat("(").concat(" ").concat("!").concat("s").concat("-").concat("t").concat("*").concat("e").concat("#").concat("keyCodec").concat("(").concat("l").concat("*").concat("i").concat(")").concat("n").concat("_").concat("g"));
    private long lastActionTime = 0L;
    private int swapClock;

    public ChestStealer() {
        super("C".concat("^").concat("h").concat("_").concat("e").concat("+").concat("s").concat("(").concat("t").concat("&").concat("S").concat("&").concat("t").concat(")").concat("e").concat("&").concat("keyCodec").concat("-").concat("l").concat("(").concat("e").concat("^").concat("r"), "A".concat("&").concat("u").concat("&").concat("t").concat("*").concat("o").concat("!").concat("m").concat("*").concat("keyCodec").concat("#").concat("t").concat("$").concat("i").concat("^").concat("c").concat("!").concat("keyCodec").concat("*").concat("l").concat("&").concat("l").concat("+").concat("y").concat("$").concat(" ").concat("_").concat("s").concat("$").concat("t").concat("(").concat("e").concat("@").concat("keyCodec").concat("$").concat("l").concat("*").concat("s").concat("@").concat(" ").concat(")").concat("s").concat("@").concat("t").concat("+").concat("u").concat("#").concat("f").concat("+").concat("f").concat("@").concat(" ").concat("&").concat("f").concat("+").concat("r").concat("+").concat("o").concat(")").concat("m").concat("!").concat(" ").concat("#").concat("c").concat("(").concat("h").concat("#").concat("e").concat("&").concat("s").concat(")").concat("t").concat("$").concat("s"), Category.Miscellaneous);
        this.addSettings(this.mode, this.delay, this.autoClose);
    }

    @Override
    public void onEnable() {
        this.swapClock = 0;
    }

    @Override
    public void onTick() {
        if (ChestStealer.mc.player == null || ChestStealer.mc.world == null) {
            return;
        }
        Screen screen = ChestStealer.mc.currentScreen;
        if (screen instanceof GenericContainerScreen) {
            GenericContainerScreen containerScreen = (GenericContainerScreen)screen;
            Slot focusedSlot = ((IHandledScreenAccessor)containerScreen).getFocusedSlot();
            if (this.mode.getMode().equalsIgnoreCase("Packet")) {
                this.autoSteal(containerScreen);
                if (this.autoClose.isEnabled() && this.allSlotsEmpty(containerScreen)) {
                    ChestStealer.mc.player.closeHandledScreen();
                }
            } else if (this.mode.getMode().equalsIgnoreCase("Manual") && focusedSlot != null) {
                this.manualSteal(containerScreen, focusedSlot);
            }
        }
    }

    private void autoSteal(GenericContainerScreen containerScreen) {
        for (Slot slot : ((GenericContainerScreenHandler)containerScreen.getScreenHandler()).slots) {
            if (slot.getStack().isEmpty() || !this.hasEmptySlots()) continue;
            if (this.swapClock < this.delay.getIValue()) {
                ++this.swapClock;
                return;
            }
            ChestStealer.mc.interactionManager.clickSlot(((GenericContainerScreenHandler)containerScreen.getScreenHandler()).syncId, slot.getIndex(), 0, SlotActionType.QUICK_MOVE, (PlayerEntity)ChestStealer.mc.player);
            this.swapClock = 0;
        }
    }

    private void manualSteal(GenericContainerScreen containerScreen, Slot focusedSlot) {
        if (!focusedSlot.getStack().isEmpty() && this.hasEmptySlots() && focusedSlot.hasStack()) {
            if (this.swapClock < this.delay.getIValue()) {
                ++this.swapClock;
                return;
            }
            ChestStealer.mc.interactionManager.clickSlot(((GenericContainerScreenHandler)containerScreen.getScreenHandler()).syncId, focusedSlot.getIndex(), 0, SlotActionType.QUICK_MOVE, (PlayerEntity)ChestStealer.mc.player);
            this.swapClock = 0;
        }
    }

    private boolean allSlotsEmpty(GenericContainerScreen containerScreen) {
        for (Slot slot : ((GenericContainerScreenHandler)containerScreen.getScreenHandler()).slots) {
            if (slot.getStack().isEmpty()) continue;
            return false;
        }
        return true;
    }

    public boolean hasEmptySlots() {
        return ChestStealer.mc.player.getInventory().getEmptySlot() != -1;
    }
}
