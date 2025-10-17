package net.fabricmc.fabric.mixin;

import java.io.Serializable;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientConnection.class})
public class ClientConnectionMixin
implements Serializable {
    @Inject(at={@At(value="HEAD")}, method={"handlePacket"}, cancellable=true)
    private static void handlePacket(Packet<?> packet, PacketListener packetListener, CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        if (ClientMain.EVENTBUS.post(PacketEvent.Receive.get(packet)).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="RETURN")}, cancellable=true)
    private void onSendPacketPost(Packet<?> packet, CallbackInfo info) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        if (ClientMain.EVENTBUS.post(PacketEvent.SendPost.get(packet)).isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    void onPacketSend(Packet packet, CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        if (ClientMain.EVENTBUS.post(PacketEvent.Send.get(packet)).isCancelled()) {
            ci.cancel();
        }
    }
}
