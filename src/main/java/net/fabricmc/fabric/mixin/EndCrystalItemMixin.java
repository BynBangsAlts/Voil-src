package net.fabricmc.fabric.mixin;

import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EndCrystalItem.class})
public class EndCrystalItemMixin {
    @Unique
    private Vec3d getPlayerLookVec(PlayerEntity p) {
        float f = (float)Math.PI / 180;
        float pi = (float)Math.PI;
        float f1 = MathHelper.cos((float)(-p.getYaw() * f - pi));
        float f2 = MathHelper.sin((float)(-p.getYaw() * f - pi));
        float f3 = -MathHelper.cos((float)(-p.getPitch() * f));
        float f4 = MathHelper.sin((float)(-p.getPitch() * f));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f1 * f3)).normalize();
    }

    @Unique
    private Vec3d getClientLookVec() {
        assert (ClientMain.mc.player != null);
        return this.getPlayerLookVec((PlayerEntity)ClientMain.mc.player);
    }

    @Unique
    private boolean isBlock(Block b, BlockPos p) {
        return this.getBlockState(p).getBlock() == b;
    }

    @Unique
    private BlockState getBlockState(BlockPos p) {
        return ClientMain.mc.world.getBlockState(p);
    }

    @Unique
    private boolean canPlaceCrystalServer(BlockPos blockPos) {
        BlockState blockState = ClientMain.mc.world.getBlockState(blockPos);
        if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK)) {
            return false;
        }
        BlockPos blockPos2 = blockPos.up();
        if (!ClientMain.mc.world.isAir(blockPos2)) {
            return false;
        }
        double d = blockPos2.getX();
        double e = blockPos2.getY();
        double f = blockPos2.getZ();
        List list = ClientMain.mc.world.getOtherEntities(null, new Box(d, e, f, d + 1.0, e + 2.0, f + 1.0));
        return list.isEmpty();
    }

    @Inject(method={"useOnBlock"}, at={@At(value="HEAD")})
    private void onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockHitResult blockHit2;
        BlockPos pos;
        HitResult hitResult;
        Vec3d e;
        BlockHitResult blockHit;
        ItemStack mainHandStack;
        if (AutoCrystal.nobounce.isEnabled() && ClientMain.mc.player != null && (mainHandStack = ClientMain.mc.player.getMainHandStack()).isOf(Items.END_CRYSTAL) && (this.isBlock(Blocks.OBSIDIAN, (blockHit = ClientMain.mc.world.raycast(new RaycastContext(e = ClientMain.mc.player.getEyePos(), e.add(this.getClientLookVec().multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)ClientMain.mc.player))).getBlockPos()) || this.isBlock(Blocks.BEDROCK, blockHit.getBlockPos())) && (hitResult = ClientMain.mc.crosshairTarget) instanceof BlockHitResult && this.canPlaceCrystalServer(pos = (blockHit2 = (BlockHitResult)hitResult).getBlockPos())) {
            context.getStack().decrement(-1);
        }
    }
}
