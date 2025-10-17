package net.fabricmc.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class ClientInit
implements ClientModInitializer {
    public static ClientInit INSTANCE;

    public void onInitializeClient() {
        INSTANCE = this;
    }
}
