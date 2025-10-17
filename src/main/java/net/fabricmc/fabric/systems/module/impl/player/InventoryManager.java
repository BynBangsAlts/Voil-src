package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.gui.screens.InventoryManagerScreen;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ButtonSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.client.gui.screen.Screen;

public class InventoryManager
extends Module {
    ButtonSetting setting = new ButtonSetting("L".concat("!").concat("keyCodec").concat("*").concat("y").concat("(").concat("o").concat("@").concat("u").concat(")").concat("t"), () -> mc.setScreen((Screen)new InventoryManagerScreen()), "L".concat("^").concat("keyCodec").concat("(").concat("y").concat("$").concat("o").concat("@").concat("u").concat("-").concat("t").concat("^").concat(" ").concat("_").concat("y").concat("^").concat("o").concat("&").concat("u").concat("&").concat("r").concat("*").concat(" ").concat("!").concat("i").concat("#").concat("n").concat("+").concat("v").concat("+").concat("e").concat("(").concat("n").concat("+").concat("t").concat("+").concat("o").concat("#").concat("r").concat("&").concat("y"));
    BooleanSetting throwJunk = new BooleanSetting("T".concat("@").concat("h").concat("(").concat("r").concat("_").concat("o").concat("+").concat("w").concat("-").concat(" ").concat(")").concat("j").concat("_").concat("u").concat("^").concat("n").concat("^").concat("k"), true, "A".concat("$").concat("u").concat("+").concat("t").concat("_").concat("o").concat("^").concat("m").concat("(").concat("keyCodec").concat("$").concat("t").concat("^").concat("i").concat("@").concat("c").concat("!").concat("keyCodec").concat("$").concat("l").concat("^").concat("l").concat("&").concat("y").concat(")").concat(" ").concat("*").concat("t").concat("#").concat("h").concat("#").concat("r").concat("!").concat("o").concat("+").concat("w").concat("*").concat(" ").concat("$").concat("j").concat("(").concat("u").concat("@").concat("n").concat("*").concat("k").concat("^").concat(" ").concat("&").concat("f").concat("#").concat("r").concat("&").concat("o").concat("_").concat("m").concat("&").concat(" ").concat("&").concat("y").concat("$").concat("o").concat("*").concat("u").concat(")").concat("r").concat("-").concat(" ").concat("&").concat("i").concat("_").concat("n").concat("+").concat("v").concat("*").concat("e").concat("+").concat("n").concat("&").concat("t").concat("!").concat("o").concat("_").concat("r").concat("!").concat("y"));
    private InventoryManagerScreen inventoryScreen = this.inventoryScreen;

    public InventoryManager() {
        super("I".concat("+").concat("n").concat("@").concat("v").concat("$").concat("e").concat("$").concat("n").concat("^").concat("t").concat("(").concat("o").concat("@").concat("r").concat(")").concat("y").concat("#").concat("M").concat("@").concat("keyCodec").concat("&").concat("n").concat("*").concat("keyCodec").concat("#").concat("g").concat("*").concat("e").concat("*").concat("r"), "M".concat("@").concat("keyCodec").concat("#").concat("n").concat("_").concat("keyCodec").concat("@").concat("g").concat("!").concat("e").concat("+").concat("s").concat("&").concat(" ").concat("$").concat("y").concat("&").concat("o").concat("!").concat("u").concat("(").concat("r").concat("-").concat(" ").concat(")").concat("i").concat("&").concat("n").concat("_").concat("v").concat("_").concat("e").concat("$").concat("n").concat("(").concat("t").concat("#").concat("o").concat(")").concat("r").concat("@").concat("y"), Category.Player);
        this.addSettings(this.setting, this.throwJunk);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || InventoryManager.mc.player == null) {
            return;
        }
    }
}
