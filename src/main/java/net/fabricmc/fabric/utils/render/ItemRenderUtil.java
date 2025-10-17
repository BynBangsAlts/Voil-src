package net.fabricmc.fabric.utils.render;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.render.SwingAnimation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.RotationAxis;

public class ItemRenderUtil {
    public static float start;
    public static float end;
    public static float current;
    public static double translateX;
    public static double translateY;
    public static double translateZ;
    public static float rotateX;
    public static float rotateY;
    public static float rotateZ;
    public static float scale;
    private static long lastFrameTime;

    public static void init() {
        end = start = -90.0f;
        current = start;
    }

    public static void onUpdate() {
        if (ClientMain.mc.player.getMainHandStack().getItem() instanceof SwordItem && ClientMain.mc.options.attackKey.wasPressed() && !ClientMain.mc.player.isBlocking()) {
            if (ClientMain.mc.targetedEntity != null) {
                ClientMain.mc.interactionManager.attackEntity((PlayerEntity)ClientMain.mc.player, ClientMain.mc.targetedEntity);
            }
            ItemRenderUtil.swing();
        }
    }

    public static void render(ItemStack stack, MatrixStack matrices) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
        float swingIncrement = 200.0f;
        float swingProgress = swingIncrement * ((float)elapsedTime / 1000.0f);
        if (current > end) {
            current -= swingProgress;
        } else if (current < end) {
            current += swingProgress;
        }
        if (end == -130.0f && current <= end) {
            end = start;
        }
        if (end == start && current >= end) {
            current = end;
        }
        try {
            if (ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled()) {
                matrices.translate(translateX, translateY, translateZ);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateY));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotateZ));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(current));
                matrices.translate(0.0, 0.6, 0.7);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swing() {
        if (ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled()) {
            end = -130.0f;
        } else {
            ClientMain.mc.player.swingHand(ClientMain.mc.player.getActiveHand());
        }
    }

    public static boolean isValid(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    public static void setTranslation(double x, double y, double z) {
        translateX = x;
        translateY = y;
        translateZ = z;
    }

    public static void setRotation(float x, float y, float z) {
        rotateX = x;
        rotateY = y;
        rotateZ = z;
    }

    public static void setScale(float newScale) {
        scale = newScale;
    }

    static {
        translateX = 0.4;
        translateY = -0.2;
        translateZ = -0.1;
        rotateX = 0.0f;
        rotateY = 85.0f;
        rotateZ = -17.0f;
        scale = 0.68f;
        lastFrameTime = 0L;
    }
}
