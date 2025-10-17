package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.network.packet.Packet;

public class PacketEvent
extends Cancellable {

    public static class SendPost
    extends PacketEvent {
        private static final SendPost INSTANCE = new SendPost();
        public Packet<?> packet;

        public Packet<?> getPacket() {
            return this.packet;
        }

        public static SendPost get(Packet<?> packet) {
            INSTANCE.setCancelled(false);
            SendPost.INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Send
    extends PacketEvent {
        private static final Send INSTANCE = new Send();
        public Packet<?> packet;

        public Packet<?> getPacket() {
            return this.packet;
        }

        public static Send get(Packet<?> packet) {
            INSTANCE.setCancelled(false);
            Send.INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Receive
    extends PacketEvent {
        private static final Receive INSTANCE = new Receive();
        public Packet<?> packet;

        public Packet<?> getPacket() {
            return this.packet;
        }

        public static Receive get(Packet<?> packet) {
            INSTANCE.setCancelled(false);
            Receive.INSTANCE.packet = packet;
            return INSTANCE;
        }
    }
}
