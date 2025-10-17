package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class Breadcrumbs
extends Module {
    ModeSetting mode = new ModeSetting("M".concat("#").concat("o").concat(")").concat("d").concat(")").concat("e"), "S".concat("-").concat("q").concat("&").concat("u").concat("_").concat("keyCodec").concat("!").concat("r").concat("+").concat("e"), "M".concat("*").concat("o").concat("#").concat("d").concat("&").concat("e").concat("-").concat(" ").concat("$").concat("o").concat("(").concat("f").concat("+").concat(" ").concat("_").concat("t").concat(")").concat("h").concat("^").concat("e").concat("_").concat(" ").concat(")").concat("B").concat("-").concat("r").concat("+").concat("e").concat("_").concat("keyCodec").concat(")").concat("d").concat("@").concat("c").concat("^").concat("r").concat("*").concat("u").concat("-").concat("m").concat("*").concat("elementCodec").concat("+").concat("s"), "L".concat("_").concat("i").concat("+").concat("n").concat("+").concat("e"), "S".concat("_").concat("q").concat(")").concat("u").concat("#").concat("keyCodec").concat("*").concat("r").concat("_").concat("e"));
    NumberSetting lifetime = new NumberSetting("L".concat("#").concat("i").concat("&").concat("f").concat(")").concat("e").concat("#").concat("t").concat("$").concat("i").concat("+").concat("m").concat("(").concat("e"), 1.0, 10.0, 3.0, 1.0, "L".concat("#").concat("i").concat("_").concat("f").concat("-").concat("e").concat("-").concat("t").concat("^").concat("i").concat("_").concat("m").concat("(").concat("e").concat(")").concat(" ").concat("^").concat("o").concat("#").concat("f").concat("#").concat(" ").concat("#").concat("t").concat("!").concat("h").concat("+").concat("e").concat("*").concat(" ").concat("&").concat("elementCodec").concat("!").concat("r").concat("#").concat("e").concat("*").concat("keyCodec").concat("$").concat("d").concat("^").concat("c").concat("!").concat("r").concat("-").concat("u").concat("#").concat("m").concat("*").concat("elementCodec").concat("@").concat("s").concat("#").concat(" ").concat(")").concat("i").concat("#").concat("n").concat("^").concat(" ").concat("(").concat("s").concat(")").concat("e").concat(")").concat("c").concat("&").concat("o").concat("+").concat("n").concat("_").concat("d").concat("*").concat("s"));
    ColorPickerSetting color = new ColorPickerSetting("C".concat("@").concat("o").concat("$").concat("l").concat("_").concat("o").concat("(").concat("r"), new Color(255, 255, 255, 255), "C".concat(")").concat("o").concat("-").concat("l").concat("-").concat("o").concat("-").concat("r").concat("-").concat(" ").concat("(").concat("o").concat("$").concat("f").concat("@").concat(" ").concat("_").concat("t").concat("-").concat("h").concat("!").concat("e").concat("#").concat(" ").concat("-").concat("elementCodec").concat("!").concat("r").concat("@").concat("e").concat("(").concat("keyCodec").concat("&").concat("d").concat("&").concat("c").concat("^").concat("r").concat(")").concat("u").concat("&").concat("m").concat("@").concat("elementCodec").concat("$").concat("s"));
    private final List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

    public Breadcrumbs() {
        super("B".concat("*").concat("r").concat("@").concat("e").concat("$").concat("keyCodec").concat("$").concat("d").concat("!").concat("c").concat(")").concat("r").concat("@").concat("u").concat("^").concat("m").concat("$").concat("elementCodec").concat("-").concat("s"), "R".concat(")").concat("e").concat("#").concat("n").concat("^").concat("d").concat("*").concat("e").concat("-").concat("r").concat("@").concat("s").concat("*").concat(" ").concat(")").concat("keyCodec").concat("^").concat(" ").concat("_").concat("p").concat("*").concat("keyCodec").concat("$").concat("t").concat("^").concat("h").concat("$").concat(" ").concat("-").concat("w").concat(")").concat("h").concat("(").concat("e").concat("@").concat("r").concat("$").concat("e").concat("&").concat(" ").concat("(").concat("y").concat(")").concat("o").concat("$").concat("u").concat("_").concat(" ").concat("*").concat("w").concat("&").concat("keyCodec").concat("!").concat("l").concat("#").concat("k"), Category.Render);
        this.addSettings(this.mode, this.lifetime, this.color);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        float spacing = 0.6f;
        block6: for (int i = 0; i < this.breadcrumbs.size(); ++i) {
            Breadcrumb current = this.breadcrumbs.get(i);
            Color renderColor = new Color(this.color.getColor().getRGB() & 0xFFFFFF | (int)(current.opacity * 255.0f) << 24, true);
            if (current.mode.equals("Square")) {
                current.position = current.position.add(new Vec3d((double)((float)i * spacing), 0.0, 0.0));
                Render3DEngine.drawBoxWithParams(current.position, renderColor, matrices, 0.1f, 0.2f);
            }
            switch (current.mode) {
                case "Line": {
                    if (i <= 0) continue block6;
                    Breadcrumb previous = this.breadcrumbs.get(i - 1);
                    Render3DEngine.drawLine(previous.position, current.position, renderColor, matrices);
                }
            }
        }
    }

    @Override
    public void onTick() {
        if (Breadcrumbs.mc.player != null) {
            Vec3d playerPos = Breadcrumbs.mc.player.getPos();
            String currentMode = this.mode.getMode();
            this.addBreadcrumb(playerPos, currentMode);
        }
        this.breadcrumbs.forEach(breadcrumb -> breadcrumb.update(this.lifetime));
        this.breadcrumbs.removeIf(breadcrumb -> breadcrumb.isExpired(this.lifetime));
    }

    public void addBreadcrumb(Vec3d position, String mode) {
        this.breadcrumbs.add(new Breadcrumb(position, mode));
    }

    class Breadcrumb {
        Vec3d position;
        long timestamp;
        String mode;
        float opacity;

        public Breadcrumb(Vec3d position, String mode) {
            this.position = position;
            this.timestamp = System.currentTimeMillis();
            this.mode = mode;
            this.opacity = 1.0f;
        }

        public boolean isExpired(NumberSetting lifetime) {
            long lifetimeInMillis = lifetime.getIValue() * 1000;
            return System.currentTimeMillis() - this.timestamp > lifetimeInMillis;
        }

        public void update(NumberSetting lifetime) {
            long fadeStart;
            long elapsed = System.currentTimeMillis() - this.timestamp;
            if (elapsed > (fadeStart = (long)(lifetime.getIValue() * 900))) {
                float fadeProgress = (float)(elapsed - fadeStart) / (float)(lifetime.getIValue() * 100);
                this.opacity = Math.max(0.0f, 1.0f - fadeProgress);
            }
        }
    }
}
