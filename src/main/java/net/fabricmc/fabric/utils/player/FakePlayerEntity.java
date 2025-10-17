package net.fabricmc.fabric.utils.player;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class FakePlayerEntity
extends OtherClientPlayerEntity {
    @Nullable
    private PlayerListEntry playerListEntry;
    public boolean doNotPush;
    public boolean hideWhenInsideCamera;

    public FakePlayerEntity(PlayerEntity player, String name, float health, boolean copyInv) {
        super(ClientMain.mc.world, new GameProfile(UUID.randomUUID(), name));
        this.copyPositionAndRotation((Entity)player);
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
        this.prevHeadYaw = this.headYaw = player.headYaw;
        this.prevBodyYaw = this.bodyYaw = player.bodyYaw;
        Byte playerModel = player.getDataTracker().get(PlayerEntity.PLAYER_MODEL_PARTS);
        this.dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        this.getAttributes().setFrom(player.getAttributes());
        this.setPose(player.getPose());
        if (!this.isPushable()) {
            this.pushAway((Entity)player);
        }
        this.capeX = this.getX();
        this.capeY = this.getY();
        this.capeZ = this.getZ();
        if (health <= 20.0f) {
            this.setHealth(health);
        } else {
            this.setHealth(health);
            this.setAbsorptionAmount(health - 20.0f);
        }
        if (copyInv) {
            this.getInventory().clone(player.getInventory());
        }
    }

    public void spawn() {
        this.unsetRemoved();
        ClientMain.mc.world.addEntity((Entity)this);
    }

    public void despawn() {
        ClientMain.mc.world.removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
        this.setRemoved(Entity.RemovalReason.DISCARDED);
    }

    @Nullable
    protected PlayerListEntry getPlayerListEntry() {
        if (this.playerListEntry == null) {
            this.playerListEntry = ClientMain.mc.getNetworkHandler().getPlayerListEntry(ClientMain.mc.player.getUuid());
        }
        return this.playerListEntry;
    }
}
