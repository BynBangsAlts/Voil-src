package net.fabricmc.fabric.managers;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.Render2DEvent;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.Client.Notifications;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;

public class NotificationManager {
    private final Deque<Notification> notifications = new ArrayDeque<Notification>(10);

    public void sendNotification(String title, CharSequence message, NotificationPosition position) {
        if (ClientMain.mc.player == null) {
            return;
        }
        int max = 10;
        if (this.notifications.size() >= max) {
            this.notifications.remove(0);
        }
        Notification notification = new Notification(title, message, position);
        float width = 150.0f;
        float height = 50.0f;
        notification.x = this.calculateXPosition(position, width);
        notification.y = this.calculateInitialYPosition(position, height);
        notification.targetY = this.calculateYPosition(position, height, this.notifications.size());
        this.notifications.add(notification);
    }

    @EventHandler
    public void onRender(Render2DEvent e) {
        if (ClientMain.mc.player == null || this.notifications.isEmpty()) {
            return;
        }
        if (!ModuleManager.INSTANCE.getModuleByClass(Notifications.class).isEnabled()) {
            return;
        }
        MatrixStack matrices = e.getMatrixStack();
        Iterator<Notification> iterator = this.notifications.iterator();
        while (iterator.hasNext()) {
            Notification notification = iterator.next();
            long timeElapsed = System.currentTimeMillis() - notification.startTime;
            if (timeElapsed > 3000L) {
                iterator.remove();
                continue;
            }
            float width = 150.0f;
            float height = 50.0f;
            notification.y += (notification.targetY - notification.y) * 0.1f;
            if (timeElapsed > 2500L) {
                notification.y += Math.min(50.0f, (float)(timeElapsed - 2500L) * 0.1f);
            }
            Render2DEngine.drawRoundedBlur(matrices, notification.x, notification.y, width, height, 12.0f, 10.0f, 0.5f, true);
            ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), notification.title.toString(), (int)(notification.x + 10.0f), (int)(notification.y + 10.0f), 0, new Color(230, 80, 200), new Color(120, 21, 178), Font.VERDANA, false);
            ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), notification.message.toString(), (int)(notification.x + 10.0f), (int)(notification.y + 30.0f), 0, Color.WHITE, Color.white.darker(), Font.VERDANA, false);
        }
    }

    private float calculateXPosition(NotificationPosition position, float width) {
        if (position == NotificationPosition.TOP_RIGHT || position == NotificationPosition.BOTTOM_RIGHT) {
            return (float)ClientMain.mc.getWindow().getScaledWidth() - width - 10.0f;
        }
        return 10.0f;
    }

    private float calculateInitialYPosition(NotificationPosition position, float height) {
        if (position == NotificationPosition.BOTTOM_LEFT || position == NotificationPosition.BOTTOM_RIGHT) {
            return ClientMain.mc.getWindow().getScaledHeight();
        }
        return -height;
    }

    private float calculateYPosition(NotificationPosition position, float height, int index) {
        if (position == NotificationPosition.BOTTOM_LEFT || position == NotificationPosition.BOTTOM_RIGHT) {
            return (float)ClientMain.mc.getWindow().getScaledHeight() - height - 10.0f - (float)index * (height + 5.0f);
        }
        return 10.0f + (float)index * (height + 5.0f);
    }

    private static class Notification {
        CharSequence title;
        CharSequence message;
        NotificationPosition position;
        float x;
        float y;
        float targetY;
        long startTime;

        public Notification(CharSequence title, CharSequence message, NotificationPosition position) {
            this.title = title;
            this.message = message;
            this.position = position;
            this.startTime = System.currentTimeMillis();
        }
    }

    public static enum NotificationPosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT;

    }

    public static enum NotificationType {
        INFO,
        WARNING,
        ERROR;

    }
}
