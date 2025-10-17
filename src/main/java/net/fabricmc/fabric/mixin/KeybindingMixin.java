package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.movement.SnapTap;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={KeyBinding.class})
public class KeybindingMixin {
    @Shadow
    @Final
    private InputUtil.Key defaultKey;
    @Shadow
    private boolean pressed;

    @Inject(method={"isPressed"}, at={@At(value="HEAD")}, cancellable=true)
    public void onGetPressed(CallbackInfoReturnable<Boolean> cir) {
        if (!ModuleManager.INSTANCE.getModuleByClass(SnapTap.class).isEnabled()) {
            return;
        }
        if (this.defaultKey.getCode() == 65) {
            if (this.pressed) {
                if (SnapTap.RIGHT_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                cir.setReturnValue(SnapTap.RIGHT_STRAFE_LAST_PRESS_TIME <= SnapTap.LEFT_STRAFE_LAST_PRESS_TIME);
                cir.cancel();
            }
        } else if (this.defaultKey.getCode() == 68) {
            if (this.pressed) {
                if (SnapTap.LEFT_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                cir.setReturnValue(SnapTap.LEFT_STRAFE_LAST_PRESS_TIME <= SnapTap.RIGHT_STRAFE_LAST_PRESS_TIME);
                cir.cancel();
            }
        } else if (this.defaultKey.getCode() == 87) {
            if (this.pressed) {
                if (SnapTap.BACKWARD_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                cir.setReturnValue(SnapTap.BACKWARD_STRAFE_LAST_PRESS_TIME <= SnapTap.FORWARD_STRAFE_LAST_PRESS_TIME);
                cir.cancel();
            }
        } else if (this.defaultKey.getCode() == 83 && this.pressed) {
            if (SnapTap.FORWARD_STRAFE_LAST_PRESS_TIME == 0L) {
            cir.setReturnValue(true);
                cir.cancel();
                return;
            }
            cir.setReturnValue(SnapTap.FORWARD_STRAFE_LAST_PRESS_TIME <= SnapTap.BACKWARD_STRAFE_LAST_PRESS_TIME);
            cir.cancel();
        }
    }

    @Inject(method={"setPressed"}, at={@At(value="HEAD")})
    public void setPressed(boolean pressed, CallbackInfo ci) {
        if (!ModuleManager.INSTANCE.getModuleByClass(SnapTap.class).isEnabled()) {
            return;
        }
        if (this.defaultKey.getCode() == 65) {
            SnapTap.LEFT_STRAFE_LAST_PRESS_TIME = pressed ? System.currentTimeMillis() : 0L;
        } else if (this.defaultKey.getCode() == 68) {
            SnapTap.RIGHT_STRAFE_LAST_PRESS_TIME = pressed ? System.currentTimeMillis() : 0L;
        } else if (this.defaultKey.getCode() == 87) {
            SnapTap.FORWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.currentTimeMillis() : 0L;
        } else if (this.defaultKey.getCode() == 83) {
            SnapTap.BACKWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.currentTimeMillis() : 0L;
        }
    }
}
