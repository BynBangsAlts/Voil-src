package net.fabricmc.fabric.mixin;

import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={NativeImage.class})
public interface INativeImage {
    @Accessor(value="pointer")
    public long getPointer();
}
