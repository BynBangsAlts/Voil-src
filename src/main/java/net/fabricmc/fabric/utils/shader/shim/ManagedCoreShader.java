package net.fabricmc.fabric.utils.shader.shim;

import net.minecraft.client.gl.ShaderProgram;

public final class ManagedCoreShader {
    public ShaderProgram getProgram() {
        return null;
    }

    public Uniform1f findUniform1f(String name) {
        return new Uniform1f();
    }

    public Uniform2f findUniform2f(String name) {
        return new Uniform2f();
    }

    public Uniform4f findUniform4f(String name) {
        return new Uniform4f();
    }

    public SamplerUniform findSampler(String name) {
        return new SamplerUniform();
    }
}



