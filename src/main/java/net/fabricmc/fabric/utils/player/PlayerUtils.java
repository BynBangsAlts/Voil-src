package net.fabricmc.fabric.utils.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerUtils {
    public static JsonObject friends = new JsonObject();

    public static boolean isFriend(PlayerEntity player) {
        return friends.get(player.getUuidAsString()) != null;
    }

    public static void addFriend(PlayerEntity player) {
        if (!PlayerUtils.isFriend(player)) {
            friends.add(player.getUuidAsString(), (JsonElement)new JsonPrimitive(player.getName().getString()));
        }
    }

    public static void removeFriend(PlayerEntity player) {
        if (PlayerUtils.isFriend(player)) {
            friends.remove(player.getUuidAsString());
        }
    }

    public static PlayerEntity findNearestPlayer(PlayerEntity toPlayer, float range, boolean seeOnly) {
        float minRange = Float.MAX_VALUE;
        PlayerEntity minPlayer = null;
        for (PlayerEntity player : ClientMain.mc.world.getPlayers()) {
            if (PlayerUtils.isFriend(player)) continue;
            float distance = player.distanceTo((Entity)toPlayer);
            if (player.getName().equals((Object)toPlayer.getName()) || player == toPlayer || !(distance <= range) || seeOnly && !toPlayer.canSee((Entity)player) || !(distance < minRange)) continue;
            minRange = distance;
            minPlayer = player;
        }
        return minPlayer;
    }

    public static PlayerEntity getLookedAtPlayer() {
        Entity entity;
        if (ClientMain.mc.crosshairTarget != null && ClientMain.mc.crosshairTarget.getType() == HitResult.Type.ENTITY && (entity = ((EntityHitResult)ClientMain.mc.crosshairTarget).getEntity()) instanceof PlayerEntity) {
            return (PlayerEntity)entity;
        }
        return null;
    }

    public static boolean canCrit(PlayerEntity pe, boolean closeGroundCheck) {
        if (pe == null) {
            return false;
        }
        if (closeGroundCheck) {
            BlockHitResult bhr = RaycastUtils.raycastBlock(10.0, RaycastContext.FluidHandling.NONE);
            double y = pe.getY() - bhr.getPos().getY();
            if (y <= 0.15) {
                return false;
            }
        }
        return !pe.hasStatusEffect(StatusEffects.BLINDNESS) && !pe.isClimbing() && !pe.isTouchingWater() && !pe.hasVehicle() && !pe.isOnGround() && pe.fallDistance > 0.065f && (double)pe.getAttackCooldownProgress(0.5f) >= 0.7 && pe.getMovement().lengthSquared() < 1.0E-7;
    }

    public static float squaredDistanceFromEyes(@NotNull Vec3d targetPos) {
        if (ClientMain.mc.player == null) {
            return 0.0f;
        }
        double dx = targetPos.x - ClientMain.mc.player.getX();
        double dy = targetPos.y - (ClientMain.mc.player.getY() + (double)ClientMain.mc.player.getEyeHeight(ClientMain.mc.player.getPose()));
        double dz = targetPos.z - ClientMain.mc.player.getZ();
        return (float)(dx * dx + dy * dy + dz * dz);
    }

    public static LivingEntity findNearestEntity(PlayerEntity toPlayer, float range, boolean seeOnly) {
        float minRange = Float.MAX_VALUE;
        LivingEntity minEntity = null;
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            PlayerEntity player;
            LivingEntity livingEntity;
            if (!(entity instanceof LivingEntity) || (livingEntity = (LivingEntity)entity).isDead() || entity instanceof PlayerEntity && PlayerUtils.isFriend(player = (PlayerEntity)entity)) continue;
            float distance = entity.distanceTo((Entity)toPlayer);
            if (entity == toPlayer || !(distance <= range) || seeOnly && !toPlayer.canSee(entity) || !(distance < minRange)) continue;
            minRange = distance;
            minEntity = livingEntity;
        }
        return minEntity;
    }

    public static <T extends Entity> List<T> findNearestEntities(Class<T> findEntity, PlayerEntity toPlayer, float range, boolean seeOnly) {
        ArrayList<T> entities = new ArrayList<>();
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            PlayerEntity player;
            if (!findEntity.isAssignableFrom(entity.getClass()) || entity instanceof PlayerEntity && PlayerUtils.isFriend(player = (PlayerEntity)entity))
                continue;
            float distance = entity.distanceTo(toPlayer);
            if (entity == toPlayer || !(distance <= range) || seeOnly && !toPlayer.canSee(entity))
                continue;
            entities.add((T) entity);
        }
        return entities;
    }


    @Nullable
    public static ItemStack findShield(PlayerEntity player) {
        ItemStack result = null;
        for (Hand hand : Hand.values()) {
            ItemStack handStack = player.getStackInHand(hand);
            if (!handStack.isOf(Items.SHIELD)) continue;
            result = handStack;
        }
        return result;
    }

    public static float getShieldCooldownProgress(PlayerEntity player) {
        ItemStack shieldStack = PlayerUtils.findShield(player);
        if (shieldStack == null) {
            return 0.0f;
        }
        return player.getItemCooldownManager().getCooldownProgress(shieldStack.getItem(), Utils.getTick());
    }

    public static float getItemCooldownProgress(PlayerEntity player, ItemStack itemStack) {
        return player.getItemCooldownManager().getCooldownProgress(itemStack.getItem(), Utils.getTick());
    }

    public static float getItemCooldownProgress(PlayerEntity player, Hand hand) {
        return player.getItemCooldownManager().getCooldownProgress(player.getStackInHand(hand).getItem(), Utils.getTick());
    }

    public static float getItemCooldownProgress(Hand hand) {
        return ClientMain.mc.player.getItemCooldownManager().getCooldownProgress(ClientMain.mc.player.getStackInHand(hand).getItem(), Utils.getTick());
    }

    public static void attackEntity(Entity entity) {
        ClientMain.mc.interactionManager.attackEntity((PlayerEntity)ClientMain.mc.player, entity);
        ClientMain.mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static GameMode getGameMode(PlayerEntity player) {
        if (player == null) {
            return null;
        }
        PlayerListEntry playerListEntry = ClientMain.mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return null;
        }
        return playerListEntry.getGameMode();
    }

    @Nullable
    public static HitResult getHitResult(PlayerEntity entity, boolean ignoreInvisibles, float yaw, float pitch, double distance) {
        if (entity == null || ClientMain.mc.world == null) {
            return null;
        }

        Vec3d cameraPosVec = entity.getCameraPosVec(Utils.getTick());
        Vec3d lookVec = RaycastUtils.getPlayerLookVec(yaw, pitch);
        Vec3d reachVec = cameraPosVec.add(lookVec.multiply(distance));

        BlockHitResult blockHit = ClientMain.mc.world.raycast(new RaycastContext(
                cameraPosVec,
                reachVec,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                entity
        ));

        double maxDistanceSq = distance * distance;
        if (blockHit != null) {
            maxDistanceSq = blockHit.getPos().squaredDistanceTo(cameraPosVec);
        }

        Box searchBox = entity.getBoundingBox().stretch(lookVec.multiply(distance)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHit = ProjectileUtil.raycast(
                entity,
                cameraPosVec,
                reachVec,
                searchBox,
                e -> !(e instanceof PlayerEntity) && !e.isSpectator() && e.canHit() && (!e.isInvisible() || !ignoreInvisibles),
                maxDistanceSq
        );

        Vec3d hitPos;
        if (entityHit != null) {
            hitPos = entityHit.getPos();
            double entityDistSq = cameraPosVec.squaredDistanceTo(hitPos);

            if (entityDistSq < maxDistanceSq) {
                return entityHit;
            } else {
                if (blockHit != null) {
                    return blockHit;
                } else {
                    return BlockHitResult.createMissed(
                            hitPos,
                            Direction.getFacing(lookVec.x, lookVec.y, lookVec.z),
                            BlockPos.ofFloored(hitPos)
                    );
                }
            }
        }

        return blockHit != null ? blockHit :
                BlockHitResult.createMissed(
                        cameraPosVec.add(lookVec.multiply(distance)),
                        Direction.getFacing(lookVec.x, lookVec.y, lookVec.z),
                        BlockPos.ofFloored(cameraPosVec.add(lookVec.multiply(distance)))
                );
    }

    public static boolean isBlockedByShield(LivingEntity attacker, LivingEntity shielder, boolean instaBlock) {
        if (attacker == null || shielder == null) {
            return false;
        }
        if (shielder.isBlocking() || instaBlock && shielder.getActiveItem().isOf(Items.SHIELD)) {
            Vec3d vec3d2 = shielder.getRotationVec(1.0f);
            Vec3d vec3d3 = attacker.getPos().relativize(shielder.getPos());
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();
            return vec3d3.dotProduct(vec3d2) < 0.0;
        }
        return false;
    }
}
