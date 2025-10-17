package net.fabricmc.fabric.handler.impl;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.handler.Handler;
import net.fabricmc.fabric.managers.SessionManager;
import net.fabricmc.fabric.security.LoginGUI;
import net.fabricmc.fabric.security.checks.SecurityManager;
import net.minecraft.client.gui.screen.Screen;

public class LoginHandler
implements Handler {
    @Override
    public void handle() {
        if (!SessionManager.isSessionValid() || !SecurityManager.e) {
            net.fabricmc.fabric.managers.SessionManager.createInstance("local", "local", "local-hwid", "local-session");
            net.fabricmc.fabric.security.checks.SecurityManager.e = true;
            this.removeLoginScreen();
        }
    }

    private void registerTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (SessionManager.isSessionValid() && SecurityManager.e) {
                this.removeLoginScreen();
            } else if (!(ClientMain.mc.currentScreen instanceof LoginGUI)) {
                this.setLoginScreen();
            }
        });
    }

    private void setLoginScreen() {
        if (!(ClientMain.mc.currentScreen instanceof LoginGUI)) {
            ClientMain.mc.submit(() -> {
                if (!(ClientMain.mc.currentScreen instanceof LoginGUI)) {
                    ClientMain.mc.setScreen((Screen)new LoginGUI());
                }
            });
        }
    }

    private void removeLoginScreen() {
        if (ClientMain.mc.currentScreen instanceof LoginGUI) {
            ClientMain.mc.submit(() -> ClientMain.mc.setScreen(null));
        }
    }

    private boolean load() {
        return true;
    }
}
