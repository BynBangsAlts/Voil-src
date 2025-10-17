package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class HitCircle
extends Module {
    private PlayerEntity target;
    private long startTime;
    ColorPickerSetting color = new ColorPickerSetting("C".concat("(").concat("o").concat(")").concat("l").concat("*").concat("o").concat("!").concat("r"), new Color(255, 0, 0, 255), "C".concat("-").concat("o").concat("+").concat("l").concat("#").concat("o").concat("&").concat("r").concat("(").concat(" ").concat("-").concat("o").concat("@").concat("f").concat("_").concat(" ").concat("^").concat("t").concat("*").concat("h").concat(")").concat("e").concat("_").concat(" ").concat(")").concat("c").concat("_").concat("i").concat("^").concat("r").concat("$").concat("c").concat("+").concat("l").concat("$").concat("e"));
    NumberSetting radius = new NumberSetting("R".concat("$").concat("keyCodec").concat("!").concat("d").concat("_").concat("i").concat("&").concat("u").concat("^").concat("s"), 0.5, 2.0, 1.0, 0.1, "R".concat("$").concat("keyCodec").concat("-").concat("d").concat("@").concat("i").concat("*").concat("u").concat("#").concat("s").concat("_").concat(" ").concat("&").concat("o").concat("!").concat("f").concat("&").concat(" ").concat("!").concat("t").concat("!").concat("h").concat("&").concat("e").concat("-").concat(" ").concat("^").concat("c").concat("&").concat("i").concat("!").concat("r").concat("+").concat("c").concat("*").concat("l").concat("#").concat("e"));
    NumberSetting speed = new NumberSetting("S".concat("_").concat("p").concat(")").concat("e").concat("^").concat("e").concat("@").concat("d"), 0.5, 2.0, 0.5, 0.1, "S".concat("#").concat("p").concat(")").concat("e").concat(")").concat("e").concat("-").concat("d").concat(")").concat(" ").concat("$").concat("o").concat("+").concat("f").concat("!").concat(" ").concat("+").concat("t").concat("(").concat("h").concat("!").concat("e").concat("-").concat(" ").concat("-").concat("c").concat("_").concat("i").concat("^").concat("r").concat("_").concat("c").concat("_").concat("l").concat(")").concat("e"));
    NumberSetting segments = new NumberSetting("S".concat("!").concat("e").concat("*").concat("g").concat("&").concat("m").concat("!").concat("e").concat("#").concat("n").concat(")").concat("t").concat("#").concat("s"), 3.0, 128.0, 3.0, 1.0, "N".concat("(").concat("u").concat("#").concat("m").concat("_").concat("elementCodec").concat(")").concat("e").concat("$").concat("r").concat("^").concat(" ").concat(")").concat("o").concat("@").concat("f").concat(")").concat(" ").concat("*").concat("s").concat("^").concat("e").concat(")").concat("g").concat(")").concat("m").concat("@").concat("e").concat("#").concat("n").concat("^").concat("t").concat("$").concat("s").concat(")").concat(" ").concat(")").concat("o").concat(")").concat("f").concat("^").concat(" ").concat("_").concat("t").concat("(").concat("h").concat("$").concat("e").concat("&").concat(" ").concat("@").concat("c").concat("_").concat("i").concat("#").concat("r").concat("_").concat("c").concat("+").concat("l").concat("^").concat("e"));
    BooleanSetting shade = new BooleanSetting("S".concat("&").concat("h").concat("&").concat("keyCodec").concat("+").concat("d").concat("_").concat("e"), true, "S".concat("^").concat("h").concat("$").concat("keyCodec").concat("^").concat("d").concat("*").concat("e").concat("*").concat(" ").concat("@").concat("t").concat("(").concat("h").concat("@").concat("e").concat("$").concat(" ").concat("#").concat("c").concat("*").concat("i").concat("+").concat("r").concat("(").concat("c").concat("+").concat("l").concat(")").concat("e").concat("@").concat(" ").concat("@").concat("w").concat("$").concat("h").concat("(").concat("e").concat("$").concat("n").concat("_").concat(" ").concat("-").concat("g").concat("#").concat("o").concat("#").concat("i").concat("&").concat("n").concat("+").concat("g").concat(")").concat(" ").concat("+").concat("u").concat("&").concat("p").concat("$").concat(" ").concat("&").concat("keyCodec").concat("(").concat("n").concat("(").concat("d").concat("^").concat(" ").concat("(").concat("d").concat("_").concat("o").concat("@").concat("w").concat("(").concat("n"));
    BooleanSetting multiTarget = new BooleanSetting("M".concat("@").concat("u").concat("@").concat("l").concat("^").concat("t").concat("_").concat("i").concat("(").concat("T").concat("!").concat("keyCodec").concat("@").concat("r").concat("^").concat("g").concat("$").concat("e").concat("!").concat("t"), false, "A".concat("#").concat("l").concat("&").concat("l").concat(")").concat("o").concat("#").concat("w").concat("!").concat("s").concat("&").concat(" ").concat(")").concat("y").concat("-").concat("o").concat(")").concat("u").concat("^").concat(" ").concat("-").concat("t").concat("(").concat("o").concat("(").concat(" ").concat(")").concat("s").concat("$").concat("e").concat("_").concat("l").concat("!").concat("e").concat("$").concat("c").concat("*").concat("t").concat("#").concat(" ").concat("_").concat("m").concat("+").concat("u").concat("+").concat("l").concat("^").concat("t").concat("#").concat("i").concat(")").concat("p").concat(")").concat("l").concat("+").concat("e").concat("-").concat(" ").concat("_").concat("t").concat("$").concat("keyCodec").concat("@").concat("r").concat("!").concat("g").concat("@").concat("e").concat("+").concat("t").concat("*").concat("s"));
    private List<PlayerEntity> targets = new ArrayList<PlayerEntity>();

    public HitCircle() {
        super("H".concat(")").concat("i").concat(")").concat("t").concat("^").concat("C").concat("^").concat("i").concat("(").concat("r").concat("&").concat("c").concat("*").concat("l").concat(")").concat("e"), "H".concat("$").concat("i").concat("&").concat("g").concat("(").concat("h").concat("-").concat("l").concat("!").concat("i").concat("_").concat("g").concat("!").concat("h").concat("_").concat("t").concat("$").concat("s").concat("_").concat(" ").concat("-").concat("y").concat("$").concat("o").concat("#").concat("u").concat("#").concat("r").concat("(").concat(" ").concat("#").concat("h").concat("-").concat("i").concat(")").concat("t").concat(")").concat(" ").concat("-").concat("c").concat("-").concat("i").concat(")").concat("r").concat("@").concat("c").concat("(").concat("l").concat(")").concat("e"), Category.Render);
        this.addSettings(this.radius, this.speed, this.segments, this.color);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.targets.isEmpty()) {
            return;
        }
        double time = (double)(System.currentTimeMillis() % 10000L) / 1000.0;
        for (PlayerEntity target : this.targets) {
            if (target == null || target.isRemoved() || !target.isAlive()) continue;
            float oscillationAmplitude = target.getHeight() / 2.0f;
            Vec3d targetPos = target.getLerpedPos(Utils.getTick());
            Vec3d lastPos = null;
            double yOffset = Math.sin(time * Math.PI * (double)this.speed.getFloatValue()) * (double)oscillationAmplitude;
            for (int i = 0; i <= this.segments.getIValue(); ++i) {
                double angle = Math.PI * 2 * (double)i / (double)this.segments.getIValue();
                double x = targetPos.x + (double)this.radius.getFloatValue() * Math.cos(angle);
                double z = targetPos.z + (double)this.radius.getFloatValue() * Math.sin(angle);
                Vec3d pos = new Vec3d(x, targetPos.y + 1.0 + yOffset, z);
                if (lastPos != null) {
                    Render3DEngine.drawLine(lastPos, pos, this.color.getColor(), matrices);
                }
                lastPos = pos;
            }
        }
    }

    @EventHandler
    public void onAttack(AttackEntityEvent event) {
        Entity entity = event.getTarget();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (this.multiTarget.isEnabled()) {
                if (!this.targets.contains(player)) {
                    this.targets.add(player);
                } else {
                    this.targets.remove(player);
                }
            } else {
                this.targets.clear();
                this.targets.add(player);
            }
            this.startTime = System.currentTimeMillis();
        }
    }
}
