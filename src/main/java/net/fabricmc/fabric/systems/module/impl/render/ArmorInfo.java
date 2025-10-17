package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class ArmorInfo
extends Module {
    public ArmorInfo() {
        super("A".concat("*").concat("r").concat("^").concat("m").concat("+").concat("o").concat("*").concat("r").concat("(").concat("I").concat("*").concat("n").concat("@").concat("f").concat(")").concat("o"), "S".concat(")").concat("h").concat(")").concat("o").concat("!").concat("w").concat("$").concat("s").concat("-").concat(" ").concat("-").concat("e").concat("(").concat("n").concat("^").concat("e").concat("+").concat("m").concat(")").concat("i").concat("&").concat("e").concat("+").concat("s").concat("_").concat(" ").concat("@").concat("keyCodec").concat("!").concat("r").concat("!").concat("m").concat("*").concat("o").concat("^").concat("r").concat("*").concat(" ").concat("$").concat("d").concat("$").concat("u").concat("&").concat("r").concat("$").concat("keyCodec").concat("*").concat("elementCodec").concat("+").concat("i").concat("(").concat("l").concat("@").concat("i").concat("(").concat("t").concat("*").concat("y"), Category.Render);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (ArmorInfo.mc.world != null && this.isEnabled()) {
            for (Entity entity : ArmorInfo.mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity) || entity == ArmorInfo.mc.player) continue;
                PlayerEntity player = (PlayerEntity)entity;
                this.renderArmorBox(player, matrices, EquipmentSlot.HEAD);
                this.renderArmorBox(player, matrices, EquipmentSlot.CHEST);
                this.renderArmorBox(player, matrices, EquipmentSlot.LEGS);
                this.renderArmorBox(player, matrices, EquipmentSlot.FEET);
            }
        }
    }

    private void renderArmorBox(PlayerEntity player, MatrixStack matrices, EquipmentSlot slot) {
        ItemStack armorPiece = player.getEquippedStack(slot);
        if (!armorPiece.isEmpty() && armorPiece.getItem() instanceof ArmorItem) {
            float durability = (float)(armorPiece.getMaxDamage() - armorPiece.getDamage()) / (float)armorPiece.getMaxDamage();
            Color durabilityColor = this.getDurabilityColor(durability);
            Vec3d position = this.getArmorPosition(player, slot);
            float boxWidth = 0.1f;
            float boxHeight = 0.15f;
            Render3DEngine.drawBoxWithParams(position, ColorUtil.addAlpha(durabilityColor, 100), matrices, boxWidth, boxHeight);
        }
    }

    private Vec3d getArmorPosition(PlayerEntity player, EquipmentSlot slot) {
        switch (slot) {
            case HEAD: {
                return player.getLerpedPos(Utils.getTick()).add(0.0, (double)player.getHeight() - 0.3, 0.0);
            }
            case CHEST: {
                return player.getLerpedPos(Utils.getTick()).add(0.0, (double)player.getHeight() * 0.6, 0.0);
            }
            case LEGS: {
                return player.getLerpedPos(Utils.getTick()).add(0.0, (double)player.getHeight() * 0.3, 0.0);
            }
            case FEET: {
                return player.getLerpedPos(Utils.getTick()).add(0.0, 0.01, 0.0);
            }
        }
        return player.getLerpedPos(Utils.getTick());
    }

    private Color getDurabilityColor(float durability) {
        if (durability > 0.75f) {
            return Color.GREEN;
        }
        if (durability > 0.5f) {
            return Color.YELLOW;
        }
        if (durability > 0.25f) {
            return Color.ORANGE;
        }
        return Color.RED;
    }
}
