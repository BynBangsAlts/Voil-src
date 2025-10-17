package net.fabricmc.fabric.utils.shader.shim;

public final class Uniform2f {
    private float x;
    private float y;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}



