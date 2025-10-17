package net.fabricmc.fabric.gui.screens;

import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.PlaytimeManager;
import net.fabricmc.fabric.managers.SessionManager;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class ProfileScreen
extends Component {
    public final ClickGUI parent;

    public ProfileScreen(ClickGUI parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = (int)this.parent.windowX;
        int y = (int)this.parent.windowY;
        Render2DEngine.renderRoundedQuad(matrices, x + 200, y + 50, x + 340, y + 150, 3.0, 10.0, new Color(38, 38, 38));
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "User Info: ", (float)(x + 200), (float)(y + 60), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "User ID: " + SessionManager.getInstance().getUid(), (float)(x + 205), (float)(y + 85), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Username: " + SessionManager.getInstance().getUsername(), (float)(x + 205), (float)(y + 95), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Plan: " + LoginHandler.plan, (float)(x + 205), (float)(y + 105), -1);
        Render2DEngine.renderRoundedQuad(matrices, x + 345, y + 40, x + 599, y + 165, 3.0, 10.0, new Color(38, 38, 38));
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), (PlayerEntity)ClientMain.mc.player, x + 540, y + 45, 46);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Session Info:", (float)(x + 360), (float)(y + 50), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Name: " + ClientMain.mc.getSession().getUsername(), (float)(x + 350), (float)(y + 100), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "UUID: " + String.valueOf(ClientMain.mc.getSession().getUuidOrNull()), (float)(x + 350), (float)(y + 115), -1);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Playtime: " + PlaytimeManager.getFormattedPlaytime(), (float)(x + 350), (float)(y + 130), -1);
        int discordX = x + 510;
        int discordY = y + 420;
        int buttonWidth = 60;
        int buttonHeight = 20;
        double shadowWidth = 5.0;
        Render2DEngine.renderRoundedShadow(RenderHelper.getContext(), Theme.ENABLED, (double)discordX - shadowWidth + 4.0, (double)discordY - shadowWidth + 4.0, (double)(discordX + buttonWidth) + shadowWidth - 4.0, (double)(discordY + buttonHeight) + shadowWidth - 4.0, 3.0, 10.0, shadowWidth);
        Render2DEngine.renderRoundedQuad(matrices, discordX, discordY, discordX + buttonWidth, discordY + buttonHeight, 3.0, 10.0, new Color(30, 30, 30));
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, "Discord", (float)(discordX + 10), (float)(discordY + 10), -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int discordX = (int)(this.x + 510.0);
        int discordY = (int)(this.y + 420.0);
        int buttonWidth = 60;
        int buttonHeight = 20;
        if (mouseX >= (double)discordX && mouseX <= (double)(discordX + buttonWidth) && mouseY >= (double)discordY && mouseY <= (double)(discordY + buttonHeight) && button == 0) {
            this.openDiscordLink();
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void openDiscordLink() {
        try {
            Desktop.getDesktop().browse(new URI("https://discord.gg/your-invite-code"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }
}
