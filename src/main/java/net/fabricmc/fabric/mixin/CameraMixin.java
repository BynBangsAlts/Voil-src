package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.CameraOffsetEvent;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Camera.class})
public class CameraMixin {
    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    void update(Args args) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        CameraOffsetEvent event = new CameraOffsetEvent((Double)args.get(0), (Double)args.get(1), (Double)args.get(2));
        ClientMain.EVENTBUS.post(event);
        args.set(0, (Object)event.getX());
        args.set(1, (Object)event.getY());
        args.set(2, (Object)event.getZ());
    }
}
