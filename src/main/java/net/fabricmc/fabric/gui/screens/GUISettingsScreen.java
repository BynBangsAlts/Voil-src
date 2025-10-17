package net.fabricmc.fabric.gui.screens;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;

public class GUISettingsScreen
extends Component {
    private final GUI parent;
    private final List<ClickableComponent> clickableComponents = new ArrayList<ClickableComponent>();
    private boolean waitingForKey = false;
    private String selectedTab = "Settings";
    private int themeScroll = 0;
    private boolean picking = false;
    private float pickerHue;
    private float pickerSat;
    private float pickerBri;
    private ColorEntry activeEntry;
    private Color previewColor;
    private Rectangle sbBounds;
    private Rectangle hueBounds;
    private static final List<ColorEntry> colorEntries = List.of(new ColorEntry("Window Color", () -> Theme.WINDOW_COLOR, c -> {
        Theme.WINDOW_COLOR = c;
    }), new ColorEntry("Left Panel", () -> Theme.LEFT_PANEL, c -> {
        Theme.LEFT_PANEL = c;
    }), new ColorEntry("Enabled", () -> Theme.ENABLED, c -> {
        Theme.ENABLED = c;
    }), new ColorEntry("Highlight", () -> Theme.HIGHLIGHT_COLOR, c -> {
        Theme.HIGHLIGHT_COLOR = c;
    }), new ColorEntry("Settings BG", () -> Theme.SETTINGS_BG, c -> {
        Theme.SETTINGS_BG = c;
    }), new ColorEntry("Settings Header", () -> Theme.SETTINGS_HEADER, c -> {
        Theme.SETTINGS_HEADER = c;
    }), new ColorEntry("Module Enabled BG", () -> Theme.MODULE_ENABLED_BG, c -> {
        Theme.MODULE_ENABLED_BG = c;
    }), new ColorEntry("Module Disabled BG", () -> Theme.MODULE_DISABLED_BG, c -> {
        Theme.MODULE_DISABLED_BG = c;
    }), new ColorEntry("Module Color", () -> Theme.MODULE_COLOR, c -> {
        Theme.MODULE_COLOR = c;
    }), new ColorEntry("Module Text", () -> Theme.MODULE_TEXT, c -> {
        Theme.MODULE_TEXT = c;
    }), new ColorEntry("Toggle Button BG", () -> Theme.TOGGLE_BUTTON_BG, c -> {
        Theme.TOGGLE_BUTTON_BG = c;
    }), new ColorEntry("Toggle Button Fill", () -> Theme.TOGGLE_BUTTON_FILL, c -> {
        Theme.TOGGLE_BUTTON_FILL = c;
    }), new ColorEntry("Unfocused Text", () -> Theme.UNFOCUSED_TEXT_COLOR, c -> {
        Theme.UNFOCUSED_TEXT_COLOR = c;
    }), new ColorEntry("Module Enabled Hover", () -> Theme.MODULE_ENABLED_BG_HOVER, c -> {
        Theme.MODULE_ENABLED_BG_HOVER = c;
    }), new ColorEntry("Module Disabled Hover", () -> Theme.MODULE_DISABLED_BG_HOVER, c -> {
        Theme.MODULE_DISABLED_BG_HOVER = c;
    }), new ColorEntry("Normal Text", () -> Theme.NORMAL_TEXT_COLOR, c -> {
        Theme.NORMAL_TEXT_COLOR = c;
    }), new ColorEntry("Mode Setting BG", () -> Theme.MODE_SETTING_BG, c -> {
        Theme.MODE_SETTING_BG = c;
    }), new ColorEntry("Mode Setting Fill", () -> Theme.MODE_SETTING_FILL, c -> {
        Theme.MODE_SETTING_FILL = c;
    }), new ColorEntry("Slider Setting BG", () -> Theme.SLIDER_SETTING_BG, c -> {
        Theme.SLIDER_SETTING_BG = c;
    }), new ColorEntry("Config Edit BG", () -> Theme.CONFIG_EDIT_BG, c -> {
        Theme.CONFIG_EDIT_BG = c;
    }), new ColorEntry("Accent Color 1", () -> Theme.ACCENT_COLOR1, c -> {
        Theme.ACCENT_COLOR1 = c;
    }), new ColorEntry("Accent Color 2", () -> Theme.ACCENT_COLOR2, c -> {
        Theme.ACCENT_COLOR2 = c;
    }));

    public GUISettingsScreen(GUI parent) {
        super(new Setting("GUI"));
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.clickableComponents.clear();
        int x = this.parent.x;
        int y = this.parent.y;
        int titleW = (int)ClientMain.fontRenderer.getWidth("Customize client to your liking!");
        int centerX = x + 180 + (340 - titleW) / 2;
        ClientMain.fontRenderer.draw(matrices, "Customize client to your liking!", centerX, y + 3, 0xFFFFFF);
        Render2DEngine.fill(matrices, x + 180, y + 24, x + 520, y + 23, new Color(0x323232).getRGB());
        int tabH = (int)ClientMain.fontRenderer.getStringHeight("Settings", false) + 3;
        if (GUISettingsScreen.isHovered(centerX, y + 20, (float)centerX + ClientMain.fontRenderer.getWidth("Settings"), y + 20 + tabH, mouseX, mouseY)) {
            this.clickableComponents.add(new ClickableComponent(new Rectangle(centerX, y + 20, (int)ClientMain.fontRenderer.getWidth("Settings"), tabH), () -> {
                this.selectedTab = "Settings";
            }));
        }
        if (GUISettingsScreen.isHovered(centerX + 100, y + 20, (float)(centerX + 100) + ClientMain.fontRenderer.getWidth("Theme"), y + 20 + tabH, mouseX, mouseY)) {
            this.clickableComponents.add(new ClickableComponent(new Rectangle(centerX + 100, y + 20, (int)ClientMain.fontRenderer.getWidth("Theme"), tabH), () -> {
                this.selectedTab = "Theme";
            }));
        }
        if (this.selectedTab.equals("Settings")) {
            Render2DEngine.drawRectangle(matrices, centerX, y + 25, ClientMain.fontRenderer.getWidth("Settings"), tabH, 4.0f, 1.0f, Theme.ENABLED);
            this.drawSettings(matrices);
        } else {
            Render2DEngine.drawRectangle(matrices, centerX + 100, y + 25, ClientMain.fontRenderer.getWidth("Theme"), tabH, 4.0f, 1.0f, Theme.ENABLED);
            this.drawTheme(matrices, y, centerX);
        }
        ClientMain.fontRenderer.draw(matrices, "Settings", centerX, y + 20, 0xFFFFFF);
        ClientMain.fontRenderer.draw(matrices, "Theme", centerX + 100, y + 20, 0xFFFFFF);
        if (this.picking && this.activeEntry != null) {
            this.drawColorPicker(matrices);
        }
    }

    private void drawSettings(MatrixStack matrices) {
        int x = this.parent.x;
        int y = this.parent.y;
        int centerX = (int)((float)(x + 180) + (340.0f - ClientMain.fontRenderer.getWidth("Customize client to your liking!")) / 2.0f);
        this.drawKeybind(matrices, "Keybind", centerX, y + 40, (Integer)UserConstants.GUIKEY.getValue());
        this.clickableComponents.add(new ClickableComponent(new Rectangle(centerX + 150, y + 40, 100, 15), () -> {
            this.waitingForKey = true;
        }));
        this.drawBoolean(matrices, "Search Bar", centerX, y + 60, (Boolean)UserConstants.searchBar.getValue());
        this.clickableComponents.add(new ClickableComponent(new Rectangle(centerX + 150, y + 60, 15, 15), () -> UserConstants.searchBar.setValue((Boolean)UserConstants.searchBar.getValue() == false)));
        this.drawBoolean(matrices, "Glow", centerX, y + 80, (Boolean)UserConstants.glow.getValue());
        this.clickableComponents.add(new ClickableComponent(new Rectangle(centerX + 150, y + 80, 15, 15), () -> UserConstants.glow.setValue((Boolean)UserConstants.glow.getValue() == false)));
    }

    private void drawTheme(MatrixStack matrices, int baseY, int centerX) {
    }

    private void drawKeybind(MatrixStack m, String name, int x, int y, int key) {
        Render2DEngine.drawRectangle(m, x + 150, y, 100.0f, 15.0f, 6.0f, 1.0f, Theme.SETTINGS_BG);
        Render2DEngine.drawOutline(m, x + 150, y, 100.0f, 15.0f, 6.0f, 1.0f, 0.1f, Theme.SETTINGS_BG.brighter().brighter());
        ClientMain.fontRenderer.draw(m, name, x - 100, y, 0xFFFFFF);
        String txt = this.waitingForKey ? "Press any key..." : "Key: " + KeyUtils.getKey(key);
        ClientMain.fontRenderer.draw(m, txt, (float)(x + 150) + (100.0f - ClientMain.fontRenderer.getWidth(txt)) / 2.0f, y - 5, 0xFFFFFF);
    }

    private void drawBoolean(MatrixStack m, String name, int x, int y, boolean val) {
        Render2DEngine.drawRectangle(m, x + 150, y, 15.0f, 15.0f, 6.0f, 1.0f, Theme.SETTINGS_BG);
        Render2DEngine.drawOutline(m, x + 150, y, 15.0f, 15.0f, 6.0f, 1.0f, 0.1f, Theme.SETTINGS_BG.brighter().brighter());
        ClientMain.fontRenderer.draw(m, name, x - 100, y, 0xFFFFFF);
        if (val) {
            Render2DEngine.drawLine(m, x + 153, y + 7, x + 156, y + 10, 1.0f, Theme.ENABLED);
            Render2DEngine.drawLine(m, x + 156, y + 10, x + 162, y + 4, 1.0f, Theme.ENABLED);
        }
    }

    private void openPicker(ColorEntry entry) {
        this.activeEntry = entry;
        Color c = entry.getter.get();
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        this.pickerHue = hsb[0];
        this.pickerSat = hsb[1];
        this.pickerBri = hsb[2];
        this.picking = true;
        int x = this.parent.x + 340;
        int y = this.parent.y + 80;
        int size = 100;
        this.sbBounds = new Rectangle(x, y, size, size);
        this.hueBounds = new Rectangle(x + size + 10, y, 10, size);
    }

    private void drawColorPicker(MatrixStack m) {
        int i;
        Render2DEngine.fill(m, this.sbBounds.x - 5, this.sbBounds.y - 5, this.hueBounds.x + this.hueBounds.width + 5, this.sbBounds.y + this.sbBounds.height + 5, new Color(0, 0, 0, 150).getRGB());
        for (i = 0; i < this.sbBounds.width; ++i) {
            for (int j = 0; j < this.sbBounds.height; ++j) {
                float s = (float)i / (float)this.sbBounds.width;
                float b = 1.0f - (float)j / (float)this.sbBounds.height;
                Render2DEngine.fill(m, this.sbBounds.x + i, this.sbBounds.y + j, this.sbBounds.x + i + 1, this.sbBounds.y + j + 1, Color.getHSBColor(this.pickerHue, s, b).getRGB());
            }
        }
        Render2DEngine.drawOutline(m, this.sbBounds.x, this.sbBounds.y, this.sbBounds.width, this.sbBounds.height, 1.0f, 1.0f, 0.3f, Color.BLACK);
        for (i = 0; i < this.hueBounds.height; ++i) {
            Render2DEngine.fill(m, this.hueBounds.x, this.hueBounds.y + i, this.hueBounds.x + this.hueBounds.width, this.hueBounds.y + i + 1, Color.getHSBColor((float)i / (float)this.hueBounds.height, 1.0f, 1.0f).getRGB());
        }
        Render2DEngine.drawOutline(m, this.hueBounds.x, this.hueBounds.y, this.hueBounds.width, this.hueBounds.height, 1.0f, 1.0f, 0.3f, Color.BLACK);
        int sx = this.sbBounds.x + (int)(this.pickerSat * (float)this.sbBounds.width);
        int sy = this.sbBounds.y + (int)((1.0f - this.pickerBri) * (float)this.sbBounds.height);
        Render2DEngine.fill(m, sx - 2, sy - 2, sx + 3, sy + 3, Color.WHITE.getRGB());
        int hy = this.hueBounds.y + (int)(this.pickerHue * (float)this.hueBounds.height);
        Render2DEngine.fill(m, this.hueBounds.x - 2, hy - 2, this.hueBounds.x + this.hueBounds.width + 2, hy + 2, Color.WHITE.getRGB());
    }

    @Override
    public void mouseClicked(double mx, double my, int button) {
        if (this.picking) {
            if (this.sbBounds.contains(mx, my)) {
                this.setSB(mx, my);
            } else if (this.hueBounds.contains(mx, my)) {
                this.setHue(my);
            }
            if (this.sbBounds.contains(mx, my) || this.hueBounds.contains(mx, my)) {
                this.applySelectedColor();
            }
        }
        super.mouseClicked(mx, my, button);
        for (ClickableComponent c : new ArrayList<ClickableComponent>(this.clickableComponents)) {
            if (!c.bounds.contains(mx, my)) continue;
            c.onClick.run();
        }
    }

    private void applySelectedColor() {
        if (this.activeEntry != null) {
            Color selectedColor = Color.getHSBColor(this.pickerHue, this.pickerSat, this.pickerBri);
            this.activeEntry.setter.accept(selectedColor);
        }
        this.picking = false;
    }

    @Override
    public void mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (!this.picking) {
            return;
        }
        if (this.sbBounds.contains(mx, my)) {
            this.setSB(mx, my);
        } else if (this.hueBounds.contains(mx, my)) {
            this.setHue(my);
        }
    }

    @Override
    public void mouseReleased(double mx, double my, int button) {
        if (this.picking) {
            this.activeEntry.setter.accept(Color.getHSBColor(this.pickerHue, this.pickerSat, this.pickerBri));
            this.picking = false;
        }
    }

    @Override
    public void mouseScrolled(double mx, double my, double horiz, double vert) {
        if ("Theme".equals(this.selectedTab) && !this.picking) {
            this.themeScroll += (int)(vert * 20.0);
            int total = colorEntries.size() * 60;
            int min = Math.min(0, 300 - total);
            this.themeScroll = Math.max(min, Math.min(0, this.themeScroll));
        }
    }

    private void setSB(double mx, double my) {
        this.pickerSat = this.clamp((float)((mx - (double)this.sbBounds.x) / (double)this.sbBounds.width), 0.0f, 1.0f);
        this.pickerBri = this.clamp(1.0f - (float)((my - (double)this.sbBounds.y) / (double)this.sbBounds.height), 0.0f, 1.0f);
    }

    private void setHue(double my) {
        this.pickerHue = this.clamp((float)((my - (double)this.hueBounds.y) / (double)this.hueBounds.height), 0.0f, 1.0f);
    }

    private float clamp(float v, float min, float max) {
        return v < min ? min : (v > max ? max : v);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.waitingForKey) {
            UserConstants.GUIKEY.setValue(keyCode);
            this.waitingForKey = false;
            ChatUtils.addChatMessage("Bound key to: " + KeyUtils.getKey(keyCode));
        }
    }

    private static class ClickableComponent {
        Rectangle bounds;
        Runnable onClick;

        ClickableComponent(Rectangle b, Runnable r) {
            this.bounds = b;
            this.onClick = r;
        }
    }

    private static class ColorEntry {
        String label;
        Supplier<Color> getter;
        Consumer<Color> setter;

        ColorEntry(String l, Supplier<Color> g, Consumer<Color> s) {
            this.label = l;
            this.getter = g;
            this.setter = s;
        }
    }
}
