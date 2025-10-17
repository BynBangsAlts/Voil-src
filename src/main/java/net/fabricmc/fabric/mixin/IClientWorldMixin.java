package net.fabricmc.fabric.mixin;

import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ClientWorld.class})
public interface IClientWorldMixin {
    @Accessor(value="pendingUpdateManager")
    public PendingUpdateManager getPendingUpdateManager();
}
