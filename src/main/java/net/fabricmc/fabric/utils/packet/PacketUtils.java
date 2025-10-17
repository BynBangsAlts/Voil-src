package net.fabricmc.fabric.utils.packet;

import java.util.Objects;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.mixin.IClientWorldMixin;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class PacketUtils {
    public static void sendPacket(Packet packet) {
        if (ClientMain.mc.player != null) {
            ClientMain.mc.getNetworkHandler().sendPacket(packet);
        }
    }

    public static void sendPosition(Vec3d pos) {
        if (ClientMain.mc.player != null) {
            ClientMain.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), ClientMain.mc.player.isOnGround()));
        }
    }

    public static void sendSequencedPacket(SequencedPacketCreator packetCreator) {
        if (ClientMain.mc.getNetworkHandler() == null || ClientMain.mc.world == null) {
            return;
        }
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorldMixin)ClientMain.mc.world).getPendingUpdateManager().incrementSequence();){
            int i = pendingUpdateManager.getSequence();
            ClientMain.mc.getNetworkHandler().sendPacket(packetCreator.predict(i));
        }
    }

    public static void sendPacketSilently(Packet packet) {
        ClientMain.mc.getNetworkHandler().getConnection().send(packet, null);
    }

    public void sendPacket2(Packet<?> packetIn) {
        if (packetIn == null) {
            return;
        }
        Objects.requireNonNull(ClientMain.mc.getNetworkHandler()).getConnection().send(packetIn);
    }
}
