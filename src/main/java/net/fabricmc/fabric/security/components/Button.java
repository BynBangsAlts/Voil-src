package net.fabricmc.fabric.security.components;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Button
extends ButtonWidget {
    public Button(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        super(x, y, width, height, message, onPress, button -> Text.translatable((String)"narration.button", (Object[])new Object[]{message}));
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer textRenderer = mc.textRenderer;
        boolean isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        int borderAlpha = 175;
        int backgroundAlpha = 127;
        Color borderColor = isHovered ? new Color(0x3F3D3F | borderAlpha << 24, true) : new Color(0x2E2D2E | borderAlpha << 24, true);
        int borderPadding = 2;
        Color backgroundColor = new Color(0x404040 | backgroundAlpha << 24, true);
        Render2DEngine.drawRoundedBlur(context.getMatrices(), this.getX(), this.getY(), this.width, this.height, 5.0f, 16.0f, 0.0f, false);
        int textColor = isHovered ? 0xFFFFFF : 0xCCCCCC;
        this.drawCenteredText(context.getMatrices(), textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 18) / 2, textColor);
    }

    private void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int x, int y, int color) {
        String textString = text.getString();
        float textWidth = ClientMain.fontRenderer.getWidth(textString);
        float centeredX = (float)x - textWidth / 2.0f;
        ClientMain.fontRenderer.draw(matrices, textString, centeredX, y, color);
    }
}
