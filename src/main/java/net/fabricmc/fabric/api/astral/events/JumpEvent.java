package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class JumpEvent
extends Cancellable {

    public static class Post
    extends JumpEvent {
        private static final Post INSTANCE = new Post();

        public static Post get() {
            return INSTANCE;
        }
    }

    public static class Pre
    extends JumpEvent {
        private static final Pre INSTANCE = new Pre();

        public static Pre get() {
            return INSTANCE;
        }
    }
}
