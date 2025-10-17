package net.fabricmc.fabric.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntity.class})
public class LivingEntityMixin {
    @Inject(method={"consumeItem"}, at={@At(value="HEAD")})
    private void onEatFood(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
    }
}
