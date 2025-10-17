package net.fabricmc.fabric.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.ViewType;
import net.fabricmc.fabric.gui.clickgui.components.ModButton;
import net.fabricmc.fabric.gui.screens.ConfigScreen;
import net.fabricmc.fabric.gui.screens.EditGuiMenu;
import net.fabricmc.fabric.gui.screens.ProfileScreen;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.security.LoginHandler;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.misc.SelfDestruct;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.Animation;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ClickGUI
extends Screen {
    private static final ClickGUI INSTANCE = new ClickGUI();
    public final List<ModButton> modButtons = new ArrayList<ModButton>();
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final boolean close = false;
    private final float leftPanelWidth = 180.0f;
    private float scrollOffsetY = 0.0f;
    public float windowX = 200.0f;
    public float windowY = 170.0f;
    public float width = 600.0f;
    public float height = 449.0f;
    public Category selectedCategory = Category.Client;
    public Module selectedModule;
    private boolean isOpening = false;
    private boolean isClosing = false;
    private float currentScale = 0.0f;
    private final float targetScale = 1.0f;
    private float scaleSpeed = 0.05f;
    public int coordModX = 0;
    public float settingsFieldX;
    public float settingsFieldY;
    public float settingsFNow;
    public float settingsF;
    private float dragX;
    private float dragY;
    private boolean drag = false;
    private boolean closed;
    private double dWheel;
    private float hy = this.windowY + 40.0f;
    private float modScrollEnd;
    private float modScrollNow;
    private ViewType viewType;
    private String searchText = "";
    private boolean isSearchFocused = false;
    private ConfigScreen configScreen;
    private EditGuiMenu editGuiMenu;
    private ProfileScreen profileScreen;
    private float deltaTime;
    private Animation transition;

    protected ClickGUI() {
        super(Text.of((String)"ClickGUI"));
    }

    public static ClickGUI getINSTANCE() {
        return INSTANCE;
    }

    protected void init() {
        this.drag = false;
        this.selectedModule = null;
        this.modButtons.clear();
        float modY = 70.0f + this.modScrollNow;
        for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory)) {
            this.modButtons.add(new ModButton(module, 0.0, modY, this));
            modY += 40.0f;
        }
        this.viewType = ViewType.HOME;
        this.configScreen = new ConfigScreen(this);
        this.editGuiMenu = new EditGuiMenu(this);
        this.profileScreen = new ProfileScreen(this);
        super.init();
    }

    public void setScrollOffsetY(float offset) {
        this.scrollOffsetY = offset;
    }

    public float getScrollOffsetY() {
        return this.scrollOffsetY;
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        int playerPlanColor;
        super.render(matrices, mouseX, mouseY, delta);
        this.deltaTime = Utils.getTick();
        if (this.isOpening) {
            this.currentScale = this.lerp(this.currentScale, 1.0f, this.scaleSpeed * this.deltaTime);
            if (Math.abs(this.currentScale - 1.0f) < 0.01f) {
                this.currentScale = 1.0f;
                this.isOpening = false;
            }
        } else if (this.isClosing) {
            this.currentScale = this.lerp(this.currentScale, 0.0f, this.scaleSpeed * this.deltaTime);
            if (this.currentScale < 0.01f) {
                this.currentScale = 0.0f;
                this.isClosing = false;
                MinecraftClient.getInstance().setScreen(null);
                return;
            }
        }
        if (this.drag) {
            if (this.dragX == 0.0f && this.dragY == 0.0f) {
                this.dragX = (float)mouseX - this.windowX;
                this.dragY = (float)mouseY - this.windowY;
            } else {
                this.windowX = (float)mouseX - this.dragX;
                this.windowY = (float)mouseY - this.dragY;
            }
        } else if (this.dragX != 0.0f || this.dragY != 0.0f) {
            this.dragX = 0.0f;
            this.dragY = 0.0f;
        }
        float scaledWidth = this.width * this.currentScale;
        float scaledHeight = this.height * this.currentScale;
        float scaledX = this.windowX + (this.width - scaledWidth) / 2.0f;
        float scaledY = this.windowY + (this.height - scaledHeight) / 2.0f;
        if (EditGuiMenu.toggleStates.get("Glow").booleanValue()) {
            Render2DEngine.renderRoundedShadow(matrices, Theme.ENABLED, scaledX, scaledY, scaledX + scaledWidth, scaledY + scaledHeight, 5.0, 15.0, 8.0);
        }
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), scaledX, scaledY, scaledX + scaledWidth, scaledY + scaledHeight, 5.0, 20.0, Theme.WINDOW_COLOR);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), scaledX, scaledY, scaledX + 180.0f * this.currentScale, scaledY + scaledHeight, 5.0, 20.0, Theme.LEFT_PANEL);
        int overlayStartX = (int)(scaledX + 180.0f * this.currentScale - 15.0f);
        int overlayEndX = (int)(scaledX + 180.0f * this.currentScale);
        Render2DEngine.fill(matrices.getMatrices(), overlayStartX, (int)scaledY, overlayEndX, (int)(scaledY + scaledHeight), Theme.LEFT_PANEL.getRGB());
        float borderStartX = scaledX + 180.0f * this.currentScale;
        float borderEndX = borderStartX + 2.0f * this.currentScale;
        float borderStartY = scaledY;
        float borderEndY = scaledY + scaledHeight;
        matrices.getMatrices().push();
        matrices.getMatrices().translate(this.windowX, this.windowY, 0.0f);
        matrices.getMatrices().scale(2.0f, 2.0f, 2.0f);
        matrices.getMatrices().pop();
        if (this.viewType == ViewType.GUI) {
            if (this.transition != null) {
                this.transition.update(true);
                Render2DEngine.fill(matrices.getMatrices(), this.transition.getValue(), this.windowY, this.transition.getEnd() - 19.0f, this.windowY + this.height, Theme.WINDOW_COLOR.getRGB());
                if (this.transition.hasEnded()) {
                    this.transition = null;
                }
            }
            this.editGuiMenu.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        if (this.viewType == ViewType.CONFIG) {
            if (this.transition != null) {
                this.transition.update(true);
                Render2DEngine.fill(matrices.getMatrices(), this.transition.getValue(), this.windowY, this.transition.getEnd() - 19.0f, this.windowY + this.height, Theme.WINDOW_COLOR.getRGB());
                if (this.transition.hasEnded()) {
                    this.transition = null;
                }
            }
            this.configScreen.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        if (this.viewType == ViewType.PROFILE) {
            if (this.transition != null) {
                this.transition.update(true);
                Render2DEngine.fill(matrices.getMatrices(), this.transition.getValue(), this.windowY, this.transition.getEnd() - 19.0f, this.windowY + this.height, Theme.WINDOW_COLOR.getRGB());
                if (this.transition.hasEnded()) {
                    this.transition = null;
                }
            }
            this.profileScreen.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
        }
        ClickGUI.enableScissor(this.windowX + 15.0f, this.windowY + 20.0f, this.windowX + this.width - 5.0f, this.windowY + this.height - 5.0f);
        float settingsTextX = this.windowX + 20.0f;
        float settingsTextY = this.windowY + 300.0f;
        float configsTextX = this.windowX + 20.0f;
        float configsTextY = this.windowY + 340.0f;
        boolean isHoveredSettings = (float)mouseX >= settingsTextX && (float)mouseX <= settingsTextX + 100.0f && (float)mouseY >= settingsTextY && (float)mouseY <= settingsTextY + 20.0f;
        boolean isHoveredConfigs = (float)mouseX >= configsTextX && (float)mouseX <= configsTextX + 100.0f && (float)mouseY >= configsTextY && (float)mouseY <= configsTextY + 20.0f;
        matrices.getMatrices().push();
        Color settingsBackColor = isHoveredSettings ? new Color(128, 128, 128, 153) : new Color(0, 0, 0, 153);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), settingsTextX - 3.0f, settingsTextY - 3.0f, settingsTextX + 103.0f, settingsTextY + 23.0f, 5.0, 16.0, settingsBackColor);
        ClientMain.fontRenderer.draw(matrices.getMatrices(), "Configs", settingsTextX, settingsTextY, isHoveredSettings ? -4210753 : -1);
        matrices.getMatrices().pop();
        float lineY = this.windowY + 290.0f;
        int lineWidth = 160;
        int lineThickness = 2;
        int padding = 10;
        Color lineColor = new Color(56, 56, 56);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Render2DEngine.fill(matrices.getMatrices(), configsTextX - (float)padding, lineY, configsTextX - (float)padding + (float)lineWidth, lineY + (float)lineThickness, lineColor.getRGB());
        float lineY2 = this.windowY + 330.0f;
        int lineWidth2 = 160;
        int lineThickness2 = 2;
        int padding2 = 10;
        Color lineColor2 = new Color(56, 56, 56);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Render2DEngine.fill(matrices.getMatrices(), configsTextX - (float)padding, lineY2, configsTextX - (float)padding2 + (float)lineWidth2, lineY2 + (float)lineThickness2, lineColor2.getRGB());
        matrices.getMatrices().push();
        Color configsBackColor = isHoveredConfigs ? new Color(128, 128, 128, 153) : new Color(0, 0, 0, 153);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), configsTextX - 3.0f, configsTextY - 3.0f, configsTextX + 103.0f, configsTextY + 23.0f, 5.0, 16.0, configsBackColor);
        ClientMain.fontRenderer.draw(matrices.getMatrices(), "Settings", configsTextX, configsTextY, isHoveredConfigs ? -4210753 : -1);
        matrices.getMatrices().pop();
        if (EditGuiMenu.toggleStates.get("Search Bar").booleanValue() && this.viewType == ViewType.HOME) {
            float relativeSearchX = 240.0f;
            float relativeSearchY = 35.0f;
            float absoluteSearchX = this.windowX + relativeSearchX;
            float absoluteSearchY = this.windowY + relativeSearchY;
            int searchBarWidth = 220;
            int searchBarHeight = 20;
            Color outlineColor = new Color(0x171616);
            Color gradientStartColor = new Color(0x282828);
            RenderSystem.disableDepthTest();
            Render2DEngine.renderRoundedQuad(matrices.getMatrices(), absoluteSearchX - 2.0f, absoluteSearchY - 2.0f, absoluteSearchX + (float)searchBarWidth + 2.0f, absoluteSearchY + (float)searchBarHeight + 2.0f, 8.0, 16.0, outlineColor);
            Render2DEngine.renderRoundedQuad(matrices.getMatrices(), absoluteSearchX, absoluteSearchY, absoluteSearchX + (float)searchBarWidth, absoluteSearchY + (float)searchBarHeight, 5.0, 16.0, gradientStartColor);
            RenderSystem.enableDepthTest();
            long currentTime = System.currentTimeMillis();
            float cursorOpacity = (float)(Math.sin((double)currentTime / 500.0) * 0.5 + 0.5);
            int cursorColor = (int)(cursorOpacity * 255.0f) << 24 | 0xFFFFFF;
            String displayText = this.searchText + (this.isSearchFocused ? "|" : "");
            ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClickGUI.mc.textRenderer, displayText, absoluteSearchX + 8.0f, absoluteSearchY + 6.0f, -1);
            if (this.isSearchFocused) {
                ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClickGUI.mc.textRenderer, "|", absoluteSearchX + 8.0f + ClientMain.fontRenderer.getWidth(this.searchText), absoluteSearchY + 6.0f, cursorColor);
            }
        }
        float relativeX = 20.0f;
        float relativeY = 260.0f;
        float absoluteX = this.windowX + relativeX;
        float absoluteY = this.windowY + relativeY;
        Color borderColor = new Color(-2130771968, true);
        Color innerColor = new Color(-1291845632, true);
        float borderWidth = 1.0f;
        float buttonWidth = 100.0f;
        float buttonHeight = 20.0f;
        float borderRadius = 5.0f;
        float relativeXP = 20.0f;
        float relativeYP = 380.0f;
        float absoluteXP = this.windowX + relativeXP;
        float absoluteYP = this.windowY + relativeYP;
        int playerHeadSize = 32;
        float profileWidth = 150.0f;
        float profileHeight = playerHeadSize + ClientMain.fontRenderer.getFont().getSize() + 10;
        Color pborder = new Color(41, 42, 42, 179);
        Color inside = new Color(19, 19, 19, 255);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), absoluteXP, absoluteYP, absoluteXP + profileWidth, absoluteYP + profileHeight, borderRadius, 16.0, pborder);
        Render2DEngine.renderRoundedQuad(matrices.getMatrices(), absoluteXP + borderWidth, absoluteYP + borderWidth, absoluteXP + profileWidth - borderWidth, absoluteYP + profileHeight - borderWidth, borderRadius, 16.0, inside);
        Render2DEngine.drawPlayerHead(matrices, (PlayerEntity)ClickGUI.mc.player, absoluteXP + 10.0f, absoluteYP + 10.0f, playerHeadSize);
        String playerName = mc.getSession().getUsername();
        String playerId = "ID: " + LoginHandler.userid;
        String playerPlan = "Plan: " + LoginHandler.plan;
        int baseX = (int)(absoluteXP + 10.0f + (float)playerHeadSize + 10.0f);
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
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), playerName, (double)baseX, (double)(absoluteYP + 10.0f), playerNameColor, false);
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), playerId, (double)baseX, (double)(absoluteYP + 20.0f), playerIdColor, false);
        ClientMain.fontRenderer.drawString(matrices.getMatrices(), playerPlan, (double)baseX, (double)(absoluteYP + 28.0f), playerPlanColor, false);
        float cateY = this.windowY + 60.0f;
        for (Category category : Category.values()) {
            if (category == this.selectedCategory) {
                ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClickGUI.mc.textRenderer, category.name(), this.windowX + 20.0f, cateY, -1);
                float bgX = this.windowX + 20.0f - 10.0f;
                float bgY = cateY - 10.0f;
                float textWidth = ClientMain.fontRenderer.getWidth(category.name);
                float bgWidth = textWidth + 20.0f;
                Color themenew = ColorUtil.addAlpha(Theme.ENABLED, 128);
                if (this.viewType != ViewType.CONFIG && this.viewType != ViewType.GUI && this.viewType != ViewType.PROFILE) {
                    Render2DEngine.renderRoundedQuad(matrices.getMatrices(), bgX + 5.0f, bgY, bgX + bgWidth, bgY + 20.0f, 10.0, 20.0, themenew);
                }
                if (this.hy != cateY) {
                    this.hy += (cateY - this.hy) / 20.0f;
                }
            } else {
                ClientMain.fontRenderer.drawString(matrices.getMatrices(), ClickGUI.mc.textRenderer, category.name(), this.windowX + 20.0f, cateY, Theme.UNFOCUSED_TEXT_COLOR.getRGB());
            }
            cateY += 25.0f;
        }
        if (this.selectedModule != null) {
            if (this.settingsFieldX > -80.0f) {
                this.settingsFieldX -= 100.0f;
            }
        } else if (this.settingsFieldX < 0.0f) {
            this.settingsFieldX += 5.0f;
        }
        if (this.selectedModule != null) {
            Render2DEngine.renderRoundedQuad(matrices.getMatrices(), this.windowX + 330.0f, this.windowY + 60.0f, this.windowX + this.width, this.windowY + this.height - 20.0f, 5.0, 20.0, Theme.SETTINGS_BG);
            if (this.isHovered(this.windowX + 430.0f + this.settingsFieldX, this.windowY + 60.0f, this.windowX + this.width, this.windowY + this.height - 20.0f, mouseX, mouseY)) {
                if (this.dWheel < 0.0 && Math.abs(this.settingsF) + 170.0f < (float)(this.selectedModule.getSettings().size() * 25)) {
                    this.settingsF -= 32.0f;
                }
                if (this.dWheel > 0.0 && this.settingsF < 0.0f) {
                    this.settingsF += 32.0f;
                }
            }
            if (this.settingsFNow != this.settingsF) {
                this.settingsFNow += (this.settingsF - this.settingsFNow) / 20.0f;
                this.settingsFNow = (int)this.settingsFNow;
            }
        }
        if (this.isHovered(this.windowX + 100.0f + this.settingsFieldX, this.windowY + 60.0f, this.windowX + 425.0f + this.settingsFieldX, this.windowY + this.height, mouseX, mouseY)) {
            if (this.dWheel < 0.0 && Math.abs(this.modScrollEnd) + 220.0f < (float)(ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).size() * 40)) {
                this.modScrollEnd -= 25.0f;
            }
            if (this.dWheel > 0.0 && this.modScrollEnd < 0.0f) {
                this.modScrollEnd += 25.0f;
            }
        }
        if (this.modScrollNow != this.modScrollEnd) {
            this.modScrollNow += (this.modScrollEnd - this.modScrollNow) / 20.0f;
            this.modScrollNow = (int)this.modScrollNow;
        }
        float modY = 70.0f + this.modScrollNow;
        if (this.viewType == ViewType.CONFIG || this.viewType == ViewType.GUI || this.viewType == ViewType.PROFILE) {
            ClickGUI.disableScissor();
            return;
        }
        if (ModuleManager.isAnyModuleExpanded()) {
            ClickGUI.enableScissor(this.windowX + 190.0f, this.windowY + 60.0f, this.windowX + this.width - 3.0f, this.windowY + this.height - 20.0f);
        }
        for (ModButton modButton : this.modButtons) {
            Module module = modButton.getModule();
            if (!ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).contains(module) || !module.getName().toString().contains(this.searchText.toLowerCase())) continue;
            modButton.setX(this.windowX + this.settingsFieldX);
            modButton.setY(modY);
            modButton.drawScreen(matrices.getMatrices(), mouseX, mouseY, delta);
            modY += 40.0f;
        }
        RenderSystem.disableScissor();
        ClickGUI.disableScissor();
        if (this.transition != null) {
            this.transition.update(true);
            Render2DEngine.fill(matrices.getMatrices(), this.transition.getValue(), this.windowY, this.transition.getEnd() - 19.0f, this.windowY + this.height, Theme.WINDOW_COLOR.getRGB());
            if (this.transition.hasEnded()) {
                this.transition = null;
            }
        }
    }

    public float smoothTrans(double current, double last) {
        return (float)(current + (last - current) / (double)(MinecraftClient.getInstance().getCurrentFps() / 10));
    }

    public void open() {
        this.isOpening = true;
        this.isClosing = false;
        this.currentScale = 0.1f;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        SelfDestruct selfDestruct;
        super.mouseClicked(mouseX, mouseY, button);
        float titleBarHeight = 20.0f;
        if (this.isHovered(this.windowX, this.windowY, this.windowX + this.width, this.windowY + titleBarHeight, mouseX, mouseY) && button == 0) {
            this.drag = true;
            this.dragX = (float)(mouseX - (double)this.windowX);
            this.dragY = (float)(mouseY - (double)this.windowY);
            return true;
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.mouseClicked(mouseX, mouseY, button);
        }
        if (this.viewType == ViewType.GUI) {
            this.editGuiMenu.mouseClicked(mouseX, mouseY, button);
        }
        if (this.viewType == ViewType.PROFILE) {
            this.profileScreen.mouseClicked(mouseX, mouseY, button);
        }
        float settingsTextWidth = ClickGUI.mc.textRenderer.getWidth("Configs");
        Objects.requireNonNull(ClickGUI.mc.textRenderer);
        float settingsTextHeight = 9.0f;
        float settingsTextX = this.windowX + 20.0f;
        float settingsTextY = this.windowY + 300.0f;
        float configsTextX = this.windowX + 20.0f;
        float configsTextY = this.windowY + 340.0f;
        float configsTextWidth = ClickGUI.mc.textRenderer.getWidth("Settings");
        Objects.requireNonNull(ClickGUI.mc.textRenderer);
        float configsTextHeight = 9.0f;
        if (mouseX >= (double)settingsTextX && mouseX <= (double)(settingsTextX + settingsTextWidth) && mouseY >= (double)settingsTextY && mouseY <= (double)(settingsTextY + settingsTextHeight) && button == 0) {
            this.viewType = ViewType.CONFIG;
            return true;
        }
        if (mouseX >= (double)configsTextX && mouseX <= (double)(configsTextX + configsTextWidth) && mouseY >= (double)configsTextY && mouseY <= (double)(configsTextY + configsTextHeight) && button == 0) {
            this.viewType = ViewType.GUI;
            return true;
        }
        float relativeXP = 20.0f;
        float relativeYP = 380.0f;
        float absoluteXP = this.windowX + relativeXP;
        float absoluteYP = this.windowY + relativeYP;
        int playerHeadSize = 32;
        float profileWidth = 150.0f;
        float profileHeight = playerHeadSize + ClientMain.fontRenderer.getFont().getSize() + 10;
        if (mouseX >= (double)absoluteXP && mouseX <= (double)(absoluteXP + profileWidth) && mouseY >= (double)absoluteYP && mouseY <= (double)(absoluteYP + profileHeight) && button == 0) {
            this.viewType = ViewType.PROFILE;
            return true;
        }
        float relativeSearchX = 220.0f;
        float relativeSearchY = 35.0f;
        float absoluteSearchX = this.windowX + relativeSearchX;
        float absoluteSearchY = this.windowY + relativeSearchY;
        int searchBarWidth = 220;
        int searchBarHeight = 20;
        if (mouseX >= (double)absoluteSearchX && mouseX <= (double)(absoluteSearchX + (float)searchBarWidth) && mouseY >= (double)absoluteSearchY && mouseY <= (double)(absoluteSearchY + (float)searchBarHeight)) {
            this.isSearchFocused = true;
            return true;
        }
        this.isSearchFocused = false;
        float backIconX = this.windowX + 20.0f;
        float backIconY = this.windowY + this.height - 50.0f;
        int backIconWidth = 32;
        int backIconHeight = 32;
        if (this.isHovered(backIconX, backIconY, backIconX + (float)backIconWidth, backIconY + (float)backIconHeight, mouseX, mouseY) && button == 0) {
            if (this.viewType == ViewType.CONFIG) {
                this.viewType = ViewType.HOME;
            }
            return true;
        }
        float relativeX = 20.0f;
        float relativeY = 260.0f;
        float absoluteX = this.windowX + relativeX;
        float absoluteY = this.windowY + relativeY;
        float buttonWidth = 100.0f;
        float buttonHeight = 20.0f;
        if (mouseX >= (double)absoluteX && mouseX <= (double)(absoluteX + buttonWidth) && mouseY >= (double)absoluteY && mouseY <= (double)(absoluteY + buttonHeight) && (selfDestruct = (SelfDestruct)ModuleManager.INSTANCE.getModuleByName("SelfDestruct")) != null) {
            selfDestruct.onEnable();
            return true;
        }
        float cateY = this.windowY + 65.0f;
        for (Category category : Category.values()) {
            Objects.requireNonNull(ClickGUI.mc.textRenderer);
            if (this.isHovered(this.windowX, cateY - 8.0f, this.windowX + 50.0f, cateY + 9.0f + 8.0f, mouseX, mouseY) && button == 0) {
                this.selectedCategory = category;
                this.viewType = ViewType.HOME;
                ModuleManager.INSTANCE.unexpandAll();
                this.dWheel = 0.0;
                this.modButtons.clear();
                float modY = 70.0f + this.modScrollNow;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory)) {
                    this.modButtons.add(new ModButton(module, 0.0, modY, this));
                    modY += 40.0f;
                }
                return true;
            }
            cateY += 25.0f;
        }
        if (this.viewType != ViewType.CONFIG && this.viewType != ViewType.GUI && this.viewType != ViewType.PROFILE) {
            for (ModButton modButton : this.modButtons) {
                if (!modButton.mouseClicked(mouseX, mouseY, button)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.drag = false;
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.mouseReleased(mouseX, mouseY, button);
            return super.mouseReleased(mouseX, mouseY, button);
        }
        if (this.viewType == ViewType.GUI) {
            this.editGuiMenu.mouseReleased(mouseX, mouseY, button);
            return super.mouseReleased(mouseX, mouseY, button);
        }
        for (ModButton modButton : this.modButtons) {
            if (!ModuleManager.INSTANCE.getModulesByCategory(this.selectedCategory).contains(modButton.getModule())) continue;
            modButton.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
        if (this.isHovered(this.windowX + 100.0f + this.settingsFieldX, this.windowY + 60.0f, this.windowX + 425.0f + this.settingsFieldX, this.windowY + this.height, mouseX, mouseY)) {
            this.dWheel = verticalScroll;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalScroll, verticalScroll);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModButton mb : this.modButtons) {
            mb.keyPressed(keyCode, scanCode, modifiers);
        }
        if (this.viewType == ViewType.CONFIG) {
            this.configScreen.keyPressed(keyCode, scanCode, modifiers);
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        if (this.viewType == ViewType.GUI) {
            this.editGuiMenu.keyPressed(keyCode, scanCode, modifiers);
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        if (this.isSearchFocused) {
            if (keyCode != 257 && keyCode == 259 && this.searchText.length() > 0) {
                this.searchText = this.searchText.substring(0, this.searchText.length() - 1);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        for (ModButton mb : this.modButtons) {
            mb.charTyped(chr, modifiers);
        }
        this.configScreen.charTyped(chr, modifiers);
        if (this.isSearchFocused) {
            this.searchText = this.searchText + chr;
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    public void close() {
        this.isClosing = true;
        this.isOpening = false;
        this.searchText = "";
        super.close();
        ModuleManager.INSTANCE.unexpandAll();
    }

    public boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)x2 && mouseY >= (double)y && mouseY <= (double)y2;
    }

    public static void betterScissor(double x, double y, double x2, double y2) {
        int xPercent = (int)(x / (double)mc.getWindow().getScaledWidth());
        int yPercent = (int)(y / (double)mc.getWindow().getHeight());
        int widthPercent = (int)(x2 / (double)mc.getWindow().getWidth());
        int heightPercent = (int)(y2 / (double)mc.getWindow().getHeight());
        RenderSystem.enableScissor((int)xPercent, (int)yPercent, (int)widthPercent, (int)heightPercent);
    }

    public static void enableScissor(int x1, int y1, int x2, int y2) {
        int scaleFactor = (int)mc.getWindow().getScaleFactor();
        RenderSystem.enableScissor((int)(x1 * scaleFactor), (int)((mc.getWindow().getScaledHeight() - y2) * scaleFactor), (int)((x2 - x1) * scaleFactor), (int)((y2 - y1) * scaleFactor));
    }

    public static void enableScissor(double x, double y, double x2, double y2) {
        ClickGUI.enableScissor((int)x, (int)y, (int)x2, (int)y2);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    private float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }
}
