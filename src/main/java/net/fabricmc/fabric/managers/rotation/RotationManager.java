package net.fabricmc.fabric.managers.rotation;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventPlayerTravel;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.api.astral.events.InputEvent;
import net.fabricmc.fabric.api.astral.events.JumpEvent;
import net.fabricmc.fabric.api.astral.events.MoveFixEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.api.astral.events.SendMovementPacketEvent;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.mixin.ILivingEntity;
import net.fabricmc.fabric.systems.module.impl.Client.MoveFix;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationManager {
    private final Map<Class<?>, Rotation> activeRotations = new HashMap();
    private boolean moveFix = false;
    private boolean resetRotation = false;
    private Rotation currentRotation;
    private float clientYaw = 0.0f;
    private float clientPitch = 0.0f;
    private float serverYaw = 0.0f;
    private float serverPitch = 0.0f;
    private float prevYaw;
    private MoveFixType moveFixType;
    private RotationPriority currentPriority = RotationPriority.NONE;

    public RotationManager() {
        this.moveFixType = MoveFixType.NONE;
    }

    public boolean isRotating(Class<?> sourceClass) {
        return this.activeRotations.containsKey(sourceClass);
    }

    public void removeRotation(Class<?> sourceClass) {
        this.activeRotations.remove(sourceClass);
    }

    public Rotation getServerRotation() {
        return new Rotation(this.serverYaw, this.serverPitch);
    }

    public void setRotation(Rotation rotation, RotationPriority priority) {
        Class src = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk(frames -> frames.skip(1L).findFirst().map(StackWalker.StackFrame::getDeclaringClass).orElse(null));
        this.activeRotations.put(src, rotation);
        if (priority.compareTo(this.currentPriority) >= 0) {
            this.currentRotation = rotation;
            this.currentPriority = priority;
        }
    }

    public void setRotation(double yaw, double pitch, RotationPriority priority) {
        this.setRotation(new Rotation((float)yaw, (float)pitch), priority);
    }

    public void setRotation(Rotation rotation, float smoothness) {
        Rotation current = this.currentRotation != null ? this.currentRotation : this.getPlayerRotation();
        float newYaw = this.smoothAngle(current.getYaw(), rotation.getYaw(), smoothness);
        float newPitch = this.smoothAngle(current.getPitch(), rotation.getPitch(), smoothness);
        this.currentRotation = new Rotation(newYaw, newPitch);
    }

    public Rotation getPlayerRotation() {
        if (ClientMain.mc.player == null) {
            return new Rotation(0.0f, 0.0f);
        }
        return new Rotation(ClientMain.mc.player.getYaw(), ClientMain.mc.player.getPitch());
    }

    public boolean isRotating() {
        return this.currentRotation != null;
    }

    public void resetRotation(boolean setCurrentRotationNull) {
        Class sourceClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk(frames -> frames.skip(1L).findFirst().map(StackWalker.StackFrame::getDeclaringClass).orElse(null));
        if (!this.resetRotation) {
            this.resetRotation = true;
            this.currentRotation = null;
            this.currentPriority = RotationPriority.NONE;
            this.removeRotation(sourceClass);
            this.serverYaw = ClientMain.mc.player.getYaw();
            this.serverPitch = ClientMain.mc.player.getPitch();
        }
    }

    public void setMoveFix(MoveFixType moveFixType, boolean moveFix) {
        this.moveFix = moveFix;
        this.moveFixType = moveFixType;
    }

    public void resetClientRotation() {
        if (Math.abs(ClientMain.mc.player.getYaw() - this.clientYaw) > 0.1f || Math.abs(ClientMain.mc.player.getPitch() - this.clientPitch) > 0.1f) {
            ClientMain.mc.player.setYaw(this.clientYaw);
            ClientMain.mc.player.setPitch(this.clientPitch);
        }
        this.resetRotation = false;
    }

    private void setClientRotation(Rotation rotation) {
        this.clientYaw = ClientMain.mc.player.getYaw();
        this.clientPitch = ClientMain.mc.player.getPitch();
        ClientMain.mc.player.setYaw(rotation.getYaw());
        ClientMain.mc.player.setPitch(rotation.getPitch());
        this.resetRotation = true;
    }

    private void setServerRotation(Rotation rotation) {
        this.serverYaw = rotation.getYaw();
        this.serverPitch = rotation.getPitch();
    }

    @EventHandler(priority=-200)
    void onPacketSend(PacketEvent.Send event) {
        Packet<?> packet = event.packet;
        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket packet2 = (PlayerMoveC2SPacket)packet;
            this.serverYaw = packet2.getYaw(this.serverYaw);
            this.serverPitch = packet2.getPitch(this.serverPitch);
        }
    }

    @EventHandler
    private void onTick(EventUpdate event) {
        if (ClientMain.mc.player == null) {
            return;
        }
        MoveFixType moveFixType = this.getMoveFix(MoveFix.movefix.getMode());
        if (moveFixType != null && moveFixType != MoveFixType.NONE) {
            this.setMoveFix(moveFixType, true);
        }
        if (this.resetRotation) {
            this.resetRotation = false;
        }
    }

    @EventHandler(priority=200)
    private void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if (packet instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet2 = (PlayerPositionLookS2CPacket)packet;
            this.serverYaw = packet2.getYaw();
            this.serverPitch = packet2.getPitch();
        }
    }

    @EventHandler(priority=-200)
    private void onSendMovementPacketPre(SendMovementPacketEvent.Pre event) {
        if (this.currentRotation != null) {
            this.setClientRotation(this.currentRotation);
            this.setServerRotation(this.currentRotation);
            return;
        }
        if (this.resetRotation) {
            Rotation serverRot = new Rotation(this.serverYaw, this.serverPitch);
            Rotation clientRot = new Rotation(ClientMain.mc.player.getYaw(), ClientMain.mc.player.getPitch());
            if (Rotation.getTotalDifference(serverRot, clientRot) > 1.0f) {
                Rotation fixedRot = Rotation.fixRots(clientRot, serverRot);
                this.setClientRotation(fixedRot);
                this.setServerRotation(fixedRot);
            } else {
                this.resetRotation = false;
            }
        }
    }

    @EventHandler(priority=200)
    private void onSendMovementPacketPost(SendMovementPacketEvent.Post event) {
        if (this.resetRotation) {
            this.resetClientRotation();
        }
        if (ClientMain.mc.player == null) {
            return;
        }
        if (this.isRotating()) {
            ClientMain.mc.player.setHeadYaw(this.serverYaw);
            ((ILivingEntity)ClientMain.mc.player).setBodyYaw(this.serverYaw);
        }
    }

    @EventHandler
    private void onMoveFix(MoveFixEvent event) {
        if (this.shouldMoveFix()) {
            event.setYaw(this.serverYaw);
        }
    }

    @EventHandler
    private void onTravelPre(EventPlayerTravel.Pre event) {
        if (this.shouldMoveFix()) {
            this.prevYaw = ClientMain.mc.player.getYaw();
            ClientMain.mc.player.setYaw(this.serverYaw);
        }
    }

    @EventHandler
    private void onTravelPost(EventPlayerTravel.Post event) {
        if (this.shouldMoveFix()) {
            ClientMain.mc.player.setYaw(this.prevYaw);
        }
    }

    @EventHandler(priority=200)
    private void onJumpPre(JumpEvent.Pre event) {
        if (this.shouldMoveFix()) {
            this.prevYaw = ClientMain.mc.player.getYaw();
            ClientMain.mc.player.setYaw(this.serverYaw);
        }
    }

    @EventHandler(priority=100)
    private void onJumpPost(JumpEvent.Post event) {
        if (this.shouldMoveFix()) {
            ClientMain.mc.player.setYaw(this.prevYaw);
        }
    }

    @EventHandler(priority=9999)
    private void onKeyInput(InputEvent.Pre e) {
        if (this.shouldMoveFix() || e.getMovementForward() == 0.0f && e.getMovementSideways() == 0.0f) {
            float realYaw = ClientMain.mc.player.getYaw();
            float fakeYaw = this.serverYaw;
            double moveX = (double)e.getMovementSideways() * Math.cos(Math.toRadians(realYaw)) - (double)e.getMovementForward() * Math.sin(Math.toRadians(realYaw));
            double moveZ = (double)e.getMovementForward() * Math.cos(Math.toRadians(realYaw)) + (double)e.getMovementSideways() * Math.sin(Math.toRadians(realYaw));
            double minDist = Double.MAX_VALUE;
            double bestForward = 0.0;
            double bestStrafe = 0.0;
            for (double forward = -1.0; forward <= 1.0; forward += 1.0) {
                for (double strafe = -1.0; strafe <= 1.0; strafe += 1.0) {
                    double newMoveZ;
                    double deltaZ;
                    double newMoveX = strafe * Math.cos(Math.toRadians(fakeYaw)) - forward * Math.sin(Math.toRadians(fakeYaw));
                    double deltaX = newMoveX - moveX;
                    double dist = Math.sqrt(deltaX * deltaX + (deltaZ = (newMoveZ = forward * Math.cos(Math.toRadians(fakeYaw)) + strafe * Math.sin(Math.toRadians(fakeYaw))) - moveZ) * deltaZ);
                    if (!(minDist > dist)) continue;
                    minDist = dist;
                    bestForward = forward;
                    bestStrafe = strafe;
                }
            }
            e.setMovementForward(Math.round(bestForward));
            e.setMovementSideways(Math.round(bestStrafe));
            ClientMain.mc.player.input.movementForward = Math.round(bestForward);
            ClientMain.mc.player.input.movementSideways = Math.round(bestStrafe);
        }
    }

    public static void fixMoveDirection(InputEvent event, float targetYaw) {
        float forwardInput = (event.isPressingForward() ? 1.0f : 0.0f) - (event.isPressingBack() ? 1.0f : 0.0f);
        float sidewaysInput = (event.isPressingLeft() ? 1.0f : 0.0f) - (event.isPressingRight() ? 1.0f : 0.0f);
        float deltaYaw = ClientMain.mc.player.getYaw() - targetYaw;
        float rotatedSideways = sidewaysInput * MathHelper.cos((float)(deltaYaw * ((float)Math.PI / 180))) - forwardInput * MathHelper.sin((float)(deltaYaw * ((float)Math.PI / 180)));
        float rotatedForward = forwardInput * MathHelper.cos((float)(deltaYaw * ((float)Math.PI / 180))) + sidewaysInput * MathHelper.sin((float)(deltaYaw * ((float)Math.PI / 180)));
        event.setMovementForward(Math.round(rotatedForward));
        event.setMovementSideways(Math.round(rotatedSideways));
        ClientMain.mc.player.input.movementForward = Math.round(rotatedForward);
        ClientMain.mc.player.input.movementSideways = Math.round(rotatedSideways);
    }

    private boolean shouldMoveFix() {
        return !Float.isNaN(this.serverYaw) && this.moveFixType != MoveFixType.NONE && !ClientMain.mc.player.isRiding() && this.isRotating();
    }

    private boolean shouldMoveFixStrict() {
        return !Float.isNaN(this.serverYaw) && (this.moveFixType == MoveFixType.STRICT || this.moveFixType == MoveFixType.BOTH) && !ClientMain.mc.player.isRiding() && this.isRotating();
    }

    public void faceEntity(Entity target, RotationPriority priority) {
        Vec3d targetPos = target.getPos().add(0.0, (double)target.getHeight() / 2.0, 0.0);
        Rotation rotation = Rotation.getRotationTo(ClientMain.mc.player.getEyePos(), targetPos);
        this.setRotation(rotation, priority);
    }

    public void faceBlock(BlockPos pos, RotationPriority priority) {
        Vec3d blockCenter = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
        this.faceVector(blockCenter, priority);
    }

    public void faceVector(Vec3d vec, RotationPriority priority) {
        Vec3d eyesPos = ClientMain.mc.player.getEyePos();
        double dx = vec.x - eyesPos.x;
        double dy = vec.y - eyesPos.y;
        double dz = vec.z - eyesPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distance)));
        this.setRotation(new Rotation(yaw, pitch), priority);
    }

    private float smoothAngle(float current, float target, float smoothness) {
        float diff = MathHelper.wrapDegrees((float)(target - current));
        return current + diff * smoothness;
    }

    public MoveFixType getMoveFix(String str) {
        if (str.equalsIgnoreCase("None")) {
            return MoveFixType.NONE;
        }
        if (str.equalsIgnoreCase("Silent")) {
            return MoveFixType.SILENT;
        }
        if (str.equalsIgnoreCase("Strict")) {
            return MoveFixType.STRICT;
        }
        if (str.equalsIgnoreCase("Both")) {
            return MoveFixType.BOTH;
        }
        return MoveFixType.NONE;
    }

    public static enum RotationPriority {
        NONE(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        HIGHEST(4);

        private final int level;

        private RotationPriority(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }

    public static enum MoveFixType {
        NONE,
        SILENT,
        STRICT,
        BOTH;

    }
}
