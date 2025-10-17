package net.fabricmc.fabric.gui.screens;

import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigScreen
extends Component {
    public final ClickGUI parent;
    private int screenX;
    private int screenY;

    public ConfigScreen(ClickGUI parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }
}
