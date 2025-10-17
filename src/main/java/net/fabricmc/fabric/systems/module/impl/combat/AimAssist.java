package net.fabricmc.fabric.systems.module.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.WorldRenderEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.CurveSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.movement.Scaffold;
import net.fabricmc.fabric.utils.math.MathUtil;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.fabricmc.fabric.utils.world.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AimAssist
extends Module {
    private final NumberSetting getDistance = new NumberSetting("D".concat("+").concat("i").concat("+").concat("s").concat("&").concat("t").concat("*").concat("keyCodec").concat("(").concat("n").concat("@").concat("c").concat("&").concat("e"), 3.0, 10.0, 6.0, 0.1, "D".concat("@").concat("i").concat("^").concat("s").concat(")").concat("t").concat("@").concat("keyCodec").concat("_").concat("n").concat("(").concat("c").concat("(").concat("e").concat("$").concat(" ").concat("$").concat("t").concat("@").concat("o").concat("&").concat(" ").concat("#").concat("t").concat("*").concat("keyCodec").concat("$").concat("r").concat("_").concat("g").concat("@").concat("e").concat("$").concat("t"));
    private final NumberSetting2 yawSpeed = new NumberSetting2("Y".concat("+").concat("keyCodec").concat("!").concat("w"), 0.1, 10.0, 6.0, 8.0, 0.1, "Y".concat("#").concat("keyCodec").concat("-").concat("w").concat("#").concat(" ").concat("^").concat("s").concat("(").concat("p").concat("+").concat("e").concat("^").concat("e").concat("#").concat("d"));
    private final NumberSetting2 pitchSpeed = new NumberSetting2("P".concat("+").concat("i").concat("$").concat("t").concat("^").concat("c").concat("-").concat("h"), 0.1, 10.0, 6.0, 8.0, 0.1, "P".concat("(").concat("i").concat("@").concat("t").concat("*").concat("c").concat("+").concat("h").concat("&").concat(" ").concat("!").concat("s").concat("&").concat("p").concat("-").concat("e").concat("!").concat("e").concat("!").concat("d"));
    private final BooleanSetting seeOnly = new BooleanSetting("S".concat("$").concat("e").concat("&").concat("e").concat("#").concat("O").concat("&").concat("n").concat("+").concat("l").concat("#").concat("y"), true, "O".concat("+").concat("n").concat("*").concat("l").concat("^").concat("y").concat("@").concat(" ").concat("-").concat("keyCodec").concat(")").concat("i").concat("^").concat("m").concat("!").concat(" ").concat("@").concat("i").concat("+").concat("f").concat("+").concat(" ").concat("$").concat("y").concat("@").concat("o").concat("@").concat("u").concat("_").concat(" ").concat("^").concat("c").concat("(").concat("keyCodec").concat("^").concat("n").concat(")").concat(" ").concat("@").concat("s").concat("-").concat("e").concat("(").concat("e").concat("-").concat(" ").concat("$").concat("t").concat("-").concat("h").concat("*").concat("e").concat(")").concat(" ").concat("&").concat("t").concat("$").concat("keyCodec").concat("-").concat("r").concat("_").concat("g").concat("@").concat("e").concat("(").concat("t"));
    private final BooleanSetting yawAssist = new BooleanSetting("V".concat("*").concat("e").concat("&").concat("r").concat("*").concat("t").concat("-").concat("i").concat("-").concat("c").concat("!").concat("keyCodec").concat("$").concat("l"), true, "A".concat("@").concat("i").concat("$").concat("m").concat("_").concat(" ").concat("$").concat("v").concat("$").concat("e").concat("_").concat("r").concat("(").concat("t").concat("_").concat("i").concat("_").concat("c").concat("(").concat("keyCodec").concat("@").concat("l").concat(")").concat("l").concat("_").concat("y"));
    private final BooleanSetting pitchAssist = new BooleanSetting("H".concat("#").concat("o").concat("&").concat("r").concat("(").concat("i").concat("-").concat("z").concat(")").concat("o").concat("(").concat("n").concat("#").concat("t").concat("-").concat("keyCodec").concat("+").concat("l"), false, "A".concat("&").concat("i").concat("&").concat("m").concat("*").concat(" ").concat("(").concat("h").concat("$").concat("o").concat("*").concat("r").concat(")").concat("i").concat("-").concat("z").concat("+").concat("o").concat("(").concat("n").concat("^").concat("t").concat("!").concat("keyCodec").concat("+").concat("l").concat("^").concat("l").concat(")").concat("y"));
    private final BooleanSetting silentAim = new BooleanSetting("S".concat("&").concat("i").concat(")").concat("l").concat("+").concat("e").concat("!").concat("n").concat("&").concat("t").concat("@").concat("A").concat("(").concat("i").concat("(").concat("m"), false, "A".concat("#").concat("t").concat("@").concat("t").concat("+").concat("keyCodec").concat("&").concat("c").concat("-").concat("k").concat("^").concat("i").concat("+").concat("n").concat("#").concat("g").concat("!").concat(" ").concat("_").concat("w").concat("$").concat("i").concat("*").concat("t").concat("*").concat("h").concat("_").concat(" ").concat("^").concat("s").concat("&").concat("i").concat("@").concat("l").concat("+").concat("e").concat("_").concat("n").concat("$").concat("t").concat("*").concat(" ").concat("-").concat("r").concat("&").concat("o").concat("*").concat("t").concat("$").concat("keyCodec").concat("!").concat("t").concat("*").concat("i").concat("$").concat("o").concat("-").concat("n").concat("+").concat("s"));
    private final BooleanSetting weaponOnly = new BooleanSetting("W".concat("_").concat("e").concat("-").concat("keyCodec").concat("#").concat("p").concat("#").concat("o").concat(")").concat("n").concat("#").concat("O").concat("^").concat("n").concat("$").concat("l").concat("-").concat("y"), true, "W".concat("^").concat("e").concat("_").concat("keyCodec").concat("+").concat("p").concat("$").concat("o").concat("*").concat("n").concat(")").concat(" ").concat("$").concat("o").concat("#").concat("n").concat("(").concat("l").concat("*").concat("y").concat("-").concat(" ").concat("^").concat("keyCodec").concat("!").concat("i").concat("@").concat("m").concat("^").concat(" ").concat("_").concat("keyCodec").concat("$").concat("s").concat("^").concat("s").concat("!").concat("i").concat("_").concat("s").concat("@").concat("t"));
    private final BooleanSetting singleTarget = new BooleanSetting("S".concat("!").concat("i").concat("$").concat("n").concat("(").concat("g").concat("#").concat("l").concat("(").concat("e").concat("*").concat("T").concat("_").concat("keyCodec").concat("!").concat("r").concat("+").concat("g").concat("+").concat("e").concat("#").concat("t"), false, "L".concat("&").concat("o").concat("*").concat("c").concat("-").concat("k").concat("!").concat("s").concat("$").concat(" ").concat("-").concat("o").concat("!").concat("n").concat("@").concat(" ").concat("$").concat("t").concat("-").concat("o").concat("-").concat(" ").concat("*").concat("1").concat("(").concat(" ").concat("+").concat("t").concat("*").concat("keyCodec").concat(")").concat("r").concat("#").concat("g").concat("_").concat("e").concat("#").concat("t").concat("#").concat(" ").concat("@").concat("u").concat("*").concat("n").concat("*").concat("t").concat("&").concat("i").concat("&").concat("l").concat("-").concat("l").concat("&").concat(" ").concat("+").concat("t").concat("+").concat("h").concat("@").concat("e").concat("^").concat("y").concat("#").concat(" ").concat(")").concat("keyCodec").concat("*").concat("r").concat("(").concat("e").concat(")").concat(" ").concat("^").concat("o").concat("+").concat("u").concat("+").concat("t").concat("!").concat(" ").concat("^").concat("o").concat("$").concat("f").concat("@").concat(" ").concat("_").concat("r").concat("@").concat("keyCodec").concat("&").concat("n").concat("@").concat("g").concat("^").concat("e").concat("-").concat(" ").concat("&").concat("o").concat("*").concat("r").concat(")").concat(" ").concat("!").concat("d").concat("(").concat("e").concat("&").concat("keyCodec").concat("-").concat("d"));
    private final ModeSetting mode = new ModeSetting("M".concat("$").concat("o").concat("#").concat("d").concat("&").concat("e"), "C".concat("#").concat("l").concat("&").concat("o").concat("@").concat("s").concat("&").concat("e").concat("^").concat("s").concat(")").concat("t").concat("(").concat(" ").concat("!").concat("F").concat("!").concat("i").concat("#").concat("r").concat("-").concat("s").concat("_").concat("t"), "w".concat("-").concat("h").concat("(").concat("o").concat("@").concat(" ").concat("+").concat("t").concat("!").concat("o").concat("@").concat(" ").concat("#").concat("t").concat("#").concat("keyCodec").concat("+").concat("r").concat("$").concat("g").concat("+").concat("e").concat("-").concat("t").concat("$").concat("/").concat("#").concat("keyCodec").concat("(").concat("i").concat("_").concat("m"), "C".concat("+").concat("l").concat("^").concat("o").concat("^").concat("s").concat("&").concat("e").concat("(").concat("s").concat("(").concat("t").concat("*").concat(" ").concat("!").concat("F").concat("*").concat("i").concat("_").concat("r").concat("$").concat("s").concat("_").concat("t"), "A".concat("^").concat("t").concat("+").concat("t").concat("&").concat("keyCodec").concat("@").concat("c").concat("#").concat("k").concat("#").concat("e").concat("_").concat("d"), "H".concat("&").concat("i").concat("-").concat("g").concat("^").concat("h").concat("!").concat("e").concat(")").concat("s").concat("$").concat("t").concat("^").concat(" ").concat("^").concat("H").concat("&").concat("e").concat("_").concat("keyCodec").concat("-").concat("l").concat("&").concat("t").concat("&").concat("h"), "L".concat("&").concat("o").concat("^").concat("w").concat(")").concat("e").concat("!").concat("s").concat("_").concat("t").concat("-").concat(" ").concat(")").concat("H").concat("#").concat("e").concat("$").concat("keyCodec").concat(")").concat("l").concat("-").concat("t").concat("^").concat("h"));
    private final ModeSetting aimMode = new ModeSetting("A".concat("!").concat("i").concat("#").concat("m").concat("*").concat("M").concat("*").concat("o").concat("_").concat("d").concat("$").concat("e"), "C".concat("(").concat("u").concat("^").concat("r").concat("^").concat("v").concat("(").concat("e"), "A".concat("!").concat("i").concat("!").concat("m").concat("#").concat(" ").concat("_").concat("keyCodec").concat("#").concat("s").concat("_").concat("s").concat("@").concat("i").concat("@").concat("s").concat("_").concat("t").concat("#").concat(" ").concat("!").concat("m").concat("-").concat("o").concat("-").concat("d").concat("-").concat("e"), "C".concat("(").concat("u").concat("@").concat("r").concat("#").concat("v").concat(")").concat("e"), "L".concat("@").concat("i").concat("&").concat("n").concat("(").concat("e").concat("#").concat("keyCodec").concat("^").concat("r"));
    CurveSetting aimCurve = new CurveSetting("A".concat("(").concat("i").concat("(").concat("m").concat("-").concat("A").concat("&").concat("s").concat("@").concat("s").concat("(").concat("i").concat("(").concat("s").concat("_").concat("t").concat("!").concat(" ").concat("&").concat("C").concat("(").concat("u").concat("@").concat("r").concat("$").concat("v").concat("*").concat("e"), "C".concat("@").concat("u").concat("^").concat("r").concat("+").concat("v").concat("(").concat("e").concat(")").concat(" ").concat("^").concat("f").concat("-").concat("o").concat("^").concat("r").concat("*").concat(" ").concat("-").concat("keyCodec").concat("-").concat("d").concat("$").concat("j").concat("#").concat("u").concat("-").concat("s").concat("#").concat("t").concat("!").concat("i").concat(")").concat("n").concat("$").concat("g").concat("&").concat(" ").concat("_").concat("keyCodec").concat("$").concat("i").concat("*").concat("m").concat("+").concat(" ").concat("(").concat("keyCodec").concat("-").concat("s").concat("_").concat("s").concat("+").concat("i").concat("+").concat("s").concat("#").concat("t").concat("@").concat(" ").concat("*").concat("s").concat("+").concat("p").concat("^").concat("e").concat("-").concat("e").concat("!").concat("d"), new CurveSetting.Point(0.0f, 0.0f), new CurveSetting.Point(0.25f, 0.4f), new CurveSetting.Point(0.5f, 0.7f), new CurveSetting.Point(0.75f, 0.6f), new CurveSetting.Point(1.0f, 0.0f));
    private PlayerEntity firstAttackedPlayer = null;
    public static UUID firstAttackedPlayerUUID = null;
    private PlayerEntity targetPlayer = null;
    private boolean canAttack;

    public AimAssist() {
        super("A".concat("^").concat("i").concat("+").concat("m").concat("(").concat(" ").concat("+").concat("A").concat("$").concat("s").concat("_").concat("s").concat("-").concat("i").concat("!").concat("s").concat(")").concat("t"), "A".concat("$").concat("i").concat("&").concat("m").concat("&").concat("s").concat("#").concat(" ").concat("+").concat("f").concat("!").concat("o").concat("@").concat("r").concat("(").concat(" ").concat("#").concat("y").concat(")").concat("o").concat("$").concat("u"), Category.Combat);
        this.addSettings(this.getDistance, this.yawSpeed, this.pitchSpeed, this.seeOnly, this.yawAssist, this.pitchAssist, this.silentAim, this.singleTarget, this.mode, this.aimMode, this.weaponOnly, this.aimCurve);
        Setting.dependSetting(this.aimCurve, "C".concat("!").concat("u").concat("!").concat("r").concat("-").concat("v").concat("@").concat("e"), this.aimMode);
        Setting.dependSetting(this.yawSpeed, "L".concat("$").concat("i").concat("@").concat("n").concat("!").concat("e").concat("*").concat("keyCodec").concat("$").concat("r"), this.aimMode);
        Setting.dependSetting(this.pitchSpeed, "L".concat("@").concat("i").concat("(").concat("n").concat("+").concat("e").concat("-").concat("keyCodec").concat("-").concat("r"), this.aimMode);
    }

    @Override
    public void onDisable() {
        this.firstAttackedPlayer = null;
        firstAttackedPlayerUUID = null;
        ClientMain.getRotationManager().resetRotation(true);
    }

    @EventHandler
    public void onWorldRenderEnd(WorldRenderEvent e) {
        float newPitch;
        float newYaw;
        if (!this.isEnabled()) {
            ClientMain.getRotationManager().resetRotation(false);
            return;
        }
        if (AimAssist.mc.currentScreen != null || this.isHoldingFirework() || this.isOverEntity() || this.weaponOnly.isEnabled() && !this.isHoldingWeapon()) {
            return;
        }
        List<PlayerEntity> players = EntityUtils.findEntities(PlayerEntity.class).stream().filter(p -> p.squaredDistanceTo((Entity)AimAssist.mc.player) <= Math.pow(this.getDistance.getFloatValue(), 2.0)).filter(p -> !SocialManager.isBot(p)).filter(p -> !SocialManager.isFriend(p.getUuid())).filter(p -> !SocialManager.isTeammate(p.getUuid())).filter(p -> p != AimAssist.mc.player).filter(p -> !this.seeOnly.isEnabled() || AimAssist.mc.player.canSee((Entity)p)).toList();
        if (this.singleTarget.isEnabled() && this.targetPlayer != null) {
            if (!this.targetPlayer.isAlive() || this.targetPlayer.squaredDistanceTo((Entity)AimAssist.mc.player) > Math.pow(this.getDistance.getFloatValue(), 2.0)) {
                this.targetPlayer = null;
            }
        } else {
            switch (this.mode.getMode()) {
                case "Lowest Health": {
                    PlayerEntity playerEntity = players.stream().min(Comparator.comparingDouble(p -> p.getHealth())).orElse(null);
                    break;
                }
                case "Highest Health": {
                    PlayerEntity playerEntity = players.stream().max(Comparator.comparingDouble(p -> p.getHealth())).orElse(null);
                    break;
                }
                case "Closest First": {
                    PlayerEntity playerEntity = players.stream().min(Comparator.comparingDouble(p -> p.squaredDistanceTo((Entity)AimAssist.mc.player))).orElse(null);
                    break;
                }
                default: {
                    PlayerEntity playerEntity = this.targetPlayer = null;
                }
            }
        }
        if (this.targetPlayer == null) {
            ClientMain.getRotationManager().resetRotation(false);
            return;
        }
        Rotation targetRot = MathUtil.getDir((Entity)AimAssist.mc.player, this.targetPlayer.getPos());
        float yawDiff = MathUtil.angleDiff(AimAssist.mc.player.getYaw(), targetRot.getYaw());
        float pitchDiff = MathUtil.angleDiff(AimAssist.mc.player.getPitch(), targetRot.getPitch());
        float diffMagnitude = (float)Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
        float t = MathHelper.clamp((float)(diffMagnitude / 180.0f), (float)0.0f, (float)1.0f);
        if (this.aimMode.isMode("Curve")) {
            float assistStrength = this.aimCurve.evaluate(t) / 50.0f;
            newYaw = MathHelper.lerpAngleDegrees((float)assistStrength, (float)AimAssist.mc.player.getYaw(), (float)targetRot.getYaw());
            newPitch = MathHelper.lerpAngleDegrees((float)assistStrength, (float)AimAssist.mc.player.getPitch(), (float)targetRot.getPitch());
        } else {
            float smoothFactorYaw = MathHelper.clamp((float)(this.yawSpeed.getFloatValue() / 50.0f), (float)0.0f, (float)1.0f);
            float smoothFactorPitch = MathHelper.clamp((float)(this.pitchSpeed.getFloatValue() / 50.0f), (float)0.0f, (float)1.0f);
            newYaw = MathHelper.lerpAngleDegrees((float)smoothFactorYaw, (float)AimAssist.mc.player.getYaw(), (float)targetRot.getYaw());
            newPitch = MathHelper.lerpAngleDegrees((float)smoothFactorPitch, (float)AimAssist.mc.player.getPitch(), (float)targetRot.getPitch());
        }
        if (!this.targetPlayer.isAlive()) {
            this.targetPlayer = null;
            ClientMain.getRotationManager().resetRotation(false);
            return;
        }
        if (this.targetPlayer.squaredDistanceTo((Entity)AimAssist.mc.player) > Math.pow(this.getDistance.getFloatValue(), 2.0)) {
            this.targetPlayer = null;
            ClientMain.getRotationManager().resetRotation(false);
            return;
        }
        if (this.weaponOnly.isEnabled() && !this.isHoldingWeapon()) {
            ClientMain.getRotationManager().resetRotation(false);
            return;
        }
        boolean scaffolding = ClientMain.getRotationManager().isRotating(Scaffold.class);
        if (this.silentAim.isEnabled() && !scaffolding) {
            Rotation rot = this.getBestAimPosition(this.targetPlayer);
            ClientMain.getRotationManager().setRotation(rot, RotationManager.RotationPriority.HIGH);
            this.canAttack = this.canAttackTarget(this.targetPlayer);
        } else {
            if (this.yawAssist.isEnabled()) {
                AimAssist.mc.player.setYaw(newYaw);
            }
            if (this.pitchAssist.isEnabled()) {
                AimAssist.mc.player.setPitch(newPitch);
            }
        }
    }

    public Rotation getBestAimPosition(PlayerEntity target) {
        Vec3d targetPosition = target.getEyePos();
        Vec3d playerPosition = AimAssist.mc.player.getEyePos();
        Vec3d direction = targetPosition.subtract(playerPosition).normalize();
        float targetYaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
        float targetPitch = (float)(-Math.toDegrees(Math.asin(direction.y)));
        targetYaw = MathHelper.wrapDegrees((float)targetYaw);
        targetPitch = MathHelper.clamp((float)targetPitch, (float)-90.0f, (float)90.0f);
        Rotation currentRotation = ClientMain.getRotationManager().getServerRotation();
        float smoothFactorYaw = MathHelper.clamp((float)(this.yawSpeed.getFloatValue() / 10.0f), (float)0.0f, (float)1.0f);
        float smoothFactorPitch = MathHelper.clamp((float)(this.pitchSpeed.getFloatValue() / 10.0f), (float)0.0f, (float)1.0f);
        if (this.aimMode.isMode("Curve")) {
            float curveStrength = this.aimCurve.evaluate(1.0f);
            smoothFactorYaw *= curveStrength;
            smoothFactorPitch *= curveStrength;
        }
        float smoothedYaw = MathHelper.lerp((float)smoothFactorYaw, (float)currentRotation.getYaw(), (float)targetYaw);
        float smoothedPitch = MathHelper.lerp((float)smoothFactorPitch, (float)currentRotation.getPitch(), (float)targetPitch);
        smoothedYaw = MathHelper.clamp((float)smoothedYaw, (float)-180.0f, (float)180.0f);
        return new Rotation(smoothedYaw, smoothedPitch);
    }

    private PlayerEntity getAttackedPlayer(List<PlayerEntity> players) {
        if (this.firstAttackedPlayer != null && (!this.firstAttackedPlayer.isAlive() || this.firstAttackedPlayer.squaredDistanceTo((Entity)AimAssist.mc.player) > Math.pow(this.getDistance.getFloatValue(), 2.0))) {
            this.firstAttackedPlayer = null;
        }
        if (this.firstAttackedPlayer == null) {
            this.firstAttackedPlayer = players.stream().filter(p -> p.getUuid().equals(firstAttackedPlayerUUID)).findFirst().orElse(null);
        }
        return this.firstAttackedPlayer;
    }

    private boolean canAttackTarget(PlayerEntity target) {
        EntityHitResult entityHit;
        Rotation serverRotation = ClientMain.getRotationManager().getServerRotation();
        HitResult hitResult = RaycastUtils.getHitResult((PlayerEntity)AimAssist.mc.player, false, serverRotation.getYaw(), serverRotation.getPitch());
        return hitResult instanceof EntityHitResult && (entityHit = (EntityHitResult)hitResult).getEntity().equals((Object)target);
    }

    private boolean isHoldingWeapon() {
        ItemStack heldItem = AimAssist.mc.player.getMainHandStack();
        return heldItem.getItem() instanceof SwordItem || heldItem.getItem() instanceof ToolItem;
    }

    private boolean isHoldingFirework() {
        ItemStack heldItem = AimAssist.mc.player.getMainHandStack();
        return heldItem.getItem() instanceof FireworkRocketItem;
    }

    private boolean isOverEntity() {
        return AimAssist.mc.crosshairTarget instanceof EntityHitResult;
    }
}
