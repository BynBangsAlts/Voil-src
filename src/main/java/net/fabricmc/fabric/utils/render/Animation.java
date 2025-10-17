package net.fabricmc.fabric.utils.render;

public class Animation {
    private float end;
    private float current;
    private boolean done;
    private float speed;

    public Animation(float start, float end, float speed) {
        this.current = start;
        this.end = end;
        this.speed = speed;
        this.done = false;
    }

    public void update(boolean castToInt) {
        if (!this.done) {
            this.current += (this.end - this.current) / this.speed;
            if (castToInt) {
                this.current = Math.round(this.current);
            }
            if (Math.abs(this.end - this.current) < 0.01f) {
                this.current = this.end;
                this.done = true;
            }
        }
    }

    public void update() {
        this.update(false);
    }

    public boolean hasEnded() {
        return this.done;
    }

    public float getValue() {
        return this.current;
    }

    public void setValue(float current) {
        this.current = current;
        this.done = Math.abs(this.end - current) < 0.01f;
    }

    public float getEnd() {
        return this.end;
    }

    public void setEnd(float end) {
        this.end = end;
        this.done = false;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
