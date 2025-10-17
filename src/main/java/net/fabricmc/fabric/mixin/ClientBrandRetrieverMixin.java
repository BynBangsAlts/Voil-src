package net.fabricmc.fabric.mixin;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ClientBrandRetriever.class})
public abstract class ClientBrandRetrieverMixin {
}
