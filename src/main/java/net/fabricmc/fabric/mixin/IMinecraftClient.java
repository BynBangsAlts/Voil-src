package net.fabricmc.fabric.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={MinecraftClient.class})
public interface IMinecraftClient {
    @Accessor(value="itemUseCooldown")
    public void setItemUseCooldown(int var1);

    @Accessor(value="itemUseCooldown")
    public int getItemUseCooldown();

    @Invoker
    public void invokeDoItemUse();

    @Invoker
    public boolean invokeDoAttack();
}
