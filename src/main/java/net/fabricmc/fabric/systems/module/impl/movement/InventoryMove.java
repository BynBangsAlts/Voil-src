package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class InventoryMove
extends Module {
    BooleanSetting inventory = new BooleanSetting("I".concat("!").concat("n").concat("-").concat("v").concat("@").concat("e").concat("$").concat("n").concat("!").concat("t").concat("#").concat("o").concat("^").concat("r").concat("(").concat("y"), true, "O".concat("^").concat("n").concat("_").concat("l").concat("-").concat("y").concat("&").concat(" ").concat("_").concat("m").concat("&").concat("o").concat("$").concat("v").concat("*").concat("e").concat(")").concat(" ").concat("#").concat("w").concat("^").concat("h").concat("*").concat("e").concat("#").concat("n").concat("$").concat(" ").concat("(").concat("i").concat("+").concat("n").concat("^").concat("v").concat("@").concat("e").concat("^").concat("n").concat("*").concat("t").concat("!").concat("o").concat("*").concat("r").concat("_").concat("y").concat("+").concat(" ").concat("&").concat("i").concat("&").concat("s").concat("^").concat(" ").concat("$").concat("o").concat("^").concat("p").concat("+").concat("e").concat("!").concat("n"));
    BooleanSetting shift = new BooleanSetting("S".concat("(").concat("h").concat(")").concat("i").concat("(").concat("f").concat("!").concat("t"), false, "B".concat("(").concat("e").concat("^").concat(" ").concat("#").concat("keyCodec").concat("+").concat("elementCodec").concat("-").concat("l").concat("!").concat("e").concat("^").concat(" ").concat("(").concat("t").concat("@").concat("o").concat("(").concat(" ").concat("^").concat("s").concat("(").concat("h").concat("!").concat("i").concat("(").concat("f").concat("^").concat("t").concat(")").concat(" ").concat("+").concat("i").concat("$").concat("n").concat("!").concat(" ").concat("$").concat("G").concat("#").concat("U").concat("!").concat("I").concat("-").concat("s"));

    public InventoryMove() {
        super("I".concat("$").concat("n").concat("*").concat("v").concat("&").concat("e").concat("_").concat("n").concat("#").concat("t").concat("!").concat("o").concat("#").concat("r").concat("*").concat("y").concat("!").concat("M").concat("+").concat("o").concat("#").concat("v").concat("-").concat("e"), "L".concat("@").concat("e").concat("^").concat("t").concat("(").concat("s").concat("!").concat(" ").concat("^").concat("y").concat("_").concat("o").concat("+").concat("u").concat("$").concat(" ").concat("-").concat("m").concat("&").concat("o").concat("(").concat("v").concat("+").concat("e").concat("_").concat(" ").concat("+").concat("w").concat("+").concat("h").concat(")").concat("e").concat("*").concat("n").concat("$").concat(" ").concat("$").concat("i").concat("+").concat("n").concat("-").concat("v").concat("^").concat("e").concat("_").concat("n").concat("_").concat("t").concat("-").concat("o").concat("!").concat("r").concat(")").concat("y").concat("@").concat(" ").concat("-").concat("i").concat("$").concat("s").concat("#").concat(" ").concat("#").concat("o").concat("^").concat("p").concat("@").concat("e").concat(")").concat("n"), Category.Movement);
        this.addSettings(this.inventory, this.shift);
    }

    @Override
    public void onTick() {
        if (this.inventory.isEnabled() && InventoryMove.mc.currentScreen != null && !(InventoryMove.mc.currentScreen instanceof ChatScreen) && !(InventoryMove.mc.currentScreen instanceof SignEditScreen) && !(InventoryMove.mc.currentScreen instanceof BookScreen)) {
            for (KeyBinding k : new KeyBinding[]{InventoryMove.mc.options.forwardKey, InventoryMove.mc.options.backKey, InventoryMove.mc.options.leftKey, InventoryMove.mc.options.rightKey, InventoryMove.mc.options.jumpKey, InventoryMove.mc.options.sprintKey}) {
                k.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)k.getBoundKeyTranslationKey()).getCode()));
            }
            if (this.shift.isEnabled()) {
                InventoryMove.mc.options.sneakKey.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)InventoryMove.mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
            }
        }
    }
}
