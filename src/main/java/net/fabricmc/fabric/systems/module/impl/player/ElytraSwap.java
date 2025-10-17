package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class ElytraSwap
extends Module {
    KeybindSetting elytraSwap = new KeybindSetting("E".concat("$").concat("l").concat(")").concat("y").concat(")").concat("t").concat("^").concat("r").concat("$").concat("keyCodec").concat("@").concat("S").concat("$").concat("w").concat("-").concat("keyCodec").concat("$").concat("p"), 0, false, "B".concat("*").concat("i").concat("!").concat("n").concat("!").concat("d").concat("_").concat(" ").concat("-").concat("t").concat("&").concat("o").concat("_").concat(" ").concat("+").concat("s").concat("$").concat("w").concat("-").concat("keyCodec").concat("^").concat("p").concat("!").concat(" ").concat("+").concat("t").concat("*").concat("o").concat("_").concat(" ").concat("^").concat("e").concat("!").concat("l").concat("+").concat("y").concat("_").concat("t").concat("^").concat("r").concat("&").concat("keyCodec"));
    BooleanSetting rocketSwap = new BooleanSetting("R".concat("+").concat("o").concat("@").concat("c").concat("_").concat("k").concat("*").concat("e").concat("+").concat("t").concat("(").concat("S").concat("^").concat("w").concat("*").concat("keyCodec").concat("+").concat("p"), true, "S".concat("*").concat("w").concat("-").concat("keyCodec").concat("-").concat("p").concat("#").concat("s").concat("!").concat(" ").concat(")").concat("t").concat(")").concat("o").concat("$").concat(" ").concat("-").concat("r").concat("(").concat("o").concat("-").concat("c").concat("@").concat("k").concat("$").concat("e").concat("-").concat("t").concat("!").concat(" ").concat(")").concat("w").concat("&").concat("h").concat(")").concat("e").concat("+").concat("n").concat(")").concat(" ").concat("+").concat("e").concat("+").concat("l").concat("&").concat("y").concat("(").concat("t").concat("^").concat("r").concat(")").concat("keyCodec").concat("!").concat(" ").concat("@").concat("i").concat("_").concat("s").concat("#").concat(" ").concat("(").concat("o").concat("+").concat("n"));
    private long lastSwapTime = 0L;
    private long lastChestplateSwapTime = 0L;
    private long lastRocketSwapTime = 0L;
    private boolean isElytraEquipped = false;
    private boolean hasRocketSwapped = false;

    public ElytraSwap() {
        super("E".concat("&").concat("l").concat("(").concat("y").concat("&").concat("t").concat("@").concat("r").concat("_").concat("keyCodec").concat("&").concat("S").concat("^").concat("w").concat("+").concat("keyCodec").concat("!").concat("p"), "S".concat("!").concat("w").concat("!").concat("keyCodec").concat("&").concat("p").concat("+").concat("s").concat("+").concat(" ").concat("&").concat("t").concat("*").concat("o").concat("^").concat(" ").concat("(").concat("y").concat("!").concat("o").concat("-").concat("u").concat("#").concat("r").concat("_").concat(" ").concat("_").concat("e").concat("(").concat("l").concat("$").concat("y").concat(")").concat("t").concat("(").concat("r").concat("$").concat("keyCodec").concat("$").concat(" ").concat(")").concat("keyCodec").concat("*").concat("n").concat("*").concat("d").concat(")").concat(" ").concat("(").concat("f").concat("_").concat("l").concat("$").concat("i").concat("@").concat("e").concat("*").concat("s"), Category.Player);
        this.addSettings(this.elytraSwap, this.rocketSwap);
    }

    @Override
    public void onTick() {
        if (ElytraSwap.mc.player == null) {
            return;
        }
        if (KeyUtils.isKeyPressed(this.elytraSwap.getKey()) && System.currentTimeMillis() - this.lastSwapTime > 500L) {
            if (this.isWearingElytra()) {
                int chestplateSlot = this.findChestplate();
                if (chestplateSlot == -1) {
                    return;
                }
                InventoryUtils.setInvSlot(chestplateSlot);
                this.isElytraEquipped = false;
                this.hasRocketSwapped = false;
            } else {
                InventoryUtils.swap(Items.ELYTRA);
                this.isElytraEquipped = true;
            }
            ElytraSwap.mc.interactionManager.interactItem((PlayerEntity)ElytraSwap.mc.player, Hand.MAIN_HAND);
            this.lastSwapTime = System.currentTimeMillis();
        }
        if (ElytraSwap.mc.player.isFallFlying() && this.rocketSwap.isEnabled() && !this.hasRocketSwapped && this.isElytraEquipped && System.currentTimeMillis() - this.lastRocketSwapTime > 1000L) {
            InventoryUtils.swap(Items.FIREWORK_ROCKET);
            ElytraSwap.mc.interactionManager.interactItem((PlayerEntity)ElytraSwap.mc.player, Hand.MAIN_HAND);
            this.hasRocketSwapped = true;
            this.lastRocketSwapTime = System.currentTimeMillis();
        }
    }

    private boolean isWearingElytra() {
        for (ItemStack itemStack : ElytraSwap.mc.player.getArmorItems()) {
            if (itemStack.getItem() != Items.ELYTRA) continue;
            return true;
        }
        return false;
    }

    private int findChestplate() {
        for (int i = 0; i < ElytraSwap.mc.player.getInventory().size(); ++i) {
            ArmorItem armorItem;
            if (!(ElytraSwap.mc.player.getInventory().getStack(i).getItem() instanceof ArmorItem) || (armorItem = (ArmorItem)ElytraSwap.mc.player.getInventory().getStack(i).getItem()).getSlotType() != EquipmentSlot.CHEST) continue;
            return i;
        }
        return -1;
    }
}
