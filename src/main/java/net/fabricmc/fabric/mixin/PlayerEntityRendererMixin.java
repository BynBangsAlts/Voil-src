package net.fabricmc.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.render.SwingAnimation;
import net.fabricmc.fabric.utils.Utils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin {
    @Inject(at={@At(value="RETURN")}, method={"getArmPose"}, cancellable=true)
    @Environment(value=EnvType.CLIENT)
    private static void swordblocking$getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (!ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled()) {
            return;
        }
        ItemStack handStack = abstractClientPlayerEntity.getStackInHand(hand);
        ItemStack offStack = abstractClientPlayerEntity.getStackInHand(hand.equals((Object)Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND);
        if (!(handStack.getItem() instanceof ShieldItem) && !Utils.canWeaponBlock((LivingEntity)abstractClientPlayerEntity)) {
            return;
        }
        if (offStack.getItem() instanceof ShieldItem && abstractClientPlayerEntity.isUsingItem()) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
        } else if (handStack.getItem() instanceof ShieldItem) {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
        }
    }
}
