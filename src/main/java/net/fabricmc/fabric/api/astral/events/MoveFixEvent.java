package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class MoveFixEvent
extends Cancellable {
    float yaw;

    public MoveFixEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
