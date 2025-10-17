package net.fabricmc.fabric.utils.player;

import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MovementUtils {
    public static boolean hasMovement() {
        Vec3d playerMovement = ClientMain.mc.player.getVelocity();
        return playerMovement.getX() != 0.0 || playerMovement.getY() != 0.0 || playerMovement.getZ() != 0.0;
    }

    public static double motionY(double motionY) {
        Vec3d vec3d = ClientMain.mc.player.getVelocity();
        ClientMain.mc.player.setVelocity(vec3d.x, motionY, vec3d.z);
        return motionY;
    }

    public static double motionYPlus(double motionY) {
        Vec3d vec3d = ClientMain.mc.player.getVelocity();
        ClientMain.mc.player.setVelocity(vec3d.x, vec3d.y + motionY, vec3d.z);
        return motionY;
    }

    public static double getDistanceToGround(Entity entity) {
        double playerX = ClientMain.mc.player.getX();
        int playerHeight = (int)Math.floor(ClientMain.mc.player.getY());
        double playerZ = ClientMain.mc.player.getZ();
        for (int height = playerHeight; height > 0; --height) {
            BlockPos checkPosition = new BlockPos((int)playerX, height, (int)playerZ);
            if (ClientMain.mc.world.isAir(checkPosition)) continue;
            return playerHeight - height;
        }
        return 0.0;
    }

    public static void setHorizontalVelocity(double speed) {
        if (ClientMain.mc.player.forwardSpeed != 0.0f || ClientMain.mc.player.sidewaysSpeed != 0.0f) {
            double yaw = Math.toRadians(ClientMain.mc.player.getYaw());
            double x = -Math.sin(yaw) * speed;
            double z = Math.cos(yaw) * speed;
            ClientMain.mc.player.setVelocity(x, ClientMain.mc.player.getVelocity().y, z);
        }
    }

    public static float getSpeed() {
        return (float)Math.sqrt(ClientMain.mc.player.getVelocity().x * ClientMain.mc.player.getVelocity().x + ClientMain.mc.player.getVelocity().z * ClientMain.mc.player.getVelocity().z);
    }

    public static float setSpeed(float speed) {
        if (ClientMain.mc.player == null) {
            return 0.0f;
        }
        ClientMain.mc.player.setVelocity(ClientMain.mc.player.getVelocity().x, 0.0, ClientMain.mc.player.getVelocity().z);
        ClientMain.mc.player.setVelocity((double)speed, 0.0, (double)speed);
        return MovementUtils.getSpeed();
    }

    public static void strafe() {
        MovementUtils.strafe(MovementUtils.getSpeed());
    }

    public static boolean test() {
        return ClientMain.mc.options.forwardKey.isPressed() || ClientMain.mc.options.backKey.isPressed() || ClientMain.mc.options.leftKey.isPressed() || ClientMain.mc.options.rightKey.isPressed();
    }

    public static void setMotionY(double y) {
        if (ClientMain.mc.player == null) {
            return;
        }
        ClientMain.mc.player.setVelocity(ClientMain.mc.player.getVelocity().x, y, ClientMain.mc.player.getVelocity().z);
    }

    public static boolean isMoving() {
        return ClientMain.mc.player != null && MovementUtils.test();
    }

    public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
        float rotationYawCalced = rotationYaw;
        if (moveForward < 0.0) {
            rotationYawCalced += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0) {
            forward = -0.5f;
        } else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYawCalced -= 90.0f * forward;
        }
        if (moveStrafing < 0.0) {
            rotationYawCalced += 90.0f * forward;
        }
        return Math.toRadians(rotationYawCalced);
    }

    public static void resetMotion(Boolean y) {
        ClientMain.mc.player.setVelocity(0.0, ClientMain.mc.player.getVelocity().y, 0.0);
        if (y.booleanValue()) {
            ClientMain.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
    }

    public static double[] forward(double multiplier) {
        if (ClientMain.mc.player == null) {
            return new double[]{0.0, 0.0};
        }
        double direction = MovementUtils.getDirection();
        double x = -Math.sin(direction) * multiplier;
        double z = Math.cos(direction) * multiplier;
        return new double[]{x, z};
    }

    public static void strafe(float speed) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        double direction = MovementUtils.getDirection();
        double x = -Math.sin(direction) * (double)speed;
        double z = Math.cos(direction) * (double)speed;
        Vec3d motion = new Vec3d(x, ClientMain.mc.player.getVelocity().y, z);
        ClientMain.mc.player.setVelocity(motion);
    }

    private static double getDirection() {
        double rotationYaw = MinecraftClient.getInstance().player.getYaw();
        if (MinecraftClient.getInstance().player.input.movementForward < 0.0f) {
            rotationYaw += 180.0;
        }
        double forward = 1.0;
        if (MinecraftClient.getInstance().player.input.movementForward < 0.0f) {
            forward = -0.5;
        } else if (MinecraftClient.getInstance().player.input.movementForward > 0.0f) {
            forward = 0.5;
        }
        if (MinecraftClient.getInstance().player.input.movementSideways > 0.0f) {
            rotationYaw -= 90.0 * forward;
        }
        if (MinecraftClient.getInstance().player.input.movementSideways < 0.0f) {
            rotationYaw += 90.0 * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static float getForward() {
        if (ClientMain.mc.player == null) {
            return 0.0f;
        }
        return ClientMain.mc.player.input.movementForward;
    }

    public static float getStrafe() {
        if (ClientMain.mc.player == null) {
            return 0.0f;
        }
        return ClientMain.mc.player.input.movementSideways;
    }
}
