package net.fabricmc.fabric.gui.screens;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.screens.ConfigEditorScreen;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.systems.settings.SettingsManager;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;

public class CreateConfigPopup
extends Component {
    private String name = "";
    private String description = "";
    private boolean isPublic = false;
    private boolean focusedName = false;
    private boolean focusedDescription = false;
    private final ConfigEditorScreen parent;

    public CreateConfigPopup(ConfigEditorScreen parent) {
        super(new Setting("Create Config"));
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = this.parent.parent.x + 200;
        int y = this.parent.parent.y + 50;
        int width = 300;
        int height = 150;
        Render2DEngine.fill(matrices, x, y, x + width, y + height, Theme.LEFT_PANEL.getRGB());
        ClientMain.fontRenderer.draw(matrices, "X", x + 5, y - 5, 0xFFFFFF);
        ClientMain.fontRenderer.draw(matrices, "Name:", x + 10, y + 10, 0xFFFFFF);
        Render2DEngine.drawRectangle(matrices, x + 60, y + 15, 200.0f, 12.0f, 2.0f, 1.0f, Theme.MODULE_DISABLED_BG);
        ClientMain.fontRenderer.draw(matrices, this.name + (this.focusedName ? "|" : ""), x + 62, y + 10, 0xCCCCCC);
        ClientMain.fontRenderer.draw(matrices, "Description:", x + 10, y + 30, 0xFFFFFF);
        Render2DEngine.drawRectangle(matrices, x + 80, y + 35, 200.0f, 12.0f, 2.0f, 1.0f, Theme.MODULE_DISABLED_BG);
        ClientMain.fontRenderer.draw(matrices, this.description + (this.focusedDescription ? "|" : ""), x + 82, y + 30, 0xCCCCCC);
        ClientMain.fontRenderer.draw(matrices, "Visibility:", x + 10, y + 50, 0xFFFFFF);
        ClientMain.fontRenderer.draw(matrices, this.isPublic ? "Public" : "Private", x + 85, y + 50, 0x88CCFF);
        Render2DEngine.drawRectangle(matrices, x + 100, y + 100, 80.0f, 15.0f, 2.0f, 1.0f, Theme.ENABLED);
        ClientMain.fontRenderer.draw(matrices, "Create", x + 120, y + 95, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int x = this.parent.parent.x + 200;
        int y = this.parent.parent.y + 50;
        if (mouseX >= (double)(x + 5) && mouseX <= (double)(x + 15) && mouseY >= (double)(y - 5) && mouseY <= (double)(y + 5)) {
            this.parent.closeCreateMenu();
            return;
        }
        this.focusedName = mouseX >= (double)(x + 60) && mouseX <= (double)(x + 260) && mouseY >= (double)(y + 15) && mouseY <= (double)(y + 20);
        boolean bl = this.focusedDescription = mouseX >= (double)(x + 80) && mouseX <= (double)(x + 280) && mouseY >= (double)(y + 35) && mouseY <= (double)(y + 40);
        if (mouseX >= (double)(x + 80) && mouseX <= (double)(x + 160) && mouseY >= (double)(y + 50) && mouseY <= (double)(y + 62)) {
            boolean bl2 = this.isPublic = !this.isPublic;
        }
        if (mouseX >= (double)(x + 100) && mouseX <= (double)(x + 180) && mouseY >= (double)(y + 100) && mouseY <= (double)(y + 115)) {
            Networking.instance.sendConfig(SettingsManager.getSettings(), this.name, this.description, this.isPublic);
            Networking.instance.sendGetAllConfigs();
            this.parent.closeCreateMenu();
        }
    }

    @Override
    public void charTyped(char chr, int keyCode) {
        if (!Character.isLetter(chr)) {
            return;
        }
        if (this.focusedName && this.name.length() < 10) {
            this.name = this.name + chr;
        } else if (this.focusedDescription && this.description.length() < 10) {
            this.description = this.description + chr;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259) {
            if (this.focusedName && this.name.length() > 0) {
                this.name = this.name.substring(0, this.name.length() - 1);
            } else if (this.focusedDescription && this.description.length() > 0) {
                this.description = this.description.substring(0, this.description.length() - 1);
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }
}
