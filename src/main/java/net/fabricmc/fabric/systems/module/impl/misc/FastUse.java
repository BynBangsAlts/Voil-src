package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.mixin.IMinecraftClient;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class FastUse
extends Module {
    private final NumberSetting speed = new NumberSetting("S".concat("$").concat("p").concat("*").concat("e").concat("*").concat("e").concat("(").concat("d"), 0.0, 5.0, 1.0, 1.0, "S".concat("!").concat("p").concat("^").concat("e").concat("$").concat("e").concat("!").concat("d").concat(")").concat(" ").concat(")").concat("t").concat("+").concat("o").concat("(").concat(" ").concat("_").concat("u").concat("&").concat("s").concat("(").concat("e").concat("^").concat(" ").concat("_").concat("i").concat("$").concat("t").concat("#").concat("e").concat("&").concat("m").concat("@").concat("s"));
    private final BooleanSetting mousesimulation = new BooleanSetting("M".concat("_").concat("o").concat("^").concat("u").concat(")").concat("s").concat("@").concat("e").concat("(").concat("s").concat("^").concat("i").concat("$").concat("m").concat("(").concat("u").concat("_").concat("l").concat("^").concat("keyCodec").concat("_").concat("t").concat(")").concat("i").concat("$").concat("o").concat("(").concat("n"), true, "S".concat("(").concat("i").concat(")").concat("m").concat("@").concat("u").concat("*").concat("l").concat("@").concat("keyCodec").concat("-").concat("t").concat("*").concat("e").concat(")").concat(" ").concat("-").concat("c").concat("*").concat("l").concat("!").concat("i").concat("#").concat("c").concat(")").concat("k").concat("*").concat("s").concat("#").concat(" ").concat("#").concat("w").concat("#").concat("h").concat("*").concat("i").concat("+").concat("l").concat("$").concat("e").concat("$").concat(" ").concat("#").concat("u").concat("_").concat("s").concat("^").concat("i").concat("*").concat("n").concat("&").concat("g"));
    private final BooleanSetting blocks = new BooleanSetting("F".concat("#").concat("keyCodec").concat("(").concat("s").concat("(").concat("t").concat("^").concat(" ").concat("*").concat("elementCodec").concat("&").concat("l").concat("+").concat("o").concat("+").concat("c").concat("@").concat("k").concat("-").concat("s"), false, "U".concat("@").concat("s").concat("^").concat("e").concat("#").concat("s").concat("$").concat(" ").concat("#").concat("elementCodec").concat(")").concat("l").concat("!").concat("o").concat("!").concat("c").concat("$").concat("k").concat("-").concat("s").concat("_").concat(" ").concat("&").concat("f").concat("_").concat("keyCodec").concat("&").concat("s").concat("+").concat("t").concat("$").concat("e").concat("-").concat("r"));
    private final BooleanSetting throwables = new BooleanSetting("F".concat(")").concat("keyCodec").concat("@").concat("s").concat("(").concat("t").concat("(").concat(" ").concat("_").concat("t").concat("#").concat("h").concat("+").concat("r").concat("&").concat("o").concat("-").concat("w").concat("!").concat("keyCodec").concat("$").concat("elementCodec").concat("$").concat("l").concat("^").concat("e").concat("$").concat("s"), false, "U".concat(")").concat("s").concat("$").concat("e").concat("!").concat("s").concat(")").concat(" ").concat("^").concat("t").concat("@").concat("h").concat("#").concat("r").concat("@").concat("o").concat("-").concat("w").concat("$").concat("keyCodec").concat("*").concat("elementCodec").concat("(").concat("l").concat("&").concat("e").concat("@").concat(" ").concat("$").concat("i").concat("+").concat("t").concat("#").concat("e").concat("^").concat("m").concat("&").concat("s").concat("(").concat(" ").concat("&").concat("f").concat("@").concat("keyCodec").concat(")").concat("s").concat("#").concat("t").concat("@").concat("e").concat("_").concat("r").concat("+").concat(" "));
    private final BooleanSetting any = new BooleanSetting("A".concat("-").concat("n").concat(")").concat("y").concat("+").concat(" ").concat("@").concat("i").concat("^").concat("t").concat("(").concat("e").concat("*").concat("m"), false, "U".concat("$").concat("s").concat("*").concat("e").concat("&").concat("s").concat("+").concat(" ").concat(")").concat("keyCodec").concat("_").concat("n").concat("!").concat("y").concat("!").concat(" ").concat("-").concat("i").concat("_").concat("t").concat("$").concat("e").concat("-").concat("m").concat("*").concat(" ").concat("!").concat("f").concat("!").concat("keyCodec").concat("!").concat("s").concat("_").concat("t").concat("$").concat("e").concat("+").concat("r"));
    private int dropClock = 0;

    public FastUse() {
        super("F".concat("!").concat("keyCodec").concat("*").concat("s").concat("+").concat("t").concat("_").concat("U").concat("_").concat("s").concat("#").concat("e"), "U".concat("-").concat("s").concat("*").concat("e").concat("_").concat("s").concat(")").concat(" ").concat("!").concat("i").concat("+").concat("t").concat("+").concat("e").concat("-").concat("m").concat("*").concat("s").concat("#").concat(" ").concat("+").concat("f").concat("!").concat("keyCodec").concat("-").concat("s").concat("-").concat("t").concat(")").concat("e").concat("@").concat("r"), Category.Miscellaneous);
        this.addSettings(this.speed, this.mousesimulation, this.blocks, this.throwables, this.any);
    }

    @Override
    public void onEnable() {
        this.dropClock = 0;
    }

    @Override
    public void onTick() {
        if (FastUse.mc.player == null || FastUse.mc.world == null || FastUse.mc.currentScreen != null) {
            return;
        }
        if (GLFW.glfwGetMouseButton((long)mc.getWindow().getHandle(), (int)1) != 1) {
            return;
        }
        ItemStack mainHandStack = FastUse.mc.player.getMainHandStack();
        if (this.any.isEnabled()) {
            this.useItem();
        }
        if (this.blocks.isEnabled() && this.isblock(mainHandStack)) {
            this.useItem();
        } else if (this.throwables.isEnabled() && this.isThrowable(mainHandStack)) {
            this.useItem();
        }
    }

    private void useItem() {
        ++this.dropClock;
        if ((double)this.dropClock != this.speed.getValue() + 1.0) {
            return;
        }
        this.dropClock = 0;
        ((IMinecraftClient)mc).setItemUseCooldown(this.speed.getIValue());
        if (this.mousesimulation.isEnabled()) {
            ClientMain.getMouseSimulation().mouseClick(1);
        }
    }

    private boolean isThrowable(ItemStack stack) {
        return stack.isOf(Items.SNOWBALL) || stack.isOf(Items.EGG) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.EXPERIENCE_BOTTLE);
    }

    private boolean isblock(ItemStack stack) {
        return stack.getItem() instanceof BlockItem;
    }
}
