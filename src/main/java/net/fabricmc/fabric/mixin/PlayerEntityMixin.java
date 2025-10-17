package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.EventPlayerTravel;
import net.fabricmc.fabric.api.astral.events.JumpEvent;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public class PlayerEntityMixin {
    @Inject(method={"jump"}, at={@At(value="HEAD")})
    private void onJumpPre(CallbackInfo ci) {
        ClientMain.EVENTBUS.post(JumpEvent.Pre.get());
    }

    @Inject(method={"jump"}, at={@At(value="RETURN")})
    private void onJumpPost(CallbackInfo ci) {
        ClientMain.EVENTBUS.post(JumpEvent.Post.get());
    }

    @Inject(method={"travel"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTravelhookPre(Vec3d movementInput, CallbackInfo ci) {
        if (ClientMain.mc.player == null) {
            return;
        }
        EventPlayerTravel.Pre event = new EventPlayerTravel.Pre(movementInput);
        ClientMain.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ClientMain.mc.player.move(MovementType.SELF, ClientMain.mc.player.getVelocity());
            ci.cancel();
        }
    }

    @Inject(method={"travel"}, at={@At(value="RETURN")}, cancellable=true)
    private void onTravelhookPost(Vec3d movementInput, CallbackInfo ci) {
        if (ClientMain.mc.player == null) {
            return;
        }
        EventPlayerTravel.Post event = new EventPlayerTravel.Post(movementInput);
        ClientMain.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ClientMain.mc.player.move(MovementType.SELF, ClientMain.mc.player.getVelocity());
            ci.cancel();
        }
    }
}
