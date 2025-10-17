package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class Trajectories
extends Module {
    ColorPickerSetting color = new ColorPickerSetting("C".concat("_").concat("o").concat("_").concat("l").concat("_").concat("o").concat("-").concat("r"), new Color(255, 255, 255), "C".concat("-").concat("o").concat("@").concat("l").concat("!").concat("o").concat("&").concat("r").concat("^").concat(" ").concat("-").concat("o").concat("!").concat("f").concat("#").concat(" ").concat("(").concat("t").concat("_").concat("h").concat("&").concat("e").concat("(").concat(" ").concat("!").concat("t").concat("!").concat("r").concat("#").concat("keyCodec").concat("!").concat("j").concat("-").concat("e").concat("&").concat("c").concat("@").concat("t").concat("*").concat("o").concat("@").concat("r").concat("-").concat("y").concat("_").concat(" ").concat("(").concat("l").concat("!").concat("i").concat("@").concat("n").concat("&").concat("e"));

    public Trajectories() {
        super("T".concat(")").concat("r").concat("+").concat("keyCodec").concat("$").concat("j").concat("$").concat("e").concat("+").concat("c").concat("(").concat("t").concat("@").concat("o").concat("(").concat("r").concat("(").concat("i").concat("-").concat("e").concat("$").concat("s"), "D".concat(")").concat("r").concat("&").concat("keyCodec").concat("*").concat("w").concat(")").concat("s").concat("$").concat(" ").concat("-").concat("w").concat(")").concat("h").concat("@").concat("e").concat("!").concat("r").concat("@").concat("e").concat("!").concat(" ").concat("$").concat("t").concat("(").concat("r").concat("_").concat("keyCodec").concat("$").concat("j").concat("&").concat("e").concat("*").concat("c").concat("@").concat("t").concat("$").concat("o").concat(")").concat("r").concat(")").concat("i").concat("-").concat("e").concat("^").concat("s").concat("_").concat(" ").concat(")").concat("g").concat("$").concat("o"), Category.Render);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Color lineColor;
        if (Trajectories.mc.player == null || Trajectories.mc.world == null) {
            return;
        }
        boolean hitEntity = false;
        Vec3d end = Trajectories.calcTrajectory(Trajectories.mc.player.getMainHandStack().getItem().getDefaultStack(), Trajectories.mc.player.getYaw(), Trajectories.mc.player.getPitch(), hitEntity);
        Vec3d start = Trajectories.mc.player.getPos();
        Color color = lineColor = hitEntity ? Color.GREEN : this.color.getColor();
        if (end != null) {
            Render3DEngine.drawLine(start, end, lineColor, matrices);
            Render3DEngine.drawBoxWithParams(end, ColorUtil.addAlpha(lineColor, 125), matrices, 0.3f, 0.5f);
        }
    }

    public static Vec3d calcTrajectory(ItemStack itemStack, float yaw, float pitch, boolean hitEntity) {
        float drag;
        float gravity;
        float initialSpeed;
        double x = Render2DEngine.interpolate(Trajectories.mc.player.prevX, Trajectories.mc.player.getX(), Utils.getTick());
        double y = Render2DEngine.interpolate(Trajectories.mc.player.prevY, Trajectories.mc.player.getY(), Utils.getTick());
        double z = Render2DEngine.interpolate(Trajectories.mc.player.prevZ, Trajectories.mc.player.getZ(), Utils.getTick());
        y += (double)Trajectories.mc.player.getEyeHeight(Trajectories.mc.player.getPose()) - 0.1;
        double motionX = -MathHelper.sin((float)(yaw * (float)Math.PI / 180.0f)) * MathHelper.cos((float)(pitch * (float)Math.PI / 180.0f));
        double motionY = -MathHelper.sin((float)(pitch * (float)Math.PI / 180.0f));
        double motionZ = MathHelper.cos((float)(yaw * (float)Math.PI / 180.0f)) * MathHelper.cos((float)(pitch * (float)Math.PI / 180.0f));
        if (itemStack.getItem() instanceof BowItem) {
            float power = (float)Trajectories.mc.player.getItemUseTime() / 20.0f;
            if ((power = (power * power + power * 2.0f) / 3.0f) > 1.0f) {
                power = 1.0f;
            }
            initialSpeed = power * 3.0f;
            gravity = 0.05f;
            drag = 0.99f;
        } else if (itemStack.getItem() instanceof CrossbowItem) {
            initialSpeed = 3.15f;
            gravity = 0.05f;
            drag = 0.99f;
        } else if (itemStack.getItem() instanceof SnowballItem || itemStack.getItem() instanceof EggItem || itemStack.getItem() instanceof EnderPearlItem) {
            initialSpeed = 1.5f;
            gravity = 0.03f;
            drag = 0.99f;
        } else {
            return null;
        }
        float norm = MathHelper.sqrt((float)((float)(motionX * motionX + motionY * motionY + motionZ * motionZ)));
        motionX = motionX / (double)norm * (double)initialSpeed;
        motionY = motionY / (double)norm * (double)initialSpeed;
        motionZ = motionZ / (double)norm * (double)initialSpeed;
        for (int i = 0; i < 300; ++i) {
            Vec3d lastPos = new Vec3d(x, y, z);
            motionX *= (double)drag;
            motionY *= (double)drag;
            Vec3d pos = new Vec3d(x += motionX, y += (motionY -= (double)gravity), z += (motionZ *= (double)drag));
            EntityHitResult entityHit = ProjectileUtil.getEntityCollision((World)Trajectories.mc.world, (Entity)Trajectories.mc.player, (Vec3d)lastPos, (Vec3d)pos, (Box)Trajectories.mc.player.getBoundingBox().expand(1.0), entity -> !entity.isSpectator() && entity instanceof LivingEntity);
            if (entityHit != null) {
                hitEntity = true;
                return entityHit.getPos();
            }
            BlockHitResult bhr = Trajectories.mc.world.raycast(new RaycastContext(lastPos, pos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)Trajectories.mc.player));
            if (bhr != null && bhr.getType() == HitResult.Type.BLOCK) {
                return bhr.getPos();
            }
            if (y <= -65.0) break;
        }
        return null;
    }
}
