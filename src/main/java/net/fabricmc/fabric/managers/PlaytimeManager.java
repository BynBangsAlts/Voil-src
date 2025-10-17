package net.fabricmc.fabric.managers;

public class PlaytimeManager {
    private long loginTime;
    private static long totalPlaytime;
    public static boolean timerOn;

    public PlaytimeManager() {
        totalPlaytime = 0L;
        this.loginTime = System.currentTimeMillis();
    }

    public void resetTimer() {
        totalPlaytime = 0L;
        this.loginTime = System.currentTimeMillis();
    }

    public void addTime() {
        long currentTime = System.currentTimeMillis();
        long sessionPlaytime = currentTime - this.loginTime;
        totalPlaytime += sessionPlaytime / 1000L;
        this.loginTime = currentTime;
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public static String getFormattedPlaytime() {
        long hours = totalPlaytime / 3600L;
        long minutes = totalPlaytime % 3600L / 60L;
        long seconds = totalPlaytime % 60L;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    static {
        timerOn = false;
    }
}
