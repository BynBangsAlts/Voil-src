package net.fabricmc.fabric.utils.shader.shim;

public final class SamplerUniform {
    private int textureId;

    public void set(int textureId) {
        this.textureId = textureId;
    }

    public int get() {
        return this.textureId;
    }
}



