package net.fabricmc.fabric.systems.module.impl.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;

public class Resolver
extends Module {
    public static final Map<PlayerEntity, Float> healthMap = new HashMap<PlayerEntity, Float>();
    private final Map<PlayerEntity, List<PositionData>> positionHistory = new HashMap<PlayerEntity, List<PositionData>>();
    BooleanSetting resolvePos = new BooleanSetting("R".concat("#").concat("e").concat("!").concat("s").concat("-").concat("o").concat("+").concat("l").concat("-").concat("v").concat("$").concat("e").concat("_").concat(" ").concat("!").concat("P").concat(")").concat("o").concat("^").concat("s").concat("#").concat("i").concat("^").concat("t").concat("^").concat("i").concat("!").concat("o").concat("@").concat("n"), true, "R".concat("#").concat("e").concat("-").concat("s").concat("_").concat("o").concat("*").concat("l").concat("(").concat("v").concat("-").concat("e").concat("+").concat("s").concat("(").concat(" ").concat("^").concat("p").concat("-").concat("o").concat("#").concat("s").concat("$").concat("i").concat(")").concat("t").concat("#").concat("i").concat("^").concat("o").concat("$").concat("n").concat("+").concat(" ").concat(")").concat("o").concat("^").concat("f").concat("-").concat(" ").concat("&").concat("e").concat(")").concat("n").concat("(").concat("e").concat("*").concat("m").concat("-").concat("y"));
    BooleanSetting resolvePotions = new BooleanSetting("R".concat("(").concat("e").concat("_").concat("s").concat("(").concat("o").concat("!").concat("l").concat("!").concat("v").concat("#").concat("e").concat("!").concat(" ").concat("#").concat("P").concat("_").concat("o").concat("#").concat("t").concat("(").concat("i").concat("_").concat("o").concat("(").concat("n").concat(")").concat("s"), false, "S".concat("*").concat("h").concat("*").concat("o").concat("@").concat("w").concat("_").concat("s").concat("#").concat(" ").concat("&").concat("p").concat("@").concat("o").concat("_").concat("t").concat("!").concat("i").concat("@").concat("o").concat(")").concat("n").concat("(").concat("s").concat("&").concat(" ").concat("!").concat("o").concat("$").concat("f").concat("&").concat(" ").concat("@").concat("e").concat("^").concat("n").concat("*").concat("e").concat("_").concat("m").concat("_").concat("y"));

    public Resolver() {
        super("R".concat("^").concat("e").concat("^").concat("s").concat("&").concat("o").concat("(").concat("l").concat("_").concat("v").concat("@").concat("e").concat(")").concat("r"), "A".concat("+").concat("u").concat("$").concat("t").concat("-").concat("o").concat("&").concat("m").concat("*").concat("keyCodec").concat("!").concat("t").concat("#").concat("i").concat("^").concat("c").concat("^").concat("keyCodec").concat("#").concat("l").concat("!").concat("l").concat("*").concat("y").concat("*").concat(" ").concat("-").concat("r").concat("$").concat("e").concat("^").concat("s").concat("*").concat("o").concat("$").concat("l").concat("_").concat("v").concat("@").concat("e").concat("-").concat("s").concat("-").concat(" ").concat("^").concat("c").concat("^").concat("e").concat("(").concat("r").concat("-").concat("t").concat(")").concat("keyCodec").concat("(").concat("i").concat("@").concat("n").concat("$").concat(" ").concat("*").concat("s").concat(")").concat("t").concat("&").concat("u").concat("@").concat("f").concat("-").concat("f"), Category.Miscellaneous);
        this.addSettings(this.resolvePos, this.resolvePotions);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        healthMap.clear();
        this.positionHistory.clear();
    }

    @Override
    public void onTick() {
        for (PlayerEntity player : Resolver.mc.world.getPlayers()) {
            this.updatePlayerPosition(player);
        }
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        for (PlayerEntity player : Resolver.mc.world.getPlayers()) {
            PositionData predictedPos = this.predictPosition(player);
            if (this.resolvePos.isEnabled() && predictedPos != null && player != Resolver.mc.player && !SocialManager.isBot(player)) {
                Vec3d pos = new Vec3d(predictedPos.x, predictedPos.y, predictedPos.z);
                Render3DEngine.drawBox(pos, ColorUtil.addAlpha(Color.white, 100), matrices);
            }
            if (!this.resolvePotions.isEnabled() || player == Resolver.mc.player) continue;
            this.resolvePots(player, matrices);
        }
    }

    private void resolvePots(PlayerEntity player, MatrixStack matrices) {
        Vec3d pos = new Vec3d(player.getX(), player.getY() + (double)player.getHeight() + 0.2, player.getZ());
        double offsetX = 0.3;
        double offsetY = 0.3;
        for (StatusEffectInstance effect : player.getStatusEffects()) {
            StatusEffectSpriteManager spriteManager;
            Sprite sprite;
            StatusEffect statusEffect = (StatusEffect)effect.getEffectType();
            if (statusEffect == null || (sprite = (spriteManager = mc.getStatusEffectSpriteManager()).getSprite((RegistryEntry)statusEffect)) == null) continue;
            int x = (int)(pos.x + offsetX);
            int y = (int)(pos.y + offsetY);
            int z = (int)pos.z;
            int width = 20;
            int height = 20;
            RenderHelper.getContext().drawSprite(x, y, z, width, height, sprite);
            offsetX += 0.25;
        }
    }

    public PositionData predictPosition(PlayerEntity player) {
        List<PositionData> history = this.positionHistory.get(player);
        if (history == null || history.size() < 2) {
            return null;
        }
        PositionData last = history.get(history.size() - 1);
        PositionData secondLast = history.get(history.size() - 2);
        double deltaX = last.x - secondLast.x;
        double deltaY = last.y - secondLast.y;
        double deltaZ = last.z - secondLast.z;
        long deltaTime = last.timestamp - secondLast.timestamp;
        double predictedX = last.x + deltaX / (double)deltaTime * 180.0;
        double predictedY = last.y + deltaY / (double)deltaTime * 180.0;
        double predictedZ = last.z + deltaZ / (double)deltaTime * 180.0;
        return new PositionData(predictedX, predictedY, predictedZ, System.currentTimeMillis());
    }

    public void updatePlayerPosition(PlayerEntity player) {
        this.positionHistory.computeIfAbsent(player, k -> new ArrayList()).add(new PositionData(player.getX(), player.getY(), player.getZ(), System.currentTimeMillis()));
        List<PositionData> history = this.positionHistory.get(player);
        if (history.size() > 60) {
            history.remove(0);
        }
    }

    private static class PositionData {
        public final double x;
        public final double y;
        public final double z;
        public final long timestamp;

        public PositionData(double x, double y, double z, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }
    }
}
