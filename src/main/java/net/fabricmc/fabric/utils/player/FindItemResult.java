package net.fabricmc.fabric.utils.player;

import net.fabricmc.fabric.ClientMain;
import net.minecraft.util.Hand;

public class FindItemResult {
    private final int slot;
    private final int count;

    public FindItemResult(int slot, int count) {
        this.slot = slot;
        this.count = count;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getCount() {
        return this.count;
    }

    public boolean found() {
        return this.slot != -1;
    }

    public void switchTo() {
        ClientMain.mc.player.getInventory().selectedSlot = this.slot;
    }

    public Hand getHand() {
        if (this.slot == 40) {
            return Hand.OFF_HAND;
        }
        if (this.slot == ClientMain.mc.player.getInventory().selectedSlot) {
            return Hand.MAIN_HAND;
        }
        return null;
    }

    public boolean isMainHand() {
        return this.getHand() == Hand.MAIN_HAND;
    }

    public boolean isOffhand() {
        return this.getHand() == Hand.OFF_HAND;
    }

    public boolean isHotbar() {
        return this.slot >= 0 && this.slot <= 8;
    }

    public boolean isMain() {
        return this.slot >= 9 && this.slot <= 35;
    }

    public boolean isArmor() {
        return this.slot >= 36 && this.slot <= 39;
    }
}
