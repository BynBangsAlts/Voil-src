package net.fabricmc.fabric.utils.shader.shim;

public final class Uniform4f {
    private float r;
    private float g;
    private float b;
    private float a;

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getR() { return r; }
    public float getG() { return g; }
    public float getB() { return b; }
    public float getA() { return a; }
}



