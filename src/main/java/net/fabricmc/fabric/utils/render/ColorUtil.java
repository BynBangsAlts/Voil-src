package net.fabricmc.fabric.utils.render;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.util.math.Vector2f;

public class ColorUtil {
    private static float hue = 0.0f;

    public static Color addAlpha(Color color, int alpha) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        alpha = Math.min(Math.max(alpha, 0), 255);
        return new Color(red, green, blue, alpha);
    }

    public static Color getBreathingRGBColor(int increment, int alpha) {
        Color color = Color.getHSBColor((float)((System.currentTimeMillis() * 3L + (long)(increment * 175)) % 7200L) / 7200.0f, 0.6f, 1.0f);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color generateGradientColor(int charIndex, Color firstColor, Color secondColor) {
        float charPosition = (float)charIndex * ClientMain.fontRenderer.getWidth("A");
        return ColorUtil.getAccentColor(new Vector2f(charPosition * 80.0f, 12.0f), firstColor, secondColor);
    }

    public static Color getThemeColor(Color baseColor, int offset, int divisor) {
        long currentTime = System.currentTimeMillis();
        float[] hsbValues = new float[3];
        Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsbValues);
        float modulation = Math.abs(((float)(currentTime % 2000L) / 1000.0f + (float)offset / (float)divisor * 2.0f) % 2.0f - 1.0f);
        hsbValues[2] = 0.25f + 0.75f * modulation;
        return new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues[2]));
    }

    private static int lerp(float t, int a, int b) {
        return (int)((float)a + (float)(b - a) * t);
    }

    public static Color getAccentColor(Vector2f screenCoordinates, Color firstColor, Color secondColor) {
        return ColorUtil.mixColors(firstColor, secondColor, ColorUtil.getBlendFactor(screenCoordinates));
    }

    static Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int)((double)color1.getRed() * percent + (double)color2.getRed() * inverse_percent);
        int greenPart = (int)((double)color1.getGreen() * percent + (double)color2.getGreen() * inverse_percent);
        int bluePart = (int)((double)color1.getBlue() * percent + (double)color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    public static double getBlendFactor(Vector2f screenCoordinates) {
        return Math.sin((double)System.currentTimeMillis() / 175.0 + (double)screenCoordinates.getX() * 7.0E-4 + (double)screenCoordinates.getY() * 7.0E-4) * 0.5 + 0.5;
    }

    public static Color TwoColor(Color cl1, Color cl2, double speed, double count) {
        int angle = (int)(((double)System.currentTimeMillis() / speed + count) % 360.0);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return ColorUtil.interpolateColorC(cl1, cl2, (float)angle / 360.0f);
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        return new Color(ColorUtil.interpolateInt(color1.getRed(), color2.getRed(), amount), ColorUtil.interpolateInt(color1.getGreen(), color2.getGreen(), amount), ColorUtil.interpolateInt(color1.getBlue(), color2.getBlue(), amount), ColorUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return (int)ColorUtil.interpolate(oldValue, newValue, (float)interpolationValue);
    }

    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    public static Color getRainbow(int speed) {
        if ((hue += (float)speed * 1000.0f) > 1.0f) {
            hue -= 1.0f;
        }
        return Color.getHSBColor(hue, 1.0f, 1.0f);
    }
}
