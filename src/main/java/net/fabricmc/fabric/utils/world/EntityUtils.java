package net.fabricmc.fabric.utils.world;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtils {
    public static boolean isDeadBodyNearby() {
        return ClientMain.mc.world.getPlayers()
                .parallelStream()
                .filter(e -> e != ClientMain.mc.player)
                .filter(e -> e.squaredDistanceTo(ClientMain.mc.player) <= 36.0)
                .anyMatch(LivingEntity::isDead);
    }

    public static <T extends Entity> List<T> findEntities(Class<T> entityClass) {
        ArrayList<T> entities = new ArrayList<>();
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            if (entity.equals(ClientMain.mc.player) || !entityClass.isAssignableFrom(entity.getClass())) continue;
            entities.add((T) entity);
        }
        return entities;
    }



    public static PlayerEntity findPlayerByUUID(UUID uuid) {
        return MinecraftClient.getInstance().world.getPlayers().stream().filter(p -> p.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public static <T extends Entity> T findClosest(Class<T> entityClass, float range) {
        for (Entity entity : ClientMain.mc.world.getEntities()) {
            if (!entityClass.isAssignableFrom(entity.getClass()) || entity.equals((Object)ClientMain.mc.player) || !(entity.distanceTo((Entity)ClientMain.mc.player) <= range)) continue;
            return (T)entity;
        }
        return null;
    }

    public static boolean isAnimal(Entity e) {
        return e instanceof PassiveEntity || e instanceof AmbientEntity || e instanceof WaterCreatureEntity || e instanceof IronGolemEntity || e instanceof SnowGolemEntity;
    }
}
