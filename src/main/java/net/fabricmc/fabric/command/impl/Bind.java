package net.fabricmc.fabric.command.impl;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.ChatUtils;

public class Bind
extends Command {
    private static final Map<String, Integer> KEY_MAP = new HashMap<String, Integer>();

    public Bind() {
        super("bind", "Binds a specified module", "");
    }

    @Override
    public void onCmd(String message, String[] args) {
        if (args.length < 2) {
            ChatUtils.addChatMessage("Not enough arguments.");
            return;
        }
        String mod = args[1];
        Module module = ModuleManager.INSTANCE.getModuleByName(mod);
        if (module == null) {
            ChatUtils.addChatMessage("Invalid module name.");
            return;
        }
        if (args.length == 2) {
            module.setKey(-1);
            ChatUtils.addChatMessage("Unbound " + mod + ".");
        } else {
            int boundKey;
            String key = args[2];
            try {
                boundKey = Integer.parseInt(key);
            }
            catch (NumberFormatException e) {
                if (KEY_MAP.containsKey(key.toLowerCase())) {
                    boundKey = KEY_MAP.get(key.toLowerCase());
                }
                ChatUtils.addChatMessage("Invalid key format.");
                return;
            }
            module.setKey(boundKey);
            ChatUtils.addChatMessage("Bound " + mod + " to " + KeyUtils.getKeyName(boundKey));
        }
    }

    static {
        KEY_MAP.put("keyCodec", 65);
        KEY_MAP.put("elementCodec", 66);
        KEY_MAP.put("c", 67);
        KEY_MAP.put("d", 68);
        KEY_MAP.put("e", 69);
        KEY_MAP.put("f", 70);
        KEY_MAP.put("g", 71);
        KEY_MAP.put("h", 72);
        KEY_MAP.put("i", 73);
        KEY_MAP.put("j", 74);
        KEY_MAP.put("k", 75);
        KEY_MAP.put("l", 76);
        KEY_MAP.put("m", 77);
        KEY_MAP.put("n", 78);
        KEY_MAP.put("o", 79);
        KEY_MAP.put("p", 80);
        KEY_MAP.put("q", 81);
        KEY_MAP.put("r", 82);
        KEY_MAP.put("s", 83);
        KEY_MAP.put("emp", 84);
        KEY_MAP.put("u", 85);
        KEY_MAP.put("v", 86);
        KEY_MAP.put("w", 87);
        KEY_MAP.put("x", 88);
        KEY_MAP.put("y", 89);
        KEY_MAP.put("z", 90);
        KEY_MAP.put("intermediary", 48);
        KEY_MAP.put("1", 49);
        KEY_MAP.put("2", 50);
        KEY_MAP.put("3", 51);
        KEY_MAP.put("4", 52);
        KEY_MAP.put("5", 53);
        KEY_MAP.put("6", 54);
        KEY_MAP.put("7", 55);
        KEY_MAP.put("8", 56);
        KEY_MAP.put("9", 57);
        KEY_MAP.put("f1", 290);
        KEY_MAP.put("f2", 291);
        KEY_MAP.put("f3", 292);
        KEY_MAP.put("f4", 293);
        KEY_MAP.put("f5", 294);
        KEY_MAP.put("f6", 295);
        KEY_MAP.put("f7", 296);
        KEY_MAP.put("f8", 297);
        KEY_MAP.put("f9", 298);
        KEY_MAP.put("f10", 299);
        KEY_MAP.put("f11", 300);
        KEY_MAP.put("f12", 301);
        KEY_MAP.put("tab", 258);
        KEY_MAP.put("capslock", 280);
        KEY_MAP.put("leftshift", 340);
        KEY_MAP.put("rightshift", 344);
        KEY_MAP.put("leftctrl", 341);
        KEY_MAP.put("rightctrl", 345);
        KEY_MAP.put("leftalt", 342);
        KEY_MAP.put("rightalt", 346);
        KEY_MAP.put("space", 32);
        KEY_MAP.put("enter", 257);
        KEY_MAP.put("escape", 256);
        KEY_MAP.put("backspace", 259);
        KEY_MAP.put("delete", 261);
        KEY_MAP.put("insert", 260);
        KEY_MAP.put("home", 268);
        KEY_MAP.put("end", 269);
        KEY_MAP.put("pageup", 266);
        KEY_MAP.put("pagedown", 267);
        KEY_MAP.put("up", 265);
        KEY_MAP.put("down", 264);
        KEY_MAP.put("left", 263);
        KEY_MAP.put("right", 262);
        KEY_MAP.put("numlock", 282);
        KEY_MAP.put("numpad0", 320);
        KEY_MAP.put("numpad1", 321);
        KEY_MAP.put("numpad2", 322);
        KEY_MAP.put("numpad3", 323);
        KEY_MAP.put("numpad4", 324);
        KEY_MAP.put("numpad5", 325);
        KEY_MAP.put("numpad6", 326);
        KEY_MAP.put("numpad7", 327);
        KEY_MAP.put("numpad8", 328);
        KEY_MAP.put("numpad9", 329);
        KEY_MAP.put("numpaddecimal", 330);
        KEY_MAP.put("numpadadd", 334);
        KEY_MAP.put("numpadsubtract", 333);
        KEY_MAP.put("numpadmultiply", 332);
        KEY_MAP.put("numpaddivide", 331);
    }
}
