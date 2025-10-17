package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.systems.module.impl.render.Cape;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayerEntity.class})
public class ClientPlayerEntityMixin {
    @Inject(method={"getSkinTextures"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> info) {
        Cape capeModule = (Cape)ModuleManager.INSTANCE.getModuleByClass(Cape.class);
        if (capeModule.isEnabled() && this.equals(ClientMain.mc.player)) {
            if (ClientMain.mc.currentScreen instanceof InventoryScreen) {
                return;
            }
        } else {
            return;
        }
        info.setReturnValue(this.getTexture((PlayerEntity)(Object)this));
    }

    public Identifier getTexture(PlayerEntity player) {
        if (player == ClientMain.mc.player) {
            switch (LoginHandler.plan) {
                case "voil+": {
                    return Identifier.of((String)"tulip", (String)"capes/voilplus.png");
                }
                case "media": {
                    return Identifier.of((String)"tulip", (String)"capes/media.png");
                }
                case "beta": {
                    return Identifier.of((String)"tulip", (String)"capes/beta.png");
                }
                case "staff": 
                case "owner": {
                    return Identifier.of((String)"tulip", (String)"capes/staff.png");
                }
            }
            return this.getDefaultCape();
        }
        return this.getDefaultCape();
    }

    public Identifier getDefaultCape() {
        return Identifier.of((String)"tulip", (String)"textures/capes/default_cape.png");
    }
}
