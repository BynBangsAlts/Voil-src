package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.MoveFixEvent;
import net.fabricmc.fabric.api.astral.events.VelocityEvent;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.systems.module.impl.combat.Hitboxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    private float bodyYaw;
    @Shadow
    @Final
    private EntityType<?> type;

    @Shadow
    protected abstract Vec3d getRotationVector(float var1, float var2);

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract float getYaw();

    @Shadow
    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    @Unique
    private static Vec3d movementInputToVelocityC(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply((double)speed);
        float f = MathHelper.sin((float)(yaw * ((float)Math.PI / 180)));
        float g = MathHelper.cos((float)(yaw * ((float)Math.PI / 180)));
        return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
    }

    @Shadow
    public abstract void remove(Entity.RemovalReason var1);

    @Inject(method={"getTargetingMargin"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetTargetingMargin(CallbackInfoReturnable<Float> info) {
        double v = ((Hitboxes)ModuleManager.INSTANCE.getModuleByClass(Hitboxes.class)).size.getValue();
        if (!ModuleManager.INSTANCE.getModuleByClass(Hitboxes.class).isEnabled()) {
            return;
        }
        info.setReturnValue((float)v);
    }

    @Inject(method={"getRotationVec"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectFakeRotation(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        Rotation rot = ClientMain.getRotationManager().getServerRotation();
        if ((Object)this == MinecraftClient.getInstance().player && rot != null) {
            cir.setReturnValue(this.getRotationVector(rot.getPitch(), rot.getYaw()));
        }
    }

    @Redirect(method={"updateVelocity"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"))
    public Vec3d updateVelocityInject(Vec3d movementInput, float speed, float yaw) {
        if ((Object)this == MinecraftClient.getInstance().player) {
            MoveFixEvent eventYawMoveFix = new MoveFixEvent(yaw);
            ClientMain.EVENTBUS.post(eventYawMoveFix);
            yaw = eventYawMoveFix.getYaw();
        }
        return EntityMixin.movementInputToVelocity(movementInput, speed, yaw);
    }

    @Inject(method={"updateVelocity"}, at={@At(value="HEAD")}, cancellable=true)
    public void elementCodec(float speed, Vec3d movementInput, CallbackInfo ci) {
        if ((Object)this == ClientMain.mc.player) {
            ci.cancel();
            VelocityEvent event = new VelocityEvent(movementInput, speed, ClientMain.mc.player.getYaw(), EntityMixin.movementInputToVelocityC(movementInput, speed, ClientMain.mc.player.getYaw()));
            ClientMain.EVENTBUS.post(event);
            ClientMain.mc.player.setVelocity(ClientMain.mc.player.getVelocity().add(event.getVelocity()));
        }
    }
}
