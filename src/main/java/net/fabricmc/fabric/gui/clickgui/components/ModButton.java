package net.fabricmc.fabric.gui.clickgui.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.components.ButtonComp;
import net.fabricmc.fabric.gui.clickgui.components.CheckBox;
import net.fabricmc.fabric.gui.clickgui.components.ColorPickerComponent;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.clickgui.components.DoubleSlider;
import net.fabricmc.fabric.gui.clickgui.components.KeybindComp;
import net.fabricmc.fabric.gui.clickgui.components.ModeComp;
import net.fabricmc.fabric.gui.clickgui.components.Slider;
import net.fabricmc.fabric.gui.clickgui.components.TextComp;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ButtonSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
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

public class ModButton {
    private final Module module;
    private final ClickGUI parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final List<Component> components = new ArrayList<Component>();
    private static double x;
    private double y;
    private int height;
    final float descriptionXOffset = 130.0f;

    public ModButton(Module module, double x, double y, ClickGUI parent) {
        this.module = module;
        this.y = y;
        ModButton.x = x;
        this.parent = parent;
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float adjustment = 110.0f;
        x = ModuleManager.isAnyModuleExpanded() ? (double)this.parent.settingsFieldX : 100.0;
        this.renderModuleButton(matrices, mouseX, mouseY, delta);
        if (this.module.isExpanded()) {
            this.renderSettings(matrices, mouseX, mouseY, delta);
        }
    }

