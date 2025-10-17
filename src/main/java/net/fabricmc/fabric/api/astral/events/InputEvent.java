package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class InputEvent
extends Cancellable {
    boolean pressingForward;
    boolean pressingBack;
    boolean pressingLeft;
    boolean pressingRight;
    boolean jumping;
    boolean sneaking;
    float movementForward;
    float movementSideways;

    public static InputEvent get(boolean pressingForward, boolean pressingBack, boolean pressingLeft, boolean pressingRight, boolean jumping, boolean sneaking, float movementForward, float movementSideways) {
        InputEvent event = new InputEvent();
        event.pressingForward = pressingForward;
        event.pressingBack = pressingBack;
        event.pressingLeft = pressingLeft;
        event.pressingRight = pressingRight;
        event.jumping = jumping;
        event.sneaking = sneaking;
        event.movementForward = movementForward;
        event.movementSideways = movementSideways;
        return event;
    }

    public boolean isPressingForward() {
        return this.pressingForward;
    }

    public boolean isPressingBack() {
        return this.pressingBack;
    }

    public boolean isPressingLeft() {
        return this.pressingLeft;
    }

    public boolean isPressingRight() {
        return this.pressingRight;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public float getMovementForward() {
        return this.movementForward;
    }

    public float getMovementSideways() {
        return this.movementSideways;
    }

    public void setMovementForward(float movementForward) {
        this.movementForward = movementForward;
    }

    public void setMovementSideways(float movementSideways) {
        this.movementSideways = movementSideways;
    }

    public void setPressingForward(boolean pressingForward) {
        this.pressingForward = pressingForward;
    }

    public void setPressingBack(boolean pressingBack) {
        this.pressingBack = pressingBack;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.pressingLeft = pressingLeft;
    }

    public void setPressingRight(boolean pressingRight) {
        this.pressingRight = pressingRight;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public static class Post
    extends InputEvent {
        private static final Post INSTANCE = new Post();

        public static Post get(boolean pressingForward, boolean pressingBack, boolean pressingLeft, boolean pressingRight, boolean jumping, boolean sneaking, float movementForward, float movementSideways) {
            Post.INSTANCE.pressingForward = pressingForward;
            Post.INSTANCE.pressingBack = pressingBack;
            Post.INSTANCE.pressingLeft = pressingLeft;
            Post.INSTANCE.pressingRight = pressingRight;
            Post.INSTANCE.jumping = jumping;
            Post.INSTANCE.sneaking = sneaking;
            Post.INSTANCE.movementForward = movementForward;
            Post.INSTANCE.movementSideways = movementSideways;
            return INSTANCE;
        }
    }

    public static class Pre
    extends InputEvent {
        private static final Pre INSTANCE = new Pre();

        public static Pre get(boolean pressingForward, boolean pressingBack, boolean pressingLeft, boolean pressingRight, boolean jumping, boolean sneaking, float movementForward, float movementSideways) {
            Pre.INSTANCE.pressingForward = pressingForward;
            Pre.INSTANCE.pressingBack = pressingBack;
            Pre.INSTANCE.pressingLeft = pressingLeft;
            Pre.INSTANCE.pressingRight = pressingRight;
            Pre.INSTANCE.jumping = jumping;
            Pre.INSTANCE.sneaking = sneaking;
            Pre.INSTANCE.movementForward = movementForward;
            Pre.INSTANCE.movementSideways = movementSideways;
            return INSTANCE;
        }
    }
}
