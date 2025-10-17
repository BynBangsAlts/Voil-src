package net.fabricmc.fabric.security.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.fabricmc.fabric.security.Util.SystemInfo;

public class Util {
    public static String getOs() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static String hwidGrabber() {
        String clientHwid = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            clientHwid = SystemInfo.getWindowsHwid();
        } else if (osName.contains("mac")) {
            clientHwid = SystemInfo.getMacOSHwid();
        } else if (osName.contains("linux")) {
            clientHwid = SystemInfo.getLinuxHwid();
        } else {
            System.err.println("Unsupported OS.");
        }
        return clientHwid;
    }

    public static void checkClass(Class<?> clazz) {
        String className = clazz.getName();
        try {
            Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            System.exit(1300);
        }
    }

    public static String getFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void exit() {
        System.exit((int)(Math.random() * 1001.0));
    }
}
