package net.fabricmc.fabric.utils.world;

import java.util.Objects;
import java.util.stream.Stream;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class WorldUtils {
    private static Vec3d previousPosition = Vec3d.ZERO;
    private static Vec3d lastVelocity = Vec3d.ZERO;

    public static boolean isDeadBodyNearby() {
        return ClientMain.mc.world.getPlayers().parallelStream().filter(e -> e != ClientMain.mc.player).filter(e -> e.squaredDistanceTo((Entity)ClientMain.mc.player) <= 36.0).anyMatch(LivingEntity::isDead);
    }

    public static Entity findNearestEntity(PlayerEntity toPlayer, float radius, boolean seeOnly) {
        float mr = Float.MAX_VALUE;
        Entity entity = null;
        assert (ClientMain.mc.world != null);
        for (Entity e : ClientMain.mc.world.getEntities()) {
            float d = e.distanceTo((Entity)toPlayer);
            if (e == toPlayer || !(d <= radius) || ClientMain.mc.player.canSee(e) != seeOnly || !(d < mr)) continue;
            mr = d;
            entity = e;
        }
        return entity;
    }

    public static Vec3d predictTargetPosition(PlayerEntity target, float timeFactor) {
        Vec3d currentPosition = target.getPos();
        Vec3d currentVelocity = currentPosition.subtract(previousPosition);
        Vec3d acceleration = currentVelocity.subtract(lastVelocity);
        boolean isSneaking = target.isSneaking();
        boolean isBlocking = target.isBlocking();
        boolean isJumping = !target.isOnGround();
        float movementFactor = 1.0f;
        if (isSneaking || isBlocking) {
            movementFactor *= 0.3f;
        }
        if (isJumping) {
            movementFactor *= 1.2f;
        }
        Vec3d futurePosition = currentPosition.add(currentVelocity.multiply((double)(timeFactor * movementFactor)).add(acceleration.multiply(0.5 * (double)timeFactor * (double)timeFactor)));
        previousPosition = currentPosition;
        lastVelocity = currentVelocity;
        return futurePosition;
    }

    @NotNull
    public static Vec3d worldToScreen(@NotNull Vec3d pos) {
        Camera camera = ClientMain.mc.getEntityRenderDispatcher().camera;
        int displayHeight = ClientMain.mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv((int)2978, (int[])viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;
        Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul((Matrix4fc)RenderHelper.getPositionMatrix());
        Matrix4f matrixProj = new Matrix4f((Matrix4fc)RenderHelper.getProjectionMatrix());
        Matrix4f matrixModel = new Matrix4f((Matrix4fc)RenderHelper.getModelViewMatrix());
        matrixProj.mul((Matrix4fc)matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new Vec3d((double)target.x / ClientMain.mc.getWindow().getScaleFactor(), (double)((float)displayHeight - target.y) / ClientMain.mc.getWindow().getScaleFactor(), (double)target.z);
    }

    public static void placeBlock(BlockHitResult blockHit, boolean swingHand) {
        ActionResult result = ClientMain.mc.interactionManager.interactBlock(ClientMain.mc.player, Hand.MAIN_HAND, blockHit);
        if (result.isAccepted() && result.shouldSwingHand() && swingHand) {
            ClientMain.mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static Stream<WorldChunk> getLoadedChunks() {
        int radius = Math.max(2, ClientMain.mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;
        ChunkPos center = ClientMain.mc.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);
        Stream<WorldChunk> stream = Stream.iterate(min, pos -> {
            int x = pos.x;
            int z = pos.z;
            if (++x > max.x) {
                x = min.x;
                ++z;
            }
            if (z > max.z) {
                throw new IllegalStateException("Stream limit didn't work.");
            }
            return new ChunkPos(x, z);
        }).limit(diameter * diameter).filter(c -> ClientMain.mc.world.isChunkLoaded(c.x, c.z)).map(c -> ClientMain.mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
        return stream;
    }

    public static boolean isShieldFacingAway(PlayerEntity player) {
        if (ClientMain.mc.player != null && player != null) {
            Vec3d playerPos = ClientMain.mc.player.getPos();
            Vec3d targetPos = player.getPos();
            Vec3d directionToPlayer = playerPos.subtract(targetPos).normalize();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Vec3d facingDirection = new Vec3d(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)), -Math.sin(Math.toRadians(pitch)), Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))).normalize();
            double dotProduct = facingDirection.dotProduct(directionToPlayer);
            return dotProduct < 0.0;
        }
        return false;
    }

    public static boolean canHit(PlayerEntity player) {
        if (ClientMain.mc.player != null && player != null) {
            Vec3d normalize = ClientMain.mc.player.getPos().subtract(player.getPos()).normalize();
            float getYaw = player.getYaw();
            float getPitch = player.getPitch();
            return new Vec3d(-Math.sin(Math.toRadians(getYaw)) * Math.cos(Math.toRadians(getPitch)), -Math.sin(Math.toRadians(getPitch)), Math.cos(Math.toRadians(getYaw)) * Math.cos(Math.toRadians(getPitch))).normalize().dotProduct(normalize) < 0.0;
        }
        return false;
    }

    public static BlockPos calcTrajectory(float yaw) {
        double x = Render2DEngine.interpolate(ClientMain.mc.player.prevX, ClientMain.mc.player.getX(), Utils.getTick());
        double y = Render2DEngine.interpolate(ClientMain.mc.player.prevY, ClientMain.mc.player.getY(), Utils.getTick());
        double z = Render2DEngine.interpolate(ClientMain.mc.player.prevZ, ClientMain.mc.player.getZ(), Utils.getTick());
        y = y + (double)ClientMain.mc.player.getEyeHeight(ClientMain.mc.player.getPose()) - 0.1000000014901161;
        double motionX = -MathHelper.sin((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.cos((float)(ClientMain.mc.player.getPitch() / 180.0f * (float)Math.PI));
        double motionY = -MathHelper.sin((float)(ClientMain.mc.player.getPitch() / 180.0f * 3.141593f));
        double motionZ = MathHelper.cos((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.cos((float)(ClientMain.mc.player.getPitch() / 180.0f * (float)Math.PI));
        float power = (float)ClientMain.mc.player.getItemUseTime() / 20.0f;
        if ((power = (power * power + power * 2.0f) / 3.0f) > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.sqrt((float)((float)(motionX * motionX + motionY * motionY + motionZ * motionZ)));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        float pow = power * 3.0f;
        motionX *= (double)pow;
        motionY *= (double)pow;
        motionZ *= (double)pow;
        if (!ClientMain.mc.player.isOnGround()) {
            motionY += ClientMain.mc.player.getVelocity().getY();
        }
        for (int i = 0; i < 300; ++i) {
            Vec3d lastPos = new Vec3d(x, y, z);
            motionX *= 0.99;
            motionY *= 0.99;
            Vec3d pos = new Vec3d(x += motionX, y += (motionY -= (double)0.05f), z += (motionZ *= 0.99));
            for (Entity ent : ClientMain.mc.world.getEntities()) {
                if (ent instanceof ArrowEntity || ent.equals((Object)ClientMain.mc.player) || !ent.getBoundingBox().intersects(new Box(x - 0.3, y - 0.3, z - 0.3, x + 0.3, y + 0.3, z + 0.3))) continue;
                return null;
            }
            BlockHitResult bhr = ClientMain.mc.world.raycast(new RaycastContext(lastPos, pos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)ClientMain.mc.player));
            if (bhr != null && bhr.getType() == HitResult.Type.BLOCK) {
                return bhr.getBlockPos();
            }
            if (y <= -65.0) break;
        }
        return null;
    }

    public static boolean isCrit(PlayerEntity player, Entity target) {
        return player.getAttackCooldownProgress(0.5f) > 0.9f && player.fallDistance > 0.0f && !player.isOnGround() && !player.isClimbing() && !player.isSubmergedInWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && target instanceof LivingEntity;
    }

    public static BlockState getBlockState(BlockPos pos) {
        return ClientMain.mc.world.getBlockState(pos);
    }

    public static boolean isBlock(Block block, BlockPos pos) {
        return WorldUtils.getBlockState(pos).getBlock() == block;
    }

    public static int getEntityPing(PlayerEntity entity) {
        if (ClientMain.mc.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry playerListEntry = ClientMain.mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }

    public static void hitEntity(Entity entity, boolean swingHand) {
        ClientMain.mc.interactionManager.attackEntity((PlayerEntity)ClientMain.mc.player, entity);
        if (swingHand) {
            ClientMain.mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static boolean useItem(Item item) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return false;
        }
        if (!ClientMain.mc.player.getMainHandStack().isOf(item)) {
            return false;
        }
        return ClientMain.mc.interactionManager.interactItem((PlayerEntity)ClientMain.mc.player, Hand.MAIN_HAND) == ActionResult.SUCCESS;
    }

    public static boolean placeBlock(Item item, BlockPos pos) {
        if (ClientMain.mc.player == null || ClientMain.mc.world == null) {
            return false;
        }
        InventoryUtils.swap(item);
        BlockState state = ClientMain.mc.world.getBlockState(pos);
        if (!(state.isAir() || state.getFluidState().isEmpty() || state.getBlock().getDefaultState().canPlaceAt((WorldView)ClientMain.mc.world, pos))) {
            return false;
        }
        BlockHitResult hitResult = new BlockHitResult(new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), Direction.UP, pos, false);
        return ClientMain.mc.interactionManager.interactBlock(ClientMain.mc.player, Hand.MAIN_HAND, hitResult) == ActionResult.SUCCESS;
    }
}
