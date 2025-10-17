package net.fabricmc.fabric.managers;

import net.fabricmc.fabric.security.Networking;

public class SessionManager {
    private String username;
    private String uid;
    private String hwid;
    private String sessionID;
    private static SessionManager instance;
    private static Networking networking;

    private SessionManager(String username, String uid, String hwid, String sessionID) {
        this.username = username;
        this.uid = uid;
        this.hwid = hwid;
        this.sessionID = sessionID;
        networking = new Networking();
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public static boolean createInstance(String username, String uid, String hwid, String sessionID) {
        if (instance == null) {
            instance = new SessionManager(username, uid, hwid, sessionID);
            return true;
        }
        return false;
    }

    public static void invalidateInstance() {
        instance = null;
    }

    public static boolean isSessionValid() {
        if (instance == null) {
            return false;
        }
        if (SessionManager.instance.username == null || SessionManager.instance.username.isEmpty()) {
            return false;
        }
        if (SessionManager.instance.uid == null || SessionManager.instance.uid.isEmpty()) {
            return false;
        }
        if (SessionManager.instance.hwid == null || SessionManager.instance.hwid.isEmpty()) {
            return false;
        }
        return SessionManager.instance.sessionID != null && !SessionManager.instance.sessionID.isEmpty();
    }

    public String getUsername() {
        return this.username;
    }

    public String getUid() {
        return this.uid;
    }

    public String getHwid() {
        return this.hwid;
    }

    public String getSessionID() {
        return this.sessionID;
    }
}
