package net.fabricmc.fabric.utils.packet.backtrack;

public class CooldownTimer {
    private long cooldownDuration;
    private long lastStartTime;
    private boolean hasCompleted;

    public CooldownTimer(long cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }

    public CooldownTimer() {
        this.cooldownDuration = System.currentTimeMillis();
    }

    public void startCooldown() {
        this.resetTimer();
        this.hasCompleted = false;
    }

    public void resetTimer() {
        this.lastStartTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return Math.max(0L, System.currentTimeMillis() - this.lastStartTime);
    }

    public boolean hasCooldownElapsed(long delay) {
        return this.getElapsedTime() >= delay;
    }
}
