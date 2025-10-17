package net.fabricmc.fabric.utils.shader.shim;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public final class ShaderEffectManager {
    private static final ShaderEffectManager INSTANCE = new ShaderEffectManager();

    public static ShaderEffectManager getInstance() {
        return INSTANCE;
    }

    public ManagedCoreShader manageCoreShader(Identifier id, VertexFormat vf) {
        return new ManagedCoreShader();
    }
}



