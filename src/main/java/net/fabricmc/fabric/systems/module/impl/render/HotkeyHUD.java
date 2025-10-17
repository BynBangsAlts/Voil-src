package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;

public class HotkeyHUD
extends Module {
    public HotkeyHUD() {
        super("H".concat("(").concat("o").concat("_").concat("t").concat("^").concat("k").concat("&").concat("e").concat("-").concat("y").concat("!").concat("H").concat("^").concat("U").concat("_").concat("D"), "S".concat("(").concat("h").concat(")").concat("o").concat("^").concat("w").concat("!").concat("s").concat("*").concat(" ").concat(")").concat("t").concat("^").concat("h").concat("$").concat("e").concat("!").concat(" ").concat("#").concat("h").concat("^").concat("o").concat("#").concat("t").concat("^").concat("k").concat("(").concat("e").concat("@").concat("y").concat("^").concat(" ").concat("&").concat("o").concat("-").concat("f").concat("@").concat(" ").concat("*").concat("t").concat("@").concat("h").concat(")").concat("e").concat("^").concat(" ").concat("_").concat("m").concat("-").concat("o").concat("+").concat("d").concat("^").concat("u").concat(")").concat("l").concat("_").concat("e"), Category.Render);
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (HotkeyHUD.mc.world != null && this.isEnabled()) {
            int x = ClientMain.getHudModuleManager().getX(this);
            int y = ClientMain.getHudModuleManager().getY(this);
            int width = 80;
            int padding = 5;
            int spacing = 4;
            int cornerRadius = 10;
            String title = "Keybinds";
            int titleHeight = (int)ClientMain.fontRenderer.getStringHeight(title, false);
            int titleSpacing = 10;
            List<Module> renderableModules = ModuleManager.INSTANCE.getModules().stream().filter(module -> module != null && module.getKey() != 0).toList();
            int totalStringHeight = renderableModules.stream().mapToInt(module -> (int)ClientMain.fontRenderer.getStringHeight(module.getName().toString(), false)).sum();
            int totalSpacingHeight = (renderableModules.size() - 1) * spacing;
            int height = totalStringHeight + totalSpacingHeight + padding * 2 + titleHeight + titleSpacing;
            Render2DEngine.drawRoundedBlur(matrices, x, y, width, height, cornerRadius, 14.0f, 0.0f, true);
            int currentY = y + padding;
            ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), title, x + padding, currentY, 0, new Color(120, 21, 178), new Color(120, 21, 178).darker().darker(), Font.VERDANA, false);
            Render2DEngine.fill(matrices, x, currentY += titleHeight + titleSpacing, x + width + 2, currentY - 1, new Color(120, 21, 178).getRGB());
            for (Module module2 : renderableModules) {
                String moduleName = module2.getName().toString();
                String key = KeyUtils.getKeyName(module2.getKey());
                String text = moduleName + " | " + key;
                Color firstColor = module2.isEnabled() ? new Color(120, 21, 178) : Color.WHITE;
                Color secondColor = module2.isEnabled() ? firstColor.darker().darker() : Color.WHITE;
                ClientMain.fontRenderer.drawGradient(RenderHelper.getContext(), text, x + padding, currentY, 0, firstColor, secondColor, Font.VERDANA, false);
                currentY += (int)(ClientMain.fontRenderer.getStringHeight(moduleName, false) + (float)spacing);
            }
        }
    }
}