    private void renderModuleButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isHovered(this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, (float)((double)this.parent.windowY + this.y - 10.0), this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (float)((double)this.parent.windowY + this.y + 25.0), mouseX, mouseY)) {
            if (this.module.isEnabled()) {
                Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y - 10.0, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + 25.0, 5.0, 20.0, Theme.MODULE_ENABLED_BG_HOVER);
            } else {
                Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y - 10.0, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + 25.0, 5.0, 20.0, Theme.MODULE_DISABLED_BG_HOVER);
            }
        } else if (this.module.isEnabled()) {
            Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y - 10.0, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + 25.0, 5.0, 20.0, Theme.MODULE_ENABLED_BG);
        } else {
            Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y - 10.0, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + 25.0, 5.0, 20.0, Theme.MODULE_DISABLED_BG);
        }
        int width = 28;
        int height = 28;
        int xOffset = 103;
        int yOffset = -7;
        Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + (float)xOffset + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + (double)yOffset, this.parent.windowX + (float)x + (float)xOffset + (float)this.parent.coordModX + (float)width, (double)this.parent.windowY + this.y + (double)yOffset + (double)height, 5.0, 20.0, Theme.MODULE_COLOR);
        Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + 410.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y - 10.0, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + 25.0, 5.0, 20.0, Theme.SETTINGS_HEADER);
        float centerX = (float)((double)this.parent.windowX + x + 417.0 + (double)this.parent.coordModX);
        float radius = 3.0f;
        int samples = 36;
        Color color = new Color(60, 62, 73, 255);
        float padding = 3.0f;
        float centerY1 = (float)((double)this.parent.windowY + this.y);
        float centerY2 = centerY1 + radius * 2.0f + padding;
        float centerY3 = centerY2 + radius * 2.0f + padding;
        Render2DEngine.drawCircle(matrices, color, (double)centerX, (double)centerY1, (double)radius, samples);
        Render2DEngine.drawCircle(matrices, color, (double)centerX, (double)centerY2, (double)radius, samples);
        Render2DEngine.drawCircle(matrices, color, (double)centerX, (double)centerY3, (double)radius, samples);
        float descriptionYOffset = 5.0f;
        if (this.isHovered(this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, this.parent.windowY + (float)this.y - 10.0f + descriptionYOffset, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, (float)((double)this.parent.windowY + this.y + 25.0) + descriptionYOffset, mouseX, mouseY)) {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getDescription() + ".", this.parent.windowX + (float)x + 130.0f + (float)this.parent.coordModX + 5.0f, this.parent.windowY + (float)(this.y + 8.0) + descriptionYOffset, Theme.MODULE_TEXT.getRGB());
        } else {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getDescription() + ".", this.parent.windowX + (float)x + 130.0f + (float)this.parent.coordModX + 5.0f, this.parent.windowY + (float)(this.y + 8.0) + descriptionYOffset, Theme.MODULE_TEXT.getRGB());
        }
        if (this.module.isEnabled()) {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getName().toString(), this.parent.windowX + (float)x + 130.0f + (float)this.parent.coordModX + 5.0f, this.parent.windowY + (float)(this.y - 2.0), Theme.NORMAL_TEXT_COLOR.getRGB());
            Render2DEngine.renderRoundedQuad(matrices, this.parent.windowX + (float)x + (float)xOffset + (float)this.parent.coordModX, (double)this.parent.windowY + this.y + (double)yOffset, this.parent.windowX + (float)x + (float)xOffset + (float)this.parent.coordModX + (float)width, (double)this.parent.windowY + this.y + (double)yOffset + (double)height, 5.0, 20.0, Theme.ENABLED);
        } else {
            ClientMain.fontRenderer.drawString(matrices, this.mc.textRenderer, this.module.getName().toString(), this.parent.windowX + (float)x + 130.0f + (float)this.parent.coordModX + 5.0f, this.parent.windowY + (float)(this.y - 2.0), Theme.MODULE_TEXT.getRGB());
        }
    }

    private void renderSettings(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float settingsStartX = (float)((double)this.parent.windowX + x + 100.0);
        float settingsStartY = (float)((double)this.parent.windowY + this.y + 30.0);
        float settingsWidth = 325.0f;
        float settingsHeight = 200.0f;
        float padding = 10.0f;
        float componentStartX = settingsStartX + padding;
        float componentY = settingsStartY + padding;
        float componentSpacing = 5.0f;
        float componentHeight = 20.0f;
        int threshold = 4;
        for (int i = 0; i < this.components.size(); ++i) {
            Component component = this.components.get(i);
            if (i >= threshold) {
                componentStartX = settingsStartX + settingsWidth + padding * 2.0f;
                componentY = settingsStartY + padding + (float)(i - threshold) * (componentHeight + componentSpacing);
            } else {
                componentStartX = settingsStartX + padding;
                componentY = settingsStartY + padding + (float)i * (componentHeight + componentSpacing);
            }
            component.setPosition(componentStartX, componentY);
            component.drawScreen(matrices, mouseX, mouseY, delta);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered(this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, this.parent.windowY + (float)this.y - 10.0f, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, this.parent.windowY + (float)this.y + 25.0f, mouseX, mouseY) && button == 0) {
            this.module.toggle();
        } else if (this.isHovered(this.parent.windowX + (float)x + 100.0f + (float)this.parent.coordModX, this.parent.windowY + (float)this.y - 10.0f, this.parent.windowX + (float)x + 425.0f + (float)this.parent.coordModX, this.parent.windowY + (float)this.y + 25.0f, mouseX, mouseY) && button == 1) {
            if (this.parent.selectedModule != null && this.parent.selectedModule != this.module) {
                this.parent.selectedModule = null;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory)) {
                    module.setExpanded(false);
                }
            }
            if (this.parent.selectedModule != this.module) {
                this.parent.settingsFieldX = 0.0f;
                this.parent.selectedModule = this.module;
                this.components.clear();
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory)) {
                    if (module == this.module) continue;
                    module.setExpanded(false);
                }
                this.module.setExpanded(true);
                float settingsY = this.parent.windowY + 100.0f;
                float settingsX = 0.0f;
                for (Setting setting : this.module.getSettings()) {
                    if (setting instanceof BooleanSetting) {
                        BooleanSetting booleanSetting = (BooleanSetting)setting;
                        this.components.add(new CheckBox(booleanSetting, this.parent, settingsX, settingsY){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof NumberSetting) {
                        NumberSetting numberSetting = (NumberSetting)setting;
                        this.components.add(new Slider(numberSetting, settingsX, settingsY, this.parent){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof ModeSetting) {
                        ModeSetting modeSetting = (ModeSetting)setting;
                        this.components.add(new ModeComp(modeSetting, this.parent, settingsX, settingsY){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof KeybindSetting) {
                        KeybindSetting keybindSetting = (KeybindSetting)setting;
                        this.components.add(new KeybindComp(keybindSetting, this.parent, settingsX, settingsY, this.module){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof TextSetting) {
                        TextSetting textSetting = (TextSetting)setting;
                        this.components.add(new TextComp(this.parent, textSetting, settingsX, settingsY, this.module){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof ColorPickerSetting) {
                        ColorPickerSetting colorPickerSetting = (ColorPickerSetting)setting;
                        this.components.add(new ColorPickerComponent(colorPickerSetting, this.parent, settingsX, (int)settingsY, this.module){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof ButtonSetting) {
                        ButtonSetting buttonSetting = (ButtonSetting)setting;
                        this.components.add(new ButtonComp(buttonSetting, this.parent, settingsX, settingsY, this.module){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    } else if (setting instanceof NumberSetting2) {
                        NumberSetting2 NumberSetting22 = (NumberSetting2)setting;
                        this.components.add(new DoubleSlider(NumberSetting22, settingsX, settingsY, this.parent){

                            @Override
                            public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                            }
                        });
                    }
                    settingsY += 25.0f;
                }
            } else if (this.parent.selectedModule == this.module) {
                this.parent.selectedModule = null;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.parent.selectedCategory)) {
                    module.setExpanded(false);
                }
            }
        }
        if (this.module.isExpanded()) {
            this.components.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
        }
        return false;
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

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        ModButton.x = x;
    }

    public static double getX() {
        return x;
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
