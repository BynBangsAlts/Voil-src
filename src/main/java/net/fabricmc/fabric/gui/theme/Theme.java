package net.fabricmc.fabric.gui.theme;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.awt.Color;
import net.fabricmc.fabric.systems.config.JsonUtils;
import net.fabricmc.fabric.utils.render.ColorUtil;

public class Theme {
    public static Color WINDOW_COLOR;
    public static Color LEFT_PANEL;
    public static Color ENABLED;
    public static Color HIGHLIGHT_COLOR;
    public static Color SETTINGS_BG;
    public static Color SETTINGS_HEADER;
    public static Color MODULE_ENABLED_BG;
    public static Color MODULE_DISABLED_BG;
    public static Color MODULE_COLOR;
    public static Color MODULE_TEXT;
    public static Color TOGGLE_BUTTON_BG;
    public static Color TOGGLE_BUTTON_FILL;
    public static Color UNFOCUSED_TEXT_COLOR;
    public static Color MODULE_ENABLED_BG_HOVER;
    public static Color MODULE_DISABLED_BG_HOVER;
    public static Color NORMAL_TEXT_COLOR;
    public static Color MODE_SETTING_BG;
    public static Color MODE_SETTING_FILL;
    public static Color SLIDER_SETTING_BG;
    public static Color CONFIG_EDIT_BG;
    public static Color ACCENT_COLOR1;
    public static Color ACCENT_COLOR2;
    public static boolean GLOW_ENABLED;
    public static int redEnable;
    public static int greenEnable;
    public static int blueEnable;
    public static int red1Enable;
    public static int green1Enable;
    public static int blue1Enable;
    public static int checkboxR;
    public static int checkboxG;
    public static int checkboxB;

    static {
        initializeDefaultTheme();
    }

    public static void initializeDefaultTheme() {
        WINDOW_COLOR = new Color(18, 18, 18, 255);
        LEFT_PANEL = new Color(25, 25, 25, 255);
        SETTINGS_BG = new Color(15, 15, 15, 230);
        SETTINGS_HEADER = new Color(35, 35, 35, 255);
        MODULE_DISABLED_BG = new Color(28, 28, 28, 255);
        MODULE_ENABLED_BG = new Color(40, 40, 40, 255);
        MODULE_ENABLED_BG_HOVER = new Color(50, 50, 50, 255);
        MODULE_DISABLED_BG_HOVER = new Color(38, 38, 38, 255);
        TOGGLE_BUTTON_BG = new Color(35, 35, 35, 255);
        MODE_SETTING_BG = new Color(30, 30, 30, 255);
        SLIDER_SETTING_BG = new Color(32, 32, 32, 255);
        CONFIG_EDIT_BG = new Color(20, 20, 20, 255);
        HIGHLIGHT_COLOR = new Color(50, 50, 50, 255);
        NORMAL_TEXT_COLOR = new Color(0, 0, 0, 255);
        MODULE_TEXT = new Color(0, 0, 0, 255);
        MODULE_COLOR = new Color(87, 87, 87, 255);
        UNFOCUSED_TEXT_COLOR = new Color(0, 0, 0, 255);
        ENABLED = new Color(65, 105, 225, 255);
        TOGGLE_BUTTON_FILL = new Color(65, 105, 225, 255);
        MODE_SETTING_FILL = new Color(65, 105, 225, 255);
        ACCENT_COLOR1 = new Color(138, 43, 226, 255);
        ACCENT_COLOR2 = new Color(30, 144, 255, 255);
        redEnable = 65;
        greenEnable = 105;
        blueEnable = 225;
        red1Enable = 30;
        green1Enable = 144;
        blue1Enable = 255;
        checkboxR = 65;
        checkboxG = 105;
        checkboxB = 225;

        GLOW_ENABLED = true;
    }

