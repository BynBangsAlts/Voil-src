package net.fabricmc.fabric.systems.module.impl.render;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4d;

public class NameTags
extends Module {
    public static BooleanSetting RenderItems = new BooleanSetting("RenderItems", false, "Renders nametags with items");

    public NameTags() {
        super("N".concat("$").concat("keyCodec").concat("(").concat("m").concat("_").concat("e").concat("#").concat("T").concat("_").concat("keyCodec").concat("^").concat("g").concat("&").concat("s"), "R".concat("(").concat("e").concat("+").concat("n").concat("!").concat("d").concat("_").concat("e").concat("@").concat("r").concat("_").concat("s").concat("&").concat(" ").concat("_").concat("n").concat("&").concat("keyCodec").concat("!").concat("m").concat("+").concat("e").concat("+").concat("t").concat("_").concat("keyCodec").concat(")").concat("g").concat("$").concat("s"), Category.Render);
        this.addSettings(RenderItems);
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (NameTags.mc.world == null || NameTags.mc.player == null) {
            return;
        }
        for (PlayerEntity player : NameTags.mc.world.getPlayers()) {
            if (player == NameTags.mc.player) continue;
            Vec3d[] vectors = NameTags.getVectors((Entity)player);
            Vector4d position = null;
            for (Vec3d vector : vectors) {
                Vec3d screenPos = WorldUtils.worldToScreen(vector);
                if (!(screenPos.z > 0.0) || !(screenPos.z < 1.0)) continue;
                if (position == null) {
                    position = new Vector4d(screenPos.x, screenPos.y, screenPos.x, screenPos.y);
                    continue;
                }
                position.x = Math.min(screenPos.x, position.x);
                position.y = Math.min(screenPos.y, position.y);
                position.z = Math.max(screenPos.x, position.z);
                position.w = Math.max(screenPos.y, position.w);
            }
            if (position == null) continue;
            String name = player.getName().getString();
            double distance = NameTags.mc.player.squaredDistanceTo((Entity)player);
            float actualDistance = (float)Math.sqrt(distance);
            String distanceText = String.format("%.1fm", Float.valueOf(actualDistance));
            float tagWidth = ClientMain.fontRenderer.getWidth(name) + 6.0f;
            float distanceWidth = ClientMain.fontRenderer.getWidth(distanceText) + 6.0f;
            float tagHeight = 14.0f;
            float textHeight = ClientMain.fontRenderer.getFont().getSize2D();
            float centerX = (float)((position.x + position.z) / 2.0);
            float x = centerX - (tagWidth + distanceWidth + 4.0f) / 2.0f;
            float y = (float)position.y - tagHeight - 2.0f;
            Render2DEngine.drawRoundedBlur(matrices, x, y, tagWidth, tagHeight, 6.0f, 6.0f, 0.0f, true);
            float distanceX = x + tagWidth + 4.0f;
            Render2DEngine.drawRoundedBlur(matrices, distanceX, y, distanceWidth, tagHeight, 6.0f, 6.0f, 0.0f, true);
            float textY = y + (tagHeight - textHeight) / 2.0f;
            ClientMain.fontRenderer.draw(matrices, name, x + 3.0f, textY - 2.0f, -1);
            ClientMain.fontRenderer.draw(matrices, distanceText, distanceX + 3.0f, textY - 2.0f, -5592406);
            if (!RenderItems.isEnabled()) continue;
            float itemScale = 0.75f;
            float itemX = centerX - 22.5f;
            float itemY = y - 16.0f;
            Render2DEngine.renderItem(matrices, player.getMainHandStack(), itemX, itemY, itemScale, true);
            Render2DEngine.renderItem(matrices, player.getOffHandStack(), itemX += 18.0f, itemY, itemScale, true);
            itemX += 18.0f;
            for (int i = 3; i >= 0; --i) {
                ItemStack armorPiece = (ItemStack)player.getInventory().armor.get(i);
                if (armorPiece.isEmpty()) continue;
                Render2DEngine.renderItem(matrices, armorPiece, itemX, itemY, itemScale, true);
                itemX += 18.0f;
            }
        }
    }

    @NotNull
    private static Vec3d[] getVectors(@NotNull Entity ent) {
        double x = ent.prevX + (ent.getX() - ent.prevX) * (double)Utils.getTick();
        double y = ent.prevY + (ent.getY() - ent.prevY) * (double)Utils.getTick();
        double z = ent.prevZ + (ent.getZ() - ent.prevZ) * (double)Utils.getTick();
        Box box = ent.getBoundingBox().offset(-ent.getX(), -ent.getY(), -ent.getZ()).offset(x, y, z);
        return new Vec3d[]{new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.minX, box.maxY, box.minZ), new Vec3d(box.maxX, box.minY, box.minZ), new Vec3d(box.maxX, box.maxY, box.minZ), new Vec3d(box.minX, box.minY, box.maxZ), new Vec3d(box.minX, box.maxY, box.maxZ), new Vec3d(box.maxX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.maxZ)};
    }
}
