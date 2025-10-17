package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.EndCrystalExplosionMcPlayerEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EndCrystalEntity.class})
public class EntityEnderCrystalMixin {
    @Shadow
    public int endCrystalAge;

    @Inject(method = "crystalDestroyed", at = @At("HEAD"))
    private void onCrystalDestroyed(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof ClientPlayerEntity) {
            if (ClientMain.getInstance().isSelfDestructed) return;

            int crystalId = ((EndCrystalEntity) (Object) this).getId();

            ClientMain.EVENTBUS.post(new EndCrystalExplosionMcPlayerEvent(crystalId));
        }
    }
}
