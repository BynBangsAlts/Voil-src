package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;

public class EndCrystalExplosionMcPlayerEvent
extends Cancellable {
    private final int crystalId;

    public EndCrystalExplosionMcPlayerEvent(int crystalId) {
        this.crystalId = crystalId;
    }

    public int getCrystalId() {
        return this.crystalId;
    }
}
