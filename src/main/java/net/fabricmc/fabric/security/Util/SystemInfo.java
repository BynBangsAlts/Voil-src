package net.fabricmc.fabric.security.Util;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.apache.commons.codec.digest.DigestUtils;

public class SystemInfo {
    public static String getWindowsHwid() {
        try {
            String device;
            String gpu = "";
            try {
                String line;
                device = InetAddress.getLocalHost().getHostName();
                Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get name");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    gpu = line.trim();
                }
            }
            catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            String part = System.getProperty("user.name") + System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS") + "sfght2@" + device + ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getName() + gpu;
            return DigestUtils.sha256Hex((String)part);
        }
        catch (Exception ignored) {
            System.out.println("Failed To Generate an HWID");
            return null;
        }
    }

    public static String getNetworkAdress() {
        try {
            Scanner scanner = new Scanner(new URL("https://checkip.amazonaws.com").openStream(), "UTF-8");
            return scanner.nextLine();
        }
        catch (IOException ignored) {
            return "Failed";
        }
    }

    public static String getMacOSHwid() {
        try {
            String serialNumber = SystemInfo.getMacSerialNumber();
            String hardwareUUID = SystemInfo.getHardwareUUID();
            String part = System.getProperty("user.name") + System.getProperty("os.name") + System.getProperty("os.version") + System.getProperty("os.arch") + ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad() + serialNumber + hardwareUUID + "@macUnique";
            return DigestUtils.sha256Hex((String)part);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate a HWID for macOS.");
            return null;
        }
    }

    private static String getMacSerialNumber() throws Exception {
        String command = "system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'";
        return SystemInfo.executeCommand(command);
    }

    private static String getHardwareUUID() throws Exception {
        String command = "system_profiler SPHardwareDataType | awk '/UUID/ {print $3}'";
        return SystemInfo.executeCommand(command);
    }

    private static String executeCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output = reader.readLine();
        process.waitFor();
        reader.close();
        return output != null ? output.trim() : "";
    }

    public static String getLinuxHwid() {
        try {
            String hwidString = System.getProperty("os.name") + System.getProperty("os.version") + System.getProperty("os.arch") + SystemInfo.getCommandOutput("hostname") + SystemInfo.getCommandOutput("lscpu | grep 'Model name'") + SystemInfo.getCommandOutput("cat /proc/meminfo | grep 'MemTotal'");
            return DigestUtils.sha256Hex((String)hwidString);
        }
        catch (Exception e) {
            System.out.println("Failed to generate HWID for Linux");
            return null;
        }
    }

    private static String getCommandOutput(String command) {
        StringBuilder output = new StringBuilder();
        try {
            String line;
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
