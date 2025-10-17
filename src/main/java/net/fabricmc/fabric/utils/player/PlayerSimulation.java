package net.fabricmc.fabric.utils.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class PlayerSimulation {
    private final PlayerEntity player;
    private Vec3d predictedPosition;

    public PlayerSimulation(PlayerEntity player) {
        this.player = player;
        this.predictedPosition = player.getPos();
    }

    public void tick(int ticks) {
        if (this.player == null) {
            return;
        }
        Vec3d velocity = this.player.getVelocity();
        Vec3d position = this.player.getPos();
        Vec3d inputMovement = this.getMovement();
        velocity = velocity.add(inputMovement);
        if (!this.player.isOnGround()) {
            velocity = velocity.add(0.0, -0.08, 0.0);
        }
        velocity = this.friction(velocity);
        this.predictedPosition = position.add(velocity);
        for (int i = 0; i < ticks; ++i) {
            velocity = velocity.add(this.getMovement());
            if (!this.player.isOnGround()) {
                velocity = velocity.add(0.0, -0.08, 0.0);
            }
            velocity = this.friction(velocity);
            this.predictedPosition = this.predictedPosition.add(velocity);
        }
    }

    private Vec3d getMovement() {
        Vec3d velocity = this.player.getVelocity();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        if (speed == 0.0) {
            return Vec3d.ZERO;
        }
        double x = velocity.x / speed * speed;
        double z = velocity.z / speed * speed;
        return new Vec3d(x, 0.0, z);
    }

    private Vec3d friction(Vec3d velocity) {
        double friction = this.player.isOnGround() ? 0.91 : 0.98;
        return velocity.multiply(friction);
    }

    public Vec3d getPredictedPosition() {
        return this.predictedPosition;
    }
}
