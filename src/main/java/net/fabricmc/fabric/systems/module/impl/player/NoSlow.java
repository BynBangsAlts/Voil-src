package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class NoSlow
extends Module {
    public BooleanSetting items = new BooleanSetting("I".concat("_").concat("t").concat("#").concat("e").concat("&").concat("m").concat("!").concat("s"), true, "M".concat("!").concat("keyCodec").concat("(").concat("k").concat("_").concat("e").concat("-").concat("s").concat("-").concat(" ").concat("-").concat("y").concat("#").concat("o").concat("@").concat("u").concat("-").concat(" ").concat("^").concat("n").concat("*").concat("o").concat("*").concat("t").concat("$").concat(" ").concat(")").concat("s").concat("&").concat("l").concat("&").concat("o").concat("-").concat("w").concat("_").concat(" ").concat("_").concat("d").concat("+").concat("o").concat("&").concat("w").concat("*").concat("n").concat("*").concat(" ").concat("*").concat("w").concat("(").concat("h").concat("+").concat("i").concat("^").concat("l").concat("+").concat("e").concat("$").concat(" ").concat("@").concat("u").concat("@").concat("s").concat("^").concat("i").concat(")").concat("n").concat("*").concat("g").concat("$").concat(" ").concat("*").concat("i").concat("#").concat("t").concat("^").concat("e").concat("_").concat("m").concat("&").concat("s"));
    public BooleanSetting sneaking = new BooleanSetting("S".concat("+").concat("n").concat("^").concat("e").concat("+").concat("keyCodec").concat("@").concat("k").concat("&").concat("i").concat("_").concat("n").concat("@").concat("g"), false, "M".concat("!").concat("keyCodec").concat("*").concat("k").concat("#").concat("e").concat("*").concat("s").concat("_").concat(" ").concat("-").concat("y").concat("+").concat("o").concat("(").concat("u").concat("*").concat(" ").concat("&").concat("n").concat("&").concat("o").concat("$").concat("t").concat("&").concat(" ").concat("&").concat("s").concat("+").concat("l").concat("+").concat("o").concat("+").concat("w").concat("+").concat(" ").concat("&").concat("d").concat("#").concat("o").concat("*").concat("w").concat("$").concat("n").concat(")").concat(" ").concat("*").concat("w").concat("@").concat("h").concat("-").concat("i").concat("-").concat("l").concat("@").concat("e").concat("(").concat(" ").concat("$").concat("s").concat(")").concat("n").concat("&").concat("e").concat("(").concat("keyCodec").concat("#").concat("k").concat("(").concat("i").concat("#").concat("n").concat("$").concat("g"));
    public BooleanSetting webs = new BooleanSetting("W".concat("-").concat("e").concat("!").concat("elementCodec").concat(")").concat("s"), false, "M".concat("!").concat("keyCodec").concat("#").concat("k").concat("*").concat("e").concat("-").concat("s").concat("^").concat(" ").concat("!").concat("y").concat("-").concat("o").concat("+").concat("u").concat("(").concat(" ").concat("@").concat("n").concat(")").concat("o").concat("-").concat("t").concat("!").concat(" ").concat("$").concat("s").concat(")").concat("l").concat("&").concat("o").concat("*").concat("w").concat("!").concat(" ").concat("(").concat("d").concat("-").concat("o").concat("(").concat("w").concat("+").concat("n").concat("$").concat(" ").concat("-").concat("w").concat("@").concat("h").concat("+").concat("i").concat("^").concat("l").concat("(").concat("e").concat(")").concat(" ").concat("!").concat("i").concat(")").concat("n").concat("(").concat(" ").concat("#").concat("w").concat(")").concat("e").concat("(").concat("elementCodec").concat("(").concat("s"));
    public static BooleanSetting grim = new BooleanSetting("Grim", false, "Makes you not slow down for grim ac");
    private int ticks = 0;

    public NoSlow() {
        super("N".concat("_").concat("o").concat("!").concat("S").concat("-").concat("l").concat("!").concat("o").concat("$").concat("w"), "M".concat("!").concat("keyCodec").concat("!").concat("k").concat("-").concat("e").concat("@").concat("s").concat("-").concat(" ").concat("&").concat("y").concat("*").concat("o").concat("@").concat("u").concat("&").concat(" ").concat("_").concat("n").concat("^").concat("o").concat("-").concat("t").concat("^").concat(" ").concat("&").concat("s").concat(")").concat("l").concat("#").concat("o").concat("+").concat("w").concat("(").concat(" ").concat("_").concat("d").concat("*").concat("o").concat("&").concat("w").concat("^").concat("n"), Category.Player);
        this.addSettings(this.items, this.sneaking, this.webs, grim);
    }

    public static boolean doesBoxTouchBlock(Box box, Block block) {
        int x = (int)Math.floor(box.minX);
        while ((double)x < Math.ceil(box.maxX)) {
            int y = (int)Math.floor(box.minY);
            while ((double)y < Math.ceil(box.maxY)) {
                int z = (int)Math.floor(box.minZ);
                while ((double)z < Math.ceil(box.maxZ)) {
                    if (NoSlow.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == block) {
                        return true;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.ticks = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (this.webs.isEnabled() && NoSlow.doesBoxTouchBlock(NoSlow.mc.player.getBoundingBox(), Blocks.COBWEB)) {
            NoSlow.mc.player.slowMovement(NoSlow.mc.world.getBlockState(NoSlow.mc.player.getBlockPos()), new Vec3d(2.35, 1.75, 2.35));
        }
    }
}
