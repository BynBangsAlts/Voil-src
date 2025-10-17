package net.fabricmc.fabric.mixin;

import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={Scoreboard.class})
public class ScoreboardMixin {
}
