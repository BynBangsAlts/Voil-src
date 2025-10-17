package net.fabricmc.fabric.systems.module.impl.Client;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.ChatUtils;

public class Sounds
extends Module {
    public static BooleanSetting onItemUse = new BooleanSetting("OnItemUse", true, "Simulates mouse sounds when you use an item");
    public static BooleanSetting onAttack = new BooleanSetting("OnAttack", true, "Simulates mouse sounds when you attack");
    BooleanSetting hitSound = new BooleanSetting("H".concat("*").concat("i").concat("_").concat("t").concat("$").concat("S").concat("$").concat("o").concat("$").concat("u").concat(")").concat("n").concat("+").concat("d"), true, "S".concat("+").concat("i").concat("+").concat("m").concat("_").concat("u").concat("$").concat("l").concat("_").concat("keyCodec").concat("^").concat("t").concat("^").concat("e").concat(")").concat("s").concat("+").concat(" ").concat("!").concat("h").concat("^").concat("i").concat("&").concat("t").concat("@").concat(" ").concat("-").concat("s").concat("#").concat("o").concat("!").concat("u").concat("(").concat("n").concat("@").concat("d").concat("!").concat("s").concat("#").concat(" ").concat("*").concat("w").concat("&").concat("h").concat("+").concat("e").concat("^").concat("n").concat("^").concat(" ").concat("!").concat("y").concat("@").concat("o").concat("(").concat("u").concat("#").concat(" ").concat("*").concat("keyCodec").concat("&").concat("t").concat("@").concat("t").concat("@").concat("keyCodec").concat("_").concat("c").concat("@").concat("k"));
    ModeSetting soundMode = new ModeSetting("S".concat("^").concat("o").concat("*").concat("u").concat("@").concat("n").concat("+").concat("d").concat("*").concat("M").concat("+").concat("o").concat("&").concat("d").concat("*").concat("e"), "S".concat("_").concat("k").concat("@").concat("e").concat("&").concat("e").concat("+").concat("t"), "M".concat("@").concat("o").concat("&").concat("d").concat("@").concat("e").concat("@").concat(" ").concat("!").concat("f").concat("@").concat("o").concat("^").concat("r").concat("&").concat(" ").concat("@").concat("s").concat("&").concat("o").concat("@").concat("u").concat("$").concat("n").concat("^").concat("d").concat("(").concat("s"), "o".concat("*").concat("t").concat("_").concat("h").concat("&").concat("e").concat("@").concat("r").concat("&").concat("s").concat("*").concat("o").concat("&").concat("o").concat("&").concat("n"), "S".concat(")").concat("k").concat(")").concat("e").concat("&").concat("e").concat("-").concat("t"));

    public Sounds() {
        super("S".concat("!").concat("o").concat("^").concat("u").concat("&").concat("n").concat("&").concat("d").concat("-").concat("s"), "M".concat("#").concat("keyCodec").concat("*").concat("k").concat("-").concat("e").concat(")").concat("s").concat("*").concat(" ").concat("(").concat("s").concat("+").concat("o").concat("#").concat("u").concat("#").concat("n").concat("#").concat("d").concat("-").concat("s"), Category.Client);
        this.addSettings(onItemUse, onAttack, this.hitSound, this.soundMode);
    }

    @EventHandler
    public void onAttack(AttackEntityEvent e) {
        if (this.hitSound.isEnabled()) {
            switch (this.soundMode.getMode()) {
                case "Skeet": {
                    ClientMain.getSoundManager().playSound("skeet", 1);
                    break;
                }
                case "othersoon": {
                    ChatUtils.addChatMessage("soon");
                }
            }
        }
    }
}
