package net.fabricmc.fabric.api.astral.events;

import net.minecraft.client.util.math.MatrixStack;

public class WorldRenderEvent {
    private static final WorldRenderEvent INSTANCE = new WorldRenderEvent();
    public MatrixStack matrices;
    public float tickDelta;

    public static WorldRenderEvent get(MatrixStack matrices, float tickDelta) {
        WorldRenderEvent.INSTANCE.matrices = matrices;
        WorldRenderEvent.INSTANCE.tickDelta = tickDelta;
        return INSTANCE;
    }
}
