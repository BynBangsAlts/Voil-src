package net.fabricmc.fabric.api.astral;

import net.fabricmc.fabric.api.astral.ICancellable;

public class Cancellable
implements ICancellable {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
