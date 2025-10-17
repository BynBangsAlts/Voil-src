package net.fabricmc.fabric.mixin;

import net.minecraft.client.resource.server.ServerResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ServerResourcePackLoader.class})
public class ServerResourcePackProviderMixin {
}
