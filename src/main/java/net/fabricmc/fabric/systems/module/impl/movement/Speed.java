package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.player.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public class Speed
extends Module {
    ModeSetting modeSetting = new ModeSetting("M".concat("(").concat("o").concat("!").concat("d").concat("(").concat("e"), "V".concat("*").concat("keyCodec").concat("$").concat("n").concat("(").concat("i").concat("-").concat("l").concat("_").concat("l").concat("^").concat("keyCodec"), "M".concat("-").concat("o").concat("!").concat("d").concat(")").concat("e").concat("@").concat(" ").concat("+").concat("o").concat("&").concat("f").concat("#").concat(" ").concat("@").concat("t").concat("+").concat("h").concat("$").concat("e").concat("#").concat(" ").concat("^").concat("S").concat("$").concat("p").concat("#").concat("e").concat("!").concat("e").concat("&").concat("d"), "V".concat("+").concat("u").concat("!").concat("l").concat("*").concat("c").concat("_").concat("keyCodec").concat("@").concat("n"), "N".concat("+").concat("C").concat("(").concat("P"), "G".concat("(").concat("r").concat("_").concat("i").concat("-").concat("m"), "V".concat("#").concat("e").concat("&").concat("r").concat("-").concat("u").concat("@").concat("s"));
    NumberSetting speed = new NumberSetting("S".concat("@").concat("p").concat("+").concat("e").concat("_").concat("e").concat("(").concat("d"), 0.1, 1.0, 0.8, 0.1, "S".concat("!").concat("p").concat("#").concat("e").concat("#").concat("e").concat("-").concat("d").concat("#").concat(" ").concat("!").concat("o").concat(")").concat("f").concat("^").concat(" ").concat("^").concat("t").concat("#").concat("h").concat(")").concat("e").concat("@").concat(" ").concat("!").concat("S").concat("+").concat("p").concat("&").concat("e").concat("@").concat("e").concat("@").concat("d"));
    private int jumpTicks = 0;
    private int ticks;
    private int offGroundTicks;
    TimerUtils timer = new TimerUtils();
    boolean boosting = false;

    public Speed() {
        super("S".concat("!").concat("p").concat("*").concat("e").concat("-").concat("e").concat(")").concat("d"), "M".concat("!").concat("keyCodec").concat("^").concat("k").concat("*").concat("e").concat("&").concat("s").concat("*").concat(" ").concat("(").concat("y").concat("(").concat("o").concat(")").concat("u").concat(")").concat(" ").concat("(").concat("m").concat("#").concat("o").concat("$").concat("v").concat("*").concat("e").concat("!").concat(" ").concat("#").concat("f").concat("-").concat("keyCodec").concat("-").concat("s").concat("$").concat("t").concat("#").concat("e").concat("+").concat("r"), Category.Movement);
        this.addSettings(this.modeSetting, this.speed);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.jumpTicks = 0;
        this.ticks = 0;
        this.ticks = 0;
        this.offGroundTicks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.jumpTicks = 0;
        this.ticks = 0;
    }

    @Override
    public void onTick() {
        if (this.modeSetting.isMode("Vulcan")) {
            Speed.mc.options.jumpKey.setPressed(false);
            if (Speed.mc.player.isOnGround() && MovementUtils.isMoving()) {
                this.ticks = 0;
                Speed.mc.player.jump();
                MovementUtils.strafe();
                if (MovementUtils.getSpeed() < 0.5f) {
                    MovementUtils.strafe(this.speed.getFloatValue());
                }
            }
            if (!Speed.mc.player.isOnGround()) {
                ++this.ticks;
            }
            if (this.ticks == 4) {
                Speed.mc.player.setVelocity(Speed.mc.player.getVelocity().getX(), Speed.mc.player.getVelocity().getY() - 0.17, Speed.mc.player.getVelocity().getZ());
            }
            if (this.ticks == 1) {
                MovementUtils.strafe(0.33f);
            }
        } else if (this.modeSetting.isMode("Grim") && MovementUtils.isMoving()) {
            int collisions = 0;
            for (Entity ent : Speed.mc.world.getEntities()) {
                if (ent == Speed.mc.player || !(ent instanceof LivingEntity) && !(ent instanceof BoatEntity) || !Speed.mc.player.getBoundingBox().expand(0.5).intersects(ent.getBoundingBox())) continue;
                ++collisions;
            }
            double[] motion = MovementUtils.forward(0.08 * (double)collisions);
            Speed.mc.player.addVelocity(motion[0], 0.0, motion[1]);
        } else if (this.modeSetting.isMode("NCP")) {
            if (Speed.mc.player.isOnGround()) {
                Speed.mc.player.jump();
                MovementUtils.strafe(0.485f);
            } else if ((double)Speed.mc.player.fallDistance > 0.6 && Speed.mc.player.fallDistance < 1.0f) {
                MovementUtils.setMotionY(-0.8);
            } else {
                MovementUtils.strafe(0.25f);
            }
        } else if (this.modeSetting.isMode("Verus")) {
            if (Speed.mc.player.isOnGround()) {
                Speed.mc.player.jump();
                MovementUtils.strafe(0.45f);
            } else {
                MovementUtils.strafe(0.28f);
            }
        }
    }
}
