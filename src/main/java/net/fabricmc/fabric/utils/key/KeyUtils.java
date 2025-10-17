package net.fabricmc.fabric.utils.key;

import net.fabricmc.fabric.ClientMain;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class KeyUtils {
    @Contract(pure=true)
    @NotNull
    public static String getKey(int i) {
        switch (i) {
            case 0: {
                return "LMB";
            }
            case 1: {
                return "RMB";
            }
            case 2: {
                return "MMB";
            }
            case 3: {
                return "MOUSE 4";
            }
            case 4: {
                return "MOUSE 5";
            }
            case 5: {
                return "MOUSE 6";
            }
            case 6: {
                return "MOUSE 7";
            }
            case 7: {
                return "MOUSE 8";
            }
            case 48: {
                return "intermediary";
            }
            case 49: {
                return "1";
            }
            case 50: {
                return "2";
            }
            case 51: {
                return "3";
            }
            case 52: {
                return "4";
            }
            case 53: {
                return "5";
            }
            case 54: {
                return "6";
            }
            case 55: {
                return "7";
            }
            case 56: {
                return "8";
            }
            case 57: {
                return "9";
            }
            case 65: {
                return "A";
            }
            case 66: {
                return "B";
            }
            case 67: {
                return "C";
            }
            case 68: {
                return "D";
            }
            case 69: {
                return "E";
            }
            case 70: {
                return "Fly";
            }
            case 71: {
                return "G";
            }
            case 72: {
                return "H";
            }
            case 73: {
                return "I";
            }
            case 74: {
                return "J";
            }
            case 75: {
                return "K";
            }
            case 76: {
                return "L";
            }
            case 77: {
                return "M";
            }
            case 78: {
                return "N";
            }
            case 79: {
                return "O";
            }
            case 80: {
                return "P";
            }
            case 81: {
                return "Q";
            }
            case 82: {
                return "R";
            }
            case 83: {
                return "S";
            }
            case 84: {
                return "T";
            }
            case 85: {
                return "U";
            }
            case 86: {
                return "V";
            }
            case 87: {
                return "W";
            }
            case 88: {
                return "X";
            }
            case 89: {
                return "Y";
            }
            case 90: {
                return "Z";
            }
            case 290: {
                return "F1";
            }
            case 291: {
                return "F2";
            }
            case 292: {
                return "F3";
            }
            case 293: {
                return "F4";
            }
            case 294: {
                return "F5";
            }
            case 295: {
                return "F6";
            }
            case 296: {
                return "F7";
            }
            case 297: {
                return "F8";
            }
            case 298: {
                return "F9";
            }
            case 299: {
                return "F10";
            }
            case 300: {
                return "F11";
            }
            case 301: {
                return "F12";
            }
            case 302: {
                return "F13";
            }
            case 303: {
                return "F14";
            }
            case 304: {
                return "F15";
            }
            case 305: {
                return "F16";
            }
            case 306: {
                return "F17";
            }
            case 307: {
                return "F18";
            }
            case 308: {
                return "F19";
            }
            case 309: {
                return "F20";
            }
            case 310: {
                return "F21";
            }
            case 311: {
                return "F22";
            }
            case 312: {
                return "F23";
            }
            case 313: {
                return "F24";
            }
            case 314: {
                return "F25";
            }
            case 320: {
                return "N 0";
            }
            case 321: {
                return "N 1";
            }
            case 322: {
                return "N 2";
            }
            case 323: {
                return "N 3";
            }
            case 324: {
                return "N 4";
            }
            case 325: {
                return "N 5";
            }
            case 326: {
                return "N 6";
            }
            case 327: {
                return "N 7";
            }
            case 328: {
                return "N 8";
            }
            case 329: {
                return "N 9";
            }
            case 330: {
                return "N .";
            }
            case 331: {
                return "N /";
            }
            case 332: {
                return "N *";
            }
            case 333: {
                return "N -";
            }
            case 334: {
                return "N +";
            }
            case 335: {
                return "N ENTR";
            }
            case 336: {
                return "N =";
            }
            case 340: {
                return "L SHIFT";
            }
            case 341: {
                return "L CTRL";
            }
            case 342: {
                return "L ALT";
            }
            case 343: {
                return "WIN L";
            }
            case 344: {
                return "R SHIFT";
            }
            case 345: {
                return "R CTRL";
            }
            case 346: {
                return "R ALT";
            }
            case 347: {
                return "WIN R";
            }
            case 348: {
                return "MENU";
            }
            case 32: {
                return "SPACE";
            }
            case 39: {
                return " '";
            }
            case 44: {
                return " ,";
            }
            case 45: {
                return " -";
            }
            case 46: {
                return " .";
            }
            case 47: {
                return " /";
            }
            case 59: {
                return " ;";
            }
            case 61: {
                return " =";
            }
            case 91: {
                return " {";
            }
            case 92: {
                return " \\";
            }
            case 93: {
                return " }";
            }
            case 96: {
                return " `";
            }
            case 161: {
                return "WORLD 1";
            }
            case 162: {
                return "WORLD 2";
            }
            case 256: {
                return "ESCAPE";
            }
            case 257: {
                return "ENTER";
            }
            case 258: {
                return "TAB";
            }
            case 260: {
                return "INS";
            }
            case 261: {
                return "DEL";
            }
            case 262: {
                return "RIGHT";
            }
            case 263: {
                return "LEFT";
            }
            case 264: {
                return "DOWN";
            }
            case 265: {
                return "UP";
            }
            case 266: {
                return "P UP";
            }
            case 267: {
                return "P DOWN";
            }
            case 268: {
                return "HOME";
            }
            case 269: {
                return "END";
            }
            case 280: {
                return "C LOCK";
            }
            case 281: {
                return "S LOCK";
            }
            case 282: {
                return "N LOCK";
            }
            case 283: {
                return "PRT SCN";
            }
            case 284: {
                return "PAUSE";
            }
            case -1: {
                return "None";
            }
        }
        return "NaN";
    }

    public static String getKeyName(int keyCode) {
        switch (keyCode) {
            case 1: {
                return "RMB";
            }
            case 2: {
                return "MMB";
            }
            case -1: {
                return "None";
            }
            case 256: {
                return "Esc";
            }
            case 96: {
                return "Grave Accent";
            }
            case 161: {
                return "World 1";
            }
            case 162: {
                return "World 2";
            }
            case 283: {
                return "Print Screen";
            }
            case 284: {
                return "Pause";
            }
            case 260: {
                return "Insert";
            }
            case 261: {
                return "Delete";
            }
            case 268: {
                return "Home";
            }
            case 266: {
                return "Page Up";
            }
            case 267: {
                return "Page Down";
            }
            case 269: {
                return "End";
            }
            case 258: {
                return "Tab";
            }
            case 341: {
                return "Left Control";
            }
            case 345: {
                return "Right Control";
            }
            case 342: {
                return "Left Alt";
            }
            case 346: {
                return "Right Alt";
            }
            case 340: {
                return "Left Shift";
            }
            case 344: {
                return "Right Shift";
            }
            case 265: {
                return "Arrow Up";
            }
            case 264: {
                return "Arrow Down";
            }
            case 263: {
                return "Arrow Left";
            }
            case 262: {
                return "Arrow Right";
            }
            case 39: {
                return "Apostrophe";
            }
            case 259: {
                return "Backspace";
            }
            case 280: {
                return "Caps Lock";
            }
            case 348: {
                return "Menu";
            }
            case 343: {
                return "Left Super";
            }
            case 347: {
                return "Right Super";
            }
            case 257: {
                return "Enter";
            }
            case 335: {
                return "Numpad Enter";
            }
            case 282: {
                return "Num Lock";
            }
            case 32: {
                return "Space";
            }
            case 290: {
                return "F1";
            }
            case 291: {
                return "F2";
            }
            case 292: {
                return "F3";
            }
            case 293: {
                return "F4";
            }
            case 294: {
                return "F5";
            }
            case 295: {
                return "F6";
            }
            case 296: {
                return "F7";
            }
            case 297: {
                return "F8";
            }
            case 298: {
                return "F9";
            }
            case 299: {
                return "F10";
            }
            case 300: {
                return "F11";
            }
            case 301: {
                return "F12";
            }
            case 302: {
                return "F13";
            }
            case 303: {
                return "F14";
            }
            case 304: {
                return "F15";
            }
            case 305: {
                return "F16";
            }
            case 306: {
                return "F17";
            }
            case 307: {
                return "F18";
            }
            case 308: {
                return "F19";
            }
            case 309: {
                return "F20";
            }
            case 310: {
                return "F21";
            }
            case 311: {
                return "F22";
            }
            case 312: {
                return "F23";
            }
            case 313: {
                return "F24";
            }
            case 314: {
                return "F25";
            }
        }
        String keyName = GLFW.glfwGetKeyName((int)keyCode, (int)0);
        if (keyName == null) {
            return "None";
        }
        return StringUtils.capitalize((String)keyName);
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < 0) {
            return false;
        }
        if (keyCode <= 8) {
            return GLFW.glfwGetMouseButton((long)ClientMain.mc.getWindow().getHandle(), (int)keyCode) == 1;
        }
        return GLFW.glfwGetKey((long)ClientMain.mc.getWindow().getHandle(), (int)keyCode) == 1;
    }
}
