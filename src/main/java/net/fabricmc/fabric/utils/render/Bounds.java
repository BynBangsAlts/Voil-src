package net.fabricmc.fabric.utils.render;

public class Bounds {
    public float x;
    public float y;
    public float width;
    public float height;

    public Bounds(float x, float y, float width, float height) {
        this.x = Math.round(x);
        this.y = Math.round(y);
        this.width = Math.round(width);
        this.height = Math.round(height);
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY <= (double)(this.y + this.height);
    }
}
