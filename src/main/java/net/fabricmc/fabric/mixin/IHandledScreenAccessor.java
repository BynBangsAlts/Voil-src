package net.fabricmc.fabric.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={HandledScreen.class})
public interface IHandledScreenAccessor {
    @Accessor
    public Slot getFocusedSlot();

    @Accessor
    public void setFocusedSlot(Slot var1);
}
