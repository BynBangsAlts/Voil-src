package net.fabricmc.fabric.gui.clickgui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.ButtonComp;
import net.fabricmc.fabric.gui.clickgui.component.CheckBox;
import net.fabricmc.fabric.gui.clickgui.component.ColorPickerComponent;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.clickgui.component.CurveComponent;
import net.fabricmc.fabric.gui.clickgui.component.DoubleSlider;
import net.fabricmc.fabric.gui.clickgui.component.KeybindComp;
import net.fabricmc.fabric.gui.clickgui.component.ModeComp;
import net.fabricmc.fabric.gui.clickgui.component.MultiChooseComponent;
import net.fabricmc.fabric.gui.clickgui.component.Slider;
import net.fabricmc.fabric.gui.clickgui.component.TextComp;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ButtonSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.CurveSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.MultiOptionSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleButton {
    private final Module module;
    private final GUI parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private List<Component> components = new ArrayList<Component>();
    public double x;
    public double y;
    private int height;
    final float descriptionXOffset = 130.0f;
    private static final float expandedheight = 250.0f;
    private static final float baseheight = 35.0f;
    private static final Map<Module, List<Component>> initializedComponentsMap = new HashMap<Module, List<Component>>();
    private float initialSettingsY;

    public ModuleButton(Module module, double x, double y, GUI parent) {
        this.module = module;
        this.y = y;
        this.x = x;
        this.parent = parent;
        this.height = 35;
        this.initializeComponents();
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.module.isHidden()) {
            return;
        }
        this.renderModuleButton(matrices, mouseX, mouseY, delta);
        if (this.module.isExpanded()) {
            float settingsBaseX = (float)(this.x - 275.0);
            float settingsBaseY = (float)((double)this.parent.y + this.y + 30.0);
            int columnCount = 0;
            for (Component component : this.components) {
                Setting setting = component.getSetting();
                if (setting != null && !setting.isVisible()) continue;
                component.setPosition(settingsBaseX, settingsBaseY);
                component.drawScreen(matrices, mouseX, mouseY, delta);
                settingsBaseY += 25.0f;
                if (++columnCount < 6) continue;
                settingsBaseX += 130.0f;
                settingsBaseY = (float)((double)this.parent.y + this.y + 30.0);
                columnCount = 0;
            }
        }
    }

    public float getExpandedHeight() {
        int rows = 6;
        float base = 45.0f;
        ArrayList<Float> columnHeights = new ArrayList<Float>();
        float currentColHeight = 0.0f;
        int rowCount = 0;
        for (Component component : this.components) {
            Setting setting = component.getSetting();
            if (setting != null && !setting.isVisible()) continue;
            currentColHeight += component.getBounds().height;
            if (++rowCount < 6) continue;
            columnHeights.add(Float.valueOf(currentColHeight));
            currentColHeight = 0.0f;
            rowCount = 0;
        }
        if (rowCount > 0) {
            columnHeights.add(Float.valueOf(currentColHeight));
        }
        float tallest = columnHeights.stream().max(Float::compare).orElse(Float.valueOf(0.0f)).floatValue();
        return base + tallest + 10.0f;
    }

    public int getHeight() {
        if (!this.module.isExpanded()) {
            return 35;
        }
        int visibleComponents = 0;
        for (Component component : this.components) {
            Setting setting = component.getSetting();
            if (setting != null && !setting.isVisible()) continue;
            ++visibleComponents;
        }
        int rows = (int)Math.ceil((float)visibleComponents / 6.0f);
        return 1000;
    }

    private void renderModuleButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isHovered((float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (float)((double)this.parent.y + this.y - 10.0), (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (float)((double)this.parent.y + this.y + 25.0), mouseX, mouseY)) {
            if (this.module.isEnabled()) {
                Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (double)this.parent.y + this.y - 10.0, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (double)this.parent.y + this.y + 25.0, 5.0, 20.0, Theme.MODULE_ENABLED_BG_HOVER);
            } else {
                Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (double)this.parent.y + this.y - 10.0, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (double)this.parent.y + this.y + 25.0, 5.0, 20.0, Theme.MODULE_DISABLED_BG_HOVER);
            }
        } else if (this.module.isEnabled()) {
            Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (double)this.parent.y + this.y - 10.0, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (double)this.parent.y + this.y + 25.0, 5.0, 20.0, Theme.MODULE_ENABLED_BG);
        } else {
            Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (double)this.parent.y + this.y - 10.0, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (double)this.parent.y + this.y + 25.0, 5.0, 20.0, Theme.MODULE_DISABLED_BG);
        }
        int width = 28;
        int height = 28;
        int xOffset = 103;
        int yOffset = -7;
        Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + (float)xOffset + (float)this.parent.coordModX, (double)this.parent.y + this.y + (double)yOffset, (float)this.parent.x + (float)this.x + (float)xOffset + (float)this.parent.coordModX + (float)width, (double)this.parent.y + this.y + (double)yOffset + (double)height, 5.0, 20.0, Theme.MODULE_COLOR);
        float descriptionYOffset = 5.0f;
        if (this.isHovered((float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (float)this.parent.y + (float)this.y - 10.0f + descriptionYOffset, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX, (float)((double)this.parent.y + this.y + 25.0) + descriptionYOffset, mouseX, mouseY)) {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getDescription() + ".", (float)this.parent.x + (float)this.x + 130.0f + (float)this.parent.coordModX + 5.0f, (float)this.parent.y + (float)(this.y + 8.0) + descriptionYOffset, Theme.MODULE_TEXT.getRGB());
        } else {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getDescription() + ".", (float)this.parent.x + (float)this.x + 130.0f + (float)this.parent.coordModX + 5.0f, (float)this.parent.y + (float)(this.y + 8.0) + descriptionYOffset, Theme.MODULE_TEXT.getRGB());
        }
        if (this.module.isEnabled()) {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getName().toString(), (float)this.parent.x + (float)this.x + 130.0f + (float)this.parent.coordModX + 5.0f, (float)this.parent.y + (float)(this.y - 2.0), Theme.NORMAL_TEXT_COLOR.getRGB());
            Render2DEngine.renderRoundedQuad(matrices, (float)this.parent.x + (float)this.x + (float)xOffset + (float)this.parent.coordModX, (double)this.parent.y + this.y + (double)yOffset, (float)this.parent.x + (float)this.x + (float)xOffset + (float)this.parent.coordModX + (float)width, (double)this.parent.y + this.y + (double)yOffset + (double)height, 5.0, 20.0, Theme.ENABLED);
        } else {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getName().toString(), (float)this.parent.x + (float)this.x + 130.0f + (float)this.parent.coordModX + 5.0f, (float)this.parent.y + (float)(this.y - 2.0), Theme.MODULE_TEXT.getRGB());
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isHovered = this.isHovered((float)this.parent.x + (float)this.x + 100.0f + (float)this.parent.coordModX, (float)this.parent.y + (float)this.y - 10.0f, (float)this.parent.x + (float)this.x + 425.0f + (float)this.parent.coordModX + 40.0f, (float)this.parent.y + (float)this.y + 25.0f, mouseX, mouseY);
        if (isHovered) {
            if (button == 0) {
                this.module.toggle();
            } else if (button == 1) {
                if (this.parent.selectedModule != null && this.parent.selectedModule != this.module) {
                    this.parent.selectedModule = null;
                    ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory).forEach(m -> m.setExpanded(false));
                }
                if (this.parent.selectedModule != this.module) {
                    this.parent.settingsFieldX = 0;
                    this.parent.selectedModule = this.module;
                    ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory).forEach(m -> {
                        if (m != this.module) {
                            m.setExpanded(false);
                        }
                    });
                    this.module.setExpanded(true);
                } else {
                    this.parent.selectedModule = null;
                    ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory).forEach(m -> m.setExpanded(false));
                }
            }
        }
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
        }
        return true;
    }

    private void renderSettings(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float baseX = (float)(this.x - 275.0);
        float baseY = (float)((double)this.parent.y + this.y + 30.0);
        int columnCount = 0;
        float currentX = baseX;
        for (Component component : this.components) {
            component.drawScreen(matrices, mouseX, mouseY, delta);
            component.setPosition(currentX, baseY);
            baseY += 25.0f;
            if (++columnCount < 6) continue;
            currentX += 130.0f;
            baseY = this.parent.y + 70;
            columnCount = 0;
        }
    }

    private void initializeComponents() {
        if (initializedComponentsMap.containsKey(this.module)) {
            this.components = initializedComponentsMap.get(this.module);
            return;
        }
        float settingsStartX = (float)(this.x - 275.0);
        float settingsStartY = (float)((double)this.parent.y + this.y + 30.0);
        int columnCount = 0;
        ArrayList<Component> newComponents = new ArrayList<Component>();
        for (Setting setting : this.module.getSettings()) {
            float componentX = settingsStartX;
            float componentY = settingsStartY;
            if (setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting)setting;
                newComponents.add(new CheckBox(booleanSetting, this.parent, componentX, componentY));
            } else if (setting instanceof NumberSetting) {
                NumberSetting numberSetting = (NumberSetting)setting;
                newComponents.add(new Slider(numberSetting, componentX, componentY, this.parent));
            } else if (setting instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting)setting;
                newComponents.add(new ModeComp(modeSetting, this.parent, componentX, componentY));
            } else if (setting instanceof KeybindSetting) {
                KeybindSetting keybindSetting = (KeybindSetting)setting;
                newComponents.add(new KeybindComp(keybindSetting, this.parent, componentX, componentY, this.module));
            } else if (setting instanceof TextSetting) {
                TextSetting textSetting = (TextSetting)setting;
                newComponents.add(new TextComp(this.parent, textSetting, componentX, (int)componentY, this.module));
            } else if (setting instanceof ColorPickerSetting) {
                ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                newComponents.add(new ColorPickerComponent(colorPickerSetting, this.parent, componentX, (int)componentY, this.module));
            } else if (setting instanceof ButtonSetting) {
                ButtonSetting buttonSetting = (ButtonSetting)setting;
                newComponents.add(new ButtonComp(buttonSetting, this.parent, componentX, componentY, this.module));
            } else if (setting instanceof NumberSetting2) {
                NumberSetting2 numberSetting2 = (NumberSetting2)setting;
                newComponents.add(new DoubleSlider(numberSetting2, componentX, componentY, this.parent));
            } else if (setting instanceof MultiOptionSetting) {
                MultiOptionSetting multiOptionSetting = (MultiOptionSetting)setting;
                newComponents.add(new MultiChooseComponent(multiOptionSetting, this.parent, (int)componentX, (int)componentY));
            } else if (setting instanceof CurveSetting) {
                CurveSetting curveSetting = (CurveSetting)setting;
                newComponents.add(new CurveComponent(curveSetting, this.parent, (int)componentX, (int)componentY));
            }
            settingsStartY += 25.0f;
            if (++columnCount < 6) continue;
            settingsStartX += 130.0f;
            settingsStartY = (float)((double)this.parent.y + this.y + 30.0);
            columnCount = 0;
        }
        initializedComponentsMap.put(this.module, newComponents);
        this.components = newComponents;
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (Component component : this.components) {
            component.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.mouseReleased(mouseX, mouseY, button));
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.keyPressed(keyCode, scanCode, modifiers));
        }
    }

    public void charTyped(char chr, int modifiers) {
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.charTyped(chr, modifiers));
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.mouseScrolled(mouseX, mouseY, horizontal, vertical));
        }
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public float getTotalHeight() {
        return this.module.isExpanded() ? this.getExpandedHeight() : 35.0f;
    }

    private boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)x2 && mouseY >= (double)y && mouseY <= (double)y2;
    }

    public static int lerp(float delta, int start, int end) {
        int step = (int)Math.ceil((float)Math.abs(end - start) * delta);
        if (start < end) {
            return Math.min(start + step, end);
        }
        return Math.max(start - step, end);
    }

    public Module getModule() {
        return this.module;
    }
}
