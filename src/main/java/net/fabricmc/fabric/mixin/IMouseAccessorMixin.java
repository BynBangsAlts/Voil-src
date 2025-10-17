package net.fabricmc.fabric.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Mouse.class})
public interface IMouseAccessorMixin {
    @Invoker
    public void callOnMouseButton(long var1, int var3, int var4, int var5);
}
