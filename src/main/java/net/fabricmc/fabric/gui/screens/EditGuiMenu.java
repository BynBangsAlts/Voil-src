package net.fabricmc.fabric.gui.screens;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.systems.module.impl.misc.SelfDestruct;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;

public class EditGuiMenu
extends Component {
    public final ClickGUI parent;
    private boolean toggleState = false;
    public static final Map<String, Boolean> toggleStates = new HashMap<String, Boolean>();
    private static List<String> modeOptions = Arrays.asList("Dark", "Light", "Moonlight", "Volcanic", "Cherry");
    private boolean awaitingKeybind = false;
    private int selectedModeIndex = LoginHandler.selectedModeIndex;
    private int keybind = LoginHandler.keybind;
    public static final Path configsDirectory = Paths.get(System.getenv("LOCALAPPDATA"), "Programs", "Common");

    public EditGuiMenu(ClickGUI parent) {
        this.parent = parent;
        toggleStates.put("Search Bar", LoginHandler.searchBar);
        toggleStates.put("Glow", LoginHandler.glow);
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = (int)this.parent.windowX;
        int y = (int)this.parent.windowY;
        this.drawBooleanToggle(matrices, x + 220, y + 120, "Glow", LoginHandler.glow);
        this.drawBooleanToggle(matrices, x + 220, y + 135, "Search Bar", LoginHandler.searchBar);
        this.drawModeDropdown(matrices, x - 260, y + 95, LoginHandler.selectedModeIndex);
        this.drawKeybindSetting(matrices, x - 200, y + 60);
        this.drawSelfDestructButton(matrices, x - 60, y + 400);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        SelfDestruct selfDestruct;
        int x = (int)this.parent.windowX;
        int y = (int)this.parent.windowY;
        if (this.isMouseOverBooleanToggle(mouseX, mouseY, x + 185, y + 120)) {
            LoginHandler.glow = !LoginHandler.glow;
            toggleStates.put("Glow", LoginHandler.glow);
            this.saveSettings();
            return true;
        }
        if (this.isMouseOverBooleanToggle(mouseX, mouseY, x + 185, y + 135)) {
            LoginHandler.searchBar = !LoginHandler.searchBar;
            toggleStates.put("Search Bar", LoginHandler.searchBar);
            this.saveSettings();
            return true;
        }
        if (this.isMouseOverDropdown(mouseX, mouseY, x + 180, y + 95)) {
            this.selectedModeIndex = LoginHandler.selectedModeIndex = (LoginHandler.selectedModeIndex + 1) % modeOptions.size();
            EditGuiMenu.applyTheme(modeOptions.get(LoginHandler.selectedModeIndex));
            this.saveSettings();
            return true;
        }
        if (this.isMouseOverKeybind(mouseX, mouseY, x - 200, y + 60)) {
            this.awaitingKeybind = true;
            return true;
        }
        if (this.isMouseOverSelfDestructButton(mouseX, mouseY, x - 60, y + 400) && (selfDestruct = (SelfDestruct)ModuleManager.INSTANCE.getModuleByName("SelfDestruct")) != null) {
            selfDestruct.onEnable();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.awaitingKeybind) {
            LoginHandler.keybind = keyCode;
            this.keybind = keyCode;
            this.saveSettings();
            this.awaitingKeybind = false;
            return true;
        }
        super.keyPressed(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    private static void applyTheme(String mode) {
        Objects.requireNonNull(mode);
    }

    private void drawBooleanToggle(MatrixStack matrices, int x, int y, String name, boolean currentState) {
        int toggleX = x - 30;
        int toggleY = y + 2;
        int toggleWidth = 20;
        int toggleHeight = 14;
        int toggleRadius = 4;
        int textColor = currentState ? -1 : Theme.SLIDER_SETTING_BG.getRGB();
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, name, (float)(x + 10), (float)(y + 8), textColor);
        Render2DEngine.renderRoundedQuad(matrices, toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, toggleRadius, 20.0, Theme.TOGGLE_BUTTON_BG);
        Render2DEngine.drawCircle(matrices, (double)(toggleX + 10), (double)(toggleY + 7), 3.5, 10.0, currentState ? Color.white : Color.gray);
    }

    private void drawModeDropdown(MatrixStack matrices, int x, int y, int selectedModeIndex) {
        Render2DEngine.renderRoundedQuad(matrices, x + 445, y + 2, x + 445 + 200, y + 22, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, x + 446, y + 3, x + 446 + 198, y + 21, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        String displayText = modeOptions.get(selectedModeIndex);
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, displayText, (float)(x + 455) + this.parent.settingsFieldX, (float)(y + 10), Theme.NORMAL_TEXT_COLOR.getRGB());
    }

    private void drawSelfDestructButton(MatrixStack matrices, int x, int y) {
        Color borderColor = new Color(-2130771968, true);
        Color innerColor = new Color(-1291845632, true);
        Render2DEngine.renderRoundedQuad(matrices, x + 445, y + 2, x + 445 + 200, y + 22, 8.0, 10.0, borderColor);
        Render2DEngine.renderRoundedQuad(matrices, x + 446, y + 3, x + 446 + 198, y + 21, 6.0, 10.0, innerColor);
        String displayText = "Self Destruct";
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, displayText, (float)(x + 500), (float)(y + 11), Theme.NORMAL_TEXT_COLOR.getRGB());
    }

    private void drawKeybindSetting(MatrixStack matrices, int x, int y) {
        Render2DEngine.renderRoundedQuad(matrices, x + 445, y + 2, x + 445 + 200, y + 22, 8.0, 10.0, Theme.MODE_SETTING_BG);
        Render2DEngine.renderRoundedQuad(matrices, x + 446, y + 3, x + 446 + 198, y + 21, 6.0, 10.0, Theme.MODE_SETTING_FILL);
        String displayText = "Current keybind: " + (this.awaitingKeybind ? "Press a key..." : (LoginHandler.keybind == -1 ? "None" : KeyUtils.getKey(LoginHandler.keybind)));
        ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, displayText, (float)(x + 455) + this.parent.settingsFieldX, (float)(y + 10), Theme.NORMAL_TEXT_COLOR.getRGB());
    }

    private boolean isMouseOverDropdown(double mouseX, double mouseY, int x, int y) {
        return mouseX >= (double)x && mouseX <= (double)(x + 200) && mouseY >= (double)y && mouseY <= (double)(y + 12);
    }

    private boolean isMouseOverBooleanToggle(double mouseX, double mouseY, int x, int y) {
        int toggleWidth = 16;
        int toggleHeight = 12;
        return mouseX >= (double)x && mouseX <= (double)(x + toggleWidth) && mouseY >= (double)y && mouseY <= (double)(y + toggleHeight);
    }

    private boolean isMouseOverSelfDestructButton(double mouseX, double mouseY, int x, int y) {
        int buttonWidth = 200;
        int buttonHeight = 22;
        return mouseX >= (double)(x + 445) && mouseX <= (double)(x + 445 + buttonWidth) && mouseY >= (double)(y + 2) && mouseY <= (double)(y + 2 + buttonHeight);
    }

    private boolean isMouseOverKeybind(double mouseX, double mouseY, int x, int y) {
        int buttonWidth = 200;
        int buttonHeight = 22;
        return mouseX >= (double)(x + 445) && mouseX <= (double)(x + 445 + buttonWidth) && mouseY >= (double)(y + 2) && mouseY <= (double)(y + 2 + buttonHeight);
    }

    public void saveSettings() {
        try {
            JsonObject settingsJson = new JsonObject();
            JsonObject toggleStatesObject = new JsonObject();
            for (Map.Entry<String, Boolean> entry : toggleStates.entrySet()) {
                toggleStatesObject.addProperty(entry.getKey(), entry.getValue());
            }
            settingsJson.addProperty("type", "6");
            settingsJson.addProperty("userId", (Number)LoginHandler.userid);
            settingsJson.add("toggleStates", (JsonElement)toggleStatesObject);
            settingsJson.addProperty("selectedModeIndex", (Number)this.selectedModeIndex);
            settingsJson.addProperty("keybind", (Number)this.keybind);
            Gson gson = new Gson();
            String json = gson.toJson((JsonElement)settingsJson);
            this.sendSettingsUpdate(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSettingsUpdate(String settingsJson) {
        try (Socket socket = new Socket("158.101.220.143", 4000);
             OutputStream outputStream = socket.getOutputStream();
             PrintWriter out = new PrintWriter(outputStream, true);){
            out.println(settingsJson);
            System.out.println("Sending settings: " + settingsJson);
            System.out.println("Settings sent successfully.");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Failed to send settings update");
        }
    }
}
