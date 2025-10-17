package net.fabricmc.fabric.utils.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class RenderHelper {
    public static Matrix4f projectionMatrix = new Matrix4f();
    public static Matrix4f modelViewMatrix = new Matrix4f();
    public static Matrix4f positionMatrix = new Matrix4f();
    public static DrawContext context;
    public static MatrixStack matrixStack;

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static Matrix4f getModelViewMatrix() {
        return modelViewMatrix;
    }

    public static Matrix4f getPositionMatrix() {
        return positionMatrix;
    }

    public static DrawContext getContext() {
        return context;
    }

    public static void setContext(DrawContext context) {
        RenderHelper.context = context;
    }

    public static MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public static void setMatrixStack(MatrixStack matrixStack) {
        RenderHelper.matrixStack = matrixStack;
    }

    public static void setProjectionMatrix(Matrix4f projectionMatrix) {
        RenderHelper.projectionMatrix = projectionMatrix;
    }

    public static void setModelViewMatrix(Matrix4f modelViewMatrix) {
        RenderHelper.modelViewMatrix = modelViewMatrix;
    }

    public static void setPositionMatrix(Matrix4f positionMatrix) {
        RenderHelper.positionMatrix = positionMatrix;
    }
}
