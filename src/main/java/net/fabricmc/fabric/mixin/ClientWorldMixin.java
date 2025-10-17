package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.EntitySpawnEvent;
import net.fabricmc.fabric.managers.PlaytimeManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientWorld.class})
public class ClientWorldMixin {
    @Inject(method={"addEntity"}, at={@At(value="HEAD")}, cancellable=true)
    public void addEntityHook(Entity entity, CallbackInfo ci) {
        EntitySpawnEvent ees = new EntitySpawnEvent(entity);
        ClientMain.EVENTBUS.post(ees);
        if (ees.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"removeEntity"}, at={@At(value="HEAD")})
    private void onEntityRemove(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
    }

    @Inject(method={"disconnect"}, at={@At(value="TAIL")})
    public void onLeave(CallbackInfo ci) {
        PlaytimeManager.timerOn = false;
    }
}
