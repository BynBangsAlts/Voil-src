package net.fabricmc.fabric.utils.shader.shim;

public final class Uniform1f {
    private float value;

    public void set(float v) {
        this.value = v;
    }

    public float get() {
        return this.value;
    }
}



