package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.render.SwingAnimation;
import net.fabricmc.fabric.utils.render.ItemRenderUtil;
import net.fabricmc.fabric.utils.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HeldItemRenderer.class})
public class HeldItemRendererMixin {
    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private float prevEquipProgressMainHand;
    @Shadow
    private float prevEquipProgressOffHand;
    @Shadow
    private float equipProgressOffHand;
    @Shadow
    private ItemStack mainHand;
    @Shadow
    private ItemStack offHand;
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")})
    public void renderItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }
        if (item.getItem() instanceof SwordItem) {
            ItemRenderUtil.render(mc.player.getInventory().getMainHandStack(), matrices);
        }
    }

    @Inject(method={"updateHeldItems"}, at={@At(value="HEAD")}, cancellable=true)
    public void updateHeldItems(CallbackInfo ci) {
        ItemStack stack;
        if (ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled() && Renderer.isValid(stack = this.client.player.getInventory().getMainHandStack()) && stack.getItem() instanceof SwordItem) {
            this.equipProgressMainHand = 1.0f;
            this.equipProgressOffHand = 1.0f;
            this.prevEquipProgressMainHand = this.equipProgressMainHand;
            this.prevEquipProgressOffHand = this.equipProgressOffHand;
            ClientPlayerEntity clientPlayerEntity = this.client.player;
            ItemStack itemStack = clientPlayerEntity.getMainHandStack();
            ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();
            this.mainHand = itemStack;
            this.offHand = itemStack2;
            ci.cancel();
        }
    }

    @ModifyVariable(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="STORE", ordinal=0), index=6)
    private float modifySwingProgress(float value) {
        ItemStack stack;
        if (ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled() && Renderer.isValid(stack = this.client.player.getInventory().getMainHandStack()) && stack.getItem() instanceof SwordItem) {
            return 0.0f;
        }
        return value;
    }
}
