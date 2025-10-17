package net.fabricmc.fabric.systems.module.impl.combat;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AutoDrain
extends Module {
    private final NumberSetting delay = new NumberSetting("D".concat("!").concat("e").concat("_").concat("l").concat("@").concat("keyCodec").concat("^").concat("y"), 1.0, 10.0, 1.0, 1.0, "D".concat("^").concat("e").concat("-").concat("l").concat("!").concat("keyCodec").concat("+").concat("y").concat("#").concat(" ").concat("_").concat("f").concat(")").concat("o").concat("!").concat("r").concat(")").concat(" ").concat("$").concat("t").concat("(").concat("h").concat("!").concat("e").concat("+").concat(" ").concat("*").concat("d").concat("(").concat("r").concat(")").concat("keyCodec").concat("@").concat("i").concat("#").concat("n"));
    private final NumberSetting range = new NumberSetting("R".concat("(").concat("keyCodec").concat("@").concat("n").concat("!").concat("g").concat("_").concat("e"), 1.0, 3.0, 3.0, 0.1, "R".concat(")").concat("keyCodec").concat("#").concat("n").concat("*").concat("g").concat("-").concat("e").concat("@").concat(" ").concat("(").concat("t").concat("^").concat("o").concat("!").concat(" ").concat("!").concat("d").concat("_").concat("e").concat(")").concat("t").concat(")").concat("e").concat("^").concat("c").concat("^").concat("t").concat("*").concat(" ").concat("@").concat("l").concat("^").concat("i").concat("*").concat("q").concat("_").concat("u").concat("!").concat("i").concat("!").concat("d"));
    private final BooleanSetting lavadrain = new BooleanSetting("L".concat("^").concat("keyCodec").concat("-").concat("v").concat("_").concat("keyCodec"), true, "D".concat("!").concat("r").concat("!").concat("keyCodec").concat("^").concat("i").concat(")").concat("n").concat("@").concat("s").concat("_").concat(" ").concat("@").concat("l").concat("-").concat("keyCodec").concat("-").concat("v").concat("#").concat("keyCodec").concat("^").concat(" ").concat("$").concat("t").concat("^").concat("o").concat("@").concat("o"));
    private final BooleanSetting mousesimulation = new BooleanSetting("M".concat(")").concat("o").concat(")").concat("u").concat("$").concat("s").concat("^").concat("e").concat("+").concat("s").concat("_").concat("i").concat("+").concat("m").concat("!").concat("u").concat("&").concat("l").concat("&").concat("keyCodec").concat("!").concat("t").concat("_").concat("i").concat("-").concat("o").concat("@").concat("n"), true, "S".concat(")").concat("i").concat("&").concat("m").concat("$").concat("u").concat("$").concat("l").concat("&").concat("keyCodec").concat("+").concat("t").concat("*").concat("e").concat("(").concat(" ").concat("$").concat("c").concat("_").concat("l").concat("-").concat("i").concat("@").concat("c").concat("-").concat("k").concat("!").concat("s").concat("*").concat(" ").concat("#").concat("w").concat(")").concat("h").concat(")").concat("i").concat("#").concat("l").concat("(").concat("e").concat("!").concat(" ").concat("_").concat("u").concat("^").concat("s").concat("(").concat("i").concat("*").concat("n").concat("*").concat("g"));
    private final BooleanSetting onKey = new BooleanSetting("O".concat("_").concat("n").concat("(").concat(" ").concat("*").concat("k").concat("-").concat("e").concat("#").concat("y"), true, "D".concat("@").concat("r").concat("*").concat("keyCodec").concat("$").concat("i").concat("-").concat("n").concat("$").concat(" ").concat("_").concat("o").concat(")").concat("n").concat("@").concat(" ").concat("^").concat("k").concat("#").concat("e").concat("$").concat("y").concat("#").concat(" ").concat(")").concat("p").concat("#").concat("r").concat("^").concat("e").concat("^").concat("s").concat("*").concat("s"));
    private final ModeSetting rotations = new ModeSetting("R".concat("@").concat("o").concat("#").concat("t").concat("@").concat("keyCodec").concat("_").concat("t").concat("_").concat("i").concat("_").concat("o").concat("@").concat("n").concat("(").concat("s"), "M".concat("_").concat("keyCodec").concat("$").concat("n").concat("(").concat("u").concat("@").concat("keyCodec").concat("(").concat("l"), "M".concat(")").concat("keyCodec").concat("@").concat("n").concat("!").concat("u").concat("*").concat("keyCodec").concat("(").concat("l").concat("+").concat(" ").concat("&").concat("m").concat("+").concat("o").concat("&").concat("d").concat("_").concat("e").concat("_").concat(" ").concat("!").concat("o").concat(")").concat("n").concat("(").concat("l").concat("!").concat("y").concat("!").concat(" ").concat(")").concat("p").concat("(").concat("i").concat("(").concat("c").concat("^").concat("k").concat("@").concat("s").concat(")").concat(" ").concat("^").concat("u").concat("!").concat("p").concat("(").concat(" ").concat("$").concat("w").concat("_").concat("h").concat("@").concat("e").concat("-").concat("n").concat("^").concat(" ").concat("!").concat("y").concat("-").concat("o").concat("-").concat("u").concat("*").concat(" ").concat("@").concat("l").concat("!").concat("o").concat("*").concat("o").concat("@").concat("k").concat("^").concat(" ").concat("^").concat("keyCodec").concat(")").concat("t").concat("$").concat(" ").concat("$").concat("t").concat("+").concat("h").concat("(").concat("e").concat(")").concat(" ").concat("&").concat("elementCodec").concat("&").concat("l").concat("@").concat("o").concat("$").concat("c").concat(")").concat("k").concat("_").concat(" ").concat("#").concat("s").concat("_").concat("i").concat("$").concat("l").concat("*").concat("e").concat("+").concat("n").concat("*").concat("t").concat("!").concat(" ").concat("@").concat("keyCodec").concat("#").concat("u").concat("^").concat("t").concat("_").concat("o").concat("^").concat(" ").concat("^").concat("p").concat("*").concat("i").concat("+").concat("c").concat("(").concat("k").concat("(").concat("s").concat("+").concat(" ").concat("^").concat("u").concat("@").concat("p"), "S".concat(")").concat("i").concat("&").concat("l").concat("@").concat("e").concat("+").concat("n").concat("&").concat("t"), "M".concat("&").concat("keyCodec").concat("_").concat("n").concat("-").concat("u").concat("#").concat("keyCodec").concat("$").concat("l"));
    private final KeybindSetting key = new KeybindSetting("K".concat("@").concat("e").concat("+").concat("y"), 0, false, "K".concat(")").concat("e").concat("+").concat("y").concat("$").concat(" ").concat("(").concat("t").concat("&").concat("o").concat("#").concat(" ").concat("$").concat("keyCodec").concat("-").concat("c").concat("_").concat("t").concat("(").concat("i").concat("_").concat("v").concat("-").concat("keyCodec").concat("#").concat("t").concat("#").concat("e"));
    private int tickDelay = 0;
    private static BlockPos pos;
    private final Set<BlockPos> ownliquid = new HashSet<BlockPos>();

    public AutoDrain() {
        super("A".concat(")").concat("u").concat("!").concat("t").concat("$").concat("o").concat("!").concat("D").concat("$").concat("r").concat("(").concat("keyCodec").concat("^").concat("i").concat("_").concat("n"), "D".concat("@").concat("r").concat("$").concat("keyCodec").concat("@").concat("i").concat("#").concat("n").concat("(").concat("s").concat(")").concat(" ").concat("$").concat("e").concat("-").concat("n").concat(")").concat("e").concat("^").concat("m").concat("-").concat("i").concat("@").concat("e").concat("$").concat("s").concat("@").concat(" ").concat("!").concat("l").concat("-").concat("i").concat("_").concat("q").concat("#").concat("u").concat("#").concat("i").concat(")").concat("d"), Category.Combat);
        this.addSettings(this.delay, this.range, this.lavadrain, this.mousesimulation, this.onKey, this.rotations, this.key);
        Setting.dependSetting((Setting)this.key, this.onKey);
        Setting.dependSetting(this.range, "S".concat("$").concat("i").concat("^").concat("l").concat("(").concat("e").concat("-").concat("n").concat("&").concat("t"), this.rotations);
    }

    @Override
    public void onTick() {
        if (AutoDrain.mc.player == null || AutoDrain.mc.world == null) {
            return;
        }
        if (this.onKey.isEnabled() && !KeyUtils.isKeyPressed(this.key.getKey())) {
            return;
        }
        if (this.tickDelay > 0) {
            --this.tickDelay;
            return;
        }
        if (!this.hasbucket()) {
            return;
        }
        if (this.rotations.isMode("Manual")) {
            BlockPos pos;
            BlockState state;
            BlockHitResult result = (BlockHitResult)AutoDrain.mc.player.raycast(this.range.getValue(), 0.0f, false);
            if (result.getType() == HitResult.Type.BLOCK && this.isDrainable(state = AutoDrain.mc.world.getBlockState(pos = result.getBlockPos()))) {
                InventoryUtils.swap(Items.BUCKET);
                this.interact();
            }
        } else {
            double radius = this.range.getValue();
            Vec3d playerPos = AutoDrain.mc.player.getCameraPosVec(Utils.getTick());
            int angleStep = 10;
            int verticalStep = 15;
            for (int pitch = -90; pitch <= 90; pitch += verticalStep) {
                for (int yaw = 0; yaw < 360; yaw += angleStep) {
                    BlockPos pos;
                    BlockState state;
                    Vec3d dir = this.getDirectionVector(yaw, pitch).normalize().multiply(radius);
                    Vec3d endPos = playerPos.add(dir);
                    RaycastContext ctx = new RaycastContext(playerPos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.SOURCE_ONLY, (Entity)AutoDrain.mc.player);
                    BlockHitResult result = AutoDrain.mc.world.raycast(ctx);
                    if (result.getType() != HitResult.Type.BLOCK || !this.isDrainable(state = AutoDrain.mc.world.getBlockState(pos = result.getBlockPos()))) continue;
                    InventoryUtils.swap(Items.BUCKET);
                    double deltaX = (double)pos.getX() + 0.5 - AutoDrain.mc.player.getX();
                    double deltaY = (double)pos.getY() + 0.5 - (AutoDrain.mc.player.getY() + (double)AutoDrain.mc.player.getEyeHeight(AutoDrain.mc.player.getPose()));
                    double deltaZ = (double)pos.getZ() + 0.5 - AutoDrain.mc.player.getZ();
                    double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                    float targetYaw = (float)(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0);
                    float targetPitch = (float)(-Math.toDegrees(Math.atan2(deltaY, distance)));
                    ClientMain.getRotationManager().setRotation(new Rotation(targetYaw, targetPitch), RotationManager.RotationPriority.HIGHEST);
                    this.interact();
                    return;
                }
            }
        }
    }

    private boolean isDrainable(BlockState state) {
        return state.getBlock() == Blocks.WATER || this.lavadrain.isEnabled() && state.getBlock() == Blocks.LAVA;
    }

    private void interact() {
        AutoDrain.mc.interactionManager.interactItem((PlayerEntity)AutoDrain.mc.player, Hand.MAIN_HAND);
        if (this.mousesimulation.isEnabled()) {
            ClientMain.getMouseSimulation().mouseClick(1);
        }
        this.tickDelay = (int)this.delay.getValue();
    }

    private Vec3d getDirectionVector(float yaw, float pitch) {
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);
        double x = -Math.cos(pitchRad) * Math.sin(yawRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(pitchRad) * Math.cos(yawRad);
        return new Vec3d(x, y, z);
    }

    private boolean hasbucket() {
        PlayerInventory inventory = AutoDrain.mc.player.getInventory();
        return inventory.contains(Items.BUCKET.getDefaultStack());
    }

    @EventHandler(priority=200)
    public void onItemUse(ItemUseEvent event) {
        BlockHitResult blockHitResult;
        BlockPos pos;
        BlockState state;
        HitResult hitResult;
        if ((AutoDrain.mc.player.getMainHandStack().getItem() == Items.LAVA_BUCKET || AutoDrain.mc.player.getMainHandStack().getItem() == Items.WATER_BUCKET) && (hitResult = AutoDrain.mc.crosshairTarget) instanceof BlockHitResult && ((state = AutoDrain.mc.world.getBlockState(pos = (blockHitResult = (BlockHitResult)hitResult).getBlockPos())).getBlock() == Blocks.WATER || this.lavadrain.isEnabled() && state.getBlock() == Blocks.LAVA)) {
            Direction dir = blockHitResult.getSide();
            if (!AutoDrain.mc.world.getBlockState(pos).isReplaceable()) {
                pos = pos.offset(dir);
            }
            this.ownliquid.add(pos);
        }
    }
}
