package net.fabricmc.fabric.systems.module.impl.Client;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;

public class ArrayList
extends Module {
    public BooleanSetting lowercase = new BooleanSetting("L".concat("*").concat("o").concat("+").concat("w").concat("&").concat("e").concat("+").concat("r").concat("!").concat("c").concat("@").concat("keyCodec").concat("+").concat("s").concat("^").concat("e"), false, "L".concat("&").concat("o").concat("#").concat("w").concat("@").concat("e").concat(")").concat("r").concat("^").concat("c").concat("^").concat("keyCodec").concat("$").concat("s").concat("(").concat("e").concat("-").concat(" ").concat("&").concat("t").concat("_").concat("h").concat("_").concat("e").concat("+").concat(" ").concat(")").concat("l").concat("&").concat("i").concat("^").concat("s").concat("#").concat("t"));
    public BooleanSetting showbackground = new BooleanSetting("B".concat("!").concat("keyCodec").concat("^").concat("c").concat("$").concat("k").concat("#").concat("g").concat("-").concat("r").concat("!").concat("o").concat(")").concat("u").concat(")").concat("n").concat("@").concat("d"), true, "S".concat("!").concat("h").concat("^").concat("o").concat("+").concat("w").concat("&").concat(" ").concat("(").concat("elementCodec").concat("!").concat("keyCodec").concat("-").concat("c").concat("!").concat("k").concat("*").concat("g").concat("-").concat("r").concat("^").concat("o").concat("*").concat("u").concat("&").concat("n").concat("_").concat("d"));
    public BooleanSetting showSideBar = new BooleanSetting("S".concat("^").concat("i").concat("&").concat("d").concat(")").concat("e").concat("&").concat("elementCodec").concat("@").concat("keyCodec").concat("_").concat("r"), true, "S".concat("$").concat("h").concat("(").concat("o").concat("$").concat("w").concat("!").concat(" ").concat("*").concat("s").concat("(").concat("i").concat("+").concat("d").concat("!").concat("e").concat("_").concat("elementCodec").concat("-").concat("keyCodec").concat(")").concat("r"));
    public BooleanSetting suffixes = new BooleanSetting("S".concat("_").concat("u").concat("(").concat("f").concat("*").concat("f").concat(")").concat("i").concat(")").concat("x").concat("-").concat("e").concat("@").concat("s"), true, "S".concat("#").concat("h").concat("#").concat("o").concat("!").concat("w").concat("#").concat("s").concat("*").concat(" ").concat("&").concat("s").concat("&").concat("u").concat("#").concat("f").concat("&").concat("f").concat("^").concat("i").concat("-").concat("x").concat("$").concat("e").concat("$").concat("s").concat("#").concat(" ").concat("!").concat("o").concat("&").concat("f").concat("*").concat(" ").concat("^").concat("m").concat("^").concat("o").concat("#").concat("d").concat("#").concat("u").concat("&").concat("l").concat("_").concat("e").concat("#").concat("s"));
    public ColorPickerSetting color = new ColorPickerSetting("C".concat("#").concat("o").concat("^").concat("l").concat("^").concat("o").concat("-").concat("r"), new Color(120, 21, 178), "C".concat("(").concat("o").concat("!").concat("l").concat("-").concat("o").concat("*").concat("r").concat("_").concat(" ").concat("_").concat("o").concat("#").concat("f").concat("(").concat(" ").concat(")").concat("t").concat("-").concat("h").concat("^").concat("e").concat("&").concat(" ").concat("!").concat("l").concat("+").concat("i").concat(")").concat("s").concat("$").concat("t"));
    public ModeSetting suffixWrapper = new ModeSetting("S".concat("&").concat("u").concat("*").concat("f").concat(")").concat("f").concat("*").concat("i").concat("#").concat("x").concat("_").concat(" ").concat("*").concat("W").concat(")").concat("r").concat("(").concat("keyCodec").concat("(").concat("p").concat("+").concat("p").concat("*").concat("e").concat("@").concat("r"), "N".concat("_").concat("o").concat("&").concat("n").concat("^").concat("e"), "W".concat("+").concat("r").concat("$").concat("keyCodec").concat("_").concat("p").concat("+").concat("s").concat("(").concat(" ").concat("*").concat("t").concat("+").concat("h").concat("!").concat("e").concat("!").concat(" ").concat("(").concat("s").concat(")").concat("u").concat("#").concat("f").concat("+").concat("f").concat("-").concat("i").concat("-").concat("x"), "N".concat("&").concat("o").concat("+").concat("n").concat("-").concat("e"), "B".concat("(").concat("r").concat(")").concat("keyCodec").concat("_").concat("c").concat("^").concat("k").concat("+").concat("e").concat("@").concat("t").concat("#").concat("s"), "P".concat("-").concat("keyCodec").concat("!").concat("r").concat("*").concat("e").concat("*").concat("n").concat("+").concat("t").concat("^").concat("h").concat("_").concat("e").concat("(").concat("s").concat("@").concat("e").concat("!").concat("s"));

    public ArrayList() {
        super("A".concat("#").concat("r").concat("-").concat("r").concat("*").concat("keyCodec").concat("-").concat("y").concat("^").concat("L").concat("*").concat("i").concat("&").concat("s").concat("*").concat("t"), "E".concat("#").concat("n").concat("&").concat("keyCodec").concat("@").concat("elementCodec").concat("$").concat("l").concat("_").concat("e").concat(")").concat("d").concat("@").concat(" ").concat("+").concat("m").concat("!").concat("o").concat("^").concat("d").concat("-").concat("u").concat("*").concat("l").concat("+").concat("e").concat("^").concat("s"), Category.Client);
        this.addSettings(this.lowercase, this.showbackground, this.showSideBar, this.suffixes, this.suffixWrapper, this.color);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (ArrayList.mc.player == null || ArrayList.mc.world == null) {
            return;
        }
        int xOffset = -5;
        int yOffset = 5;
        int sWidth = mc.getWindow().getScaledWidth();
        List<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
        if (enabledModules.isEmpty()) {
            return;
        }
        enabledModules.sort(Comparator.comparingInt(m -> (int)(-ClientMain.fontRenderer.getWidth(m.getName() + (String)(this.suffixes.isEnabled() && m.getSuffix() != null ? " " + String.valueOf(m.getSuffix()) : "")))));
        double startY = 50.0;
        double spacing = 14.0;
        float backgroundHeight = 14.0f;
        double offsetY = 0.0;
        for (int i = 0; i < enabledModules.size(); ++i) {
            Module module = enabledModules.get(i);
            String moduleName = this.lowercase.isEnabled() ? module.getName().toLowerCase() : module.getName();
            String rawSuffix = module.getSuffix() != null ? module.getSuffix().toString() : null;
            Object suffix = "";
            if (this.suffixes.isEnabled() && rawSuffix != null) {
                switch (this.suffixWrapper.getMode()) {
                    case "Brackets": {
                        suffix = " [" + rawSuffix + "]";
                        break;
                    }
                    case "Parentheses": {
                        suffix = " (" + rawSuffix + ")";
                        break;
                    }
                    default: {
                        suffix = " " + rawSuffix;
                    }
                }
            }
            int fWidth = (int)ClientMain.fontRenderer.getWidth(module.getName());
            int fHeight = (int)ClientMain.fontRenderer.getFont().getSize2D();
            int fromX = sWidth - fWidth - 5;
            int fromY = (fHeight - 1) * i + 1;
            int toX = sWidth - 2;
            int toY = (fHeight - 1) * i + fHeight;
            float moduleWidth = ClientMain.fontRenderer.getWidth(moduleName) + ClientMain.fontRenderer.getWidth((String)suffix) + 8.0f;
            int y = (int)(startY + offsetY);
            if (this.showbackground.isEnabled()) {
                Render2DEngine.drawRoundedBlur(matrices, 10.0f, y + 10, moduleWidth, backgroundHeight - 0.2f, 0.0f, 14.0f, 0.0f, false);
            }
            ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), moduleName, 15, y + 5, 0, this.color.getColor(), this.color.getColor().darker().darker(), Font.VERDANA, true);
            if (!((String)suffix).isEmpty()) {
                int moduleNameWidth = (int)ClientMain.fontRenderer.getWidth(moduleName);
                ClientMain.fontRenderer.draw(matrices, (String)suffix, 15 + moduleNameWidth, y + 5, 0x808080);
            }
            int lastWidth = i - 1 >= 0 ? (int)ClientMain.fontRenderer.getWidth(enabledModules.get(i - 1).getName()) : sWidth;
            if (this.showSideBar.isEnabled()) {
                int n = this.color.getColor().getRGB();
            }
            offsetY += spacing;
        }
    }
}