    public static void loadTheme(String jsonString) {
        try {
            JsonObject json = (JsonObject)JsonUtils.gson.fromJson(jsonString, JsonObject.class);
            if (json == null) {
                System.out.println("Theme: JSON is null, using default theme");
                initializeDefaultTheme();
                return;
            }

            System.out.println("Theme: Loading theme from JSON");
            WINDOW_COLOR = getColorSafe(json, "windowColor", WINDOW_COLOR);
            LEFT_PANEL = getColorSafe(json, "leftPanel", LEFT_PANEL);
            ENABLED = getColorSafe(json, "enabled", ENABLED);
            HIGHLIGHT_COLOR = getColorSafe(json, "highlightColor", HIGHLIGHT_COLOR);
            SETTINGS_BG = getColorSafe(json, "settingsBg", SETTINGS_BG);
            SETTINGS_HEADER = getColorSafe(json, "settingsHeader", SETTINGS_HEADER);
            MODULE_ENABLED_BG = getColorSafe(json, "moduleEnabledBg", MODULE_ENABLED_BG);
            MODULE_DISABLED_BG = getColorSafe(json, "moduleDisabledBg", MODULE_DISABLED_BG);
            MODULE_COLOR = getColorSafe(json, "moduleColor", MODULE_COLOR);
            MODULE_TEXT = getColorSafe(json, "moduleText", MODULE_TEXT);
            TOGGLE_BUTTON_BG = getColorSafe(json, "toggleButtonBg", TOGGLE_BUTTON_BG);
            TOGGLE_BUTTON_FILL = getColorSafe(json, "toggleButtonFill", TOGGLE_BUTTON_FILL);
            UNFOCUSED_TEXT_COLOR = getColorSafe(json, "unfocusedTextColor", UNFOCUSED_TEXT_COLOR);
            MODULE_ENABLED_BG_HOVER = getColorSafe(json, "moduleEnabledBgHover", MODULE_ENABLED_BG_HOVER);
            MODULE_DISABLED_BG_HOVER = getColorSafe(json, "moduleDisabledBgHover", MODULE_DISABLED_BG_HOVER);
            NORMAL_TEXT_COLOR = getColorSafe(json, "normalTextColor", NORMAL_TEXT_COLOR);
            MODE_SETTING_BG = getColorSafe(json, "modeSettingBg", MODE_SETTING_BG);
            MODE_SETTING_FILL = getColorSafe(json, "modeSettingFill", MODE_SETTING_FILL);
            SLIDER_SETTING_BG = getColorSafe(json, "sliderSettingBg", SLIDER_SETTING_BG);
            CONFIG_EDIT_BG = getColorSafe(json, "configEditBg", CONFIG_EDIT_BG);
            ACCENT_COLOR1 = getColorSafe(json, "accentColor1", ACCENT_COLOR1);
            ACCENT_COLOR2 = getColorSafe(json, "accentColor2", ACCENT_COLOR2);

            GLOW_ENABLED = getBooleanSafe(json, "glowEnabled", GLOW_ENABLED);
            redEnable = getIntSafe(json, "redEnable", redEnable);
            greenEnable = getIntSafe(json, "greenEnable", greenEnable);
            blueEnable = getIntSafe(json, "blueEnable", blueEnable);
            red1Enable = getIntSafe(json, "red1Enable", red1Enable);
            green1Enable = getIntSafe(json, "green1Enable", green1Enable);
            blue1Enable = getIntSafe(json, "blue1Enable", blue1Enable);
            checkboxR = getIntSafe(json, "checkboxR", checkboxR);
            checkboxG = getIntSafe(json, "checkboxG", checkboxG);
            checkboxB = getIntSafe(json, "checkboxB", checkboxB);

            System.out.println("Theme: Successfully loaded theme");
            logCurrentTheme();

        } catch (Exception e) {
            System.err.println("Theme: Error loading theme, using defaults");
            e.printStackTrace();
            initializeDefaultTheme();
        }
    }

    public static void logCurrentTheme() {
    }

    private static Color getColorSafe(JsonObject json, String key, Color defaultColor) {
        try {
            Color color = getColor(json, key);
            if (color.equals(Color.BLACK) && !Color.BLACK.equals(defaultColor)) {
                return defaultColor;
            }
            return color.equals(Color.BLACK) ? defaultColor : color;
        } catch (Exception e) {
            System.err.println("Theme: Error getting color for key: " + key);
            e.printStackTrace();
            return defaultColor;
        }
    }

    private static Color getColor(JsonObject json, String key) {
        if (json.has(key)) {
            try {
                if (json.get(key).isJsonPrimitive() && json.get(key).getAsJsonPrimitive().isString()) {
                    String value = json.get(key).getAsString();
                    if ("rainbow".equalsIgnoreCase(value)) {
                        return ColorUtil.getRainbow(1);
                    }
                } else if (json.get(key).isJsonArray()) {
                    int[] rgb = JsonUtils.gson.fromJson(json.get(key), int[].class);
                    if (rgb != null && rgb.length >= 3) {
                        return new Color(
                                Math.max(0, Math.min(255, rgb[0])),
                                Math.max(0, Math.min(255, rgb[1])),
                                Math.max(0, Math.min(255, rgb[2]))
                        );
                    }
                }
            } catch (JsonSyntaxException | NumberFormatException e) {
                System.err.println("Theme: Error parsing color for key: " + key);
                e.printStackTrace();
            }
        }
        return Color.BLACK;
    }

    private static int getIntSafe(JsonObject json, String key, int defaultValue) {
        try {
            return json.has(key) ? json.get(key).getAsInt() : defaultValue;
        } catch (Exception e) {
            System.err.println("Theme: Error getting int for key: " + key);
            e.printStackTrace();
            return defaultValue;
        }
    }

    private static boolean getBooleanSafe(JsonObject json, String key, boolean defaultValue) {
        try {
            return json.has(key) ? json.get(key).getAsBoolean() : defaultValue;
        } catch (Exception e) {
            System.err.println("Theme: Error getting boolean for key: " + key);
            e.printStackTrace();
            return defaultValue;
        }
    }
}