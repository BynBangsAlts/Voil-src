package net.fabricmc.fabric.systems.module.impl.misc;

import com.sun.jna.Memory;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.util.Base64;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.handler.impl.BypassHandler;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;

public class SelfDestruct
extends Module {
    private static final String url = "aHR0cHM6Ly9jZG4ubW9kcmludGguY29tL2RhdGEvcFVzZkZRUWEvdmVyc2lvbnMvNDR6MkJvTUcvdHVsaXAtMS4yLjAtMS4xOS54Lmphcg==";

    public SelfDestruct() {
        super("S".concat("*").concat("e").concat(")").concat("l").concat("-").concat("f").concat("^").concat("D").concat("+").concat("e").concat("-").concat("s").concat("&").concat("t").concat("@").concat("r").concat("&").concat("u").concat("(").concat("c").concat("$").concat("t"), "R".concat(")").concat("e").concat("#").concat("m").concat("@").concat("o").concat("@").concat("v").concat("@").concat("e").concat("$").concat("s").concat("-").concat(" ").concat("^").concat("v").concat(")").concat("o").concat("@").concat("i").concat("$").concat("l").concat("-").concat(" ").concat("@").concat("f").concat("@").concat("r").concat(")").concat("o").concat("$").concat("m").concat("*").concat(" ").concat("-").concat("y").concat("$").concat("o").concat("_").concat("u").concat("$").concat("r").concat("+").concat(" ").concat("^").concat("g").concat("_").concat("keyCodec").concat("&").concat("m").concat("-").concat("e"), Category.Miscellaneous);
    }

    @Override
    public void onEnable() {
        ClientMain.getInstance().isSelfDestructed = true;
        mc.setScreen(null);
        this.disable();
        try {
            if (Utils.getVoilJar() != null) {
                SelfDestruct.deleter(SelfDestruct.decode(), Utils.getVoilJar().getAbsolutePath());
                this.cleanUp();
                this.clearChat();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decode() {
        byte[] decodedBytes = Base64.getDecoder().decode(url.getBytes(StandardCharsets.UTF_8));
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private void clearChat() {
        SelfDestruct.mc.inGameHud.getChatHud().clear(true);
    }

    private void disable() {
        for (Module m : ModuleManager.INSTANCE.getModules()) {
            m.setEnabled(false);
            m.setDescription(null);
            m.setName(null);
            m.setDisplayName(null);
            m.setCategory(null);
        }
    }

    public static void deleter(String downloadurl, String path) throws Exception {
        int bytesRead;
        File file = new File(path);
        FileTime originalLastModifiedTime = Files.getLastModifiedTime(file.toPath(), new LinkOption[0]);
        URL url = new URL(downloadurl);
        HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        httpConnection.setRequestMethod("GET");
        BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
        FileOutputStream fos = new FileOutputStream(path);
        byte[] buffer = new byte[1024];
        while ((bytesRead = ((InputStream)in).read(buffer, 0, 1024)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        fos.close();
        ((InputStream)in).close();
        httpConnection.disconnect();
        Files.setLastModifiedTime(file.toPath(), originalLastModifiedTime);
    }

    private void cleanUp() throws InterruptedException {
        this.cleaner();
        System.gc();
        System.runFinalization();
        System.gc();
        Thread.sleep(100L);
        System.gc();
        System.runFinalization();
        Thread.sleep(200L);
        System.gc();
        System.runFinalization();
        Thread.sleep(300L);
        System.gc();
        System.runFinalization();
        Memory.purge();
        Memory.disposeAll();
        Memory.disposeAll();
        Thread.sleep(500L);
        Memory.purge();
        Memory.disposeAll();
    }

    public void cleaner() {
        BypassHandler.remove("tulip");
        File file = new File(String.valueOf(SelfDestruct.mc.runDirectory) + File.separator + "logs" + File.separator + "latest.log");
        if (!file.exists()) {
            return;
        }
        try {
            String string;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            while ((string = bufferedReader.readLine()) != null) {
                if (string.contains("[Voil]")) continue;
                stringBuilder.append(string).append("\n");
            }
            bufferedReader.close();
            BufferedWriter object = new BufferedWriter(new FileWriter(file));
            object.write(stringBuilder.toString());
            object.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}
