package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.InputEvent;
import net.fabricmc.fabric.mixin.InputMixin;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={KeyboardInput.class})
public class KeyboardInputMixin
extends InputMixin {
    @Unique
    private static float getMovementMultiplier(boolean positive, boolean negative) {
        return positive == negative ? 0.0f : (positive ? 1.0f : -1.0f);
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void injectPreInput(boolean slowDown, float f, CallbackInfo ci) {
        float forward = KeyboardInputMixin.getMovementMultiplier(this.pressingForward, this.pressingBack);
        float sideways = KeyboardInputMixin.getMovementMultiplier(this.pressingLeft, this.pressingRight);
        InputEvent.Pre pre = InputEvent.Pre.get(this.pressingForward, this.pressingBack, this.pressingLeft, this.pressingRight, this.jumping, this.sneaking, forward, sideways);
        ClientMain.EVENTBUS.post(pre);
        if (pre.isCancelled()) {
            return;
        }
        this.pressingForward = pre.isPressingForward();
        this.pressingBack = pre.isPressingBack();
        this.pressingLeft = pre.isPressingLeft();
        this.pressingRight = pre.isPressingRight();
        this.jumping = pre.isJumping();
        this.sneaking = pre.isSneaking();
        this.movementForward = pre.getMovementForward();
        this.movementSideways = pre.getMovementSideways();
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void injectPostInput(boolean slowDown, float f, CallbackInfo ci) {
        float forward = KeyboardInputMixin.getMovementMultiplier(this.pressingForward, this.pressingBack);
        float sideways = KeyboardInputMixin.getMovementMultiplier(this.pressingLeft, this.pressingRight);
        InputEvent.Post post = InputEvent.Post.get(this.pressingForward, this.pressingBack, this.pressingLeft, this.pressingRight, this.jumping, this.sneaking, forward, sideways);
        ClientMain.EVENTBUS.post(post);
    }
}
