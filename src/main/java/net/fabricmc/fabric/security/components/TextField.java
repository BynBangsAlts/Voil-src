package net.fabricmc.fabric.security.components;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextField
extends TextFieldWidget {
    private static final int INNER_PADDING = 4;
    private static final long fade = 500L;
    private final Map<Integer, Long> charAppearTimes = new HashMap<Integer, Long>();

    public TextField(TextRenderer textRenderer, int x, int y, int width, int height, Text message) {
        super(textRenderer, x, y, width, height, message);
    }

    public void write(String text) {
        super.write(text);
        long currentTime = System.currentTimeMillis();
        for (int i = this.getText().length() - text.length(); i < this.getText().length(); ++i) {
            this.charAppearTimes.put(i, currentTime);
        }
    }

    public void renderWidget(DrawContext matrices, int mouseX, int mouseY, float delta) {
        Color backgroundColor = new Color(44, 44, 44, 255);
        double cornerRadius = 5.0;
        Render2DEngine.drawRoundedBlur(matrices.getMatrices(), this.getX(), this.getY(), this.width, this.height, (float)cornerRadius, 14.0f, 0.0f, false);
        long currentTime = System.currentTimeMillis();
        String text = this.getText();
        int x = this.getX() + 4;
        int y = this.getY() + (this.height - 18) / 2;
        for (int i = 0; i < text.length(); ++i) {
            long appearTime = this.charAppearTimes.getOrDefault(i, currentTime);
            float fadeFactor = Math.min(1.0f, (float)(currentTime - appearTime) / 500.0f);
            int alpha = (int)(fadeFactor * 255.0f);
            int color = new Color(255, 255, 255, alpha).getRGB();
            ClientMain.fontRenderer.draw(matrices.getMatrices(), String.valueOf(text.charAt(i)), x, y, color);
            x += (int)ClientMain.fontRenderer.getWidth(String.valueOf(text.charAt(i)));
        }
        if (this.isFocused()) {
            int cursorX = (int)((float)this.getX() + ClientMain.fontRenderer.getWidth(text.substring(0, this.getCursor())) + 4.0f);
            int cursorY = this.getY() + (this.height - 8) / 2;
            int cursorEndY = cursorY + 9;
            this.renderCursor(matrices.getMatrices(), cursorX, cursorY, cursorEndY);
        }
    }

    private void renderCursor(MatrixStack matrices, int cursorX, int cursorY, int cursorEndY) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp((GlStateManager.LogicOp)GlStateManager.LogicOp.OR_REVERSE);
        Render2DEngine.fill(matrices, cursorX, cursorY - 1, cursorX + 1, cursorEndY, -1);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableDepthTest();
    }
}
