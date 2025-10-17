package net.fabricmc.fabric.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={LivingEntity.class})
public interface ILivingEntity {
    @Accessor(value="jumpingCooldown")
    public void setJumpingCooldown(int var1);

    @Accessor(value="bodyYaw")
    public void setBodyYaw(float var1);
}
