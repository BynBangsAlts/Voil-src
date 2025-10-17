package net.fabricmc.fabric.systems.module.impl.Client;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;

public class HUD
extends Module {
    public BooleanSetting vignette = new BooleanSetting("V".concat("(").concat("i").concat("&").concat("g").concat("#").concat("n").concat("-").concat("e").concat("_").concat("t").concat("*").concat("t").concat("+").concat("e"), true, "T".concat("!").concat("o").concat("&").concat(" ").concat("!").concat("r").concat("*").concat("e").concat(")").concat("n").concat("&").concat("d").concat("-").concat("e").concat("(").concat("r").concat("-").concat(" ").concat("^").concat("v").concat("#").concat("i").concat(")").concat("g").concat("!").concat("n").concat("!").concat("e").concat("_").concat("t").concat(")").concat("t").concat("@").concat("e").concat("+").concat(" ").concat("-").concat("o").concat("^").concat("n").concat("@").concat(" ").concat("&").concat("h").concat("-").concat("u").concat("@").concat("d"));

    public HUD() {
        super("H".concat("&").concat("U").concat("_").concat("D"), "H".concat("(").concat("u").concat("^").concat("d").concat("&").concat(" ").concat("$").concat("e").concat("_").concat("l").concat("!").concat("e").concat("_").concat("m").concat("$").concat("e").concat("-").concat("n").concat("(").concat("t").concat("(").concat("s"), Category.Client);
        this.addSettings(this.vignette);
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (HUD.mc.player == null || HUD.mc.world == null) {
            return;
        }
        int x = ClientMain.getHudModuleManager().getX(this);
        int y = ClientMain.getHudModuleManager().getY(this);
        Color color1 = new Color(250, 171, 10);
        Color color2 = new Color(120, 21, 178);
        Color vig = new Color(0, 0, 0);
        Render2DEngine.drawGlow(matrices, x + 10, y + 60, 40.0f, 20.0f, 6.0f, 10.0f, ColorUtil.TwoColor(Color.GREEN, Color.RED, 10.0, 0.0));
        Render2DEngine.drawRoundedBlur(matrices, x + 10, y + 60, 40.0f, 20.0f, 6.0f, 12.0f, 1.0f, this.vignette.isEnabled());
        int boxWidth = 40;
        int boxHeight = 20;
        int centerX = x + 10 + boxWidth / 2;
        int centerY = y + 60 + boxHeight / 2;
        int textX = (int)((float)centerX - ClientMain.fontRenderer.getCurrentFont(Font.VERDANA).getWidth("Voil.lol") / 2.0f);
        int textY = (int)((float)centerY - ClientMain.fontRenderer.getCurrentFont(Font.VERDANA).getStringHeight("Voil.lol", false) / 2.0f);
        ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), "Voil.lol", textX + 1, textY - 3, 0, color1, color2, Font.VERDANA, false);
    }
}
