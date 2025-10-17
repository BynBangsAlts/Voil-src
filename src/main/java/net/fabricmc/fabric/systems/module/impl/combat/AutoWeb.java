package net.fabricmc.fabric.systems.module.impl.combat;

import java.awt.Color;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.player.PlayerSimulation;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

public class AutoWeb
extends Module {
    NumberSetting delay = new NumberSetting("D".concat(")").concat("e").concat("@").concat("l").concat("(").concat("keyCodec").concat("_").concat("y"), 0.0, 10.0, 2.0, 1.0, "D".concat("-").concat("e").concat("(").concat("l").concat("_").concat("keyCodec").concat("+").concat("y").concat("^").concat(" ").concat("-").concat("elementCodec").concat(")").concat("e").concat("+").concat("t").concat("@").concat("w").concat("!").concat("e").concat("*").concat("e").concat("(").concat("n").concat("!").concat(" ").concat("_").concat("w").concat("@").concat("e").concat("(").concat("elementCodec").concat("_").concat("s"));
    NumberSetting switchDelay = new NumberSetting("S".concat("^").concat("w").concat("&").concat("i").concat("!").concat("t").concat("*").concat("c").concat("*").concat("h").concat("-").concat(" ").concat("(").concat("D").concat("!").concat("e").concat("!").concat("l").concat("@").concat("keyCodec").concat(")").concat("y"), 0.0, 10.0, 2.0, 1.0, "D".concat("(").concat("e").concat("&").concat("l").concat("(").concat("keyCodec").concat("&").concat("y").concat("&").concat(" ").concat(")").concat("t").concat("@").concat("o").concat("@").concat(" ").concat("&").concat("s").concat("_").concat("w").concat("-").concat("i").concat("&").concat("t").concat("*").concat("c").concat("&").concat("h").concat("(").concat(" ").concat("^").concat("elementCodec").concat("(").concat("e").concat("^").concat("t").concat("-").concat("w").concat("*").concat("e").concat("_").concat("e").concat("&").concat("n").concat("$").concat(" ").concat("^").concat("i").concat("$").concat("t").concat("-").concat("e").concat("@").concat("m").concat("@").concat("s"));
    NumberSetting range = new NumberSetting("R".concat("$").concat("keyCodec").concat("!").concat("n").concat(")").concat("g").concat(")").concat("e"), 1.0, 3.0, 2.0, 1.0, "R".concat("#").concat("keyCodec").concat(")").concat("n").concat("*").concat("g").concat("(").concat("e").concat("-").concat(" ").concat("$").concat("t").concat("$").concat("o").concat("!").concat(" ").concat("#").concat("p").concat("(").concat("l").concat("@").concat("keyCodec").concat("!").concat("c").concat(")").concat("e").concat("^").concat(" ").concat("_").concat("w").concat("!").concat("e").concat("-").concat("elementCodec").concat("^").concat("s"));
    NumberSetting predictionTicks = new NumberSetting("P".concat("&").concat("r").concat("^").concat("e").concat("+").concat("d").concat("#").concat("i").concat("@").concat("c").concat(")").concat("t").concat("*").concat("i").concat("#").concat("o").concat("+").concat("n").concat("(").concat(" ").concat("#").concat("T").concat("+").concat("i").concat("*").concat("c").concat("*").concat("k").concat("!").concat("s"), 1.0, 8.0, 4.0, 1.0, "T".concat("^").concat("i").concat("_").concat("c").concat("^").concat("k").concat("@").concat("s").concat("&").concat(" ").concat("+").concat("t").concat("*").concat("o").concat("^").concat(" ").concat("*").concat("p").concat("(").concat("r").concat("$").concat("e").concat("@").concat("d").concat("*").concat("i").concat(")").concat("c").concat("^").concat("t").concat("#").concat(" ").concat("+").concat("m").concat("_").concat("o").concat("@").concat("v").concat(")").concat("e").concat("*").concat("m").concat("+").concat("e").concat("_").concat("n").concat("@").concat("t"));
    ModeSetting placeMode = new ModeSetting("P".concat("_").concat("l").concat("^").concat("keyCodec").concat("$").concat("c").concat("+").concat("e").concat("(").concat(" ").concat("$").concat("M").concat("(").concat("o").concat("(").concat("d").concat("$").concat("e"), "R".concat("-").concat("keyCodec").concat("&").concat("n").concat("-").concat("g").concat("!").concat("e"), "M".concat("@").concat("o").concat("$").concat("d").concat("-").concat("e").concat("#").concat(" ").concat("^").concat("t").concat("^").concat("o").concat("#").concat(" ").concat("-").concat("p").concat("+").concat("l").concat("(").concat("keyCodec").concat("*").concat("c").concat("*").concat("e"), "O".concat("*").concat("n").concat("$").concat("K").concat(")").concat("e").concat("*").concat("y"), "R".concat("_").concat("keyCodec").concat("+").concat("n").concat("!").concat("g").concat("$").concat("e"));
    KeybindSetting keybind = new KeybindSetting("A".concat("^").concat("c").concat("_").concat("t").concat("_").concat("i").concat("&").concat("v").concat("(").concat("keyCodec").concat(")").concat("t").concat("*").concat("e"), 0, false, "K".concat("!").concat("e").concat("_").concat("y").concat("_").concat("elementCodec").concat("+").concat("i").concat("*").concat("n").concat("&").concat("d").concat("@").concat(" ").concat("@").concat("t").concat("#").concat("o").concat("$").concat(" ").concat("_").concat("u").concat("+").concat("s").concat(")").concat("e"));
    BooleanSetting placeLava = new BooleanSetting("P".concat("^").concat("l").concat("-").concat("keyCodec").concat("+").concat("c").concat("^").concat("e").concat("(").concat(" ").concat("^").concat("L").concat("*").concat("keyCodec").concat("*").concat("v").concat("#").concat("keyCodec"), true, "T".concat("&").concat("o").concat("-").concat(" ").concat("@").concat("p").concat("#").concat("l").concat("^").concat("keyCodec").concat("$").concat("c").concat("+").concat("e").concat("^").concat(" ").concat("#").concat("l").concat(")").concat("keyCodec").concat("-").concat("v").concat(")").concat("keyCodec").concat("^").concat(" ").concat("#").concat("o").concat("-").concat("n").concat("!").concat(" ").concat("#").concat("w").concat("!").concat("e").concat("*").concat("elementCodec").concat("!").concat("s"));
    BooleanSetting placeTnt = new BooleanSetting("P".concat("*").concat("l").concat("^").concat("keyCodec").concat("!").concat("c").concat("_").concat("e").concat("_").concat(" ").concat("$").concat("T").concat(")").concat("n").concat("$").concat("t"), true, "T".concat("@").concat("o").concat("@").concat(" ").concat("_").concat("p").concat("_").concat("l").concat("@").concat("keyCodec").concat("&").concat("c").concat("$").concat("e").concat("$").concat(" ").concat("!").concat("t").concat("_").concat("n").concat(")").concat("t").concat("$").concat(" ").concat(")").concat("o").concat("!").concat("n").concat("@").concat(" ").concat("&").concat("w").concat("$").concat("e").concat(")").concat("elementCodec").concat("(").concat("s"));
    BooleanSetting lightTnt = new BooleanSetting("L".concat("-").concat("i").concat("*").concat("g").concat("(").concat("h").concat("$").concat("t").concat("#").concat(" ").concat("&").concat("T").concat("$").concat("n").concat("(").concat("t"), true, "T".concat("!").concat("o").concat("-").concat(" ").concat("*").concat("l").concat("-").concat("i").concat("(").concat("g").concat(")").concat("h").concat("^").concat("t").concat("(").concat(" ").concat("(").concat("t").concat("$").concat("n").concat("*").concat("t"));
    BooleanSetting placeCreepers = new BooleanSetting("P".concat("*").concat("l").concat("^").concat("keyCodec").concat("+").concat("c").concat("$").concat("e").concat("^").concat(" ").concat("^").concat("C").concat("(").concat("r").concat("#").concat("e").concat("+").concat("e").concat(")").concat("p").concat("(").concat("e").concat("&").concat("r").concat("(").concat("s"), true, "T".concat("#").concat("o").concat("$").concat(" ").concat("#").concat("p").concat(")").concat("l").concat("+").concat("keyCodec").concat("_").concat("c").concat("@").concat("e").concat("*").concat(" ").concat("*").concat("c").concat("*").concat("r").concat("!").concat("e").concat("&").concat("e").concat("!").concat("p").concat("$").concat("e").concat("-").concat("r").concat("!").concat("s").concat("#").concat(" ").concat("!").concat("o").concat("#").concat("n").concat("+").concat(" ").concat(")").concat("w").concat("+").concat("e").concat("&").concat("elementCodec").concat("(").concat("s"));
    BooleanSetting mousesimulation = new BooleanSetting("M".concat("+").concat("o").concat("_").concat("u").concat("*").concat("s").concat("+").concat("e").concat("!").concat(" ").concat("^").concat("S").concat(")").concat("i").concat("^").concat("m"), true, "T".concat("&").concat("o").concat("^").concat(" ").concat("^").concat("s").concat("@").concat("i").concat("@").concat("m").concat("-").concat("u").concat("(").concat("l").concat("&").concat("keyCodec").concat(")").concat("t").concat("$").concat("e").concat("@").concat(" ").concat("-").concat("m").concat("^").concat("o").concat("*").concat("u").concat("_").concat("s").concat("_").concat("e").concat("#").concat(" ").concat("-").concat("c").concat("(").concat("l").concat("*").concat("i").concat("!").concat("c").concat("^").concat("k"));
    BooleanSetting switchback = new BooleanSetting("S".concat("-").concat("w").concat("_").concat("i").concat("^").concat("t").concat("-").concat("c").concat("_").concat("h").concat("#").concat(" ").concat("$").concat("B").concat("@").concat("keyCodec").concat("-").concat("c").concat("(").concat("k"), false, "T".concat("-").concat("o").concat("!").concat(" ").concat("(").concat("s").concat("^").concat("w").concat("(").concat("i").concat(")").concat("t").concat("^").concat("c").concat("_").concat("h").concat(")").concat(" ").concat("-").concat("elementCodec").concat("*").concat("keyCodec").concat("!").concat("c").concat("-").concat("k").concat("(").concat(" ").concat("-").concat("t").concat("#").concat("o").concat("^").concat(" ").concat("*").concat("p").concat("$").concat("r").concat("#").concat("e").concat("$").concat("v").concat("(").concat("i").concat("+").concat("o").concat("!").concat("u").concat("(").concat("s").concat("_").concat(" ").concat("&").concat("s").concat("@").concat("l").concat("!").concat("o").concat("&").concat("t"));
    BooleanSetting rotations = new BooleanSetting("R".concat("!").concat("o").concat("(").concat("t").concat("+").concat("keyCodec").concat(")").concat("t").concat("+").concat("i").concat("^").concat("o").concat("^").concat("n").concat("@").concat("s"), true, "T".concat("+").concat("o").concat("!").concat(" ").concat("*").concat("r").concat("$").concat("o").concat("-").concat("t").concat("&").concat("keyCodec").concat("&").concat("t").concat("(").concat("e").concat("+").concat(" ").concat("@").concat("p").concat("*").concat("l").concat("_").concat("keyCodec").concat("#").concat("y").concat(")").concat("e").concat("*").concat("r").concat("+").concat("s").concat("!").concat(" ").concat("$").concat("h").concat("+").concat("e").concat("*").concat("keyCodec").concat("-").concat("d"));
    private int prevSlot;
    private int placeClock;
    private int switchClock;
    private boolean selectedWeb;
    private boolean placedWeb;
    private boolean selectedTnt;
    private boolean placedTnt;
    private boolean selectedCreepers;
    private boolean placedCreepers;
    private boolean selectedFlint;
    private boolean usedFlint;
    private boolean selectedLava;
    private boolean placedLava;
    private BlockPos predictedBlockPos;

    public AutoWeb() {
        super("A".concat("&").concat("u").concat("_").concat("t").concat("@").concat("o").concat("-").concat("W").concat("(").concat("e").concat(")").concat("elementCodec"), "A".concat("(").concat("u").concat("-").concat("t").concat(")").concat("o").concat("$").concat("m").concat("-").concat("keyCodec").concat("(").concat("t").concat("^").concat("i").concat("^").concat("c").concat("*").concat("keyCodec").concat("_").concat("l").concat("!").concat("l").concat("&").concat("y").concat("_").concat(" ").concat("-").concat("p").concat("$").concat("l").concat("+").concat("keyCodec").concat("@").concat("c").concat("+").concat("e").concat("_").concat("s").concat("$").concat(" ").concat("-").concat("w").concat("$").concat("e").concat("^").concat("elementCodec").concat("^").concat("s"), Category.Combat);
        this.addSettings(this.delay, this.range, this.switchDelay, this.predictionTicks, this.keybind, this.placeLava, this.placeTnt, this.lightTnt, this.placeCreepers, this.placeMode, this.switchback, this.mousesimulation, this.rotations);
    }

    public void reset() {
        this.prevSlot = 0;
        this.placeClock = 0;
        this.switchClock = 0;
        this.selectedWeb = false;
        this.placedWeb = false;
        this.selectedTnt = false;
        this.placedTnt = false;
        this.selectedLava = false;
        this.placedLava = false;
        this.selectedCreepers = false;
        this.placedCreepers = false;
        this.selectedFlint = false;
        this.usedFlint = false;
        ClientMain.getRotationManager().resetRotation(true);
    }

    private int getWebSlot() {
        return InventoryUtils.findItemSlot(Items.COBWEB);
    }

    private int getTntSlot() {
        return InventoryUtils.findItemSlot(Items.TNT);
    }

    private int getCreeperSlot() {
        return InventoryUtils.findItemSlot(Items.CREEPER_SPAWN_EGG);
    }

    private int getFlintSlot() {
        return InventoryUtils.findItemSlot(Items.FLINT_AND_STEEL);
    }

    private int getLavaSlot() {
        return InventoryUtils.findItemSlot(Items.LAVA_BUCKET);
    }

    private boolean checkStack(ItemStack handStack) {
        return handStack.isOf(Items.TNT) || handStack.isOf(Items.CREEPER_SPAWN_EGG) || handStack.isOf(Items.FLINT_AND_STEEL) || handStack.isOf(Items.LAVA_BUCKET) || handStack.isOf(Items.COBWEB);
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    @Override
    public void onTick() {
        if (AutoWeb.mc.player == null || AutoWeb.mc.world == null) {
            return;
        }
        boolean inRange = false;
        PlayerEntity target = null;
        for (PlayerEntity player : AutoWeb.mc.world.getPlayers()) {
            if (player == AutoWeb.mc.player || !(AutoWeb.mc.player.distanceTo((Entity)player) <= (float)this.range.getIValue())) continue;
            target = player;
            inRange = true;
            break;
        }
        if (target == null) {
            return;
        }
        PlayerSimulation simulation = new PlayerSimulation(target);
        simulation.tick(this.predictionTicks.getIValue());
        if (this.placeMode.getMode().equals("OnKey") && KeyUtils.isKeyPressed(this.keybind.getKey()) || this.placeMode.getMode().equals("Range") && inRange) {
            if (this.rotations.isEnabled()) {
                Rotation targetRot2 = Rotation.getDirection((Entity)AutoWeb.mc.player, target.getPos().add(0.0, -1.0, 0.0));
                ClientMain.getRotationManager().setRotation(targetRot2, RotationManager.RotationPriority.MEDIUM);
            }
            Vec3d predictedPos = simulation.getPredictedPosition();
            this.predictedBlockPos = new BlockPos((int)predictedPos.x, (int)predictedPos.y, (int)predictedPos.z);
            if (!this.placedWeb && this.selectAndPlaceWeb(this.predictedBlockPos)) {
                this.placedWeb = true;
                if (this.mousesimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(1);
                }
                this.reset();
            }
            if (this.placeLava.isEnabled() && !this.placedLava && this.selectAndPlaceLava()) {
                this.placedLava = true;
                if (this.mousesimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(1);
                }
                this.reset();
            }
            if (this.placeTnt.isEnabled() && !this.placedTnt && this.selectAndPlaceTnt(target.getBlockPos())) {
                this.placedTnt = true;
                if (this.mousesimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(1);
                }
                this.reset();
            }
            if (this.lightTnt.isEnabled() && !this.usedFlint && this.placedTnt && this.selectAndUseFlint()) {
                this.usedFlint = true;
                if (this.mousesimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(1);
                }
                this.reset();
            }
            if (this.placeCreepers.isEnabled() && !this.placedCreepers && this.selectAndPlaceCreepers(target.getBlockPos())) {
                this.placedCreepers = true;
                if (this.mousesimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(1);
                }
                this.reset();
            }
        }
    }

    private boolean selectAndPlaceWeb(BlockPos targetPos) {
        int webSlot = this.getWebSlot();
        if (webSlot != -1) {
            InventoryUtils.setInvSlot(webSlot);
            BlockPos posToPlace = targetPos;
            BlockPos posBelow = posToPlace.down();
            if (!this.isWebd(posBelow) && WorldUtils.placeBlock(Items.COBWEB, posBelow)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWebd(BlockPos targetPos) {
        BlockState state = AutoWeb.mc.world.getBlockState(targetPos.down());
        return state.getBlock() == Blocks.COBWEB;
    }

    private boolean selectAndPlaceLava() {
        int lavaSlot = this.getLavaSlot();
        if (lavaSlot != -1) {
            InventoryUtils.setInvSlot(lavaSlot);
            WorldUtils.useItem(Items.LAVA_BUCKET);
            return true;
        }
        return false;
    }

    private boolean selectAndUseFlint() {
        int flintSlot = this.getFlintSlot();
        if (flintSlot != -1) {
            InventoryUtils.setInvSlot(flintSlot);
            WorldUtils.useItem(Items.FLINT_AND_STEEL);
            return true;
        }
        return false;
    }

    private boolean selectAndPlaceTnt(BlockPos targetPos) {
        int tntSlot = this.getTntSlot();
        if (tntSlot != -1) {
            BlockHitResult hitResult;
            BlockState state;
            InventoryUtils.setInvSlot(tntSlot);
            BlockPos tntPos = this.findpos(targetPos);
            if (tntPos != null && ((state = AutoWeb.mc.world.getBlockState(tntPos)).isAir() || state.getBlock().getDefaultState().canPlaceAt((WorldView)AutoWeb.mc.world, tntPos)) && AutoWeb.mc.interactionManager.interactBlock(AutoWeb.mc.player, Hand.MAIN_HAND, hitResult = new BlockHitResult(new Vec3d((double)tntPos.getX() + 0.5, (double)tntPos.getY() + 0.5, (double)tntPos.getZ() + 0.5), Direction.DOWN, tntPos, false)) == ActionResult.SUCCESS) {
                return true;
            }
        }
        return false;
    }

    private boolean selectAndPlaceCreepers(BlockPos targetPos) {
        int creeperSlot = this.getCreeperSlot();
        if (creeperSlot != -1) {
            InventoryUtils.setInvSlot(creeperSlot);
            BlockPos adjacentPos = this.findpos(targetPos.down());
            return WorldUtils.placeBlock(Items.CREEPER_SPAWN_EGG, targetPos);
        }
        return false;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.predictedBlockPos != null) {
            Render3DEngine.drawBoxWithParams(this.predictedBlockPos.toCenterPos(), ColorUtil.addAlpha(Color.WHITE, 100), matrices, 0.5f, 1.0f);
        }
    }

    private BlockPos findpos(BlockPos webPos) {
        Direction[] directions;
        for (Direction direction : directions = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN}) {
            BlockPos adjacentPos = webPos.offset(direction);
            BlockState state = AutoWeb.mc.world.getBlockState(adjacentPos);
            if (!state.isAir() && !state.getBlock().getDefaultState().canPlaceAt((WorldView)AutoWeb.mc.world, adjacentPos)) continue;
            return adjacentPos;
        }
        return null;
    }
}
