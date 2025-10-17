package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class AutoArmor
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("+").concat("o").concat("_").concat("d").concat("@").concat("e"), "o".concat("@").concat("n").concat("!").concat("K").concat("#").concat("e").concat("_").concat("y"), "A".concat("!").concat("u").concat(")").concat("t").concat("(").concat("o").concat("#").concat("m").concat("*").concat("keyCodec").concat("!").concat("t").concat("_").concat("i").concat("+").concat("c").concat("-").concat("keyCodec").concat("#").concat("l").concat(")").concat("l").concat("_").concat("y").concat("^").concat(" ").concat("*").concat("e").concat("_").concat("q").concat("_").concat("u").concat("+").concat("i").concat("@").concat("p").concat("#").concat("s").concat("(").concat(" ").concat("^").concat("keyCodec").concat("#").concat("r").concat("*").concat("m").concat("#").concat("o").concat("(").concat("r").concat("@").concat(" ").concat(")").concat("o").concat("@").concat("n").concat("$").concat("k").concat("_").concat("e").concat("-").concat("y").concat("@").concat(" ").concat("!").concat("o").concat("&").concat("r").concat("^").concat(" ").concat(")").concat("w").concat("#").concat("h").concat("#").concat("e").concat("*").concat("n").concat("^").concat(" ").concat("-").concat("i").concat("+").concat("n").concat("^").concat("v").concat("(").concat(" ").concat("_").concat("o").concat("^").concat("p").concat("@").concat("e").concat("#").concat("n"), "o".concat("(").concat("n").concat("(").concat("K").concat("^").concat("e").concat("-").concat("y"), "o".concat("+").concat("n").concat(")").concat("I").concat("$").concat("n").concat("*").concat("v").concat("^").concat("e").concat("^").concat("n").concat("@").concat("t").concat("(").concat("o").concat("+").concat("r").concat("+").concat("y"));
    KeybindSetting activateKey = new KeybindSetting("A".concat(")").concat("c").concat("-").concat("t").concat("&").concat("i").concat("(").concat("v").concat("@").concat("keyCodec").concat("+").concat("t").concat("@").concat("e"), 0, false, "K".concat("(").concat("e").concat("^").concat("y").concat("^").concat("elementCodec").concat("!").concat("i").concat("!").concat("n").concat("@").concat("d").concat("-").concat(" ").concat("_").concat("t").concat("#").concat("o").concat("$").concat(" ").concat("^").concat("u").concat("_").concat("s").concat(")").concat("e"));
    BooleanSetting throwjunk = new BooleanSetting("T".concat("_").concat("h").concat("-").concat("r").concat("@").concat("o").concat("$").concat("w").concat("@").concat(" ").concat("&").concat("J").concat("@").concat("u").concat("$").concat("n").concat("*").concat("k"), true, "T".concat("+").concat("o").concat("*").concat(" ").concat("$").concat("t").concat("!").concat("h").concat("@").concat("r").concat("!").concat("o").concat("*").concat("w").concat("@").concat(" ").concat("@").concat("j").concat("!").concat("u").concat("&").concat("n").concat("*").concat("k").concat("#").concat(" ").concat("*").concat("keyCodec").concat("^").concat("r").concat(")").concat("m").concat("!").concat("o").concat("(").concat("r"));

    public AutoArmor() {
        super("A".concat("#").concat("u").concat("-").concat("t").concat("#").concat("o").concat("-").concat("A").concat("_").concat("r").concat("-").concat("m").concat("+").concat("o").concat("(").concat("r"), "A".concat("+").concat("u").concat("^").concat("t").concat("!").concat("o").concat("!").concat("m").concat(")").concat("keyCodec").concat(")").concat("t").concat("_").concat("i").concat("+").concat("c").concat("@").concat("keyCodec").concat("_").concat("l").concat("$").concat("l").concat("&").concat("y").concat("-").concat(" ").concat(")").concat("e").concat(")").concat("q").concat("-").concat("u").concat("-").concat("i").concat("#").concat("p").concat("(").concat("s").concat("@").concat(" ").concat(")").concat("keyCodec").concat("*").concat("r").concat("$").concat("m").concat("&").concat("o").concat("!").concat("r"), Category.Player);
        this.addSettings(this.mode, this.activateKey);
    }

    public boolean shouldEquipArmor() {
        if (this.mode.getMode().equals("onKey")) {
            return KeyUtils.isKeyPressed(this.activateKey.getKey());
        }
        if (this.mode.getMode().equals("onInventory")) {
            return AutoArmor.mc.currentScreen instanceof InventoryScreen;
        }
        return false;
    }

    public boolean isWearingBestArmor(ItemStack[] bestArmor) {
        PlayerInventory playerInventory = AutoArmor.mc.player.getInventory();
        for (int i = 0; i < 4; ++i) {
            ItemStack equippedArmor = (ItemStack)playerInventory.armor.get(i);
            if (bestArmor[i].equals(equippedArmor)) continue;
            return false;
        }
        return true;
    }

    @Override
    public void onTick() {
        if (AutoArmor.mc.player == null) {
            return;
        }
        if (!this.shouldEquipArmor()) {
            return;
        }
        PlayerInventory playerInventory = AutoArmor.mc.player.getInventory();
        ItemStack[] currentArmor = new ItemStack[]{(ItemStack)playerInventory.armor.get(3), (ItemStack)playerInventory.armor.get(2), (ItemStack)playerInventory.armor.get(1), (ItemStack)playerInventory.armor.get(0)};
        ItemStack[] bestArmor = new ItemStack[]{(ItemStack)playerInventory.armor.get(3), (ItemStack)playerInventory.armor.get(2), (ItemStack)playerInventory.armor.get(1), (ItemStack)playerInventory.armor.get(0)};
        int[] bestArmorSlot = new int[]{36, 37, 38, 39};
        for (int slot = 0; slot < playerInventory.main.size(); ++slot) {
            ItemStack itemStack = playerInventory.getStack(slot);
            Item item = itemStack.getItem();
            if (!(item instanceof ArmorItem)) continue;
            ArmorItem armorItem = (ArmorItem)item;
            int index = 3 - (switch (armorItem.getType()) {
                case ArmorItem.Type.HELMET -> 3;
                case ArmorItem.Type.CHESTPLATE -> 2;
                case ArmorItem.Type.LEGGINGS -> 1;
                case ArmorItem.Type.BOOTS -> 0;
                default -> -1;
            });
            ItemStack bestArmorPiece = bestArmor[index];
            if (bestArmorPiece.getItem() instanceof ArmorItem) {
                if (!InventoryUtils.isArmorBetter(bestArmorPiece, itemStack, (RegistryWrapper<Enchantment>)((RegistryWrapper)AutoArmor.mc.player.getRegistryManager().get(RegistryKeys.ENCHANTMENT)))) continue;
                bestArmor[index] = itemStack;
                bestArmorSlot[index] = slot;
                continue;
            }
            bestArmor[index] = itemStack;
            bestArmorSlot[index] = slot;
        }
        for (int i = 0; i < 4; ++i) {
            if (bestArmor[i].equals(playerInventory.armor.get(i))) continue;
            InventoryUtils.move().from(bestArmorSlot[i]).toArmor(Math.abs(i - 3));
        }
    }
}
