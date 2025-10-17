package net.fabricmc.fabric.gui.screens;

import java.awt.Color;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class IRCGui
extends Component {
    private String input = "";
    private int scrollOffset = 0;
    private final int scrollAmount = 1;
    private final LinkedList<AbstractMap.SimpleEntry<String, String>> orderedMessages = new LinkedList();
    private final Set<String> seenMessages = new HashSet<String>();
    private final int maxMessages = 6;
    private boolean isFocused = false;
    public final GUI parent;
    private Networking irc;

    public IRCGui(GUI parent) {
        this.parent = parent;
        this.irc = new Networking();
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = this.parent.x;
        int y = this.parent.y;
        int textWidth = (int)ClientMain.fontRenderer.getWidth("Talk with other users!");
        int centerX = x + 180 + (340 - textWidth) / 2;
        ClientMain.fontRenderer.draw(matrices, "Talk with other users!", centerX, y + 3, 0xFFFFFF);
        Render2DEngine.fill(matrices, x + 180, y + 24, x + 520, y + 23, new Color(0x323232).getRGB());
        this.renderMessages(matrices, x + 180, y + 230, 100, 100);
        Render2DEngine.renderRoundedQuad(matrices, x + 177, y + 287, x + 523, y + 323, 10.0, 20.0, new Color(0x1C1C1C));
        Render2DEngine.renderRoundedQuad(matrices, x + 180, y + 290, x + 520, y + 320, 10.0, 20.0, new Color(47, 46, 46));
        if (this.input.isEmpty()) {
            ClientMain.fontRenderer.draw(matrices, "Type here...", x + 190, y + 295, 0x808080);
        } else if (this.isFocused) {
            ClientMain.fontRenderer.draw(matrices, this.input, x + 190, y + 295, 0xFFFFFF);
        }
        long currentTime = System.currentTimeMillis();
        long blinkInterval = 500L;
        long lastBlinkTime = currentTime - blinkInterval;
        if (this.isFocused && currentTime - lastBlinkTime >= blinkInterval) {
            lastBlinkTime = currentTime;
        }
        if (this.isFocused && !this.input.isEmpty() && currentTime - lastBlinkTime < blinkInterval / 2L) {
            int caretX = (int)((float)(x + 190) + ClientMain.fontRenderer.getWidth(this.input));
            int caretY = y + 295;
            ClientMain.fontRenderer.draw(matrices, "|", caretX, caretY, 0xFFFFFF);
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused) {
            return false;
        }

        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (modifiers & 8) != 0 : (modifiers & 2) != 0;

        if (control && keyCode == 67) {
            ClientMain.mc.keyboard.setClipboard(this.input);
            return true;
        }

        if (control && keyCode == 88) {
            ClientMain.mc.keyboard.setClipboard(this.input);
            this.input = "";
            return true;
        }

        if (keyCode == 257 || keyCode == 335) {
            if (this.input.isEmpty()) return true;
            String sanitizedMessage = this.sanitizeInput(this.input);
            this.sendMessage(sanitizedMessage);
            this.input = "";
            return true;
        }

        if (control && keyCode == 86) {
            String clipboard = ClientMain.mc.keyboard.getClipboard();
            clipboard = this.sanitizeInput(clipboard);
            if (this.input.length() + clipboard.length() > 50) {
                clipboard = clipboard.substring(0, 50 - this.input.length());
            }
            this.input = this.input + clipboard;
            return true;
        }

        if (keyCode == 259 && this.input.length() > 0) {
            this.input = this.input.substring(0, this.input.length() - 1);
        }

        return true;
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        boolean shift;
        if (!this.isFocused) {
            return;
        }
        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (modifiers & 8) != 0 : (modifiers & 2) != 0;
        boolean bl = shift = (modifiers & 1) != 0;
        if (!control && this.isValid(chr)) {
            if (shift) {
                chr = Character.toUpperCase(chr);
            }
            if (this.input.length() < 50) {
                this.input = this.input + chr;
            }
        }
    }

    private boolean isValid(char chr) {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"$%&/()=? _-:.,;+*/#'";
        return validChars.indexOf(chr) >= 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            boolean clickedInside;
            int x = this.parent.x;
            int y = this.parent.y;
            this.isFocused = clickedInside = mouseX >= (double)(x + 180) && mouseX <= (double)(x + 520) && mouseY >= (double)(y + 290) && mouseY <= (double)(y + 320);
            return clickedInside;
        }
        return false;
    }

    private String sanitizeInput(String input) {
        input = input.replaceAll("<[^>]*>", "");
        input = input.replaceAll("javascript:", "");
        input = input.replaceAll("on\\w+=", "");
        input = input.replaceAll("[^a-zA-Z0-9\\s!\"$%&/()=? _:.,;+*/#']", "");
        return input;
    }

    private void displayMessage(MatrixStack matrices, String username, String message, int x, int y, int width, int height) {
        int usernameWidth = (int)ClientMain.fontRenderer.getWidth(username);
        int messageWidth = (int)ClientMain.fontRenderer.getWidth(message);
        int textWidth = Math.max(usernameWidth, messageWidth);
        String playerName = (String)UserConstants.USERNAME.getValue();
        int posX = 0;
        posX = username.equals(playerName) ? x + width - textWidth + 230 : x + 10;
        Render2DEngine.fill(matrices, posX - 10, y, posX + textWidth + 10, y + height, new Color(0x313131).getRGB());
        ClientMain.fontRenderer.draw(matrices, username, posX, y, 0xFFFFFF);
        ClientMain.fontRenderer.draw(matrices, message, posX, y + 15, 0xFFFFFF);
    }

    public void renderMessages(MatrixStack matrices, int x, int y, int width, int height) {
        int lineHeight = 35;
        int messageSpacing = 5;
        for (Map.Entry<String, List<String>> entry : UserConstants.ircMessages.entrySet()) {
            String username = entry.getKey();
            for (String message : entry.getValue()) {
                this.orderedMessages.add(new AbstractMap.SimpleEntry<String, String>(username, message));
            }
        }
        while (this.orderedMessages.size() > 6) {
            this.orderedMessages.removeFirst();
        }
        int visibleMessages = Math.min(6, this.orderedMessages.size());
        int posY = y + height - visibleMessages * (lineHeight + messageSpacing) - 50;
        for (AbstractMap.SimpleEntry simpleEntry : this.orderedMessages) {
            posY += lineHeight + messageSpacing;
        }
    }

    private void sendMessage(String message) {
        String username = UserConstants.USERNAME.getValue().toString();
        try {
            Networking.instance.sendIRC(message);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.orderedMessages.add(new AbstractMap.SimpleEntry<String, String>(username, message));
        this.seenMessages.add(username + ":" + message);
        while (this.orderedMessages.size() > 6) {
            AbstractMap.SimpleEntry<String, String> removed = this.orderedMessages.removeFirst();
            this.seenMessages.remove(removed.getKey() + ":" + removed.getValue());
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        if (mouseX >= (double)(this.parent.x + 180) && mouseX <= (double)(this.parent.x + 520) && mouseY >= (double)(this.parent.y + 24) && mouseY <= (double)(this.parent.y + 230)) {
            if (vertical > 0.0) {
                if (this.scrollOffset < this.orderedMessages.size() - 6) {
                    ++this.scrollOffset;
                }
            } else if (vertical < 0.0 && this.scrollOffset > 0) {
                --this.scrollOffset;
            }
        }
    }
}
