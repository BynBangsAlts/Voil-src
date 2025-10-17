package net.fabricmc.fabric.gui.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.gui.clickgui.ViewType;
import net.fabricmc.fabric.gui.clickgui.component.ModuleButton;
import net.fabricmc.fabric.gui.screens.ConfigEditorScreen;
import net.fabricmc.fabric.gui.screens.GUISettingsScreen;
import net.fabricmc.fabric.gui.screens.IRCGui;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GUI
extends Screen {
    private static final GUI INSTANCE = new GUI();
    public int x;
    public int y;
    public int width;
    public int height;
    public int leftpanelwidth;
    public int dragX;
    public int dragY;
    private String search;
    private boolean isDragging;
    private boolean isSearching;
    private boolean searchBarExpanded;
    public Category selectedCategory = Category.Combat;
    private float scale = 0.5f;
    private double animationProgress;
    private double animationSpeed;
    private Networking networking;
    private ViewType viewType;
    public final List<ModuleButton> modButtons = new ArrayList<ModuleButton>();
    public Module selectedModule;
    public int coordModX;
    public int coordModY;
    private int moduleY;
    private double dWheel;
    private float modScrollEnd;
    private float modScrollNow;
    private float modScrollTarget;
    private final float scrollSpeed = 10.0f;
    public int settingsFieldX;
    private long lastBlinkTime;
    private boolean caretVisible;
    private GUISettingsScreen settingsScreen;
    private ConfigEditorScreen configScreen;
    private IRCGui ircScreen;

    protected GUI() {
        super(Text.of((String)"ClickGUI"));
    }

    protected void init() {
        this.width = 540;
        this.height = 330;
        this.leftpanelwidth = 165;
        this.dragX = 0;
        this.dragY = 0;
        this.animationSpeed = 0.03;
        this.animationProgress = 0.0;
        this.isDragging = false;
        this.isSearching = false;
        this.searchBarExpanded = false;
        this.x = (ClientMain.mc.getWindow().getScaledWidth() - this.width) / 2;
        this.y = (ClientMain.mc.getWindow().getScaledHeight() - this.height) / 2;
        this.scale = 0.5f;
        this.search = "";
        this.viewType = ViewType.HOME;
        this.coordModX = 70;
        this.coordModY = 40;
        this.settingsFieldX = 0;
        this.selectedModule = null;
        this.modButtons.clear();
        for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory)) {
            this.modButtons.add(new ModuleButton(module, 0.0, this.moduleY, this));
            this.moduleY += 40;
        }
        this.lastBlinkTime = System.currentTimeMillis();
        this.caretVisible = true;
        this.settingsScreen = new GUISettingsScreen(this);
        this.configScreen = new ConfigEditorScreen(this);
        this.ircScreen = new IRCGui(this);
        this.networking = new Networking();
        super.init();
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        float bgWidth;
        float textWidth;
        float bgY;
        int playerPlanColor;
        if (this.scale == 1.0f && UserConstants.glow.getValue() instanceof Boolean && ((Boolean)UserConstants.glow.getValue()).booleanValue()) {
            Render2DEngine.drawGlow(matrices.getMatrices(), this.x, this.y, this.width, this.height, 6.0f, 10.0f, ColorUtil.TwoColor(Theme.ACCENT_COLOR1, Theme.ACCENT_COLOR2, 10.0, 0.0));
        }
        ClickGUI.enableScissor(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height - 1);
        if (this.scale < 1.0f) {
            this.scale = (float)((double)this.scale + 0.010000000149011612);
            if (this.scale > 1.0f) {
                this.scale = 1.0f;
            }
        }
        matrices.getMatrices().push();
        matrices.getMatrices().translate((1.0f - this.scale) * (float)ClientMain.mc.getWindow().getScaledWidth() / 2.0f, (1.0f - this.scale) * (float)ClientMain.mc.getWindow().getScaledHeight() / 2.0f, 0.0f);
        matrices.getMatrices().scale(this.scale, this.scale, 1.0f);
        double toX = this.x + this.width;
        double toY = this.y + this.height;
        Render2DEngine.drawRectangle(matrices.getMatrices(), this.x, this.y, this.width, this.height, 10.0f, 1.0f, Theme.WINDOW_COLOR);
        double leftPanelToX = this.leftpanelwidth;
        Render2DEngine.drawRectangle(matrices.getMatrices(), this.x, this.y, this.leftpanelwidth, this.height, 10.0f, 1.0f, Theme.LEFT_PANEL);
        Render2DEngine.drawRectangle(matrices.getMatrices(), this.x + 160, this.y, 5.0f, (int)toY, 0.0f, 0.0f, Theme.LEFT_PANEL);
        if (this.viewType == ViewType.GUI) {
            this.settingsScreen.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        if (this.viewType == ViewType.IRC) {
            this.ircScreen.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        if (this.isDragging) {
            if (this.dragX == 0 && this.dragY == 0) {
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            } else {
                this.x = mouseX - this.dragX;
                this.y = mouseY - this.dragY;
            }
        } else if (this.dragX != 0 || this.dragY != 0) {
            this.dragX = 0;
            this.dragY = 0;
        }
        int buttonY = this.y + 40;
        for (ModuleButton button : this.modButtons) {
            button.x = this.x + this.leftpanelwidth + 10;
            button.y = buttonY;
            buttonY += 40;
        }
        if (this.viewType == ViewType.HOME && ((Boolean)UserConstants.searchBar.getValue()).booleanValue()) {
            int searchBarWidth = 160;
            int searchBarX = this.x + (this.width - searchBarWidth) / 2;
            int searchBarY = this.y + 7;
            Render2DEngine.drawRectangle(matrices.getMatrices(), searchBarX + 83, searchBarY - 2, searchBarWidth, 20.0f, 6.0f, 1.0f, new Color(35, 35, 35));
            Render2DEngine.drawTexturedRectangle(matrices, (float)searchBarX + 88.0f, searchBarY, 15, 15, Identifier.of((String)"tulip", (String)"icons/search2.png"));
            if (this.isSearching) {
                ClientMain.fontRenderer.draw(matrices.getMatrices(), this.search, searchBarX + 105, searchBarY - 4, 0xFFFFFF);
            } else {
                ClientMain.fontRenderer.draw(matrices.getMatrices(), "Search", searchBarX + 105, searchBarY - 4, new Color(0xAAAAAA).getRGB());
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastBlinkTime >= 500L) {
                this.caretVisible = !this.caretVisible;
                this.lastBlinkTime = currentTime;
            }
            if (this.isSearching && this.caretVisible) {
                int caretX = (int)((float)(searchBarX + 105) + ClientMain.fontRenderer.getWidth(this.search));
                ClientMain.fontRenderer.draw(matrices.getMatrices(), "|", caretX, searchBarY - 4, Color.WHITE.getRGB());
            }
        }
        Color pborder = new Color(41, 42, 42, 179);
        Color inside = new Color(19, 19, 19, 255);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), this.x + 10, this.y + 270, this.x + 160, this.y + 270 + 32 + ClientMain.fontRenderer.getFont().getSize() + 10 - 11, 5.0, 16.0, pborder);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), this.x + 11, this.y + 271, this.x + 160 - 1, this.y + 270 + 32 + ClientMain.fontRenderer.getFont().getSize() + 10 - 12, 5.0, 16.0, inside);
        Render2DEngine.drawPlayerHead(matrices, (PlayerEntity)ClientMain.mc.player, this.x + 20, this.y + 270 + 10, 32);
        int playerNameColor = -1;
        int playerIdColor = -5592406;
        int VOIL_PLUS_COLOR = -10496;
        int VOIL_COLOR = -4144960;
        int VOIL_MONTHLY_COLOR = -3309774;
        int OWNER_COLOR = -65536;
        int BETA_TESTER_COLOR = -8388480;
        int DEFAULT_COLOR = -5592406;
        if (LoginHandler.plan != null) {
            switch (LoginHandler.plan.toLowerCase()) {
                case "voil+": {
                    playerPlanColor = -10496;
                    break;
                }
                case "voil": {
                    playerPlanColor = -4144960;
                    break;
                }
                case "voil_monthly": {
                    playerPlanColor = -3309774;
                    break;
                }
                case "owner": {
                    playerPlanColor = -65536;
                    break;
                }
                case "beta_tester": {
                    playerPlanColor = -8388480;
                    break;
                }
                default: {
                    playerPlanColor = -5592406;
                    break;
                }
            }
        } else {
            playerPlanColor = -5592406;
        }
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.getSession().getUsername(), (double)(this.x + 20 + 32 + 10), (double)(this.y + 280), playerNameColor, false);
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), "ID: " + LoginHandler.userid, (double)(this.x + 20 + 32 + 10), (double)(this.y + 290), playerIdColor, false);
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), "Plan: " + LoginHandler.plan, (double)(this.x + 20 + 32 + 10), (double)(this.y + 300), playerPlanColor, false);
        Render2DEngine.drawTexturedRectangle(matrices, this.x + 16, this.y + 175, 15, 15, Identifier.of((String)"tulip", (String)"icons/config.png"));
        Render2DEngine.drawTexturedRectangle(matrices, this.x + 16, this.y + 205, 15, 15, Identifier.of((String)"tulip", (String)"icons/cfg.png"));
        Render2DEngine.drawTexturedRectangle(matrices, this.x + 16, this.y + 235, 15, 15, Identifier.of((String)"tulip", (String)"icons/irc.png"));
        float cateY = this.y + 55;
        if (this.viewType == ViewType.GUI) {
            float bgX = (float)this.x + 31.5f;
            bgY = cateY + 150.0f;
            textWidth = ClientMain.fontRenderer.getWidth("GUI");
            bgWidth = textWidth + 4.0f;
            Render2DEngine.drawRectangle(matrices.getMatrices(), bgX + 5.0f, bgY, bgWidth, 13.0f, 2.0f, 1.0f, Theme.ENABLED);
        }
        if (this.viewType == ViewType.CONFIG) {
            float bgX = (float)this.x + 31.5f;
            bgY = cateY + 121.0f;
            textWidth = ClientMain.fontRenderer.getWidth("Config");
            bgWidth = textWidth + 4.0f;
            Render2DEngine.drawRectangle(matrices.getMatrices(), bgX + 5.0f, bgY, bgWidth, 13.0f, 2.0f, 1.0f, Theme.ENABLED);
        }
        if (this.viewType == ViewType.IRC) {
            float bgX = (float)this.x + 31.5f;
            bgY = cateY + 180.0f;
            textWidth = ClientMain.fontRenderer.getWidth("IRC");
            bgWidth = textWidth + 4.0f;
            Render2DEngine.drawRectangle(matrices.getMatrices(), bgX + 5.0f, bgY, bgWidth, 13.0f, 2.0f, 1.0f, Theme.ENABLED);
        }
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, "Categories", (float)(this.x + 16), cateY - 15.0f, -1);
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY - 31.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY - 32.0f, new Color(0x323232).getRGB());
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, "Voil", (float)(this.x + 16), cateY - 40.0f, -1);
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 116.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 117.0f, new Color(0x323232).getRGB());
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, "Config", (float)(this.x + 38), cateY + 128.0f, -1);
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 137.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 138.0f, new Color(0x323232).getRGB());
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 147.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 148.0f, new Color(0x323232).getRGB());
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, "GUI", (float)(this.x + 38), cateY + 157.0f, -1);
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 166.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 167.0f, new Color(0x323232).getRGB());
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 176.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 177.0f, new Color(0x323232).getRGB());
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, "IRC", (float)(this.x + 38), cateY + 186.0f, -1);
        Render2DEngine.fill(matrices.getMatrices(), this.x + 16, cateY + 196.0f, (float)(this.x + 90) + ClientMain.fontRenderer.getWidth("Categories") + 10.0f, cateY + 197.0f, new Color(0x323232).getRGB());
        if (this.selectedModule != null) {
            if (this.settingsFieldX > -80) {
                this.settingsFieldX -= 100;
            }
        } else if (this.settingsFieldX < 0) {
            this.settingsFieldX += 5;
        }
        for (Category category : Category.values()) {
            if (category == this.selectedCategory) {
                float bgX = this.x + 14;
                float bgY2 = cateY - 7.0f;
                float textWidth2 = ClientMain.fontRenderer.getWidth(category.name);
                float bgWidth2 = textWidth2 + 10.0f;
                if (this.viewType == ViewType.HOME) {
                    Render2DEngine.renderRoundedQuad(matrices.getMatrices(), bgX + 5.0f, bgY2, bgX + bgWidth2, bgY2 + 13.0f, 2.0, 20.0, Theme.ENABLED);
                }
                int renderColor = this.viewType == ViewType.HOME ? -1 : Theme.UNFOCUSED_TEXT_COLOR.getRGB();
                ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, category.name(), (float)(this.x + 20), cateY, renderColor);
                Render2DEngine.fill(matrices.getMatrices(), this.x + 16, this.y + 50, this.x + 15, this.y + 150, new Color(0x323232).getRGB());
                this.modButtons.clear();
                float modY = 40.0f;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory)) {
                    this.modButtons.add(new ModuleButton(module, 0.0, modY, this));
                    modY += 40.0f;
                }
            } else {
                ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClientMain.mc.textRenderer, category.name(), (float)(this.x + 20), cateY, Theme.UNFOCUSED_TEXT_COLOR.getRGB());
            }
            cateY += 15.0f;
        }
        int currentY = (int)(this.dWheel + 40.0);
        for (ModuleButton modButton : this.modButtons) {
            Module module = modButton.getModule();
            boolean matchesCategory = ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).contains(module);
            boolean matchesSearch = module.getName().toString().toLowerCase().contains(this.search.toLowerCase());
            boolean isVisible = matchesCategory && matchesSearch;
            module.setHidden(!isVisible);
            if (!isVisible || this.viewType != ViewType.HOME) continue;
            modButton.setY(currentY);
            currentY += (int)(modButton.getTotalHeight() + 5.0f);
            modButton.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        ClickGUI.disableScissor();
        matrices.getMatrices().pop();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float bgWidth;
        float textWidth;
        float bgY;
        float bgX;
        if (button == 0 && (double)this.x < mouseX && mouseX < (double)(this.x + this.width) && (double)this.y < mouseY && mouseY < (double)(this.y + 15)) {
            this.isDragging = true;
            this.dragX = (int)(mouseX - (double)this.x);
            this.dragY = (int)(mouseY - (double)this.y);
            return true;
        }
        if (button == 0) {
            bgX = this.x + 25;
            bgY = this.y + 55 + 150;
            textWidth = ClientMain.fontRenderer.getWidth("GUI");
            bgWidth = textWidth + 8.0f;
            if (mouseX >= (double)(bgX + 5.0f) && mouseX <= (double)(bgX + bgWidth) && mouseY >= (double)bgY && mouseY <= (double)(bgY + 13.0f)) {
                this.viewType = ViewType.GUI;
                return true;
            }
        }
        if (button == 0) {
            bgX = this.x + 25;
            bgY = this.y + 55 + 121;
            textWidth = ClientMain.fontRenderer.getWidth("Config");
            bgWidth = textWidth + 8.0f;
            if (mouseX >= (double)(bgX + 5.0f) && mouseX <= (double)(bgX + bgWidth + 5.0f) && mouseY >= (double)bgY && mouseY <= (double)(bgY + 13.0f)) {
                this.viewType = ViewType.CONFIG;
                return true;
            }
        }
        if (button == 0) {
            bgX = this.x + 25;
            bgY = this.y + 55 + 180;
            textWidth = ClientMain.fontRenderer.getWidth("IRC");
            bgWidth = textWidth + 8.0f;
            if (mouseX >= (double)(bgX + 5.0f) && mouseX <= (double)(bgX + bgWidth + 5.0f) && mouseY >= (double)bgY && mouseY <= (double)(bgY + 13.0f)) {
                this.viewType = ViewType.IRC;
                return true;
            }
        }
        float cateY = this.y + 55;
        for (Category category : Category.values()) {
            float bgX2 = this.x + 14;
            float bgY2 = cateY - 7.0f;
            float textWidth2 = ClientMain.fontRenderer.getWidth(category.name);
            float bgWidth2 = textWidth2 + 10.0f;
            if (this.isHovered(bgX2 + 5.0f, bgY2, bgX2 + bgWidth2, bgY2 + 13.0f, mouseX, mouseY) && button == 0) {
                this.selectedCategory = category;
                this.viewType = ViewType.HOME;
                ModuleManager.INSTANCE.unexpandAll();
                this.modButtons.clear();
                float modY = (float)(70.0 + this.dWheel);
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory)) {
                    this.modButtons.add(new ModuleButton(module, 0.0, modY, this));
                    modY += 40.0f;
                }
                return true;
            }
            cateY += 15.0f;
        }
        if (this.viewType == ViewType.GUI) {
            this.settingsScreen.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        if (this.viewType == ViewType.IRC) {
            this.ircScreen.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        if (button == 0) {
            int searchBarWidth = 120;
            int searchBarHeight = 20;
            int searchBarX = this.x + (this.width - searchBarWidth) / 2 + 50;
            int searchBarY = this.y + 5;
            if (mouseX >= (double)searchBarX && mouseX <= (double)(searchBarX + searchBarWidth) && mouseY >= (double)searchBarY && mouseY <= (double)(searchBarY + searchBarHeight)) {
                this.isSearching = true;
                return true;
            }
        }
        for (ModuleButton modButton : this.modButtons) {
            if (!ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).contains(modButton.getModule()) || modButton.getModule().isHidden() || this.viewType != ViewType.HOME) continue;
            modButton.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horiz, double vert) {
        if (this.isHovered(this.x + 100, this.y + 60, this.x + 425, this.y + this.height, mouseX, mouseY)) {
            this.dWheel += vert * 30.0;
            double minScroll = -(ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).size() * 40 - (this.height - 180));
            double maxScroll = 0.0;
            this.dWheel = Math.max(minScroll, Math.min(this.dWheel, maxScroll));
            return true;
        }
        if (this.viewType == ViewType.IRC) {
            this.ircScreen.mouseScrolled(mouseX, mouseY, horiz, vert);
            return true;
        }
        for (ModuleButton modButton : this.modButtons) {
            modButton.mouseScrolled(mouseX, mouseY, horiz, vert);
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.mouseScrolled(mouseX, mouseY, horiz, vert);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horiz, vert);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDragging = false;
        }
        for (ModuleButton modButton : this.modButtons) {
            if (!ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).contains(modButton.getModule()) || this.viewType != ViewType.HOME) continue;
            modButton.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging) {
            this.x = (int)mouseX - this.dragX;
            this.y = (int)mouseY - this.dragY;
        }
        for (ModuleButton modButton : this.modButtons) {
            modButton.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean charTyped(char c, int modifiers) {
        for (ModuleButton mb : this.modButtons) {
            mb.charTyped(c, modifiers);
        }
        if (this.viewType == ViewType.IRC) {
            this.ircScreen.charTyped(c, modifiers);
            return true;
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.charTyped(c, modifiers);
            return true;
        }
        if (this.isSearching) {
            if (c == '\b' && this.search.length() > 0) {
                this.search = this.search.substring(0, this.search.length() - 1);
            } else if (this.search.length() < 16) {
                this.search = this.search + c;
            }
            return true;
        }
        return super.charTyped(c, modifiers);
    }

    public boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)x2 && mouseY >= (double)y && mouseY <= (double)y2;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton mb : this.modButtons) {
            mb.keyPressed(keyCode, scanCode, modifiers);
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        if (this.isSearching) {
            if (keyCode == 256) {
                this.isSearching = false;
                this.search = "";
                return true;
            }
            if (keyCode == 259 && this.search.length() > 0) {
                this.search = this.search.substring(0, this.search.length() - 1);
            }
            return true;
        }
        if (this.viewType == ViewType.IRC) {
            this.ircScreen.keyPressed(keyCode, scanCode, modifiers);
            if (keyCode == 256) {
                this.close();
                return true;
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void open() {
    }

    public void close() {
        ModuleManager.INSTANCE.unexpandAll();
        this.search = "";
        this.isSearching = false;
        this.searchBarExpanded = false;
        super.close();
    }

    public static GUI getInstance() {
        return INSTANCE;
    }
}
