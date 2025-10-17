package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.Client.Spoofer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResourcePackSendS2CPacket.class})
public class ResourcePackSendS2CPacketMixin {
    @Inject(method={"apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void apply(ClientCommonPacketListener clientCommonPacketListener, CallbackInfo ci) {
        ServerInfo serverInfo;
        if (ModuleManager.INSTANCE.getModuleByClass(Spoofer.class).isEnabled() && ((Spoofer)ModuleManager.INSTANCE.getModuleByClass(Spoofer.class)).resource.isEnabled() && (serverInfo = MinecraftClient.getInstance().getCurrentServerEntry()) != null && serverInfo.getResourcePackPolicy() == ServerInfo.ResourcePackPolicy.DISABLED) {
            ci.cancel();
        }
    }
}
