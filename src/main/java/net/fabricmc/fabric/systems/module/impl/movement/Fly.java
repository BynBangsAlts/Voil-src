package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class Fly
extends Module {
    private final NumberSetting speed = new NumberSetting("S".concat("!").concat("p").concat("$").concat("e").concat("$").concat("e").concat("-").concat("d"), 0.01, 50.0, 5.0, 1.0, "M".concat("_").concat("o").concat("&").concat("v").concat("$").concat("e").concat("$").concat("m").concat("+").concat("e").concat("#").concat("n").concat("_").concat("t").concat("_").concat(" ").concat("(").concat("s").concat("&").concat("p").concat("@").concat("e").concat("-").concat("e").concat("&").concat("d").concat("#").concat(" ").concat("!").concat("o").concat("&").concat("f").concat("*").concat(" ").concat("*").concat("t").concat("^").concat("h").concat("+").concat("e").concat("+").concat(" ").concat("$").concat("elementCodec").concat("#").concat("o").concat("#").concat("keyCodec").concat("!").concat("t"));
    private final NumberSetting verticalSpeed = new NumberSetting("V".concat(")").concat("e").concat("*").concat("r").concat("*").concat("t").concat("$").concat("i").concat("*").concat("c").concat("^").concat("keyCodec").concat("*").concat("l").concat("-").concat("S").concat("#").concat("p").concat("&").concat("e").concat("(").concat("e").concat("+").concat("d"), 0.01, 5.0, 0.2, 0.01, "V".concat("^").concat("e").concat("-").concat("r").concat("#").concat("t").concat("-").concat("i").concat("_").concat("c").concat("+").concat("keyCodec").concat(")").concat("l").concat("^").concat(" ").concat("^").concat("s").concat("$").concat("p").concat("#").concat("e").concat("+").concat("e").concat(")").concat("d").concat("_").concat(" ").concat("!").concat("w").concat("(").concat("h").concat("*").concat("i").concat("_").concat("l").concat("$").concat("e").concat("(").concat(" ").concat("^").concat("f").concat("*").concat("l").concat("(").concat("y").concat("!").concat("i").concat("(").concat("n").concat("+").concat("g"));
    private final NumberSetting glideFactor = new NumberSetting("G".concat(")").concat("l").concat(")").concat("i").concat(")").concat("d").concat("$").concat("e").concat("+").concat("F").concat("(").concat("keyCodec").concat("$").concat("c").concat("-").concat("t").concat("^").concat("o").concat(")").concat("r"), 0.0, 1.0, 0.95, 0.01, "G".concat("#").concat("l").concat("$").concat("i").concat(")").concat("d").concat("-").concat("e").concat("*").concat(" ").concat(")").concat("f").concat("^").concat("keyCodec").concat(")").concat("c").concat("-").concat("t").concat("_").concat("o").concat("@").concat("r").concat("-").concat(" ").concat("(").concat("f").concat("^").concat("o").concat("$").concat("r").concat("!").concat(" ").concat(")").concat("s").concat("!").concat("m").concat("@").concat("o").concat(")").concat("o").concat("#").concat("t").concat("^").concat("h").concat("*").concat("e").concat("@").concat("r").concat("*").concat(" ").concat(")").concat("m").concat("-").concat("o").concat("-").concat("v").concat("*").concat("e").concat(")").concat("m").concat("^").concat("e").concat("+").concat("n").concat("@").concat("t"));
    private double fixedY;

    public Fly() {
        super("F".concat("!").concat("l").concat("+").concat("y"), "A".concat("_").concat("l").concat("!").concat("l").concat("*").concat("o").concat("_").concat("w").concat("+").concat("s").concat("*").concat(" ").concat("!").concat("y").concat("#").concat("o").concat("$").concat("u").concat("-").concat(" ").concat(")").concat("t").concat("#").concat("o").concat("_").concat(" ").concat("*").concat("f").concat("+").concat("l").concat("_").concat("y").concat("*").concat(" ").concat("(").concat("w").concat("&").concat("i").concat(")").concat("t").concat("+").concat("h").concat("@").concat(" ").concat("_").concat("y").concat("^").concat("o").concat("&").concat("u").concat("#").concat("r").concat("!").concat(" ").concat("+").concat("elementCodec").concat("+").concat("o").concat("!").concat("keyCodec").concat("-").concat("t"), Category.Movement);
        this.addSettings(this.speed, this.verticalSpeed, this.glideFactor);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Fly.mc.player == null || !(Fly.mc.player.getVehicle() instanceof BoatEntity)) {
            this.toggle();
            return;
        }
        this.fixedY = Fly.mc.player.getY();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Fly.mc.player != null && Fly.mc.player.getVehicle() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity)Fly.mc.player.getVehicle();
            boat.setVelocity(Vec3d.ZERO);
        }
    }

    @EventHandler
    public void onUpdate(EventUpdate e) {
        if (Fly.mc.player == null || !(Fly.mc.player.getVehicle() instanceof BoatEntity)) {
            return;
        }
        this.handleBoatFly();
    }

    private void handleBoatFly() {
        BoatEntity boat = (BoatEntity)Fly.mc.player.getVehicle();
        if (boat == null) {
            return;
        }
        double speedValue = this.speed.getFloatValue();
        double verticalSpeedValue = this.verticalSpeed.getFloatValue();
        double glideFactorValue = this.glideFactor.getFloatValue();
        Vec3d velocity = boat.getVelocity();
        boolean forward = Fly.mc.options.forwardKey.isPressed();
        boolean backward = Fly.mc.options.backKey.isPressed();
        boolean left = Fly.mc.options.leftKey.isPressed();
        boolean right = Fly.mc.options.rightKey.isPressed();
        boolean up = Fly.mc.options.jumpKey.isPressed();
        boolean down = Fly.mc.options.sneakKey.isPressed();
        Vec3d inputVelocity = Vec3d.ZERO;
        if (forward) {
            inputVelocity = inputVelocity.add(boat.getRotationVector().multiply(speedValue));
        }
        if (backward) {
            inputVelocity = inputVelocity.subtract(boat.getRotationVector().multiply(speedValue));
        }
        if (left) {
            inputVelocity = inputVelocity.add(boat.getRotationVector().rotateY((float)Math.toRadians(90.0)).multiply(speedValue));
        }
        if (right) {
            inputVelocity = inputVelocity.add(boat.getRotationVector().rotateY((float)Math.toRadians(-90.0)).multiply(speedValue));
        }
        if (up) {
            inputVelocity = inputVelocity.add(0.0, verticalSpeedValue, 0.0);
        }
        if (down) {
            inputVelocity = inputVelocity.subtract(0.0, verticalSpeedValue, 0.0);
        }
        Vec3d newVelocity = velocity.multiply(glideFactorValue).add(inputVelocity.multiply(1.0 - glideFactorValue));
        boat.setVelocity(newVelocity);
    }
}
