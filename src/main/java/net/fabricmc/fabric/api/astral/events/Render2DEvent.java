package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Render2DEvent
extends Cancellable {
    public MatrixStack matrixStack;
    public DrawContext context;
    public int scaledWidth;
    public int scaledHeight;

    public Render2DEvent(DrawContext context, MatrixStack matrixStack, int scaledWidth, int scaledHeight) {
        this.matrixStack = matrixStack;
        this.context = context;
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
    }

    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }

    public DrawContext getContext() {
        return this.context;
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }
}
