package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoStun
extends Module {
    private final NumberSetting hitDelay = new NumberSetting("H".concat("+").concat("i").concat("$").concat("t").concat("*").concat(" ").concat("*").concat("D").concat("&").concat("e").concat("+").concat("l").concat("&").concat("keyCodec").concat("(").concat("y"), 0.0, 500.0, 0.0, 1.0, "D".concat("-").concat("e").concat(")").concat("l").concat("@").concat("keyCodec").concat("!").concat("y").concat("@").concat(" ").concat("+").concat("t").concat("@").concat("o").concat("(").concat(" ").concat("@").concat("elementCodec").concat("(").concat("r").concat("_").concat("e").concat(")").concat("keyCodec").concat("+").concat("k").concat("!").concat(" ").concat("&").concat("s").concat("(").concat("h").concat(")").concat("i").concat("@").concat("e").concat("*").concat("l").concat("(").concat("d"));
    private final NumberSetting switchDelay = new NumberSetting("S".concat(")").concat("w").concat("+").concat("i").concat("(").concat("t").concat("+").concat("c").concat("&").concat("h").concat("$").concat(" ").concat("^").concat("D").concat("*").concat("e").concat("$").concat("l").concat("&").concat("keyCodec").concat("^").concat("y"), 0.0, 500.0, 0.0, 1.0, "D".concat("^").concat("e").concat("-").concat("l").concat("(").concat("keyCodec").concat(")").concat("y").concat("^").concat(" ").concat(")").concat("t").concat("*").concat("o").concat("!").concat(" ").concat("+").concat("s").concat("(").concat("w").concat("(").concat("i").concat("+").concat("t").concat("!").concat("c").concat("^").concat("h").concat("*").concat(" ").concat("*").concat("elementCodec").concat("$").concat("keyCodec").concat("*").concat("c").concat("@").concat("k").concat("!").concat(" ").concat("!").concat("t").concat("!").concat("o").concat("^").concat(" ").concat("-").concat("o").concat("#").concat("l").concat("+").concat("d").concat("*").concat(" ").concat("!").concat("s").concat("$").concat("l").concat("+").concat("o").concat("^").concat("t"));
    private final NumberSetting chance = new NumberSetting("C".concat("&").concat("h").concat(")").concat("keyCodec").concat("-").concat("n").concat("!").concat("c").concat("&").concat("e"), 0.0, 100.0, 100.0, 1.0, "C".concat("$").concat("h").concat("#").concat("keyCodec").concat("-").concat("n").concat("&").concat("c").concat("$").concat("e").concat("!").concat(" ").concat("@").concat("t").concat(")").concat("o").concat("-").concat(" ").concat("*").concat("p").concat("$").concat("e").concat("#").concat("r").concat("*").concat("f").concat("!").concat("o").concat("^").concat("r").concat("@").concat("m").concat("!").concat(" ").concat("^").concat("keyCodec").concat("_").concat("c").concat("$").concat("t").concat("&").concat("i").concat("#").concat("o").concat("!").concat("n"));
    private final BooleanSetting switchBack = new BooleanSetting("S".concat("_").concat("w").concat("(").concat("i").concat(")").concat("t").concat("@").concat("c").concat("_").concat("h").concat("&").concat(" ").concat("@").concat("B").concat("*").concat("keyCodec").concat("+").concat("c").concat("(").concat("k").concat("*").concat(" ").concat("+").concat("t").concat("!").concat("o").concat("(").concat(" ").concat("_").concat("P").concat("(").concat("r").concat("&").concat("e").concat("+").concat("v").concat("-").concat("i").concat("_").concat("o").concat("(").concat("u").concat("!").concat("s").concat("_").concat(" ").concat("_").concat("S").concat("(").concat("l").concat("#").concat("o").concat(")").concat("t"), true, "S".concat("&").concat("w").concat("*").concat("i").concat("_").concat("t").concat("@").concat("c").concat("*").concat("h").concat("-").concat("e").concat("@").concat("s").concat("$").concat(" ").concat("$").concat("elementCodec").concat("@").concat("keyCodec").concat("$").concat("c").concat("^").concat("k").concat("!").concat(" ").concat("&").concat("t").concat("#").concat("o").concat("!").concat(" ").concat("@").concat("o").concat("^").concat("l").concat("-").concat("d").concat("#").concat(" ").concat("$").concat("s").concat("*").concat("l").concat(")").concat("o").concat("+").concat("t").concat("^").concat(" ").concat("*").concat("keyCodec").concat("@").concat("f").concat("^").concat("t").concat("!").concat("e").concat(")").concat("r").concat("$").concat(" ").concat("^").concat("elementCodec").concat(")").concat("r").concat("@").concat("e").concat("$").concat("keyCodec").concat("@").concat("k").concat("(").concat("i").concat("*").concat("n").concat("@").concat("g").concat("+").concat(" ").concat("@").concat("s").concat("$").concat("h").concat(")").concat("i").concat("^").concat("e").concat(")").concat("l").concat("@").concat("d"));
    private final BooleanSetting block = new BooleanSetting("C".concat("$").concat("keyCodec").concat("#").concat("n").concat("(").concat("c").concat(")").concat("e").concat("*").concat("l").concat("!").concat(" ").concat("-").concat("W").concat("@").concat("h").concat("_").concat("i").concat("$").concat("l").concat("(").concat("e").concat("^").concat(" ").concat("*").concat("B").concat("_").concat("l").concat("&").concat("o").concat("!").concat("c").concat("@").concat("k").concat(")").concat("i").concat(")").concat("n").concat("*").concat("g"), true, "C".concat("+").concat("keyCodec").concat("#").concat("n").concat("@").concat("c").concat("&").concat("e").concat("_").concat("l").concat("#").concat(" ").concat("_").concat("w").concat("(").concat("h").concat("&").concat("i").concat("-").concat("l").concat("#").concat("e").concat("(").concat(" ").concat("*").concat("elementCodec").concat("(").concat("l").concat("+").concat("o").concat("@").concat("c").concat("@").concat("k").concat("$").concat("i").concat("*").concat("n").concat("&").concat("g").concat("@").concat(",").concat("(").concat("e").concat("(").concat("keyCodec").concat("!").concat("t").concat("(").concat("i").concat("(").concat("n").concat("+").concat("g"));
    private final BooleanSetting clickSimulation = new BooleanSetting("C".concat("+").concat("l").concat("_").concat("i").concat("(").concat("c").concat("&").concat("k").concat("&").concat(" ").concat(")").concat("S").concat("+").concat("i").concat("(").concat("m").concat("&").concat("u").concat("#").concat("l").concat("*").concat("keyCodec").concat("^").concat("t").concat("$").concat("i").concat("@").concat("o").concat("@").concat("n"), true, "S".concat("(").concat("i").concat("^").concat("m").concat("$").concat("u").concat("$").concat("l").concat("!").concat("keyCodec").concat("#").concat("t").concat("*").concat("e").concat("@").concat("s").concat(")").concat(" ").concat("!").concat("c").concat("$").concat("l").concat("^").concat("i").concat("^").concat("c").concat("+").concat("k").concat("@").concat("s").concat("-").concat(" ").concat("^").concat("w").concat("#").concat("h").concat("@").concat("i").concat("^").concat("l").concat("_").concat("e").concat("$").concat(" ").concat("+").concat("elementCodec").concat("*").concat("r").concat("&").concat("e").concat("@").concat("keyCodec").concat(")").concat("k").concat("&").concat("i").concat("!").concat("n").concat("+").concat("g").concat("(").concat(" ").concat("#").concat("s").concat("(").concat("h").concat("*").concat("i").concat("-").concat("e").concat("@").concat("l").concat("!").concat("d"));
    private final TimerUtils actionTimer = new TimerUtils();
    private final TimerUtils swapBackTimer = new TimerUtils();
    private int oldSlot = -1;
    private boolean swapped = false;
    private boolean attacked = false;
    private PlayerEntity lastTarget = null;
    Entity target;

    public AutoStun() {
        super("A".concat("*").concat("u").concat("-").concat("t").concat(")").concat("o").concat("+").concat("S").concat("#").concat("t").concat("$").concat("u").concat("(").concat("n"), "A".concat("-").concat("u").concat("@").concat("t").concat("@").concat("o").concat("@").concat("m").concat("-").concat("keyCodec").concat("@").concat("t").concat("@").concat("i").concat("#").concat("c").concat("(").concat("keyCodec").concat("^").concat("l").concat("@").concat("l").concat("-").concat("y").concat("$").concat(" ").concat("(").concat("elementCodec").concat("$").concat("r").concat("_").concat("e").concat("$").concat("keyCodec").concat("*").concat("k").concat("@").concat("s").concat("#").concat(" ").concat("(").concat("s").concat("#").concat("h").concat(")").concat("i").concat(")").concat("e").concat("$").concat("l").concat("(").concat("d"), Category.Combat);
        this.addSettings(this.hitDelay, this.switchDelay, this.chance, this.switchBack, this.block, this.clickSimulation);
    }

    @Override
    public void onEnable() {
        this.resetState();
    }

    @Override
    public void onDisable() {
        if (this.switchBack.isEnabled() && this.oldSlot != -1) {
            AutoStun.mc.player.getInventory().selectedSlot = this.oldSlot;
        }
        this.oldSlot = -1;
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (AutoStun.mc.player == null || AutoStun.mc.currentScreen != null || AutoStun.mc.targetedEntity == null) {
            return;
        }
        if (AutoStun.mc.player.isBlocking() && this.block.isEnabled()) {
            return;
        }
        if (AutoStun.mc.player.isUsingItem() && this.block.isEnabled()) {
            return;
        }
        if (AutoStun.mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if (this.targetUsingShield()) {
            if (SocialManager.isFriend(this.target.getUuid()) || SocialManager.isTeammate(this.target.getUuid())) {
                return;
            }
            if (Math.random() * 100.0 > this.chance.getValue()) {
                return;
            }
            if (!this.swapped) {
                this.oldSlot = AutoStun.mc.player.getInventory().selectedSlot;
                if (this.selectAxe()) {
                    this.swapped = true;
                    this.actionTimer.reset();
                }
            }
            if (this.swapped && this.actionTimer.hasReached(this.hitDelay.getValue())) {
                AutoStun.mc.interactionManager.attackEntity((PlayerEntity)AutoStun.mc.player, this.target);
                AutoStun.mc.player.swingHand(Hand.MAIN_HAND);
                this.attacked = true;
                this.actionTimer.reset();
                if (this.clickSimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(0);
                }
            }
            if (this.attacked && this.switchBack.isEnabled() && this.swapBackTimer.hasReached(this.switchDelay.getIValue())) {
                AutoStun.mc.player.getInventory().selectedSlot = this.oldSlot;
                this.resetState();
            }
        } else {
            if (this.swapped) {
                AutoStun.mc.player.getInventory().selectedSlot = this.oldSlot;
            }
            this.resetState();
        }
    }

    private boolean selectAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = AutoStun.mc.player.getInventory().getStack(i);
            Item item = itemStack.getItem();
            if (!this.isAxe(item)) continue;
            AutoStun.mc.player.getInventory().selectedSlot = i;
            return true;
        }
        return false;
    }

    private boolean isAxe(Item item) {
        return item instanceof AxeItem && !InventoryUtils.nameContains("Mace", item.getDefaultStack());
    }

    private boolean targetUsingShield() {
        this.target = AutoStun.mc.targetedEntity;
        if (this.target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)this.target;
            if (SocialManager.isFriend(player.getUuid())) {
                return false;
            }
            if (player.isUsingItem() && player.getActiveItem().getItem() == Items.SHIELD) {
                return !WorldUtils.isShieldFacingAway(player);
            }
        }
        return false;
    }

    private void resetState() {
        this.oldSlot = -1;
        this.swapped = false;
        this.attacked = false;
        this.lastTarget = null;
        this.actionTimer.reset();
        this.swapBackTimer.reset();
    }
}
