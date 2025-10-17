package net.fabricmc.fabric.utils.font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.fabricmc.fabric.utils.font.Font;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.binary.Base64;
import org.joml.Matrix4f;

public class FontRenderer {
    private static final Map<Font, FontRenderer> fonts = new HashMap<Font, FontRenderer>();
    private java.awt.Font theFont;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    private float fontSize;
    private int startChar;
    private int endChar;
    private float[] xPos;
    private float[] yPos;
    public BufferedImage bufferedImage;
    public Identifier resourceLocation;
    private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
    private final Pattern patternUnsupported = Pattern.compile("(?i)\\u00A7[L-O]");
    private Identifier fontPath;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void registerFont(Font font, Identifier fontPath, float size) {
        fonts.put(font, new FontRenderer(fontPath, size));
    }

    public static FontRenderer getFontRenderer(Font name) {
        return fonts.get((Object)name);
    }

    public FontRenderer(Identifier fontPath, float size) {
        this(null, fontPath, size);
    }

    public FontRenderer(Object font, Identifier fontPath, float size) {
        this.fontPath = fontPath;
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size, fontPath);
    }

    private void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.theGraphics = (Graphics2D)this.bufferedImage.getGraphics();
        this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void createFont(Object font, float size, Identifier fontPath) {
        try {
            if (font instanceof java.awt.Font) {
                this.theFont = ((java.awt.Font)font).deriveFont(size);
            } else if (font instanceof File) {
                this.theFont = java.awt.Font.createFont(0, (File)font).deriveFont(size);
            } else if (font instanceof InputStream) {
                this.theFont = java.awt.Font.createFont(0, (InputStream)font).deriveFont(size);
            } else if (font instanceof Identifier) {
                this.theFont = this.loadFontFromIdentifier((Identifier)font, size);
            } else if (font instanceof String) {
                Identifier actualFontPath = Identifier.of((String)fontPath.getNamespace(), (String)(fontPath.getPath() + "/" + String.valueOf(font)));
                this.theFont = this.loadFontFromIdentifier(actualFontPath, size);
            } else {
                this.theFont = this.loadFontFromIdentifier(fontPath, size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.theFont = new java.awt.Font("Verdana", 0, Math.round(size));
        }
        this.theGraphics.setFont(this.theFont);
        this.setupFontMetrics(size);
    }

    private java.awt.Font loadFontFromIdentifier(Identifier id, float size) throws Exception {
        String path = "/assets/" + id.getNamespace() + "/" + id.getPath();
        try (InputStream stream = FontRenderer.class.getResourceAsStream(path);){
            if (stream == null) {
                java.awt.Font font = new java.awt.Font("Verdana", 0, Math.round(size));
                return font;
            }
            java.awt.Font font = java.awt.Font.createFont(0, stream).deriveFont(size);
            return font;
        }
    }

    private void setupFontMetrics(float size) {
        this.theGraphics.setColor(new Color(255, 255, 255, 0));
        this.theGraphics.fillRect(0, 0, 256, 256);
        this.theGraphics.setColor(Color.white);
        this.theMetrics = this.theGraphics.getFontMetrics();
        float x = 5.0f;
        float y = 5.0f;
        for (int i = this.startChar; i < this.endChar; ++i) {
            this.theGraphics.drawString(Character.toString((char)i), x, y + (float)this.theMetrics.getAscent());
            this.xPos[i - this.startChar] = x;
            this.yPos[i - this.startChar] = y - (float)this.theMetrics.getMaxDescent();
            x += (float)this.theMetrics.stringWidth(Character.toString((char)i)) + 2.0f;
            if (!(x >= (float)(250 - this.theMetrics.getMaxAdvance()))) continue;
            x = 5.0f;
            y += (float)(this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent()) + size / 2.0f;
        }
        String base64 = this.imageToBase64String(this.bufferedImage, "png");
        this.setResourceLocation(base64, this.theFont, this.fontPath, size);
    }

    private String imageToBase64String(BufferedImage image, String type) {
        String ret = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image, type, bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace(System.lineSeparator(), "");
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    private void setResourceLocation(String base64, Object font, Identifier identifier, float size) {
        NativeImage image = FontRenderer.readTexture(base64);
        if (image == null) {
            return;
        }
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }
        image.close();
        this.resourceLocation = Identifier.of((String)identifier.getNamespace(), (String)(identifier.getPath() + this.getFont().getFontName().toLowerCase().replace(" ", "-") + size));
        this.applyTexture(this.resourceLocation, imgNew);
    }

    private static NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64((String)textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return NativeImage.read((InputStream)bais);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, (AbstractTexture)new NativeImageBackedTexture(nativeImage)));
    }

    public void drawString(MatrixStack matrixStack, String text, double x, double y, int color, boolean b) {
        this.drawString(matrixStack, text, (float)x, (float)y, FontType.NORMAL, color);
    }

    public void drawString(MatrixStack matrixStack, TextRenderer textRenderer, String text, float x, float y, int color) {
        this.drawString(matrixStack, text, x, y - 10.0f, FontType.NORMAL, color);
    }

    public void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, int color, int color2) {
        text = this.stripUnsupported(text);
        Renderer.setup2DRender(false);
        String text2 = this.stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1: {
                this.drawer(matrixStack, text2, x + 0.5f, y, color2);
                this.drawer(matrixStack, text2, x - 0.5f, y, color2);
                this.drawer(matrixStack, text2, x, y + 0.5f, color2);
                this.drawer(matrixStack, text2, x, y - 0.5f, color2);
                break;
            }
            case 2: {
                this.drawer(matrixStack, text2, x + 0.5f, y + 0.5f, color2);
                break;
            }
            case 3: {
                this.drawer(matrixStack, text2, x + 0.5f, y + 1.0f, color2);
                break;
            }
            case 4: {
                this.drawer(matrixStack, text2, x, y + 0.5f, color2);
                break;
            }
            case 5: {
                this.drawer(matrixStack, text2, x, y - 0.5f, color2);
                break;
            }
        }
        this.drawer(matrixStack, text, x, y, color);
        Renderer.end2DRender();
    }

    public void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, int color) {
        matrixStack.scale(0.5f, 0.5f, 1.0f);
        this.drawString(matrixStack, text, x, y, fontType, color, -1157627904);
        matrixStack.scale(2.0f, 2.0f, 1.0f);
    }

    private void drawer(MatrixStack matrices, String text, float x, float y, int color) {
        x *= 2.0f;
        y *= 2.0f;
        Renderer.setup2DRender(false);
        Renderer.bindTexture(this.resourceLocation);
        if ((color & 0xFF000000) == 0) {
            color |= 0xFF000000;
        }
        float startX = x;
        boolean scramble = false;
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        int vertexCount = 0;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\u00a7' && i + 1 < text.length()) {
                char formatChar = Character.toLowerCase(text.charAt(i + 1));
                switch (formatChar) {
                    case 'n': {
                        y += (float)(this.theMetrics.getAscent() + 2);
                        x = startX;
                        break;
                    }
                    case 'k': {
                        scramble = true;
                        break;
                    }
                    case 'r': {
                        color = color & 0xFF000000 | 0xFFFFFF;
                        break;
                    }
                    default: {
                        int newColor = this.getColorFromCode(formatChar);
                        if (newColor == -1) break;
                        color = newColor;
                    }
                }
                ++i;
                continue;
            }
            if (scramble) {
                String obfChars = "\\:><&%$@!?/";
                c = obfChars.charAt((int)(Math.random() * (double)obfChars.length()));
            }
            Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(c), this.theGraphics);
            float charWidth = (float)bounds.getWidth();
            float charHeight = (float)bounds.getHeight() + (float)this.theMetrics.getMaxDescent() + 1.0f;
            if (c < this.startChar || c >= this.startChar + this.xPos.length) continue;
            float u = this.xPos[c - this.startChar];
            float v = this.yPos[c - this.startChar];
            float scale = 0.0039063f;
            float r = (float)(color >> 16 & 0xFF) / 255.0f;
            float g = (float)(color >> 8 & 0xFF) / 255.0f;
            float b = (float)(color & 0xFF) / 255.0f;
            float a = (float)(color >> 24 & 0xFF) / 255.0f;
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            bufferBuilder.vertex(matrix, x, y + charHeight, 0.0f).texture(u * scale, (v + charHeight) * scale).color(r, g, b, a);
            bufferBuilder.vertex(matrix, x + charWidth, y + charHeight, 0.0f).texture((u + charWidth) * scale, (v + charHeight) * scale).color(r, g, b, a);
            bufferBuilder.vertex(matrix, x + charWidth, y, 0.0f).texture((u + charWidth) * scale, v * scale).color(r, g, b, a);
            bufferBuilder.vertex(matrix, x, y, 0.0f).texture(u * scale, v * scale).color(r, g, b, a);
            vertexCount += 4;
            x += this.getStringWidth(Character.toString(c), false) * 2.0f;
        }
        if (vertexCount > 0) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
        Renderer.shaderColor(-1);
    }

    private void drawChar(MatrixStack matrixStack, char character, float x, float y, int color) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
        this.drawTexturedModalRect(matrixStack, x, y, this.xPos[character - this.startChar], this.yPos[character - this.startChar], (float)bounds.getWidth(), (float)bounds.getHeight() + (float)this.theMetrics.getMaxDescent() + 1.0f, color);
    }

    private void drawTexturedModalRect(MatrixStack matrixStack, float x, float y, float u, float v, float width, float height, int color) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float scale = 0.0039063f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        bufferBuilder.vertex(matrix4f, x + 0.0f, y + height, 0.0f).texture((u + 0.0f) * scale, (v + height) * scale).color(f1, f2, f3, f);
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0.0f).texture((u + width) * scale, (v + height) * scale).color(f1, f2, f3, f);
        bufferBuilder.vertex(matrix4f, x + width, y + 0.0f, 0.0f).texture((u + width) * scale, (v + 0.0f) * scale).color(f1, f2, f3, f);
        bufferBuilder.vertex(matrix4f, x + 0.0f, y + 0.0f, 0.0f).texture((u + 0.0f) * scale, (v + 0.0f) * scale).color(f1, f2, f3, f);
    }

    private String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = this.sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        String split = s.substring(0, wrapWidth);
        String split2 = this.getFormatFromString(split) + s.substring(wrapWidth + (s.charAt(wrapWidth) == ' ' || s.charAt(wrapWidth) == '\n' ? 1 : 0));
        try {
            return split + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
        catch (Exception e) {
            System.err.println("Cannot wrap string to width.");
            return "";
        }
    }

    private int sizeStringToWidth(String par1Str, float par2) {
        int var5;
        int var3 = par1Str.length();
        float var4 = 0.0f;
        int var6 = -1;
        boolean var7 = false;
        for (var5 = 0; var5 < var3; ++var5) {
            char var8 = par1Str.charAt(var5);
            switch (var8) {
                case '\n': {
                    --var5;
                    break;
                }
                case '\u00a7': {
                    char var9;
                    if (var5 >= var3 - 1) break;
                    if ((var9 = par1Str.charAt(++var5)) != 'l' && var9 != 'L') {
                        if (var9 != 'r' && var9 != 'R' && !this.isFormatColor(var9)) break;
                        var7 = false;
                        break;
                    }
                    var7 = true;
                    break;
                }
                case ' ': {
                    var6 = var5;
                }
                case '-': {
                    var6 = var5;
                }
                case '_': {
                    var6 = var5;
                }
                case ':': {
                    var6 = var5;
                }
                default: {
                    String text = String.valueOf(var8);
                    var4 += this.getStringWidth(text, false);
                    if (!var7) break;
                    var4 += 1.0f;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                continue;
            }
            if (var4 > par2) break;
        }
        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    private String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 >= var3 - 1) continue;
            char var4 = par0Str.charAt(var2 + 1);
            if (this.isFormatColor(var4)) {
                var1 = "\u00a7" + var4;
                continue;
            }
            if (!this.isFormatSpecial(var4)) continue;
            var1 = var1 + "\u00a7" + var4;
        }
        return var1;
    }

    private boolean isFormatColor(char par0) {
        return par0 >= '0' && par0 <= '9' || par0 >= 'a' && par0 <= 'f' || par0 >= 'A' && par0 <= 'F';
    }

    private boolean isFormatSpecial(char par0) {
        return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
    }

    public void drawGradient(DrawContext drawContext, String text, int x, int y, int spaceBetweenLetters, Color firstColor, Color secondColor, Font fonttype, boolean vertical) {
        float charX = x;
        FontRenderer font = fonts.get((Object)fonttype);
        if (font == null) {
            return;
        }
        for (char c : text.toCharArray()) {
            String string = String.valueOf(c);
            Vector2f position = vertical ? new Vector2f(0.0f, charX * 80.0f) : new Vector2f(charX * 80.0f, 0.0f);
            Color color = ColorUtil.getAccentColor(position, firstColor, secondColor);
            font.drawString(drawContext.getMatrices(), string, (double)((int)charX), (double)y, color.getRGB(), true);
            charX += font.getWidth(string) + (float)spaceBetweenLetters;
        }
    }

    public final String stripControlCodes(String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }

    public final String stripUnsupported(String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }

    public final java.awt.Font getFont() {
        return this.theFont;
    }

    private int getColorFromCode(char code) {
        return switch (code) {
            case '0' -> Color.BLACK.getRGB();
            case '1' -> -16777046;
            case '2' -> -16733696;
            case '3' -> -16733526;
            case '4' -> -5636096;
            case '5' -> -5635926;
            case '6' -> -22016;
            case '7' -> -5592406;
            case '8' -> -11184811;
            case '9' -> -11184641;
            case 'a' -> -11141291;
            case 'b' -> -11141121;
            case 'c' -> -43691;
            case 'd' -> -43521;
            case 'e' -> -171;
            case 'f' -> -1;
            case 'g' -> -2238971;
            default -> -1;
        };
    }

    public float getStringWidth(String string, boolean mcFont) {
        if (!mcFont) {
            return (float)this.getBounds(this.stripControlCodes(string)).getWidth() / 2.0f;
        }
        return FontRenderer.mc.textRenderer.getWidth(string);
    }

    public FontRenderer getCurrentFont(Font font) {
        return FontRenderer.getFontRenderer(font);
    }

    public float getWidth(String s) {
        return this.getStringWidth(s, false);
    }

    private Rectangle2D getBounds(String text) {
        return this.theMetrics.getStringBounds(text, this.theGraphics);
    }

    public float getStringHeight(String string, boolean mcFont) {
        if (!mcFont) {
            return (float)(this.getBounds(this.stripControlCodes(string)).getHeight() / 2.0);
        }
        Objects.requireNonNull(FontRenderer.mc.textRenderer);
        return 9.0f;
    }

    public float getStringWidth(Text string) {
        return FontRenderer.mc.textRenderer.getWidth((StringVisitable)string);
    }

    public void draw(MatrixStack matrixStack, String text, float x, float y, int color) {
        this.drawString(matrixStack, text, x, y + 2.0f, FontType.NORMAL, color);
    }

    public void draw(MatrixStack matrixStack, String text1, String text2, float x, float y, Color color1, Color color2) {
        this.drawString(matrixStack, text1, x, y + 2.0f, FontType.NORMAL, color1.getRGB());
        float text1Width = this.getWidth(text1);
        this.drawString(matrixStack, text2, x + text1Width, y + 2.0f, FontType.NORMAL, color2.getRGB());
    }

    public void drawCenteredString(MatrixStack matrixStack, String string, float x, float y, int color, boolean shadow, boolean mcFont) {
        float newX = x - this.getStringWidth(string, mcFont) / 2.0f;
        this.drawString(matrixStack, string, newX, y, shadow ? FontType.SHADOW_THIN : FontType.NORMAL, color);
    }

    public void draw(MatrixStack matrices, String text, float x, float y, Color color, Font fontName) {
        FontRenderer font = fonts.get((Object)fontName);
        if (font == null) {
            throw new IllegalArgumentException("Font " + String.valueOf((Object)fontName) + " is not registered!");
        }
        font.drawString(matrices, text, (double)x, (double)y, color.getRGB(), false);
    }

    public void drawWithShadow(MatrixStack matrixStack, Text text, float x, float y, int color, boolean mcFont) {
        String s = text.getString();
        this.draw(matrixStack, s, x + 0.5f, y + 0.5f + 4.0f, -16777216);
        this.draw(matrixStack, s, x, y, color);
    }

    public void drawCenteredString(MatrixStack matrixStack, Text string, float x, float y, int color, boolean mcFont) {
        float newX = x - this.getStringWidth(string) / 2.0f;
        this.drawWithShadow(matrixStack, string, newX, y, color, mcFont);
    }

    public String fix(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        for (int i = 0; i < 9; ++i) {
            if (!s.contains("\u00a7" + i)) continue;
            s = s.replace("\u00a7" + i, "");
        }
        return s.replace("\u00a7a", "").replace("\u00a7b", "").replace("\u00a7c", "").replace("\u00a7d", "").replace("\u00a7e", "").replace("\u00a7f", "").replace("\u00a7g", "");
    }

    public String trimToWidth(String string, int width) {
        try {
            return string.substring(0, width);
        }
        catch (Exception e) {
            return string;
        }
    }

    public String trimToWidth(String string, int width, boolean backwards) {
        try {
            return backwards ? string.substring(width) : string.substring(0, width);
        }
        catch (Exception e) {
            return string;
        }
    }

    public static Font getFont(String name) {
        if (name == null) {
            return null;
        }
        switch (name.toLowerCase()) {
            case "verdana": {
                return Font.VERDANA;
            }
            case "verdanabold": {
                return Font.VERDANABOLD;
            }
            case "robotobold": {
                return Font.ROBOTOBOLD;
            }
        }
        return null;
    }

    public static enum FontType {
        NORMAL,
        SHADOW_THICK,
        SHADOW_THIN,
        OUTLINE_THIN,
        EMBOSS_TOP,
        EMBOSS_BOTTOM;

    }

    private static class Renderer {
        private Renderer() {
        }

        public static void setup2DRender(boolean disableDepth) {
            RenderSystem.enableBlend();
            RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
            RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
            RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ONE, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            if (disableDepth) {
                RenderSystem.disableDepthTest();
            }
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }

        public static void end2DRender() {
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }

        public static void bindTexture(Identifier identifier) {
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
        }

        public static void shaderColor(int rgb) {
            float alpha = (float)(rgb >> 24 & 0xFF) / 255.0f;
            float red = (float)(rgb >> 16 & 0xFF) / 255.0f;
            float green = (float)(rgb >> 8 & 0xFF) / 255.0f;
            float blue = (float)(rgb & 0xFF) / 255.0f;
            RenderSystem.setShaderColor((float)red, (float)green, (float)blue, (float)alpha);
        }
    }
}
