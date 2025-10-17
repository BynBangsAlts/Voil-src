package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class EventMotion
extends Cancellable {

    public static final class Pre
    extends EventMotion {
        private static final Pre instance = new Pre();
        private static float staticYaw;
        private static float prevYaw;
        private static float staticPitch;
        private static float prevPitch;
        private float yaw;
        private float pitch;
        private double posX;
        private double posY;
        private double posZ;
        private boolean onGround;

        public static Pre get(float yaw, float pitch, double posX, double posY, double posZ, boolean onGround) {
            instance.setCancelled(false);
            Pre.instance.yaw = yaw;
            Pre.instance.pitch = pitch;
            Pre.instance.posX = posX;
            Pre.instance.posY = posY;
            Pre.instance.posZ = posZ;
            Pre.instance.onGround = onGround;
            return instance;
        }

        public static float getStaticYaw() {
            return staticYaw;
        }

        public static float getPrevYaw() {
            return prevYaw;
        }

        public static float getStaticPitch() {
            return staticPitch;
        }

        public static float getPrevPitch() {
            return prevPitch;
        }

        public void updateLast() {
            prevYaw = staticYaw;
            prevPitch = staticPitch;
            staticYaw = this.yaw;
            staticPitch = this.pitch;
        }

        public boolean isOnGround() {
            return this.onGround;
        }

        public void setOnGround(boolean onGround) {
            this.onGround = onGround;
        }

        public double getPosX() {
            return this.posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return this.posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public double getPosZ() {
            return this.posZ;
        }

        public void setPosZ(double posZ) {
            this.posZ = posZ;
        }

        public float getYaw() {
            return this.yaw;
        }

        public void setYaw(float yaw) {
            MinecraftClient.getInstance().player.setBodyYaw(yaw);
            MinecraftClient.getInstance().player.headYaw = yaw;
            this.yaw = yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = MathHelper.clamp((float)pitch, (float)-90.0f, (float)90.0f);
        }

        public void setPosition(double posX, double posY, double posZ, boolean onGround) {
            this.setPosX(posX);
            this.setPosY(posY);
            this.setPosZ(posZ);
            this.setOnGround(onGround);
        }
    }
}
