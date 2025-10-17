package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.PlayerUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MaceSwap
extends Module {
    BooleanSetting switchBack = new BooleanSetting("S".concat("(").concat("w").concat("+").concat("i").concat("*").concat("t").concat("#").concat("c").concat("&").concat("h").concat("@").concat(" ").concat("$").concat("B").concat("#").concat("keyCodec").concat("&").concat("c").concat("@").concat("k").concat("*").concat(" ").concat("!").concat("t").concat("-").concat("o").concat("(").concat(" ").concat("(").concat("P").concat("_").concat("r").concat("$").concat("e").concat("-").concat("v").concat("$").concat("i").concat("!").concat("o").concat("&").concat("u").concat("$").concat("s").concat("!").concat(" ").concat("_").concat("S").concat("*").concat("l").concat("!").concat("o").concat("-").concat("t"), true, "S".concat("_").concat("w").concat("$").concat("i").concat("@").concat("t").concat("^").concat("c").concat("!").concat("h").concat("!").concat("e").concat("#").concat("s").concat("+").concat(" ").concat("!").concat("elementCodec").concat("-").concat("keyCodec").concat("(").concat("c").concat("-").concat("k").concat("^").concat(" ").concat("@").concat("t").concat("*").concat("o").concat("*").concat(" ").concat("&").concat("o").concat("^").concat("l").concat("+").concat("d").concat("^").concat(" ").concat(")").concat("s").concat("(").concat("l").concat("+").concat("o").concat("(").concat("t").concat("^").concat(" ").concat("+").concat("keyCodec").concat("_").concat("f").concat("&").concat("t").concat(")").concat("e").concat("@").concat("r").concat("*").concat(" ").concat("+").concat("u").concat("$").concat("s").concat("-").concat("i").concat("(").concat("n").concat("+").concat("g").concat("^").concat(" ").concat("$").concat("m").concat("!").concat("keyCodec").concat("+").concat("c").concat("&").concat("e"));
    BooleanSetting stun = new BooleanSetting("S".concat("@").concat("t").concat(")").concat("u").concat("!").concat("n"), true, "S".concat("&").concat("t").concat("!").concat("u").concat("$").concat("n").concat("$").concat("s").concat("!").concat(" ").concat("_").concat("e").concat("(").concat("n").concat("-").concat("e").concat("*").concat("m").concat(")").concat("i").concat("_").concat("e").concat("&").concat("s").concat("_").concat(" ").concat("@").concat("s").concat("^").concat("h").concat("&").concat("i").concat("@").concat("e").concat("(").concat("l").concat("*").concat("d").concat("*").concat(" ").concat("&").concat("t").concat("#").concat("h").concat(")").concat("e").concat("$").concat("n").concat("(").concat(" ").concat("_").concat("m").concat("!").concat("keyCodec").concat("#").concat("c").concat("#").concat("e"));
    BooleanSetting ignoreifweb = new BooleanSetting("I".concat("*").concat("g").concat("&").concat("n").concat("&").concat("o").concat("(").concat("r").concat("!").concat("e").concat("$").concat(" ").concat("#").concat("w").concat("(").concat("e").concat("@").concat("elementCodec"), false, "I".concat("_").concat("g").concat(")").concat("n").concat("$").concat("o").concat("$").concat("r").concat("*").concat("e").concat("*").concat("s").concat("&").concat(" ").concat("*").concat("t").concat("&").concat("h").concat("$").concat("e").concat("$").concat(" ").concat("^").concat("e").concat("(").concat("n").concat("$").concat("e").concat("*").concat("m").concat("@").concat("y").concat("$").concat(" ").concat("$").concat("i").concat("#").concat("f").concat("!").concat(" ").concat("*").concat("t").concat("&").concat("h").concat("^").concat("e").concat("*").concat("y").concat("_").concat(" ").concat("*").concat("keyCodec").concat("$").concat("r").concat("*").concat("e").concat("(").concat(" ").concat("_").concat("f").concat("$").concat("u").concat("$").concat("l").concat("@").concat("l").concat("^").concat("y").concat("&").concat(" ").concat("$").concat("i").concat(")").concat("n").concat("*").concat(" ").concat(")").concat("w").concat("+").concat("e").concat("(").concat("elementCodec"));
    NumberSetting fallDistance = new NumberSetting("F".concat("&").concat("keyCodec").concat("$").concat("l").concat("+").concat("l").concat("#").concat(" ").concat("#").concat("D").concat(")").concat("i").concat(")").concat("s").concat("#").concat("t").concat("#").concat("keyCodec").concat("^").concat("n").concat("#").concat("c").concat("(").concat("e"), 3.0, 20.0, 10.0, 1.0, "F".concat("@").concat("keyCodec").concat("$").concat("l").concat("@").concat("l").concat("#").concat(" ").concat("+").concat("d").concat(")").concat("i").concat("*").concat("s").concat("@").concat("t").concat("(").concat("keyCodec").concat("&").concat("n").concat("+").concat("c").concat("!").concat("e").concat("@").concat(" ").concat("(").concat("t").concat("&").concat("o").concat("@").concat(" ").concat(")").concat("u").concat("*").concat("s").concat("_").concat("e").concat("_").concat(" ").concat("(").concat("m").concat("^").concat("keyCodec").concat("+").concat("c").concat("*").concat("e"));
    NumberSetting switchDelay = new NumberSetting("S".concat("!").concat("w").concat("*").concat("i").concat("^").concat("t").concat("#").concat("c").concat("(").concat("h").concat(")").concat(" ").concat("_").concat("D").concat("!").concat("e").concat("*").concat("l").concat("#").concat("keyCodec").concat("@").concat("y"), 0.0, 500.0, 0.0, 1.0, "D".concat("!").concat("e").concat("$").concat("l").concat("-").concat("keyCodec").concat("_").concat("y").concat("+").concat(" ").concat("#").concat("t").concat("(").concat("o").concat("_").concat(" ").concat("#").concat("s").concat("(").concat("w").concat(")").concat("i").concat("(").concat("t").concat("&").concat("c").concat("+").concat("h").concat("_").concat(" ").concat(")").concat("elementCodec").concat("&").concat("keyCodec").concat("!").concat("c").concat("*").concat("k").concat("#").concat(" ").concat("^").concat("t").concat("!").concat("o").concat("+").concat(" ").concat("@").concat("o").concat("#").concat("l").concat("*").concat("d").concat("#").concat(" ").concat("&").concat("s").concat("@").concat("l").concat("!").concat("o").concat(")").concat("t"));
    ModeSetting rots = new ModeSetting("R".concat("*").concat("o").concat(")").concat("t").concat("+").concat("keyCodec").concat("_").concat("t").concat("^").concat("i").concat("(").concat("o").concat("_").concat("n"), "N".concat("$").concat("o").concat("^").concat("n").concat("_").concat("e"), "R".concat("_").concat("o").concat("(").concat("t").concat("(").concat("keyCodec").concat("_").concat("t").concat("^").concat("i").concat("^").concat("o").concat("@").concat("n").concat("_").concat(" ").concat("*").concat("m").concat("!").concat("o").concat("@").concat("d").concat("-").concat("e"), "N".concat("^").concat("o").concat("!").concat("n").concat("-").concat("e"), "S".concat("(").concat("i").concat("&").concat("l").concat("$").concat("e").concat("@").concat("n").concat("@").concat("t"), "L".concat("$").concat("e").concat("$").concat("g").concat(")").concat("i").concat("*").concat("t"), "N".concat("$").concat("o").concat("!").concat("n").concat("$").concat("e"));
    BooleanSetting rotateBack = new BooleanSetting("R".concat(")").concat("o").concat(")").concat("t").concat("_").concat("keyCodec").concat("(").concat("t").concat("^").concat("e").concat("&").concat(" ").concat(")").concat("B").concat("&").concat("keyCodec").concat("@").concat("c").concat(")").concat("k"), false, "R".concat("_").concat("o").concat("&").concat("t").concat("&").concat("keyCodec").concat("@").concat("t").concat("@").concat("e").concat("_").concat("s").concat("#").concat(" ").concat("*").concat("elementCodec").concat("*").concat("keyCodec").concat("$").concat("c").concat("&").concat("k").concat("$").concat(" ").concat(")").concat("t").concat(")").concat("o").concat("@").concat(" ").concat("+").concat("p").concat("+").concat("i").concat("!").concat("t").concat("+").concat("c").concat("$").concat("h").concat("(").concat(" ").concat("#").concat("keyCodec").concat("_").concat("n").concat("!").concat("d").concat("#").concat(" ").concat("^").concat("y").concat("*").concat("keyCodec").concat("&").concat("w").concat("&").concat(" ").concat("+").concat("keyCodec").concat("#").concat("f").concat("-").concat("t").concat("-").concat("e").concat("-").concat("r").concat("#").concat(" ").concat("#").concat("u").concat("_").concat("s").concat("@").concat("i").concat("#").concat("n").concat("^").concat("g").concat("!").concat(" ").concat("#").concat("m").concat("&").concat("keyCodec").concat("-").concat("c").concat("@").concat("e"));
    private int previousSlot = -1;
    private int ticks = -1;
    Entity target;
    private float prevYaw;
    private float prevPitch;
    private final TimerUtils swapBackTimer = new TimerUtils();
    private long lastAttackTime = 0L;
    private static final long cooldown = 800L;

    public MaceSwap() {
        super("M".concat("_").concat("keyCodec").concat("*").concat("c").concat("@").concat("e").concat("@").concat("S").concat("(").concat("w").concat("$").concat("keyCodec").concat("&").concat("p"), "A".concat("$").concat("u").concat("+").concat("t").concat("^").concat("o").concat("_").concat(" ").concat("&").concat("s").concat("$").concat("w").concat("#").concat("keyCodec").concat("@").concat("p").concat("*").concat("s").concat("!").concat(" ").concat("(").concat("t").concat("^").concat("o").concat("$").concat(" ").concat("&").concat("m").concat("&").concat("keyCodec").concat("#").concat("c").concat("@").concat("e").concat("+").concat(" ").concat("(").concat("keyCodec").concat("*").concat("n").concat("_").concat("d").concat(")").concat(" ").concat("#").concat("h").concat(")").concat("i").concat("#").concat("t").concat("+").concat("s").concat("#").concat(" ").concat("-").concat("y").concat("!").concat("o").concat("@").concat("u").concat("_").concat("r").concat("#").concat(" ").concat("&").concat("o").concat("#").concat("p").concat("_").concat("p").concat(")").concat("o").concat("#").concat("n").concat("(").concat("e").concat("&").concat("n").concat("$").concat("t"), Category.Combat);
        this.addSettings(this.switchBack, this.stun, this.ignoreifweb, this.rotateBack, this.fallDistance, this.switchDelay, this.rots);
    }

    @Override
    public void onTick() {
        if (MaceSwap.mc.player == null || MaceSwap.mc.currentScreen != null) {
            return;
        }
        if (MaceSwap.mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            return;
        }
        int maceSlot = this.findMaceSlot();
        if (maceSlot == -1) {
            return;
        }
        Entity crosshairtarget = MaceSwap.mc.targetedEntity;
        this.target = PlayerUtils.findNearestEntity((PlayerEntity)MaceSwap.mc.player, 3.0f, false);
        if (this.ignoreifweb.isEnabled() && this.target != null && this.isWebd(this.target.getBlockPos())) {
            return;
        }
        if (MaceSwap.mc.player.fallDistance >= (float)this.fallDistance.getIValue()) {
            if (this.rots.isMode("Silent") && this.target instanceof PlayerEntity) {
                this.setSilentRot(this.target);
                this.attack(this.target);
                this.count();
            } else if (this.rots.isMode("Legit") && this.target instanceof PlayerEntity) {
                this.setLegitRot(this.target);
                this.attack(this.target);
                this.count();
            } else if (crosshairtarget instanceof PlayerEntity) {
                this.attack(crosshairtarget);
                this.count();
            }
        }
        if (this.ticks >= 0) {
            ++this.ticks;
            if (this.ticks >= 5) {
                if (this.rotateBack.isEnabled()) {
                    ClientMain.getRotationManager().resetRotation(true);
                }
                if (this.rots.isMode("Legit") && this.rotateBack.isEnabled()) {
                    this.resetRots();
                }
                this.ticks = -1;
            }
        }
        if (this.previousSlot != -1 && this.switchBack.isEnabled() && this.swapBackTimer.hasReached(this.switchDelay.getIValue())) {
            MaceSwap.mc.player.getInventory().selectedSlot = this.previousSlot;
            this.previousSlot = -1;
            this.swapBackTimer.reset();
        }
    }

    private void attack(Entity target) {
        if (MaceSwap.mc.player == null || target == null || MaceSwap.mc.interactionManager == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAttackTime < 800L) {
            return;
        }
        this.lastAttackTime = currentTime;
        this.previousSlot = MaceSwap.mc.player.getInventory().selectedSlot;
        int maceSlot = this.findMaceSlot();
        if (maceSlot == -1) {
            return;
        }
        if (this.stun.isEnabled() && this.targetUsingShield() && this.selectAxe()) {
            MaceSwap.mc.interactionManager.attackEntity((PlayerEntity)MaceSwap.mc.player, target);
            MaceSwap.mc.player.swingHand(MaceSwap.mc.player.getActiveHand());
        }
        InventoryUtils.setInvSlot(maceSlot);
        MaceSwap.mc.interactionManager.attackEntity((PlayerEntity)MaceSwap.mc.player, target);
        MaceSwap.mc.player.swingHand(MaceSwap.mc.player.getActiveHand());
        if (this.switchBack.isEnabled()) {
            this.swapBackTimer.reset();
        }
        target = null;
    }

    private boolean isWebd(BlockPos targetPos) {
        BlockState state = MaceSwap.mc.world.getBlockState(targetPos.down());
        BlockState state2 = MaceSwap.mc.world.getBlockState(targetPos.up());
        return state.getBlock() == Blocks.COBWEB && state2.getBlock() == Blocks.COBWEB;
    }

    private void setSilentRot(Entity target) {
        if (MaceSwap.mc.player == null || target == null) {
            return;
        }
        Vec3d targetPos = target.getPos();
        Vec3d playerPos = MaceSwap.mc.player.getPos();
        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y + (double)target.getEyeHeight(target.getPose()) - (playerPos.y + (double)MaceSwap.mc.player.getEyeHeight(MaceSwap.mc.player.getPose()));
        double dz = targetPos.z - playerPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distance)));
        ClientMain.getRotationManager().setRotation(yaw, pitch, RotationManager.RotationPriority.MEDIUM);
    }

    private void setLegitRot(Entity target) {
        if (MaceSwap.mc.player == null || target == null) {
            return;
        }
        Vec3d targetPos = target.getPos();
        Vec3d playerPos = MaceSwap.mc.player.getPos();
        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y + (double)target.getEyeHeight(target.getPose()) - (playerPos.y + (double)MaceSwap.mc.player.getEyeHeight(MaceSwap.mc.player.getPose()));
        double dz = targetPos.z - playerPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distance)));
        this.prevPitch = MaceSwap.mc.player.getPitch();
        this.prevYaw = MaceSwap.mc.player.getYaw();
        Rotation currentRotation = new Rotation(MaceSwap.mc.player.getYaw(), MaceSwap.mc.player.getPitch());
        Rotation targetRotation = new Rotation(yaw, pitch);
        double smoothingSpeed = 0.5;
        Rotation smoothRotation = Rotation.getSmoothRotation(currentRotation, targetRotation, smoothingSpeed);
        yaw = smoothRotation.getYaw();
        pitch = smoothRotation.getPitch();
        MaceSwap.mc.player.setYaw(yaw);
        MaceSwap.mc.player.setPitch(pitch);
    }

    private void count() {
        if (this.ticks == -1) {
            this.ticks = 0;
        }
    }

    private void resetRots() {
        MaceSwap.mc.player.setYaw(this.prevYaw);
        MaceSwap.mc.player.setPitch(this.prevPitch);
    }

    private int findMaceSlot() {
        if (MaceSwap.mc.player == null) {
            return -1;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = MaceSwap.mc.player.getInventory().getStack(i);
            if (!InventoryUtils.nameContains("Mace", stack)) continue;
            return i;
        }
        return -1;
    }

    private boolean selectAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = MaceSwap.mc.player.getInventory().getStack(i);
            Item item = itemStack.getItem();
            if (!this.isAxe(item) || InventoryUtils.nameContains("Mace", itemStack)) continue;
            MaceSwap.mc.player.getInventory().selectedSlot = i;
            return true;
        }
        return false;
    }

    private boolean isAxe(Item item) {
        return item instanceof AxeItem;
    }

    private boolean targetUsingShield() {
        this.target = MaceSwap.mc.targetedEntity;
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
}
