package net.fabricmc.fabric.systems.module.impl.combat;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AutoCrossbow
extends Module {
    BooleanSetting autoswitch = new BooleanSetting("A".concat("+").concat("u").concat("&").concat("t").concat("*").concat("o").concat("&").concat(" ").concat("*").concat("S").concat("_").concat("w").concat(")").concat("i").concat("(").concat("t").concat("#").concat("c").concat("!").concat("h"), false, "A".concat("!").concat("u").concat("!").concat("t").concat("-").concat("o").concat("#").concat("m").concat("!").concat("keyCodec").concat("!").concat("t").concat("!").concat("i").concat("-").concat("c").concat("&").concat("keyCodec").concat("*").concat("l").concat(")").concat("l").concat("+").concat("y").concat("&").concat(" ").concat("&").concat("s").concat("!").concat("w").concat("&").concat("i").concat("&").concat("t").concat(")").concat("c").concat("(").concat("h").concat("-").concat("e").concat("+").concat("s").concat("&").concat(" ").concat("$").concat("t").concat("_").concat("o").concat("+").concat(" ").concat("$").concat("c").concat("!").concat("r").concat("^").concat("o").concat("_").concat("s").concat("$").concat("s").concat("!").concat("elementCodec").concat("^").concat("o").concat("&").concat("w").concat("!").concat(" ").concat("^").concat("w").concat("#").concat("h").concat("+").concat("e").concat("^").concat("n").concat("(").concat(" ").concat("&").concat("n").concat("+").concat("o").concat("+").concat("t").concat("#").concat(" ").concat("@").concat("i").concat("^").concat("n").concat("!").concat(" ").concat("_").concat("c").concat("#").concat("r").concat("@").concat("o").concat("#").concat("s").concat("^").concat("s").concat("!").concat("elementCodec").concat("!").concat("o").concat("@").concat("w"));
    BooleanSetting switchBack = new BooleanSetting("S".concat("@").concat("w").concat("+").concat("i").concat("(").concat("t").concat("!").concat("c").concat("(").concat("h").concat("&").concat(" ").concat("@").concat("B").concat("@").concat("keyCodec").concat("!").concat("c").concat("_").concat("k"), false, "S".concat("^").concat("w").concat(")").concat("i").concat("_").concat("t").concat("+").concat("c").concat("^").concat("h").concat("$").concat("e").concat("_").concat("s").concat(")").concat(" ").concat("#").concat("elementCodec").concat("(").concat("keyCodec").concat("!").concat("c").concat("&").concat("k").concat("@").concat(" ").concat("@").concat("t").concat("@").concat("o").concat("$").concat(" ").concat("*").concat("o").concat("(").concat("l").concat("_").concat("d").concat("$").concat(" ").concat("^").concat("s").concat("^").concat("l").concat("#").concat("o").concat("-").concat("t").concat("&").concat(" ").concat("*").concat("keyCodec").concat("-").concat("f").concat("+").concat("t").concat("@").concat("e").concat("$").concat("r").concat("-").concat(" ").concat("-").concat("s").concat("@").concat("h").concat("^").concat("o").concat("@").concat("o").concat("(").concat("t").concat("_").concat("i").concat("$").concat("n").concat("&").concat("g"));
    NumberSetting switchDelay = new NumberSetting("S".concat("(").concat("w").concat("$").concat("i").concat("(").concat("t").concat("_").concat("c").concat("^").concat("h").concat(")").concat(" ").concat("&").concat("D").concat("-").concat("e").concat("*").concat("l").concat("@").concat("keyCodec").concat("^").concat("y"), 0.0, 800.0, 300.0, 1.0, "D".concat("#").concat("e").concat("+").concat("l").concat("^").concat("keyCodec").concat("+").concat("y").concat("-").concat(" ").concat("@").concat("t").concat("(").concat("o").concat("_").concat(" ").concat("+").concat("s").concat("*").concat("w").concat("@").concat("i").concat("^").concat("t").concat("@").concat("c").concat("#").concat("h").concat("$").concat(" ").concat("*").concat("t").concat("!").concat("o").concat("^").concat(" ").concat("!").concat("c").concat("(").concat("r").concat("*").concat("o").concat("#").concat("s").concat("(").concat("s").concat("-").concat("elementCodec").concat("*").concat("o").concat("(").concat("w"));
    NumberSetting raycastDistance = new NumberSetting("R".concat("*").concat("keyCodec").concat("-").concat("y").concat("(").concat("c").concat("!").concat("keyCodec").concat(")").concat("s").concat("$").concat("t").concat("!").concat(" ").concat("@").concat("D").concat("*").concat("i").concat("^").concat("s").concat("-").concat("t").concat("+").concat("keyCodec").concat("^").concat("n").concat(")").concat("c").concat("-").concat("e"), 1.0, 50.0, 20.0, 1.0, "M".concat("@").concat("keyCodec").concat("^").concat("x").concat(")").concat("i").concat("^").concat("m").concat("_").concat("u").concat("$").concat("m").concat("&").concat(" ").concat("(").concat("d").concat("$").concat("i").concat("^").concat("s").concat("^").concat("t").concat("_").concat("keyCodec").concat("!").concat("n").concat("(").concat("c").concat("@").concat("e").concat("(").concat(" ").concat("$").concat("f").concat("_").concat("o").concat("_").concat("r").concat("$").concat(" ").concat("^").concat("r").concat("+").concat("keyCodec").concat("*").concat("y").concat("-").concat("c").concat("!").concat("keyCodec").concat(")").concat("s").concat("@").concat("t"));
    TimerUtils timer = new TimerUtils();
    public int slot = -1;

    public AutoCrossbow() {
        super("A".concat("(").concat("u").concat("!").concat("t").concat(")").concat("o").concat("@").concat("C").concat("@").concat("r").concat("+").concat("o").concat("$").concat("s").concat("_").concat("s").concat("(").concat("elementCodec").concat("(").concat("o").concat("$").concat("w"), "A".concat("*").concat("u").concat("!").concat("t").concat("-").concat("o").concat("-").concat("m").concat("&").concat("keyCodec").concat("_").concat("t").concat("*").concat("i").concat(")").concat("c").concat(")").concat("keyCodec").concat("$").concat("l").concat("+").concat("l").concat(")").concat("y").concat("#").concat(" ").concat("*").concat("s").concat("#").concat("h").concat("&").concat("o").concat("@").concat("o").concat("+").concat("t").concat("+").concat("s").concat("&").concat(" ").concat("$").concat("c").concat("@").concat("r").concat("@").concat("o").concat("&").concat("s").concat("_").concat("s").concat(")").concat("elementCodec").concat("&").concat("o").concat("$").concat("w"), Category.Combat);
        this.addSettings(this.autoswitch, this.switchBack, this.switchDelay, this.raycastDistance);
    }

    @Override
    public void onTick() {
        boolean hasCrossbow;
        if (AutoCrossbow.mc.player == null || AutoCrossbow.mc.world == null) {
            return;
        }
        boolean bl = hasCrossbow = AutoCrossbow.mc.player.getMainHandStack().getItem() instanceof CrossbowItem || AutoCrossbow.mc.player.getOffHandStack().getItem() instanceof CrossbowItem;
        if (!hasCrossbow && this.autoswitch.isEnabled()) {
            this.switchToCrossbow();
            return;
        }
        PlayerEntity targetPlayer = this.findPlayerInRaycast(180, this.raycastDistance.getValue());
        if (targetPlayer != null) {
            this.lookAtEntity((Entity)targetPlayer);
            if (this.timer.hasReached(this.switchDelay.getValue())) {
                this.shootCrossbow();
                if (this.switchBack.isEnabled() && this.slot != -1) {
                    InventoryUtils.swap(AutoCrossbow.mc.player.getInventory().getStack(this.slot).getItem());
                    this.slot = -1;
                }
                this.timer.reset();
            }
        }
    }

    private PlayerEntity findPlayerInRaycast(int degrees, double distance) {
        List<HitResult> results = AutoCrossbow.raycast(degrees, distance);
        for (HitResult result : results) {
            Entity entity;
            if (result.getType() != HitResult.Type.ENTITY || !((entity = ((EntityHitResult)result).getEntity()) instanceof PlayerEntity) || entity == AutoCrossbow.mc.player) continue;
            return (PlayerEntity)entity;
        }
        return null;
    }

    private void lookAtEntity(Entity entity) {
        Vec3d eyes = AutoCrossbow.mc.player.getEyePos();
        Vec3d targetPos = entity.getEyePos();
        double dx = targetPos.x - eyes.x;
        double dy = targetPos.y - eyes.y;
        double dz = targetPos.z - eyes.z;
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distanceXZ)));
        AutoCrossbow.mc.player.setYaw(yaw);
        AutoCrossbow.mc.player.setPitch(pitch);
    }

    public static List<HitResult> raycast(int degrees, double distance) {
        ArrayList<HitResult> results = new ArrayList<HitResult>();
        if (AutoCrossbow.mc.player == null || AutoCrossbow.mc.world == null) {
            return results;
        }
        Vec3d eyePos = AutoCrossbow.mc.player.getCameraPosVec(1.0f);
        double step = 360.0 / (double)degrees;
        for (double angle = 0.0; angle < (double)degrees; angle += step) {
            double radian = Math.toRadians(angle + (double)AutoCrossbow.mc.player.getYaw());
            Vec3d direction = new Vec3d(Math.cos(radian), 0.0, Math.sin(radian)).normalize();
            Vec3d targetPos = eyePos.add(direction.multiply(distance));
            BlockHitResult result = AutoCrossbow.mc.world.raycast(new RaycastContext(eyePos, targetPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)AutoCrossbow.mc.player));
            results.add((HitResult)result);
        }
        return results;
    }

    private void switchToCrossbow() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = AutoCrossbow.mc.player.getInventory().getStack(i);
            if (!(itemStack.getItem() instanceof CrossbowItem)) continue;
            this.slot = AutoCrossbow.mc.player.getInventory().selectedSlot;
            AutoCrossbow.mc.player.getInventory().selectedSlot = i;
            break;
        }
    }

    private void shootCrossbow() {
        if (AutoCrossbow.mc.player.getMainHandStack().getItem() instanceof CrossbowItem) {
            AutoCrossbow.mc.interactionManager.interactItem((PlayerEntity)AutoCrossbow.mc.player, Hand.MAIN_HAND);
        } else if (AutoCrossbow.mc.player.getOffHandStack().getItem() instanceof CrossbowItem) {
            AutoCrossbow.mc.interactionManager.interactItem((PlayerEntity)AutoCrossbow.mc.player, Hand.OFF_HAND);
        }
    }
}
