package net.fabricmc.fabric.utils.math;

import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil {
    public static int compareDist(Entity entityA, Entity entityB) {
        return Float.compare(entityA.distanceTo((Entity)ClientMain.mc.player), entityB.distanceTo((Entity)ClientMain.mc.player));
    }

    public static Rotation getDir(Entity entity, Vec3d vec) {
        double dx = vec.x - entity.getX();
        double dy = vec.y - entity.getY();
        double dz = vec.z - entity.getZ();
        double dist = MathHelper.sqrt((float)((float)(dx * dx + dz * dz)));
        return new Rotation((float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0)), (float)(-MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(dy, dist)))));
    }

    public static boolean isInFOV(Entity entity, double angle) {
        angle *= 0.5;
        if (entity != null) {
            double angleDiff = MathUtil.getAngleDifference(ClientMain.mc.player.getYaw(), MathUtil.getRotations(entity)[0]);
            return angleDiff > 0.0 && angleDiff < angle || -angle < angleDiff && angleDiff < 0.0 && entity != null;
        }
        return false;
    }

    public static float getAngleDifference(float dir, float yaw) {
        float f;
        float dist = f = Math.abs(yaw - dir) % 360.0f;
        if (f > 180.0f) {
            dist = 360.0f - f;
        }
        return dist;
    }

    public static float[] getRotations(Entity ent) {
        double x = ent.getPos().getX();
        double y = ent.getPos().getY() + (double)ent.getEyeHeight(ent.getPose());
        double z = ent.getPos().getZ();
        return MathUtil.getRotationFromPosition(x, y, z);
    }

    public static float[] getRotationFromPosition(double x, double y, double z) {
        double xDiff = x - ClientMain.mc.player.getPos().getX();
        double yDiff = y - (ClientMain.mc.player.getPos().getY() + (double)ClientMain.mc.player.getEyeHeight(ClientMain.mc.player.getPose()));
        double zDiff = z - ClientMain.mc.player.getPos().getZ();
        double dist = MathHelper.hypot((double)xDiff, (double)zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = ClientMain.mc.player.getEyePos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapDegrees((float)yaw), MathHelper.wrapDegrees((float)pitch)};
    }

    public static double goodLerp(float delta, double start, double end) {
        int step = (int)Math.ceil(Math.abs(end - start) * (double)delta);
        if (start < end) {
            return Math.min(start + (double)step, end);
        }
        return Math.max(start - (double)step, end);
    }

    public static float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }

    public static double getAngleToLookVec(Vec3d vec) {
        float[] needed = MathUtil.getNeededRotations(vec);
        float currentYaw = MathHelper.wrapDegrees((float)ClientMain.mc.player.getYaw());
        float currentPitch = MathHelper.wrapDegrees((float)ClientMain.mc.player.getPitch());
        float diffYaw = currentYaw - needed[0];
        float diffPitch = currentPitch - needed[1];
        return Math.hypot(diffYaw, diffPitch);
    }

    public static float angleDiff(float a, float b) {
        float diff = ((b - a + 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f;
        return Math.abs(diff);
    }

    public static int getRandomInt(int from, int to) {
        if (from >= to) {
            return from;
        }
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    public static double getRandomDouble(double from, double to) {
        if (from >= to) {
            return from;
        }
        return ThreadLocalRandom.current().nextDouble(from, to);
    }

    public static double wrapAngleTo180(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }
}
