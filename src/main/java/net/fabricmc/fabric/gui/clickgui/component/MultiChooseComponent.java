package net.fabricmc.fabric.gui.clickgui.component;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.MultiOptionSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;

public class MultiChooseComponent
extends Component {
    private final MultiOptionSetting setting;
    private final GUI parent;
    private boolean isExpanded = false;
    int keyCodec;

    public MultiChooseComponent(MultiOptionSetting setting, GUI parent, int x, int y) {
        super(setting);
        this.setting = setting;
        this.parent = parent;
        this.setPosition(0.0f, 0.0f);
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int optionY = (int)this.y;
        int spacing = 15;
        Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 450.0f, optionY, this.x + (float)this.parent.x + 450.0f + ClientMain.fontRenderer.getWidth(this.setting.getName()) + 20.0f, optionY + 15, 3.0, 10.0, new Color(47, 47, 47, 255));
        ClientMain.fontRenderer.draw(matrices, this.setting.getName(), this.x + (float)this.parent.x + 460.0f, optionY - 3, 0xFFFFFF);
        int arrowColor = this.isExpanded ? 65280 : 0xFFFFFF;
        ClientMain.fontRenderer.draw(matrices, this.isExpanded ? "\u25b2" : "\u25bc", this.x + (float)this.parent.x + 600.0f, optionY + 4, arrowColor);
        optionY += 20;
        if (this.isExpanded) {
            Render2DEngine.renderRoundedQuad(matrices, this.x + (float)this.parent.x + 450.0f, optionY - 5, this.x + (float)this.parent.x + 550.0f, optionY + this.setting.getOptions().size() * spacing + 5, 3.0, 10.0, new Color(47, 46, 46, 255));
            for (String option : this.setting.getOptions()) {
                boolean isSelected = this.setting.getSelectedOptions().contains(option);
                int optionColor = isSelected ? 65280 : 0xFFFFFF;
                ClientMain.fontRenderer.draw(matrices, option, this.x + (float)this.parent.x + 465.0f, optionY, optionColor);
                Color dotColor = isSelected ? Theme.ENABLED : Color.GRAY;
                Render2DEngine.drawCircle(matrices, this.x + (float)this.parent.x + 455.0f, optionY + 5, 3.0f, 1.0f, dotColor);
                optionY += spacing;
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int spacing = 15;
        int optionY = (int)(this.y + 20.0f);
        if (MultiChooseComponent.isHovered(this.x + (float)this.parent.x + 450.0f, this.y, this.x + (float)this.parent.x + 600.0f, this.y + 15.0f, mouseX, mouseY)) {
            this.isExpanded = !this.isExpanded;
            return;
        }
        if (this.isExpanded) {
            for (String option : this.setting.getOptions()) {
                if (MultiChooseComponent.isHovered(this.x + (float)this.parent.x + 450.0f, optionY - 2, this.x + (float)this.parent.x + 550.0f, optionY + 10, mouseX, mouseY)) {
                    if (this.setting.getSelectedOptions().contains(option)) {
                        this.setting.unselect(option);
                        break;
                    }
                    this.setting.select(option);
                    break;
                }
                optionY += spacing;
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    @Override
    public Bounds getBounds() {
        int width = (int)(ClientMain.fontRenderer.getWidth(this.setting.getName()) + 20.0f);
        int height = 45;
        if (this.isExpanded) {
            height += this.setting.getOptions().size() * 15;
        }
        return new Bounds(this.x + (float)this.parent.x + 450.0f, this.y, width, height);
    }
}
