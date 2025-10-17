package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EntitySpawnEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PearlChase
extends Module {
    private ModeSetting rotation = new ModeSetting("R".concat("*").concat("o").concat(")").concat("t").concat("+").concat("keyCodec").concat("#").concat("t").concat("!").concat("i").concat("@").concat("o").concat("@").concat("n"), "N".concat("!").concat("o").concat("#").concat("r").concat("*").concat("m").concat("(").concat("keyCodec").concat("(").concat("l"), "R".concat("#").concat("o").concat("!").concat("t").concat("@").concat("keyCodec").concat("(").concat("t").concat("@").concat("i").concat("-").concat("o").concat("&").concat("n").concat("&").concat(" ").concat(")").concat("t").concat("#").concat("y").concat("-").concat("p").concat("-").concat("e"), "N".concat("^").concat("o").concat("(").concat("r").concat("#").concat("m").concat("_").concat("keyCodec").concat("*").concat("l"), "S".concat("#").concat("i").concat("!").concat("l").concat(")").concat("e").concat("_").concat("n").concat("*").concat("t"));
    private BooleanSetting onlyifHeatlhBigger = new BooleanSetting("H".concat("+").concat("e").concat("_").concat("keyCodec").concat("$").concat("l").concat("_").concat("t").concat("_").concat("h").concat("^").concat(" ").concat("-").concat("c").concat("&").concat("h").concat("$").concat("e").concat("_").concat("c").concat("@").concat("k"), false, "O".concat("-").concat("n").concat("*").concat("l").concat("!").concat("y").concat("_").concat(" ").concat("+").concat("c").concat(")").concat("h").concat("&").concat("keyCodec").concat("_").concat("s").concat("-").concat("e").concat("+").concat("s").concat("_").concat(" ").concat("@").concat("p").concat("*").concat("e").concat("^").concat("keyCodec").concat("(").concat("r").concat("!").concat("l").concat("$").concat(" ").concat("+").concat("i").concat("_").concat("f").concat("-").concat(" ").concat("(").concat("y").concat("$").concat("o").concat("@").concat("u").concat("&").concat("r").concat(")").concat(" ").concat("(").concat("h").concat("_").concat("e").concat("$").concat("keyCodec").concat("+").concat("l").concat("$").concat("t").concat("#").concat("h").concat("!").concat(" ").concat("$").concat("i").concat("!").concat("s").concat("@").concat(" ").concat("@").concat("m").concat(")").concat("o").concat("#").concat("r").concat("+").concat("e").concat("@").concat(" ").concat("&").concat("t").concat("!").concat("h").concat("_").concat("keyCodec").concat("_").concat("n").concat("@").concat(" ").concat("^").concat("t").concat("^").concat("h").concat("*").concat("e").concat("*").concat("i").concat("&").concat("r").concat("&").concat(" ").concat("$").concat("h").concat("*").concat("e").concat("$").concat("keyCodec").concat("_").concat("l").concat("+").concat("t").concat("#").concat("h"));
    private EnderPearlEntity targetPearl;

    public PearlChase() {
        super("P".concat("@").concat("e").concat(")").concat("keyCodec").concat("$").concat("r").concat("#").concat("l").concat("-").concat("C").concat("+").concat("h").concat("@").concat("keyCodec").concat("^").concat("s").concat("^").concat("e"), "M".concat("+").concat("keyCodec").concat("*").concat("k").concat(")").concat("e").concat("#").concat("s").concat("^").concat(" ").concat("$").concat("y").concat("!").concat("o").concat("*").concat("u").concat("_").concat(" ").concat("!").concat("c").concat("&").concat("h").concat("!").concat("keyCodec").concat("*").concat("s").concat("&").concat("e").concat("!").concat(" ").concat("#").concat("t").concat("+").concat("h").concat("&").concat("e").concat("$").concat(" ").concat("@").concat("e").concat("#").concat("n").concat("$").concat("e").concat("!").concat("m").concat("*").concat("y").concat("$").concat(" ").concat("$").concat("p").concat("*").concat("e").concat(")").concat("keyCodec").concat("+").concat("r").concat("*").concat("l"), Category.Player);
        this.addSettings(this.rotation, this.onlyifHeatlhBigger);
    }

    @EventHandler
    private void onEntitySpawn(EntitySpawnEvent e) {
        EnderPearlEntity pearl;
        Entity owner;
        Entity entity = e.getEntity();
        if (entity instanceof EnderPearlEntity && (owner = (pearl = (EnderPearlEntity)entity).getOwner()) != null && !owner.getUuid().equals(PearlChase.mc.player.getUuid())) {
            this.targetPearl = pearl;
        }
    }

    @Override
    public void onTick() {
        Entity entity;
        if (this.targetPearl == null || this.targetPearl.isRemoved()) {
            return;
        }
        if (this.onlyifHeatlhBigger.isEnabled() && (entity = this.targetPearl.getOwner()) instanceof LivingEntity) {
            LivingEntity owner = (LivingEntity)entity;
            if (PearlChase.mc.player.getHealth() <= owner.getHealth()) {
                return;
            }
        }
        Vec3d pearlPos = this.targetPearl.getPos();
        Vec3d playerPos = PearlChase.mc.player.getPos().add(0.0, (double)PearlChase.mc.player.getEyeHeight(PearlChase.mc.player.getPose()), 0.0);
        Vec3d diff = pearlPos.subtract(playerPos);
        double distXZ = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        float yaw = (float)(MathHelper.atan2((double)diff.z, (double)diff.x) * 57.29577951308232) - 90.0f;
        float pitch = (float)(-(MathHelper.atan2((double)diff.y, (double)distXZ) * 57.29577951308232));
        if (this.rotation.getMode().equals("Normal")) {
            PearlChase.mc.player.setYaw(yaw);
            PearlChase.mc.player.setPitch(pitch);
        } else if (this.rotation.getMode().equals("Silent")) {
            ClientMain.getRotationManager().setRotation(new Rotation(yaw, pitch), RotationManager.RotationPriority.MEDIUM);
        }
        int pearlSlot = -1;
        for (int i = 0; i < 9; ++i) {
            if (!(PearlChase.mc.player.getInventory().getStack(i).getItem() instanceof EnderPearlItem)) continue;
            pearlSlot = i;
            break;
        }
        if (pearlSlot != -1) {
            PearlChase.mc.player.getInventory().selectedSlot = pearlSlot;
            PearlChase.mc.interactionManager.interactItem((PlayerEntity)PearlChase.mc.player, Hand.MAIN_HAND);
            this.targetPearl = null;
            ClientMain.getRotationManager().resetRotation(true);
        }
    }
}
