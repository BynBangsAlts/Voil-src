package net.fabricmc.fabric.managers.rotation;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = Rotation.clampYaw(yaw);
        this.pitch = Rotation.clampPitch(pitch);
    }

    public float getYaw() {
        return (float)MathUtil.wrapAngleTo180(this.yaw);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void set(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation copy() {
        return new Rotation(this.yaw, this.pitch);
    }

    public float getYawDifference(Rotation other) {
        return Rotation.angleDiff(this.yaw, other.yaw);
    }

    public float getPitchDifference(Rotation other) {
        return Rotation.angleDiff(this.pitch, other.pitch);
    }

    public boolean isCloseTo(Rotation other, float tolerance) {
        return Math.abs(this.getYawDifference(other)) <= tolerance && Math.abs(this.getPitchDifference(other)) <= tolerance;
    }

    public static Rotation lerp(Rotation from, Rotation to, float speed) {
        float lerpedYaw = from.yaw + Rotation.clamp(Rotation.angleDiff(from.yaw, to.yaw), -speed, speed);
        float lerpedPitch = from.pitch + Rotation.clamp(Rotation.angleDiff(from.pitch, to.pitch), -speed, speed);
        return new Rotation(lerpedYaw, lerpedPitch);
    }

    public Rotation lerp(Rotation to, float speed) {
        return Rotation.lerp(this, to, speed);
    }

    public static Rotation getDirection(Entity entity, Vec3d vec) {
        double dx = vec.x - entity.getX();
        double dy = vec.y - entity.getY();
        double dz = vec.z - entity.getZ();
        double dist = MathHelper.sqrt((float)((float)(dx * dx + dz * dz)));
        return new Rotation((float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0)), (float)(-MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(dy, dist)))));
    }

    public static Rotation getDirection(Vec3d from, Vec3d vec) {
        double dx = vec.x - from.x;
        double dy = vec.y - from.y;
        double dz = vec.z - from.z;
        double dist = MathHelper.sqrt((float)((float)(dx * dx + dz * dz)));
        return new Rotation((float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0)), (float)(-MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(dy, dist)))));
    }

    public static Rotation getRotationTo(Vec3d from, Vec3d vec) {
        return Rotation.getDirection(from, vec);
    }

    public static float getTotalDifference(Rotation from, Rotation to) {
        return Math.abs(from.getYawDifference(to)) + Math.abs(from.getPitchDifference(to));
    }

    public static Rotation getSmoothRotation(Rotation from, Rotation to, double smoothingSpeed) {
        return Rotation.lerp(from, to, (float)smoothingSpeed);
    }

    private static float angleDiff(float from, float to) {
        float diff = ((to - from) % 360.0f + 360.0f) % 360.0f;
        return diff > 180.0f ? diff - 360.0f : diff;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clampYaw(float yaw) {
        if ((yaw %= 360.0f) >= 180.0f) {
            yaw -= 360.0f;
        }
        if (yaw < -180.0f) {
            yaw += 360.0f;
        }
        return yaw;
    }

    public static float clampPitch(float pitch) {
        return MathHelper.clamp((float)pitch, (float)-90.0f, (float)90.0f);
    }

    private Vec3d getDirectionVector(float yaw, float pitch) {
        double radYaw = Math.toRadians(yaw);
        double radPitch = Math.toRadians(pitch);
        double x = -Math.cos(radPitch) * Math.sin(radYaw);
        double y = -Math.sin(radPitch);
        double z = Math.cos(radPitch) * Math.cos(radYaw);
        return new Vec3d(x, y, z);
    }

    public static Rotation fixRots(Rotation prev, Rotation current) {
        return Rotation.fromArray(Rotation.gcd(Rotation.toArray(Rotation.getCappedRotations(prev, current)), Rotation.toArray(prev)));
    }

    public static Rotation getCappedRotations(Rotation prev, Rotation current) {
        float yawDiff = Rotation.getDelta(current.getYaw(), prev.getYaw());
        float cappedYaw = prev.getYaw() + yawDiff;
        float pitchDiff = Rotation.getDelta(current.getPitch(), prev.getPitch());
        float cappedPitch = prev.getPitch() + pitchDiff;
        return new Rotation(cappedYaw, cappedPitch);
    }

    public static float getDelta(float current, float previous) {
        return MathHelper.wrapDegrees((float)(current - previous));
    }

    public static float[] gcd(float[] rotations, float[] lastRotations) {
        float sensitivity = (float)((Double)ClientMain.mc.options.getMouseSensitivity().getValue() * (double)0.6f + (double)0.2f);
        float gcd = sensitivity * sensitivity * sensitivity * 1.2f;
        float deltaYaw = rotations[0] - lastRotations[0];
        float deltaPitch = rotations[1] - lastRotations[1];
        return new float[]{lastRotations[0] + (deltaYaw - deltaYaw % gcd), lastRotations[1] + (deltaPitch - deltaPitch % gcd)};
    }

    public static float[] toArray(Rotation rot) {
        return new float[]{rot.getYaw(), rot.getPitch()};
    }

    public static Rotation fromArray(float[] rotArray) {
        return new Rotation(rotArray[0], rotArray[1]);
    }

    public boolean canPlace(float yaw, float pitch) {
        Vec3d targetPos;
        Vec3d direction = this.getDirectionVector(yaw, pitch);
        Vec3d eyePos = ClientMain.mc.player.getEyePos();
        BlockHitResult hitResult = ClientMain.mc.world.raycast(new RaycastContext(eyePos, targetPos = eyePos.add(direction.multiply(3.0)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)ClientMain.mc.player));
        return hitResult != null && hitResult.getType() == HitResult.Type.BLOCK;
    }
}
