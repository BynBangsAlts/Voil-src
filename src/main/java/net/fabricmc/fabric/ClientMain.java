package net.fabricmc.fabric;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.astral.EventBus;
import net.fabricmc.fabric.api.astral.IEventBus;
import net.fabricmc.fabric.api.scripting.ScriptAPI;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.screens.HudGuiScreen;
import net.fabricmc.fabric.handler.Handler;
import net.fabricmc.fabric.handler.impl.BypassHandler;
import net.fabricmc.fabric.handler.impl.LoginHandler;
import net.fabricmc.fabric.handler.impl.PreLogin;
import net.fabricmc.fabric.handler.impl.SecurityHandler;
import net.fabricmc.fabric.managers.HealthManager;
import net.fabricmc.fabric.managers.HudModuleManager;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.managers.NotificationManager;
import net.fabricmc.fabric.managers.ServerManager;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.managers.SoundManager;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.security.checks.SecurityManager;
import net.fabricmc.fabric.systems.config.ConfigLoader;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.font.FontRenderer;
import net.fabricmc.fabric.utils.key.KeyboardSimulation;
import net.fabricmc.fabric.utils.key.MouseSimulation;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ClientMain
implements ModInitializer {
    public static ClientMain INSTANCE;
    public static final String name = "Boil"; // Tuff
    public static final String version = "b1.3";
    public static final String PACKAGE_PREFIX = "net.fabricmc.fabric";
    private static final String COMMAND_PREFIX = ".";
    public static MinecraftClient mc;
    public static IEventBus EVENTBUS;
    private ModuleManager moduleManager;
    public HudModuleManager hudModuleManager;
    public static FontRenderer fontRenderer;
    private MouseSimulation mouseSimulation;
    private KeyboardSimulation keyboardSimulation;
    public RotationManager rotationManager;
    public NotificationManager notificationManager;
    public HealthManager healthManager;
    private SecurityManager securityManager;
    public SoundManager soundManager;
    public SocialManager socialManager;
    public ServerManager serverManager;
    public boolean isSelfDestructed = false;
    private List<Handler> handlers;
    private Identifier fontPath = Identifier.of((String)"tulip", (String)"font/fontnormal.ttf");
    private Identifier verdanaBold = Identifier.of((String)"tulip", (String)"font/fontbold.ttf");
    private Identifier robotoBold = Identifier.of((String)"tulip", (String)"font/robotobold.ttf");

    public static ClientMain getInstance() {
        return INSTANCE;
    }

    public static String getCommandPrefix() {
        return COMMAND_PREFIX;
    }

    public static ModuleManager getModuleManager() {
        return ClientMain.INSTANCE.moduleManager;
    }

    public static RotationManager getRotationManager() {
        return ClientMain.INSTANCE.rotationManager;
    }

    public static NotificationManager getNotificationManager() {
        return ClientMain.INSTANCE.notificationManager;
    }

    public static FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public static MouseSimulation getMouseSimulation() {
        return ClientMain.INSTANCE.mouseSimulation;
    }

    public static HealthManager getHealthManager() {
        return ClientMain.INSTANCE.healthManager;
    }

    public static KeyboardSimulation getKeyboardSimulation() {
        return ClientMain.INSTANCE.keyboardSimulation;
    }

    public static SecurityManager getSecurityManager() {
        return ClientMain.INSTANCE.securityManager;
    }

    public static HudModuleManager getHudModuleManager() {
        return ClientMain.INSTANCE.hudModuleManager;
    }

    public static SocialManager getSocialManager() {
        return ClientMain.INSTANCE.socialManager;
    }

    public static SoundManager getSoundManager() {
        return ClientMain.INSTANCE.soundManager;
    }

    public static ServerManager getServerManager() {
        return ClientMain.INSTANCE.serverManager;
    }

    public void onInitialize() {
        mc = MinecraftClient.getInstance();
        Render2DEngine.initShaders();
        INSTANCE = this;
        this.rotationManager = new RotationManager();
        this.mouseSimulation = new MouseSimulation();
        this.moduleManager = new ModuleManager();
        this.hudModuleManager = new HudModuleManager();
        this.keyboardSimulation = new KeyboardSimulation();
        this.healthManager = new HealthManager();
        this.securityManager = new SecurityManager();
        this.notificationManager = new NotificationManager();
        this.soundManager = new SoundManager();
        this.socialManager = new SocialManager();
        this.serverManager = new ServerManager();
        fontRenderer = new FontRenderer(null, this.fontPath, 20.0f);
        FontRenderer.registerFont(Font.VERDANABOLD, this.verdanaBold, 20.0f);
        FontRenderer.registerFont(Font.VERDANA, this.fontPath, 20.0f);
        FontRenderer.registerFont(Font.ROBOTOBOLD, this.robotoBold, 20.0f);
        Renderer.init();
        ScriptAPI.init();
        this.handlers = Arrays.asList(new LoginHandler(), new SecurityHandler(), new PreLogin());
        this.handlers.forEach(Handler::handle);
        EVENTBUS.registerLambdaFactory("net.sexobf", (lookupInMethod, klass) -> (MethodHandles.Lookup)lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        EVENTBUS.registerLambdaFactory(PACKAGE_PREFIX, (lookupInMethod, klass) -> (MethodHandles.Lookup)lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        EVENTBUS.subscribe(this.moduleManager);
        EVENTBUS.subscribe(this.mouseSimulation);
        EVENTBUS.subscribe(this.rotationManager);
        EVENTBUS.subscribe(this.securityManager);
        EVENTBUS.subscribe(this.notificationManager);
        EVENTBUS.subscribe(this.soundManager);
        EVENTBUS.subscribe(this.socialManager);
        EVENTBUS.subscribe(this.serverManager);
        for (Item item : Registries.ITEM) {
            if (!(item instanceof SwordItem) && !(item instanceof AxeItem)) continue;
            ModelPredicateProviderRegistry.register((Item)item, (Identifier)Identifier.of((String)"blocking"), (stack, world, entity, seed) -> entity != null && Utils.isWeaponBlocking(entity) ? 1.0f : 0.0f);
        }
        ConfigLoader.loadConfigs();
        BypassHandler.hide("tulip");
    }

    public void onKeyPress(int key, int action) {
        if (ClientMain.mc.currentScreen == null && action == 1) {
            if (key == (Integer)UserConstants.GUIKEY.getValue()) {
                mc.setScreen((Screen)GUI.getInstance());
            }
            if (key == 72) {
                mc.setScreen((Screen)new HudGuiScreen());
            }
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                GUI.getInstance().open();
                if (module.getKey() != key) continue;
                module.toggle();
            }
        }
    }

    public void onTick() {
        if (ClientMain.mc.player != null) {
            for (Module module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.onTick();
            }
        }
    }

    static {
        EVENTBUS = new EventBus();
    }
}
