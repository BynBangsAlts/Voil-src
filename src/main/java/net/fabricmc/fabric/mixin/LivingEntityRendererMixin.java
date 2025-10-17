package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.command.Command;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @ModifyVariable(method={"render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, ordinal=5, at=@At(value="STORE", ordinal=3))
    public float changePitch(float oldValue, LivingEntity entity) {
        if (Command.mc.player == null) {
            return oldValue;
        }
        if (entity.equals((Object)ClientMain.mc.player) && ClientMain.getRotationManager().isRotating()) {
            return ClientMain.getRotationManager().getServerRotation().getPitch();
        }
        return oldValue;
    }
}
