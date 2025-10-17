package net.fabricmc.fabric.mixin;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Keyboard.class})
public interface IKeyboardAccessorMixin {
    @Invoker
    public void invokeOnKey(long var1, int var3, int var4, int var5, int var6);
}
