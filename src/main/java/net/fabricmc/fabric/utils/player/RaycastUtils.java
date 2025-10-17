package net.fabricmc.fabric.utils.player;

import java.util.function.Predicate;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public class RaycastUtils {
    @Nullable
    public static BlockHitResult raycastBlock(double maxDistance, RaycastContext.FluidHandling fluidHandling) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return null;
        }
        Vec3d eyePos = ClientMain.mc.player.getCameraPosVec(Utils.getTick());
        Vec3d lookVec = ClientMain.mc.player.getRotationVec(Utils.getTick()).normalize();
        Vec3d targetPos = eyePos.add(lookVec.multiply(maxDistance));
        return ClientMain.mc.world.raycast(new RaycastContext(eyePos, targetPos, RaycastContext.ShapeType.OUTLINE, fluidHandling, (Entity)ClientMain.mc.player));
    }

    @Nullable
    public static EntityHitResult raycastEntity(double maxDistance, Predicate<Entity> targetPredicate, boolean ignoreSelf, float precision) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return null;
        }
        Vec3d eyePos = ClientMain.mc.player.getCameraPosVec(Utils.getTick());
        Vec3d lookVec = ClientMain.mc.player.getRotationVec(Utils.getTick()).normalize();
        EntityHitResult bestHit = null;
        double bestDistance = maxDistance;
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            if (!targetPredicate.test(entity) || ignoreSelf && entity == ClientMain.mc.player) continue;
            Box expandedBox = entity.getBoundingBox().expand(0.1);
            float step = 0.0f;
            while ((double)step < maxDistance) {
                double distance;
                Vec3d checkPos = eyePos.add(lookVec.multiply((double)step));
                if (expandedBox.contains(checkPos) && (distance = eyePos.distanceTo(checkPos)) < bestDistance) {
                    bestHit = new EntityHitResult(entity, checkPos);
                    bestDistance = distance;
                }
                step += precision;
            }
        }
        return bestHit;
    }

    @Nullable
    public static HitResult raycast(double maxDistance, Predicate<Entity> entityPredicate, boolean ignoreSelf, RaycastContext.FluidHandling fluidHandling, float precision) {
        BlockHitResult blockHit = RaycastUtils.raycastBlock(maxDistance, fluidHandling);
        EntityHitResult entityHit = RaycastUtils.raycastEntity(maxDistance, entityPredicate, ignoreSelf, precision);
        if (blockHit == null && entityHit == null) {
            return null;
        }
        double blockDistance = blockHit == null ? Double.MAX_VALUE : ClientMain.mc.player.getEyePos().distanceTo(blockHit.getPos());
        double entityDistance = entityHit == null ? Double.MAX_VALUE : ClientMain.mc.player.getEyePos().distanceTo(entityHit.getPos());
        return blockDistance <= entityDistance ? blockHit : entityHit;
    }

    @Nullable
    public static EntityHitResult raycastWithPrediction(double maxDistance, Predicate<Entity> targetPredicate, boolean ignoreSelf, float predictionFactor, float precision) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return null;
        }
        Vec3d eyePos = ClientMain.mc.player.getCameraPosVec(Utils.getTick());
        Vec3d lookVec = ClientMain.mc.player.getRotationVec(Utils.getTick()).normalize();
        EntityHitResult bestHit = null;
        double bestDistance = maxDistance;
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            if (!targetPredicate.test(entity) || ignoreSelf && entity == ClientMain.mc.player) continue;
            Vec3d predictedPos = entity.getPos().add(entity.getVelocity().multiply((double)predictionFactor));
            Box predictedBox = new Box(predictedPos.subtract((double)(entity.getWidth() / 2.0f), 0.0, (double)(entity.getWidth() / 2.0f)), predictedPos.add((double)(entity.getWidth() / 2.0f), (double)entity.getHeight(), (double)(entity.getWidth() / 2.0f))).expand(0.1);
            float step = 0.0f;
            while ((double)step < maxDistance) {
                double distance;
                Vec3d checkPos = eyePos.add(lookVec.multiply((double)step));
                if (predictedBox.contains(checkPos) && (distance = eyePos.distanceTo(checkPos)) < bestDistance) {
                    bestHit = new EntityHitResult(entity, checkPos);
                    bestDistance = distance;
                }
                step += precision;
            }
        }
        return bestHit;
    }

    @Nullable
    public static EntityHitResult raycastIgnoringBlocks(double maxDistance, Predicate<Entity> targetPredicate, boolean ignoreSelf, float precision) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return null;
        }
        Vec3d eyePos = ClientMain.mc.player.getCameraPosVec(Utils.getTick());
        Vec3d lookVec = ClientMain.mc.player.getRotationVec(Utils.getTick()).normalize();
        EntityHitResult bestHit = null;
        double bestDistance = maxDistance;
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            if (!targetPredicate.test(entity) || ignoreSelf && entity == ClientMain.mc.player) continue;
            Box expandedBox = entity.getBoundingBox().expand(0.1);
            float step = 0.0f;
            while ((double)step < maxDistance) {
                double distance;
                Vec3d checkPos = eyePos.add(lookVec.multiply((double)step));
                if (expandedBox.contains(checkPos) && (distance = eyePos.distanceTo(checkPos)) < bestDistance) {
                    bestHit = new EntityHitResult(entity, checkPos);
                    bestDistance = distance;
                }
                step += precision;
            }
        }
        return bestHit;
    }

    private static boolean isBlockedByBlock(Vec3d start, Vec3d end) {
        BlockHitResult blockHit = ClientMain.mc.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)ClientMain.mc.player));
        return blockHit.getType() == HitResult.Type.BLOCK && blockHit.getPos().squaredDistanceTo(start) < start.squaredDistanceTo(end);
    }

    public static HitResult getHitResult(PlayerEntity entity, boolean ignoreInvisibles, float yaw, float pitch) {
        HitResult result = null;
        if (entity != null && ClientMain.mc.world != null) {
            double d = ClientMain.mc.player.getEntityInteractionRange();
            Vec3d cameraPosVec = entity.getCameraPosVec(Utils.getTick());
            Vec3d rotationVec = RaycastUtils.getPlayerLookVec(yaw, pitch);
            Vec3d range = cameraPosVec.add(rotationVec.x * d, rotationVec.y * d, rotationVec.z * d);

            result = ClientMain.mc.world.raycast(
                    new RaycastContext(cameraPosVec, range, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity)
            );

            boolean bl = d > 3.0;
            double e = d * d;

            if (result != null) {
                e = result.getPos().squaredDistanceTo(cameraPosVec);
            }

            Vec3d vec3d3 = cameraPosVec.add(rotationVec.x * d, rotationVec.y * d, rotationVec.z * d);
            Box box = entity.getBoundingBox().stretch(rotationVec.multiply(d)).expand(1.0, 1.0, 1.0);

            EntityHitResult entityHitResult = ProjectileUtil.raycast(
                    entity, cameraPosVec, vec3d3, box,
                    entityx -> !entityx.isSpectator() && entityx.canHit() && entityx.isInvisible() && !ignoreInvisibles,
                    e
            );

            if (entityHitResult != null) {
                Vec3d vec3d4 = entityHitResult.getPos();
                double g = cameraPosVec.squaredDistanceTo(vec3d4);

                if (bl && g > 9.0) {
                    result = BlockHitResult.createMissed(
                            vec3d4,
                            Direction.getFacing(rotationVec.x, rotationVec.y, rotationVec.z),
                            BlockPos.ofFloored(vec3d4)
                    );
                } else if (g < e || result == null) {
                    result = entityHitResult;
                }
            }
        }
        return result;
    }


    public static Vec3d getPlayerLookVec(float yaw, float pitch) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos((float)g);
        float i = MathHelper.sin((float)g);
        float j = MathHelper.cos((float)f);
        float k = MathHelper.sin((float)f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static Vec3d getPlayerLookVec(PlayerEntity player) {
        return RaycastUtils.getPlayerLookVec(player.getYaw(), player.getPitch());
    }

    public static boolean isLookingAt(Entity entity, double maxDistance, float precision) {
        EntityHitResult hitResult = RaycastUtils.raycastEntity(maxDistance, e -> e == entity, true, precision);
        return hitResult != null && hitResult.getEntity() == entity;
    }

    public static boolean canHitEntity(Entity entity, double maxDistance, float precision) {
        return RaycastUtils.raycastEntity(maxDistance, e -> e == entity, true, precision) != null;
    }

    public static Vec3d getSurfaceNormal(BlockHitResult hit) {
        Direction dir = hit.getSide();
        return new Vec3d((double)dir.getOffsetX(), (double)dir.getOffsetY(), (double)dir.getOffsetZ());
    }

    public static boolean isFacingBlock(BlockHitResult hit, float thresholdAngle) {
        Vec3d normal = RaycastUtils.getSurfaceNormal(hit);
        Vec3d lookVec = ClientMain.mc.player.getRotationVec(1.0f).normalize();
        double dot = lookVec.dotProduct(normal);
        return Math.toDegrees(Math.acos(dot)) <= (double)thresholdAngle;
    }
}
