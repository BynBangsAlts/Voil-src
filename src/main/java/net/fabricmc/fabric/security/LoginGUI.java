package net.fabricmc.fabric.security;

import java.awt.Color;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.security.components.Button;
import net.fabricmc.fabric.security.components.TextField;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class LoginGUI
extends Screen {
    private TextFieldWidget usernameField;
    private Button loginButton;
    private Map<String, String> credentials = null;
    String savedUsername = this.credentials != null ? this.credentials.getOrDefault("username", "") : "";
    String savedPassword = this.credentials != null ? this.credentials.getOrDefault("password", "") : "";

    public LoginGUI() {
        super((Text)Text.literal((String)"login to continue"));
    }

    protected void init() {
        int fieldWidth = 125;
        int fieldHeight = 20;
        int fieldX = this.width / 2 - fieldWidth / 2;
        int fieldY = 225;
        this.usernameField = new TextField(ClientMain.mc.textRenderer, fieldX, fieldY, fieldWidth, fieldHeight, Text.of((String)"Username"));
        this.usernameField.setMaxLength(24);
        this.addSelectableChild(this.usernameField);
        this.usernameField.setText(this.savedUsername);
        this.setInitialFocus(this.usernameField);
        this.loginButton = new Button(fieldX, fieldY + 30, fieldWidth, fieldHeight, (Text)Text.translatable((String)"Login"), button -> this.onLoginButtonClick());
        this.addDrawableChild(this.usernameField);
        this.addDrawableChild(this.loginButton);
        super.init();
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        int screenWidth = ClientMain.mc.getWindow().getScaledWidth();
        int screenHeight = ClientMain.mc.getWindow().getScaledHeight();
        int textWidth = (int)ClientMain.fontRenderer.getWidth("Voil.lol industrious");
        int x = screenWidth - textWidth - 10;
        int y = (int)((float)screenHeight - ClientMain.fontRenderer.getFont().getSize2D() - 10.0f);
        ClientMain.fontRenderer.draw(matrices.getMatrices(), "Voil.lol industrious", x, y, new Color(156, 107, 173).getRGB());
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void tick() {
        String username = this.usernameField.getText();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            return true;
        }
        if (keyCode == 257) {
            this.onLoginButtonClick();
            return true;
        }
        return this.usernameField.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int keyCode) {
        return this.usernameField.charTyped(chr, keyCode) || super.charTyped(chr, keyCode);
    }

    private void onLoginButtonClick() {
        String username = this.usernameField.getText();
        UserConstants.USERNAME.setValue(username);
        LoginHandler.getInstance().authenticate(username);
    }
}
