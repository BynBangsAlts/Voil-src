package net.fabricmc.fabric.handler.impl;

import net.fabricmc.fabric.handler.Handler;
import net.fabricmc.fabric.handler.impl.LoginHandler;
import net.fabricmc.fabric.managers.SessionManager;
import net.fabricmc.fabric.security.LoginGUI;
import net.fabricmc.fabric.security.Util.Util;

public class SecurityHandler
implements Handler {
    @Override
    public void handle() {
        Util.checkClass(LoginGUI.class);
        Util.checkClass(SecurityHandler.class);
        Util.checkClass(LoginHandler.class);
        Util.checkClass(SessionManager.class);
        Util.checkClass(Handler.class);
    }
}
