package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.util.math.Vec3d;

public class EventPlayerTravel
extends Cancellable {
    private Vec3d motionVector;

    public EventPlayerTravel(Vec3d motionVector) {
        this.motionVector = motionVector;
    }

    public Vec3d getMotionVector() {
        return this.motionVector;
    }

    public static class Post
    extends EventPlayerTravel {
        public Post(Vec3d motionVector) {
            super(motionVector);
        }
    }

    public static class Pre
    extends EventPlayerTravel {
        public Pre(Vec3d motionVector) {
            super(motionVector);
        }
    }
}
