package net.fabricmc.fabric.systems.module.impl.player;

import java.util.List;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AutoCraft
extends Module {
    private final NumberSetting delay = new NumberSetting("D".concat("(").concat("e").concat(")").concat("l").concat("&").concat("keyCodec").concat("+").concat("y"), 1.0, 10.0, 1.0, 1.0, "D".concat("(").concat("e").concat(")").concat("l").concat(")").concat("keyCodec").concat("#").concat("y").concat("+").concat(" ").concat("+").concat("elementCodec").concat("+").concat("e").concat("+").concat("t").concat("!").concat("w").concat("!").concat("e").concat("-").concat("e").concat("$").concat("n").concat("_").concat(" ").concat("(").concat("e").concat("$").concat("keyCodec").concat("#").concat("c").concat("@").concat("h").concat("^").concat(" ").concat("*").concat("c").concat("-").concat("r").concat("$").concat("keyCodec").concat("(").concat("f").concat("^").concat("t"));
    private final BooleanSetting armor = new BooleanSetting("A".concat(")").concat("r").concat("(").concat("m").concat("@").concat("o").concat("_").concat("r"), true, "A".concat("^").concat("u").concat("@").concat("t").concat("#").concat("o").concat("(").concat("m").concat("*").concat("keyCodec").concat("-").concat("t").concat("!").concat("i").concat("_").concat("c").concat("_").concat("keyCodec").concat("(").concat("l").concat("(").concat("l").concat("_").concat("y").concat("+").concat(" ").concat("$").concat("c").concat(")").concat("r").concat("^").concat("keyCodec").concat("@").concat("f").concat("-").concat("t").concat("^").concat(" ").concat("+").concat("keyCodec").concat("@").concat("r").concat("!").concat("m").concat(")").concat("o").concat("*").concat("r"));
    private final BooleanSetting tools = new BooleanSetting("T".concat("#").concat("o").concat("-").concat("o").concat("@").concat("l").concat("(").concat("s"), true, "A".concat("$").concat("u").concat("*").concat("t").concat("^").concat("o").concat("^").concat("m").concat(")").concat("keyCodec").concat("!").concat("t").concat("&").concat("i").concat(")").concat("c").concat("_").concat("keyCodec").concat("$").concat("l").concat("&").concat("l").concat("(").concat("y").concat("_").concat(" ").concat("+").concat("c").concat("!").concat("r").concat("^").concat("keyCodec").concat("(").concat("f").concat("#").concat("t").concat("@").concat(" ").concat("_").concat("t").concat("&").concat("o").concat(")").concat("o").concat("@").concat("l").concat("@").concat("s"));
    private static final List<Item> ARMOR_ITEMS = List.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
    private static final List<Item> TOOL_ITEMS = List.of(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_SWORD);

    public AutoCraft() {
        super("A".concat("*").concat("u").concat("#").concat("t").concat("(").concat("o").concat("&").concat("C").concat("+").concat("r").concat("_").concat("keyCodec").concat("$").concat("f").concat("#").concat("t"), "A".concat("_").concat("u").concat("#").concat("t").concat("#").concat("o").concat("&").concat("m").concat("@").concat("keyCodec").concat("_").concat("t").concat("$").concat("i").concat("!").concat("c").concat("@").concat("keyCodec").concat(")").concat("l").concat("&").concat("l").concat("@").concat("y").concat("+").concat(" ").concat("_").concat("c").concat("&").concat("r").concat("*").concat("keyCodec").concat("@").concat("f").concat("#").concat("t").concat("+").concat(" ").concat("-").concat("i").concat("^").concat("t").concat("$").concat("e").concat("(").concat("m").concat("^").concat("s"), Category.Player);
        this.addSettings(this.delay, this.armor, this.tools);
    }
}
