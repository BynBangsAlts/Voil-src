package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.MouseUpdateEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Mouse.class})
public class MouseMixin {
    @Inject(method={"updateMouse"}, at={@At(value="HEAD")})
    private void onMouseUpdate(CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        ClientMain.EVENTBUS.post(MouseUpdateEvent.get());
    }
}
