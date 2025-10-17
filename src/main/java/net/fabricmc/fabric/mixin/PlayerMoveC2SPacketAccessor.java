package net.fabricmc.fabric.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={PlayerMoveC2SPacket.class})
public abstract class PlayerMoveC2SPacketAccessor {
}
