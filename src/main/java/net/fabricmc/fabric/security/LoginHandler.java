package net.fabricmc.fabric.security;

import java.nio.file.Path;
import java.nio.file.Paths;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.security.checks.SecurityManager;

public class LoginHandler {
    SecurityManager sm = new SecurityManager();
    private static final Path USER_FILE = Paths.get(System.getenv("LOCALAPPDATA"), "Programs", "Common").resolve("user.json");
    private static LoginHandler instance = new LoginHandler();
    public static int userid;
    public static String SessionID;
    public static String plan;
    public static int red;
    public static int green;
    public static int blue;
    public static boolean searchBar;
    public static boolean glow;
    public static int keybind;
    public static int selectedModeIndex;
    private Networking networking = new Networking();

    public void authenticate(String username) {
        try {
            net.fabricmc.fabric.managers.SessionManager.createInstance(username, "local", "local-hwid", "local-session");
            net.fabricmc.fabric.security.checks.SecurityManager.e = true;
            net.fabricmc.fabric.ClientMain.mc.submit(() -> net.fabricmc.fabric.ClientMain.mc.setScreen(null));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoginHandler getInstance() {
        return instance;
    }

    static {
        plan = UserConstants.PLAN.getValue().toString();
    }
}
