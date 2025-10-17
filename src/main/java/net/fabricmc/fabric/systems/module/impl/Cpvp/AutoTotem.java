package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.mixin.IHandledScreenAccessor;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem
extends Module {
    private final ModeSetting mode = new ModeSetting("m".concat("#").concat("o").concat("#").concat("d").concat("#").concat("e"), "A".concat("^").concat("u").concat("-").concat("t").concat("&").concat("o").concat("!").concat("T").concat("!").concat("o").concat(")").concat("t").concat("(").concat("e").concat("-").concat("m"), "M".concat("+").concat("o").concat("$").concat("d").concat("!").concat("e").concat("+").concat(" ").concat("*").concat("t").concat("&").concat("o").concat("@").concat(" ").concat("(").concat("u").concat("(").concat("s").concat("*").concat("e").concat("@").concat(" ").concat("*").concat("f").concat("$").concat("o").concat("$").concat("r").concat("^").concat(" ").concat("!").concat("keyCodec").concat("^").concat("u").concat("$").concat("t").concat("^").concat("o").concat("#").concat("t").concat("-").concat("o").concat("!").concat("t").concat("#").concat("e").concat("@").concat("m").concat("^").concat("i").concat("@").concat("n").concat("&").concat("g"), "I".concat("@").concat("n").concat("@").concat("v").concat("^").concat("e").concat("^").concat("n").concat("-").concat("t").concat("(").concat("o").concat("-").concat("r").concat("^").concat("y").concat("#").concat("T").concat("#").concat("o").concat(")").concat("t").concat("+").concat("e").concat(")").concat("m"), "L".concat("(").concat("e").concat("!").concat("g").concat("*").concat("i").concat("&").concat("t"), "A".concat("#").concat("u").concat("(").concat("t").concat("(").concat("o").concat("*").concat("T").concat("-").concat("o").concat("#").concat("t").concat("_").concat("e").concat("_").concat("m"));
    private final NumberSetting delay = new NumberSetting("d".concat("@").concat("e").concat("!").concat("l").concat("!").concat("keyCodec").concat("&").concat("y"), 0.0, 20.0, 0.0, 1.0, "D".concat("(").concat("e").concat("#").concat("l").concat("-").concat("keyCodec").concat("(").concat("y").concat("$").concat(" ").concat("!").concat("elementCodec").concat("$").concat("e").concat("&").concat("f").concat("*").concat("o").concat("&").concat("r").concat(")").concat("e").concat("(").concat(" ").concat(")").concat("t").concat("#").concat("o").concat("$").concat("t").concat("+").concat("e").concat("^").concat("m").concat("+").concat("i").concat("^").concat("n").concat("#").concat("g"));
    private final NumberSetting totemSlot = new NumberSetting("t".concat("^").concat("o").concat("@").concat("t").concat("_").concat("e").concat("^").concat("m").concat("@").concat(" ").concat("_").concat("s").concat("!").concat("l").concat("@").concat("o").concat("!").concat("t"), 0.0, 8.0, 8.0, 1.0, "S".concat("_").concat("l").concat("@").concat("o").concat("!").concat("t").concat("*").concat(" ").concat("-").concat("t").concat("@").concat("o").concat("^").concat(" ").concat("_").concat("u").concat("!").concat("s").concat("-").concat("e").concat("(").concat(" ").concat("(").concat("f").concat("^").concat("o").concat("@").concat("r").concat("#").concat(" ").concat("_").concat("t").concat("!").concat("o").concat("@").concat("t").concat("!").concat("e").concat("@").concat("m").concat("!").concat("i").concat(")").concat("n").concat("$").concat("g"));
    private final BooleanSetting autoSwitch = new BooleanSetting("A".concat("^").concat("u").concat("!").concat("t").concat("-").concat("o").concat("+").concat(" ").concat("_").concat("S").concat("+").concat("w").concat("_").concat("i").concat("-").concat("t").concat(")").concat("c").concat("#").concat("h"), false, "A".concat("&").concat("u").concat("+").concat("t").concat("_").concat("o").concat("@").concat("m").concat("@").concat("keyCodec").concat("*").concat("t").concat("(").concat("i").concat("+").concat("c").concat("$").concat("keyCodec").concat("$").concat("l").concat("$").concat("l").concat("*").concat("y").concat("^").concat(" ").concat("+").concat("s").concat("+").concat("w").concat("+").concat("i").concat("&").concat("t").concat(")").concat("c").concat("_").concat("h").concat("&").concat(" ").concat("_").concat("t").concat("@").concat("o").concat("#").concat("t").concat("-").concat("e").concat("*").concat("m").concat("+").concat(" ").concat("$").concat("i").concat("^").concat("n").concat("#").concat(" ").concat("+").concat("h").concat("(").concat("o").concat("-").concat("t").concat(")").concat("elementCodec").concat("^").concat("keyCodec").concat("$").concat("r"));
    private final BooleanSetting fillhotbar = new BooleanSetting("F".concat("#").concat("i").concat("_").concat("l").concat("!").concat("l").concat("(").concat(" ").concat("_").concat("H").concat("^").concat("o").concat("_").concat("t").concat("*").concat("elementCodec").concat("^").concat("keyCodec").concat(")").concat("r"), false, "A".concat("!").concat("u").concat("(").concat("t").concat("(").concat("o").concat("^").concat("m").concat("+").concat("keyCodec").concat("+").concat("t").concat("$").concat("i").concat("^").concat("c").concat("-").concat("keyCodec").concat("-").concat("l").concat("$").concat("l").concat("@").concat("y").concat("+").concat(" ").concat(")").concat("p").concat("+").concat("u").concat("$").concat("t").concat("(").concat("s").concat("_").concat(" ").concat("^").concat("t").concat("&").concat("o").concat("#").concat("t").concat("_").concat("e").concat("@").concat("m").concat("#").concat(" ").concat("!").concat("t").concat("+").concat("o").concat("_").concat(" ").concat("$").concat("e").concat(")").concat("m").concat("+").concat("p").concat("+").concat("t").concat("_").concat("y").concat("(").concat(" ").concat("!").concat("h").concat("#").concat("o").concat("^").concat("t").concat("(").concat("elementCodec").concat("^").concat("keyCodec").concat(")").concat("r"));
    private int nextTickSlot;
    private int totems;
    private int clock;

    public AutoTotem() {
        super("A".concat("*").concat("u").concat("&").concat("t").concat("$").concat("o").concat(")").concat("T").concat("_").concat("o").concat("+").concat("t").concat("$").concat("e").concat("_").concat("m"), "T".concat("_").concat("o").concat("&").concat("t").concat("(").concat("e").concat("&").concat("m").concat(")").concat("s").concat("^").concat(" ").concat("*").concat("f").concat("@").concat("o").concat("_").concat("r").concat("$").concat(" ").concat("+").concat("y").concat("$").concat("o").concat("@").concat("u"), Category.CrystalPvP);
        this.addSettings(this.mode, this.delay, this.totemSlot, this.fillhotbar);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.clock = -1;
    }

    @Override
    public void onTick() {
        Screen targetSlot2;
        InventoryScreen screen;
        if (this.mode.isMode("AutoTotem")) {
            this.finishMovingTotem();
            PlayerInventory inventory = AutoTotem.mc.player.getInventory();
            ItemStack offhandStack = inventory.getStack(40);
            if (this.isTotem(offhandStack)) {
                ++this.totems;
                return;
            }
            if (AutoTotem.mc.currentScreen instanceof HandledScreen && !(AutoTotem.mc.currentScreen instanceof AbstractInventoryScreen)) {
                return;
            }
            if (this.clock > 0) {
                --this.clock;
                return;
            }
            int nextTotemSlot = this.searchForTotems(inventory);
            if (offhandStack.isEmpty()) {
                AutoTotem.moveItem(nextTotemSlot, 45);
            }
            if (this.autoSwitch.isEnabled()) {
                inventory.selectedSlot = (int)this.totemSlot.getValue();
            }
            this.clock = (int)this.delay.getValue();
        }
        if (this.mode.isMode("InventoryTotem")) {
            if (AutoTotem.mc.currentScreen instanceof InventoryScreen) {
                int nextTotemSlot;
                if (this.clock == -1) {
                    this.clock = (int)this.delay.getValue();
                }
                if (this.clock > 0) {
                    --this.clock;
                    return;
                }
                PlayerInventory inv = AutoTotem.mc.player.getInventory();
                if (this.autoSwitch.isEnabled()) {
                    inv.selectedSlot = (int)this.totemSlot.getValue();
                }
                if (((ItemStack)inv.offHand.get(0)).getItem() != Items.TOTEM_OF_UNDYING && (nextTotemSlot = this.searchForTotems(inv)) != -1) {
                    assert (AutoTotem.mc.interactionManager != null);
                    AutoTotem.mc.interactionManager.clickSlot(((PlayerScreenHandler)((InventoryScreen)AutoTotem.mc.currentScreen).getScreenHandler()).syncId, nextTotemSlot, 40, SlotActionType.SWAP, (PlayerEntity)AutoTotem.mc.player);
                }
            } else {
                this.clock = -1;
            }
            Screen nextTotemSlot = AutoTotem.mc.currentScreen;
            if (this.mode.isMode("Legit") && (AutoTotem.mc.currentScreen instanceof InventoryScreen)) {
                InventoryScreen currentScreen = (InventoryScreen) AutoTotem.mc.currentScreen;
                if (AutoTotem.mc.mouse != null) {
                    Slot focusedSlot = ((IHandledScreenAccessor)currentScreen).getFocusedSlot();
                    if (focusedSlot != null && focusedSlot.hasStack() && focusedSlot.getStack().getItem() == Items.TOTEM_OF_UNDYING
                            && AutoTotem.mc.player.getOffHandStack().isEmpty()) {
                        if (this.clock == -1) {
                            this.clock = (int)this.delay.getValue();
                        }
                        if (this.clock > 0) {
                            --this.clock;
                            return;
                        }
                        AutoTotem.mc.interactionManager.clickSlot(
                                ((PlayerScreenHandler)currentScreen.getScreenHandler()).syncId,
                                focusedSlot.id,
                                40,
                                SlotActionType.SWAP,
                                (PlayerEntity)AutoTotem.mc.player
                        );
                    }
                }
            }

        }
        if (this.mode.isMode("Legit") && (targetSlot2 = AutoTotem.mc.currentScreen) instanceof InventoryScreen) {
            screen = (InventoryScreen)targetSlot2;
            if (AutoTotem.mc.mouse != null) {
                Screen screen2;
                ItemStack focusedItemStack;
                Slot focusedSlot = ((IHandledScreenAccessor)screen).getFocusedSlot();
                if (focusedSlot != null && focusedSlot.hasStack() && (focusedItemStack = focusedSlot.getStack()).getItem() == Items.TOTEM_OF_UNDYING && AutoTotem.mc.player.getOffHandStack().isEmpty()) {
                    if (this.clock == -1) {
                        this.clock = (int)this.delay.getValue();
                    }
                    if (this.clock > 0) {
                        --this.clock;
                        return;
                    }
                    AutoTotem.mc.interactionManager.clickSlot(((PlayerScreenHandler)screen.getScreenHandler()).syncId, focusedSlot.id, 40, SlotActionType.SWAP, (PlayerEntity)AutoTotem.mc.player);
                }
                PlayerInventory inventory = AutoTotem.mc.player.getInventory();
                int targetSlot3 = this.totemSlot.getIValue();
                if (this.fillhotbar.isEnabled() && inventory.getStack(targetSlot3).isEmpty() && (screen2 = AutoTotem.mc.currentScreen) instanceof InventoryScreen) {
                    InventoryScreen inventoryScreen = (InventoryScreen)screen2;
                    if (focusedSlot != null && focusedSlot.getIndex() >= 0 && focusedSlot.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                        AutoTotem.mc.interactionManager.clickSlot(((PlayerScreenHandler)inventoryScreen.getScreenHandler()).syncId, focusedSlot.getIndex(), targetSlot3, SlotActionType.SWAP, (PlayerEntity)AutoTotem.mc.player);
                    }
                }
            }
        }
    }

    private int findEmptyHotbarSlot(PlayerInventory inventory) {
        for (int slot = 0; slot < 9; ++slot) {
            if (!inventory.getStack(slot).isEmpty()) continue;
            return slot;
        }
        return -1;
    }

    public static void moveItem(int fromSlot, int toSlot) {
        AutoTotem.mc.interactionManager.clickSlot(AutoTotem.mc.player.currentScreenHandler.syncId, fromSlot, 0, SlotActionType.PICKUP, (PlayerEntity)AutoTotem.mc.player);
        AutoTotem.mc.interactionManager.clickSlot(AutoTotem.mc.player.currentScreenHandler.syncId, toSlot, 0, SlotActionType.PICKUP, (PlayerEntity)AutoTotem.mc.player);
    }

    private void finishMovingTotem() {
        if (this.nextTickSlot == -1) {
            return;
        }
        AutoTotem.mc.interactionManager.clickSlot(0, this.nextTickSlot, 0, SlotActionType.PICKUP, (PlayerEntity)AutoTotem.mc.player);
        this.nextTickSlot = -1;
    }

    private int searchForTotems(PlayerInventory inventory) {
        this.totems = 0;
        int nextTotemSlot = -1;
        for (int slot = 0; slot <= 36; ++slot) {
            if (!this.isTotem(inventory.getStack(slot))) continue;
            ++this.totems;
            if (nextTotemSlot != -1) continue;
            nextTotemSlot = slot < 9 ? slot + 36 : slot;
        }
        return nextTotemSlot;
    }

    private boolean isTotem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}
