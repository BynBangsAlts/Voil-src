package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AttackEntityEvent
        extends Cancellable {
    public Entity target;
    public PlayerEntity player;

    public Entity getTarget() {
        return this.target;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public static class Post
            extends AttackEntityEvent {
        private static final Post INSTANCE = new Post();

        public static Post get(PlayerEntity player, Entity target) {
            INSTANCE.setCancelled(false);
            Post.INSTANCE.player = player;
            Post.INSTANCE.target = target;
            return INSTANCE;
        }
    }

    public static class Pre
            extends AttackEntityEvent {
        private static final Pre INSTANCE = new Pre();

        public static Pre get(PlayerEntity player, Entity target) {
            INSTANCE.setCancelled(false);
            Pre.INSTANCE.player = player;
            Pre.INSTANCE.target = target;
            return INSTANCE;
        }
    }
}
