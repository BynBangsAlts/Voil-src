package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class JumpCircle
extends Module {
    private final List<Circle> circles = new ArrayList<Circle>();
    private final NumberSetting radius = new NumberSetting("R".concat("!").concat("keyCodec").concat("#").concat("d").concat("(").concat("i").concat("!").concat("u").concat("@").concat("s"), 0.5, 2.0, 1.0, 0.1, "R".concat("+").concat("keyCodec").concat("&").concat("d").concat("+").concat("i").concat("!").concat("u").concat("@").concat("s").concat("+").concat(" ").concat("-").concat("o").concat("!").concat("f").concat("*").concat(" ").concat("$").concat("t").concat("!").concat("h").concat("-").concat("e").concat("^").concat(" ").concat(")").concat("c").concat(")").concat("i").concat("&").concat("r").concat("$").concat("c").concat("$").concat("l").concat("(").concat("e"));
    private final ColorPickerSetting color = new ColorPickerSetting("C".concat("(").concat("o").concat("@").concat("l").concat("-").concat("o").concat(")").concat("r"), new Color(109, 9, 164, 255), "C".concat("&").concat("o").concat("@").concat("l").concat("*").concat("o").concat(")").concat("r").concat("_").concat(" ").concat("^").concat("o").concat("(").concat("f").concat("-").concat(" ").concat("!").concat("t").concat("-").concat("h").concat(")").concat("e").concat("#").concat(" ").concat("-").concat("c").concat("_").concat("i").concat("_").concat("r").concat("$").concat("c").concat("@").concat("l").concat("*").concat("e"));

    public JumpCircle() {
        super("J".concat("!").concat("u").concat("^").concat("m").concat("&").concat("p").concat("@").concat("C").concat(")").concat("i").concat("*").concat("r").concat("$").concat("c").concat("#").concat("l").concat("-").concat("e"), "S".concat("*").concat("h").concat("_").concat("o").concat("#").concat("w").concat("(").concat("s").concat(")").concat(" ").concat("+").concat("keyCodec").concat("+").concat(" ").concat(")").concat("c").concat("&").concat("i").concat("-").concat("r").concat("*").concat("c").concat(")").concat("l").concat("&").concat("e").concat("!").concat(" ").concat(")").concat("w").concat("^").concat("h").concat("&").concat("e").concat(")").concat("n").concat("@").concat(" ").concat(")").concat("y").concat("*").concat("o").concat("!").concat("u").concat("@").concat(" ").concat("+").concat("j").concat("_").concat("u").concat("$").concat("m").concat("@").concat("p"), Category.Render);
        this.addSettings(this.radius, this.color);
    }

    @Override
    public void onTick() {
        if (JumpCircle.mc.player == null) {
            return;
        }
        boolean onGround = JumpCircle.mc.player.isOnGround();
        if (JumpCircle.mc.options.jumpKey.isPressed()) {
            this.circles.add(new Circle(JumpCircle.mc.player.getPos(), System.currentTimeMillis()));
        }
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (JumpCircle.mc.player == null) {
            return;
        }
        long now = System.currentTimeMillis();
        Iterator<Circle> iterator = this.circles.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            long elapsed = now - circle.startTime();
            if (elapsed > 1000L) {
                iterator.remove();
                continue;
            }
            float progress = (float)elapsed / 1000.0f;
            float alpha = 1.0f - progress;
            float scale = 1.0f + (1.0f - progress);
            this.drawCircle(circle.pos(), scale, alpha, matrices);
        }
    }

    private void drawCircle(Vec3d center, float scale, float alpha, MatrixStack matrices) {
        Vec3d lastPos = null;
        float baseRadius = this.radius.getFloatValue() * scale;
        Color baseColor = this.color.getColor();
        Color fadedColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)((float)baseColor.getAlpha() * alpha));
        for (int i = 0; i <= 128; ++i) {
            double angle = Math.PI * 2 * (double)i / 128.0;
            double x = center.x + (double)baseRadius * Math.cos(angle);
            double z = center.z + (double)baseRadius * Math.sin(angle);
            Vec3d pos = new Vec3d(x, center.y - 1.0, z);
            if (lastPos != null) {
                Render3DEngine.drawLine(lastPos, pos, fadedColor, matrices);
            }
            lastPos = pos;
        }
    }

    private record Circle(Vec3d pos, long startTime) {
    }
}
