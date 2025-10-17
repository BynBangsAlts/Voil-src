package net.fabricmc.fabric.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={ClientPlayerInteractionManager.class})
public interface IClientPlayerInteractionManagerAccessor {
    @Invoker
    public void callSyncSelectedSlot();
}
