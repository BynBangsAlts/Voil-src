package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.EventMotion;
import net.fabricmc.fabric.api.astral.events.HandSwingEvent;
import net.fabricmc.fabric.api.astral.events.MoveEvent;
import net.fabricmc.fabric.api.astral.events.SendMovementPacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.player.NoSlow;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerEntity.class})
public abstract class ClientPlayerMixin
extends LivingEntity {
    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract void move(MovementType var1, Vec3d var2);

    @Shadow
    public abstract float getPitch(float var1);

    protected ClientPlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at={@At(value="HEAD")}, method={"swingHand"})
    public void swingHand(Hand hand, CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        HandSwingEvent event = new HandSwingEvent(hand);
        ClientMain.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require=0)
    private boolean tickMovement(ClientPlayerEntity player) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).isEnabled() && ((BooleanSetting)ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).getSettings().get(1)).isEnabled()) {
            return false;
        }
        return player.isUsingItem();
    }

    @Inject(method={"shouldSlowDown"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShouldSlowDown(CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).isEnabled() && ((BooleanSetting)ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).getSettings().get(1)).isEnabled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method={"isSneaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsSneaking(CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).isEnabled() && ((BooleanSetting)ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).getSettings().get(2)).isEnabled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method={"move"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")}, cancellable=true)
    public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
        ClientMain.EVENTBUS.post(event);
        if (event.isCancelled()) {
            this.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
            ci.cancel();
        }
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")})
    private void onSendMovementPacketsHead(CallbackInfo info) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        ClientMain.EVENTBUS.post(SendMovementPacketEvent.Pre.get());
        EventMotion.Pre event = EventMotion.Pre.get(ClientMain.mc.player.getYaw(), ClientMain.mc.player.getPitch(), ClientMain.mc.player.getX(), ClientMain.mc.player.getY(), ClientMain.mc.player.getZ(), ClientMain.mc.player.isOnGround());
        ClientMain.EVENTBUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
            return;
        }
        ClientMain.mc.player.setYaw(event.getYaw());
        ClientMain.mc.player.setPitch(event.getPitch());
        ClientMain.mc.player.setPos(event.getPosX(), event.getPosY(), event.getPosZ());
        ClientMain.mc.player.setOnGround(event.isOnGround());
        event.updateLast();
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal=0)})
    private void onTickHasVehicleBeforeSendPackets(CallbackInfo info) {
        ClientMain.EVENTBUS.post(SendMovementPacketEvent.Pre.get());
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="TAIL")})
    private void onSendMovementPacketsTail(CallbackInfo info) {
        ClientMain.EVENTBUS.post(SendMovementPacketEvent.Post.get());
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal=1, shift=At.Shift.AFTER)})
    private void onTickHasVehicleAfterSendPackets(CallbackInfo info) {
        ClientMain.EVENTBUS.post(SendMovementPacketEvent.Post.get());
    }
}
