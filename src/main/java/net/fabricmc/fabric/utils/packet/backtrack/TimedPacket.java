package net.fabricmc.fabric.utils.packet.backtrack;

import net.fabricmc.fabric.utils.packet.backtrack.CooldownTimer;
import net.minecraft.network.packet.Packet;

public class TimedPacket {
    private final Packet<?> packet;
    private final CooldownTimer time;
    private final long millis;

    public TimedPacket(Packet<?> packet) {
        this.packet = packet;
        this.time = new CooldownTimer();
        this.millis = System.currentTimeMillis();
    }

    public TimedPacket(Packet<?> packet, long millis) {
        this.packet = packet;
        this.millis = millis;
        this.time = new CooldownTimer();
    }

    public Packet getPacket() {
        return this.packet;
    }

    public CooldownTimer getTimer() {
        return this.getTime();
    }

    public CooldownTimer getTime() {
        return this.time;
    }

    public long getMillis() {
        return this.millis;
    }
}
