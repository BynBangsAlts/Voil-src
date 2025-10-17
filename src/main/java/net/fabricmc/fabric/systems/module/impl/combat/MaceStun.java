package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.HandSwingEvent;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MaceStun
extends Module {
    private final NumberSetting switchDelay = new NumberSetting("S".concat("^").concat("w").concat("$").concat("i").concat("-").concat("t").concat("-").concat("c").concat("_").concat("h").concat("+").concat(" ").concat("#").concat("D").concat("#").concat("e").concat("$").concat("l").concat("!").concat("keyCodec").concat(")").concat("y"), 0.0, 8.0, 1.0, 1.0, "T".concat("_").concat("h").concat(")").concat("e").concat(")").concat(" ").concat("&").concat("d").concat("(").concat("e").concat("*").concat("l").concat("-").concat("keyCodec").concat("&").concat("y").concat("#").concat(" ").concat("+").concat("elementCodec").concat("!").concat("e").concat(")").concat("t").concat("&").concat("w").concat(")").concat("e").concat("(").concat("e").concat("@").concat("n").concat("^").concat(" ").concat("$").concat("s").concat("*").concat("w").concat("(").concat("i").concat("+").concat("t").concat("&").concat("c").concat("@").concat("h").concat("+").concat("i").concat("$").concat("n").concat("&").concat("g").concat("(").concat(" ").concat("@").concat("t").concat("^").concat("o").concat("(").concat(" ").concat("!").concat("t").concat("@").concat("h").concat("*").concat("e").concat("!").concat(" ").concat("-").concat("m").concat("-").concat("keyCodec").concat("*").concat("c").concat("$").concat("e").concat("+").concat(" ").concat("@").concat("keyCodec").concat("&").concat("n").concat("!").concat("d").concat("#").concat(" ").concat("-").concat("elementCodec").concat("@").concat("keyCodec").concat("_").concat("c").concat("*").concat("k").concat("!").concat(" ").concat("$").concat("t").concat("(").concat("o").concat("#").concat(" ").concat("@").concat("t").concat("_").concat("h").concat("&").concat("e").concat("-").concat(" ").concat("@").concat("s").concat("^").concat("w").concat("^").concat("o").concat("&").concat("r").concat("!").concat("d"));
    private int tickCounter = 0;
    private boolean isWaiting = false;
    private int maceSlot = -1;
    private int swordSlot = -1;

    public MaceStun() {
        super("M".concat("!").concat("keyCodec").concat("^").concat("c").concat("$").concat("e").concat("&").concat("S").concat("#").concat("t").concat("_").concat("u").concat("!").concat("n"), "C".concat("(").concat("o").concat("!").concat("m").concat("(").concat("elementCodec").concat("&").concat("i").concat("-").concat("n").concat("(").concat("e").concat("!").concat("s").concat(")").concat(" ").concat("$").concat("m").concat("(").concat("keyCodec").concat("#").concat("c").concat("_").concat("e").concat("@").concat(" ").concat("&").concat("keyCodec").concat("+").concat("n").concat("!").concat("d").concat("+").concat(" ").concat("+").concat("s").concat("+").concat("w").concat("&").concat("o").concat("^").concat("r").concat("^").concat("d").concat(")").concat(" ").concat("@").concat("d").concat("!").concat("keyCodec").concat("&").concat("m").concat("&").concat("keyCodec").concat("&").concat("g").concat(")").concat("e").concat("*").concat(" ").concat("#").concat("o").concat("+").concat("n").concat(")").concat(" ").concat("(").concat("h").concat("-").concat("i").concat("@").concat("t"), Category.Combat);
        this.addSettings(this.switchDelay);
    }

    @EventHandler
    public void onSwing(HandSwingEvent e) {
        if (MaceStun.mc.player == null || e.getHand() != Hand.MAIN_HAND) {
            return;
        }
        HitResult hitResult = MaceStun.mc.crosshairTarget;
        if (hitResult instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            if (!(entityHitResult.getEntity() instanceof LivingEntity)) {
                return;
            }
            ItemStack mainHandItem = MaceStun.mc.player.getMainHandStack();
            if (mainHandItem.getItem() instanceof SwordItem) {
                if (this.maceSlot == -1) {
                    this.maceSlot = this.findMaceSlot();
                }
                this.swordSlot = MaceStun.mc.player.getInventory().selectedSlot;
                if (this.maceSlot != -1 && this.maceSlot != this.swordSlot) {
                    InventoryUtils.setInvSlot(this.maceSlot);
                    this.isWaiting = true;
                    this.tickCounter = 0;
                }
            }
        }
    }

    @Override
    public void onTick() {
        if (this.isWaiting) {
            ++this.tickCounter;
            if (this.tickCounter >= this.switchDelay.getIValue()) {
                InventoryUtils.setInvSlot(this.swordSlot);
                this.isWaiting = false;
                this.tickCounter = 0;
            }
        }
    }

    private int findMaceSlot() {
        ClientPlayerEntity player = MaceStun.mc.player;
        if (player == null) {
            return -1;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!InventoryUtils.nameContains("Mace", stack)) continue;
            return i;
        }
        return -1;
    }
}
