package net.fabricmc.fabric.security;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.SessionManager;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.security.Util.SystemInfo;
import net.fabricmc.fabric.security.checks.SecurityManager;
import net.fabricmc.fabric.systems.config.ConfigWrapper;
import net.fabricmc.fabric.systems.config.JsonUtils;
import net.fabricmc.fabric.systems.settings.SettingsManager;
import net.fabricmc.fabric.utils.encryption.AESUtil;

public class Networking {
    private static final String HOST = "51.38.127.0";
    private static final int PORT = 2098;
    public static Networking instance = new Networking();
    private SecurityManager sm = new SecurityManager();
    private volatile Socket socket;
    private volatile DataInputStream in;
    private volatile DataOutputStream out;
    private volatile boolean running;
    private Thread connectThread;
    private Thread listenThread;
    private Thread sendThread;
    private final BlockingQueue<String> sendQueue = new LinkedBlockingQueue<String>();

    public void start() {
    }

    public boolean isConnectedTo(String ip, int port) {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed() && this.socket.getPort() == port && (this.socket.getInetAddress().getHostAddress().equals(ip) || this.socket.getInetAddress().getHostName().equalsIgnoreCase(ip));
    }

    public void startConnectThread() {
    }

    private void startListenThread() {
    }

    private void startSendThread() {
    }

    public void sendPacket(Object packet) {
    }

    private void resetConnection() {
    }

    public void close() {
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private String generateKey() throws Exception {
        return "";
    }

    private void handleServerMessage(String json) {
        JsonObject resp = JsonParser.parseString((String)json).getAsJsonObject();
        String type = resp.has("id") && resp.has("sessionId") ? "auth" : (resp.has("settings") ? "settings" : (resp.has("theme") ? "theme" : (resp.has("status") && "error".equalsIgnoreCase(resp.get("status").getAsString()) ? "error" : (resp.has("config") && resp.get("config").isJsonObject() ? "config" : (resp.has("list") && resp.get("list").isJsonArray() ? "configlist" : (resp.has("message") && resp.get("message").isJsonPrimitive() ? "irc" : "unknown"))))));
        System.out.println(resp);
        switch (type) {
            case "auth": {

                break;
            }
            case "configlist": {
                JsonArray list = resp.getAsJsonArray("list");
                ArrayList<ConfigWrapper> wrappers = new ArrayList<ConfigWrapper>();
                for (JsonElement el : list) {
                    JsonObject item = el.getAsJsonObject();
                    String user = item.get("user").getAsString();
                    String name = item.get("name").getAsString();
                    String id = item.get("id").getAsString();
                    String description = item.get("description").getAsString();
                    wrappers.add(new ConfigWrapper(user, name, description, id, "", new JsonObject()));
                    UserConstants.configs.put(name, (ConfigWrapper)wrappers.get(wrappers.size() - 1));
                }
                break;
            }
            case "settings": {
                JsonElement raw = resp.get("settings");
                JsonObject settingsJson = raw.isJsonObject() ? raw.getAsJsonObject() : JsonParser.parseString((String)raw.getAsString()).getAsJsonObject();
                SettingsManager.loadsettings(settingsJson.toString());
                break;
            }
            case "theme": {
                JsonElement raw = resp.get("theme");
                JsonObject themeJson = raw.isJsonObject() ? raw.getAsJsonObject() : JsonParser.parseString((String)raw.getAsString()).getAsJsonObject();
                Theme.loadTheme(themeJson.toString());
                break;
            }
            case "irc": {
                String msg = resp.get("message").getAsString();
                UserConstants.ircMessages.computeIfAbsent("IRC", k -> new ArrayList()).add(msg);
                break;
            }
            case "config": {
                JsonObject outerObj;
                JsonElement innerConfig;
                JsonElement outerConfig = resp.get("config");
                if (outerConfig == null || !outerConfig.isJsonObject() || (innerConfig = (outerObj = outerConfig.getAsJsonObject()).get("config")) == null || !innerConfig.isJsonObject()) break;
                JsonObject settingsOnly = innerConfig.getAsJsonObject();
                SettingsManager.loadsettings(settingsOnly.toString());
                break;
            }
            case "error": {
                break;
            }
        }
    }

    public void sendLoginRequest(String username) {
    }

    public void sendIRC(String message) {
    }

    public void sendGetConfig(String id) {
    }

    public void sendKeepAlive() {
    }

    public void sendGetSettings() {
    }

    public void sendSettings(JsonObject settings) {
    }

    public void sendGetTheme() {
    }

    public void sendTheme(JsonObject theme) {
    }

    public void sendConfig(JsonObject config, String name, String description, boolean isPublic) {
    }

    public void sendGetAllConfigs() {
    }

    public void sendDeleteConfig(String id) {
    }
}
