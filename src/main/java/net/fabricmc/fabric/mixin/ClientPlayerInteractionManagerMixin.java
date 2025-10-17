package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin {
    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void preAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (ClientMain.EVENTBUS.post(AttackEntityEvent.Pre.get(player, target)).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"attackEntity"}, at={@At(value="TAIL")})
    private void postAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        ClientMain.EVENTBUS.post(AttackEntityEvent.Post.get(player, target));
    }
}
