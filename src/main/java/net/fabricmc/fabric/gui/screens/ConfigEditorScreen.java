package net.fabricmc.fabric.gui.screens;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.scripting.ScriptAPI;
import net.fabricmc.fabric.api.scripting.ScriptParser;
import net.fabricmc.fabric.api.scripting.ScriptWrapper;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.screens.CreateConfigPopup;
import net.fabricmc.fabric.gui.screens.EditScriptScreen;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.security.UserConstants;
import net.fabricmc.fabric.systems.config.ConfigWrapper;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigEditorScreen
extends Component {
    public final GUI parent;
    private final String[] tabs = new String[]{"Configs", "Scripts"};
    private String currentTab = this.tabs[0];
    public EditScriptScreen editScriptScreen = null;
    private CreateConfigPopup createMenu = null;
    private ConfigWrapper configToDelete = null;
    private boolean showDeleteConfirmation = false;
    private final List<Hitbox> hitboxes = new ArrayList<Hitbox>();

    public ConfigEditorScreen(GUI parent) {
        this.parent = parent;
        UserConstants.configs.values().clear();
        Networking.instance.sendGetAllConfigs();
    }

    @Override
    public void drawScreen(MatrixStack ms, int mx, int my, float delta) {
        if (this.editScriptScreen != null) {
            this.editScriptScreen.drawScreen(ms, mx, my, delta);
            return;
        }
        if (this.createMenu != null) {
            this.createMenu.drawScreen(ms, mx, my, delta);
            return;
        }
        int x = this.parent.x;
        int y = this.parent.y;
        String title = "Choose and load configs or scripts!";
        int tw = (int)ClientMain.fontRenderer.getWidth(title);
        ClientMain.fontRenderer.draw(ms, title, x + 180 + (340 - tw) / 2, y + 3, 0xFFFFFF);
        Render2DEngine.fill(ms, x + 180, y + 24, x + 520, y + 23, new Color(0x323232).getRGB());
        int pw = 340;
        int qw = 60;
        int sp = -3;
        int groupX = x + 180 + (pw - (qw * 2 + sp)) / 2;
        float createX = (float)groupX + (float)qw / 2.0f - (ClientMain.fontRenderer.getWidth("Create New") - 200.0f);
        Render2DEngine.drawRectangle(ms, createX - 2.5f, y + 30, ClientMain.fontRenderer.getWidth("Create New") + 5.0f, ClientMain.fontRenderer.getStringHeight("Create New", false) + 3.0f, 3.0f, 1.0f, Theme.ENABLED);
        ClientMain.fontRenderer.draw(ms, "Create New", createX, y + 25, 0xFFFFFF);
        float offset = 0.0f;
        float tabSpacing = 20.0f;
        for (String t : this.tabs) {
            float tx = (float)groupX + (float)qw / 2.0f - (ClientMain.fontRenderer.getWidth(t) + 100.0f) + offset;
            if (t.equals(this.currentTab)) {
                Render2DEngine.renderRoundedQuad(ms, tx - 5.0f, y + 30, tx + ClientMain.fontRenderer.getWidth(t) + 5.0f, y + 45, 3.0, 15.0, Theme.ENABLED);
            }
            ClientMain.fontRenderer.draw(ms, t, tx, y + 25, 0xFFFFFF);
            offset += ClientMain.fontRenderer.getWidth(t) + tabSpacing;
        }
        this.hitboxes.clear();
        int y0 = y + 55;
        int x0 = groupX - 125;
        int col = 0;
        int cw = 120;
        int ch = 50;
        int rowSp = 15;
        int colSp = 5;
        double radius = 8.0;
        double samples = 10.0;
        if (this.currentTab.equals("Configs")) {
            int cx = x0;
            int cy = y0;
            for (ConfigWrapper cfg : UserConstants.configs.values()) {
                Render2DEngine.renderRoundedQuad(ms, cx, cy, cx + cw, cy + ch, radius, samples, new Color(27, 27, 27));
                Render2DEngine.renderRoundedQuad(ms, cx, cy + ch + sp - 10, cx + cw, cy + ch * 2 + sp - 40, radius, samples, new Color(34, 34, 34));
                Render2DEngine.fill(ms, cx, cy + ch + sp - 10, cx + cw, cy + ch + sp - 5, new Color(27, 27, 27).getRGB());
                ClientMain.fontRenderer.draw(ms, cfg.getName(), cx + 5, cy, 0xFFFFFF);
                ClientMain.fontRenderer.draw(ms, cfg.getUser() + " - ", cfg.getPlan(), cx + 5, cy + 38, Color.white, cfg.getColor());
                String desc = cfg.getDescription();
                int lh = 10;
                int yLine = cy + 10;
                ArrayList<String> lines = new ArrayList<String>();
                StringBuilder sb = new StringBuilder();
                for (String w : desc.split(" ")) {
                    if (sb.length() + w.length() + 1 > 20) {
                        lines.add(sb.toString());
                        sb = new StringBuilder(w);
                        continue;
                    }
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(w);
                }
                if (sb.length() > 0) {
                    lines.add(sb.toString());
                }
                for (int i = 0; i < lines.size(); ++i) {
                    ClientMain.fontRenderer.draw(ms, (String)lines.get(i), cx + 5, yLine + i * lh, 0xAAAAAA);
                }
                this.hitboxes.add(new Hitbox(cfg, null, cx, cy, cw, ch));
                if (++col % 3 == 0) {
                    cy += ch + rowSp;
                    cx = x0;
                    continue;
                }
                cx += cw + colSp;
            }
        } else {
            int sx = x0;
            int sy = y0;
            for (ScriptWrapper s : UserConstants.scripts.values()) {
                Render2DEngine.renderRoundedQuad(ms, sx, sy, sx + cw, sy + ch, radius, samples, new Color(27, 27, 27));
                Render2DEngine.renderRoundedQuad(ms, sx, sy + ch + sp - 10, sx + cw, sy + ch * 2 + sp - 40, radius, samples, new Color(34, 34, 34));
                Render2DEngine.fill(ms, sx, sy + ch + sp - 10, sx + cw, sy + ch + sp - 5, new Color(27, 27, 27).getRGB());
                ClientMain.fontRenderer.draw(ms, s.getName(), sx + 5, sy, 0xFFFFFF);
                ClientMain.fontRenderer.draw(ms, s.getUser() + " - ", s.getPlan(), sx + 5, sy + 38, Color.white, s.getColor());
                this.hitboxes.add(new Hitbox(null, s, sx, sy, cw, ch));
                if (++col % 3 == 0) {
                    sy += ch + rowSp;
                    sx = x0;
                    continue;
                }
                sx += cw + colSp;
            }
        }
        if (this.showDeleteConfirmation && this.configToDelete != null) {
            int pw1 = 200;
            int ph = 100;
            int px = this.parent.x + (340 - pw1) / 2 + 150;
            int py = this.parent.y + 100;
            Render2DEngine.drawRectangle(ms, px, py, 200.0f, 100.0f, 5.0f, 1.0f, new Color(20, 20, 20));
            ClientMain.fontRenderer.draw(ms, "Delete config '" + this.configToDelete.getName() + "'?", px + 10, py + 15, 0xFFFFFF);
            Render2DEngine.renderRoundedQuad(ms, px + 20, py + 60, px + 80, py + 80, 3.0, 10.0, Theme.ENABLED);
            ClientMain.fontRenderer.draw(ms, "Yes", px + 40, py + 60, 0xFFFFFF);
            Render2DEngine.renderRoundedQuad(ms, px + 120, py + 60, px + 180, py + 80, 3.0, 10.0, new Color(90, 90, 90));
            ClientMain.fontRenderer.draw(ms, "No", px + 140, py + 60, 0xFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (this.editScriptScreen != null) {
            return this.editScriptScreen.mouseClicked(mx, my, btn);
        }
        if (this.createMenu != null) {
            this.createMenu.mouseClicked(mx, my, btn);
            return true;
        }
        if (this.showDeleteConfirmation && this.configToDelete != null) {
            int px = this.parent.x + 70 + 150;
            int py = this.parent.y + 100;
            if (btn == 0 && mx >= (double)(px + 20) && mx <= (double)(px + 80) && my >= (double)(py + 60) && my <= (double)(py + 80)) {
                String id = this.configToDelete.getId().replace(".json", "");
                Networking.instance.sendDeleteConfig(id);
                UserConstants.configs.values().clear();
                Networking.instance.sendGetAllConfigs();
                this.showDeleteConfirmation = false;
                this.configToDelete = null;
                return true;
            }
            if (btn == 0 && mx >= (double)(px + 120) && mx <= (double)(px + 180) && my >= (double)(py + 60) && my <= (double)(py + 80)) {
                this.showDeleteConfirmation = false;
                this.configToDelete = null;
                return true;
            }
            return false;
        }
        int pw = 340;
        int qw = 60;
        int sp = -3;
        int groupX = this.parent.x + 180 + (pw - (qw * 2 + sp)) / 2;
        if (btn == 0) {
            float cx = (float)groupX + (float)qw / 2.0f - (ClientMain.fontRenderer.getWidth("Create New") - 200.0f);
            float cy = (float)this.parent.y + 25.0f;
            float cw = ClientMain.fontRenderer.getWidth("Create New") + 5.0f;
            float ch = ClientMain.fontRenderer.getStringHeight("Create New", false) + 3.0f;
            if (mx >= (double)(cx - 2.5f) && mx <= (double)(cx - 2.5f + cw) && my >= (double)cy && my <= (double)(cy + ch)) {
                this.createMenu = new CreateConfigPopup(this);
                return true;
            }
        }
        if (btn == 0) {
            float offset = 0.0f;
            float tabSp = 20.0f;
            int tabY = this.parent.y + 25;
            int tabH = 15;
            for (String t : this.tabs) {
                float tx = (float)groupX + (float)qw / 2.0f - (ClientMain.fontRenderer.getWidth(t) + 100.0f) + offset;
                float tw = ClientMain.fontRenderer.getWidth(t) + 10.0f;
                if (mx >= (double)(tx - 5.0f) && mx <= (double)(tx + tw + 5.0f) && my >= (double)tabY && my <= (double)(tabY + tabH)) {
                    this.currentTab = t;
                    return true;
                }
                offset += ClientMain.fontRenderer.getWidth(t) + tabSp;
            }
        }
        for (Hitbox hb : this.hitboxes) {
            if (!hb.contains((int)mx, (int)my)) continue;
            if (hb.script != null) {
                if (btn == 1) {
                    this.editScriptScreen = new EditScriptScreen(this, hb.script);
                } else {
                    ScriptParser.parse(hb.script.getScript(), hb.script.getName());
                    ScriptAPI.getInstance().toggleScript(hb.script.getName());
                }
            } else if (btn == 1) {
                this.configToDelete = hb.config;
                this.showDeleteConfirmation = true;
            } else {
                String id = hb.config.getId().replace(".json", "");
                Networking.instance.sendGetConfig(id);
            }
            return true;
        }
        return false;
    }

    @Override
    public void mouseDragged(double mX, double mY, int b, double dx, double dy) {
    }

    @Override
    public void charTyped(char c, int k) {
        if (this.createMenu != null) {
            this.createMenu.charTyped(c, k);
        } else if (this.editScriptScreen != null) {
            this.editScriptScreen.charTyped(c, k);
        }
    }

    @Override
    public boolean keyPressed(int kc, int sc, int m) {
        if (this.createMenu != null) {
            this.createMenu.keyPressed(kc, sc, m);
            return true;
        }
        if (this.editScriptScreen != null) {
            return this.editScriptScreen.keyPressed(kc, sc, m);
        }
        return false;
    }

    @Override
    public void mouseScrolled(double x, double y, double h, double v) {
        if (this.editScriptScreen != null) {
            this.editScriptScreen.mouseScrolled(x, y, h, v);
        }
    }

    public void closeCreateMenu() {
        this.createMenu = null;
    }

    private static class Hitbox {
        final ConfigWrapper config;
        final ScriptWrapper script;
        final int x;
        final int y;
        final int w;
        final int h;

        Hitbox(ConfigWrapper c, ScriptWrapper s, int x, int y, int w, int h) {
            this.config = c;
            this.script = s;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean contains(int mx, int my) {
            return mx >= this.x && mx <= this.x + this.w && my >= this.y && my <= this.y + this.h;
        }
    }
}
