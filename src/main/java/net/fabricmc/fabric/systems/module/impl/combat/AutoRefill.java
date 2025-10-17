package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.mixin.IHandledScreenAccessor;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AutoRefill
extends Module {
    private final NumberSetting swapDelay = new NumberSetting("S".concat("_").concat("w").concat("(").concat("keyCodec").concat("-").concat("p").concat("-").concat(" ").concat("!").concat("D").concat("&").concat("e").concat("$").concat("l").concat("+").concat("keyCodec").concat("_").concat("y"), 0.0, 10.0, 2.0, 1.0, "D".concat("(").concat("e").concat("(").concat("l").concat("(").concat("keyCodec").concat("#").concat("y").concat("!").concat(" ").concat(")").concat("f").concat("-").concat("o").concat(")").concat("r").concat("!").concat(" ").concat("&").concat("p").concat(")").concat("u").concat("+").concat("t").concat("@").concat("t").concat("#").concat("i").concat("-").concat("n").concat("_").concat("g").concat("*").concat(" ").concat("*").concat("p").concat(")").concat("o").concat("*").concat("t").concat("-").concat("i").concat("(").concat("o").concat("(").concat("n").concat("-").concat("s"));
    private static final ModeSetting mode = new ModeSetting("Mode", "Legit", "Mode to use when refilling", "Packet", "Legit");
    private int swapClock;

    @Override
    public void onEnable() {
        this.swapClock = 0;
    }

    public AutoRefill() {
        super("A".concat("-").concat("u").concat("(").concat("t").concat("-").concat("o").concat("!").concat("R").concat("*").concat("e").concat("-").concat("f").concat("@").concat("i").concat("+").concat("l").concat("&").concat("l"), "A".concat("@").concat("u").concat("(").concat("t").concat("-").concat("o").concat("$").concat("m").concat("^").concat("keyCodec").concat("#").concat("t").concat("(").concat("i").concat("_").concat("c").concat("#").concat("keyCodec").concat(")").concat("l").concat("^").concat("l").concat("$").concat("y").concat("!").concat(" ").concat("&").concat("r").concat("_").concat("e").concat(")").concat("f").concat("+").concat("i").concat("_").concat("l").concat("^").concat("l").concat("!").concat("s").concat("#").concat(" ").concat("!").concat("y").concat("*").concat("o").concat("@").concat("u").concat("-").concat("r").concat("@").concat(" ").concat("^").concat("i").concat("(").concat("n").concat("@").concat("v").concat("_").concat("e").concat("*").concat("n").concat(")").concat("t").concat("-").concat("o").concat("_").concat("r").concat("#").concat("y"), Category.Combat);
        this.addSettings(mode, this.swapDelay);
    }

    @Override
    public void onTick() {
        Screen screen = AutoRefill.mc.currentScreen;
        if (screen instanceof InventoryScreen) {
            InventoryScreen inventoryScreen = (InventoryScreen)screen;
            PlayerInventory inventory = AutoRefill.mc.player.getInventory();
            if (mode.isMode("Packet")) {
                for (int i = 0; i < inventory.size(); ++i) {
                    int emptyHotbarSlot;
                    ItemStack itemStack;
                    if (InventoryUtils.isArmor(i) || InventoryUtils.isHotbar(i) || (itemStack = inventory.getStack(i)).getItem() != Items.SPLASH_POTION || !InventoryUtils.hasStatusEffect(itemStack, (StatusEffects)StatusEffects.INSTANT_HEALTH) || (emptyHotbarSlot = this.findEmptyHotbarSlot(inventory)) == -1) continue;
                    AutoRefill.mc.interactionManager.clickSlot(((PlayerScreenHandler)inventoryScreen.getScreenHandler()).syncId, i, emptyHotbarSlot, SlotActionType.SWAP, (PlayerEntity)AutoRefill.mc.player);
                }
            } else if (mode.isMode("Legit")) {
                Slot focusedSlot = ((IHandledScreenAccessor)inventoryScreen).getFocusedSlot();
                if (focusedSlot == null || InventoryUtils.isArmor(focusedSlot.getIndex()) || InventoryUtils.isHotbar(focusedSlot.getIndex())) {
                    return;
                }
                int emptyHotbarSlot = this.findEmptyHotbarSlot(inventory);
                ItemStack focusedItemStack = focusedSlot.getStack();
                if (focusedItemStack.getItem() == Items.SPLASH_POTION && InventoryUtils.hasStatusEffect(focusedItemStack, (StatusEffects)StatusEffects.INSTANT_HEALTH) && emptyHotbarSlot != -1) {
                    if (this.swapClock < this.swapDelay.getIValue()) {
                        ++this.swapClock;
                        return;
                    }
                    AutoRefill.mc.interactionManager.clickSlot(((PlayerScreenHandler)inventoryScreen.getScreenHandler()).syncId, focusedSlot.getIndex(), emptyHotbarSlot, SlotActionType.SWAP, (PlayerEntity)AutoRefill.mc.player);
                    this.swapClock = 0;
                }
            }
        }
    }

    private int findEmptyHotbarSlot(PlayerInventory inventory) {
        for (int i = 0; i <= 8; ++i) {
            if (!inventory.getStack(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }
}
