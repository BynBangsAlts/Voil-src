package net.fabricmc.fabric.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.WorldRenderEvent;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GameRenderer.class})
public abstract class WorldRendererMixin {
    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode=180, ordinal=0)}, method={"renderWorld"})
    private void onWorldRender(RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        MatrixStack matrixStack = new MatrixStack();
        RenderSystem.getModelViewStack().pushMatrix().mul((Matrix4fc)matrixStack.peek().getPositionMatrix());
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        RenderSystem.applyModelViewMatrix();
        Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
        ClientMain.EVENTBUS.post(WorldRenderEvent.get(matrixStack, tickCounter.getTickDelta(false)));
        RenderHelper.setMatrixStack(matrixStack);
        for (Module m : ModuleManager.INSTANCE.getModules()) {
            if (!m.isEnabled()) continue;
            m.onWorldRender(matrixStack);
        }
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode=180, ordinal=0)}, method={"renderWorld"})
    void render3dHook(RenderTickCounter tickCounter, CallbackInfo ci) {
        Camera camera = ClientMain.mc.gameRenderer.getCamera();
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        RenderSystem.applyModelViewMatrix();
        RenderHelper.setProjectionMatrix(RenderSystem.getProjectionMatrix());
        RenderHelper.setModelViewMatrix(RenderSystem.getModelViewMatrix());
        RenderHelper.setPositionMatrix(matrixStack.peek().getPositionMatrix());
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.getModelViewStack().popMatrix();
    }
}
