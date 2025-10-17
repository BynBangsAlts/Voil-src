package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.ScoreboardObjective;

public class ScoreboardEvent
extends Cancellable {
    public DrawContext context;
    public ScoreboardObjective objective;

    public ScoreboardEvent(DrawContext context, ScoreboardObjective objective) {
        this.context = context;
        this.objective = objective;
    }
}
