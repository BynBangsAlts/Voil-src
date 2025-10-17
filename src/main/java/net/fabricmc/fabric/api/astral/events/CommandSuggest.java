package net.fabricmc.fabric.api.astral.events;

import net.fabricmc.fabric.api.astral.Cancellable;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

public class CommandSuggest
extends Cancellable {
    private final CommandSuggestionsS2CPacket packet;

    public CommandSuggest(CommandSuggestionsS2CPacket packet) {
        this.packet = packet;
    }

    public CommandSuggestionsS2CPacket getPacket() {
        return this.packet;
    }

    public static CommandSuggest get(CommandSuggestionsS2CPacket packet) {
        return new CommandSuggest(packet);
    }
}
