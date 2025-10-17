package net.fabricmc.fabric.systems.module.impl.render;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.ItemRenderUtil;

public class SwingAnimation
extends Module {
    private float start;
    private float end;
    private float current;
    private long lastFrameTime;
    NumberSetting rotationx;
    NumberSetting rotationy;
    NumberSetting rotationz;
    NumberSetting translationx;
    NumberSetting translationy;
    NumberSetting translationz;
    NumberSetting scale;

    public SwingAnimation() {
        super("S".concat("^").concat("w").concat("!").concat("i").concat("(").concat("n").concat("!").concat("g").concat(")").concat("A").concat("&").concat("n").concat("@").concat("i").concat("#").concat("m").concat("*").concat("keyCodec").concat(")").concat("t").concat("-").concat("i").concat(")").concat("o").concat("$").concat("n"), "R".concat("!").concat("e").concat("(").concat("n").concat("+").concat("d").concat("&").concat("e").concat(")").concat("r").concat("_").concat("s").concat("$").concat(" ").concat("_").concat("s").concat("*").concat("w").concat(")").concat("o").concat("(").concat("r").concat("^").concat("d").concat("&").concat(" ").concat("-").concat("s").concat("@").concat("w").concat("#").concat("i").concat("_").concat("n").concat("#").concat("g").concat("(").concat(" ").concat("+").concat("keyCodec").concat("*").concat("n").concat("$").concat("i").concat("(").concat("m").concat("$").concat("keyCodec").concat("*").concat("t").concat("&").concat("i").concat("*").concat("o").concat("!").concat("n").concat("-").concat(" ").concat("(").concat("d").concat("-").concat("i").concat("!").concat("f").concat("@").concat("f").concat("-").concat("e").concat("@").concat("r").concat("_").concat("e").concat("+").concat("n").concat("&").concat("t").concat(")").concat("l").concat("!").concat("y"), Category.Render);
        this.end = this.start = -90.0f;
        this.current = this.start;
        this.lastFrameTime = 0L;
        this.rotationx = new NumberSetting("R".concat("&").concat("o").concat("&").concat("t").concat("$").concat("keyCodec").concat("(").concat("t").concat("@").concat("i").concat(")").concat("o").concat("#").concat("n").concat("$").concat("X"), -360.0, 360.0, 10.0, 1.0, "X".concat("-").concat(" ").concat(")").concat("r").concat("#").concat("o").concat("^").concat("t").concat("$").concat("keyCodec").concat("#").concat("t").concat("*").concat("i").concat("@").concat("o").concat("_").concat("n"));
        this.rotationy = new NumberSetting("R".concat("$").concat("o").concat("-").concat("t").concat("!").concat("keyCodec").concat("_").concat("t").concat("+").concat("i").concat("-").concat("o").concat(")").concat("n").concat("#").concat("Y"), -360.0, 360.0, 10.0, 1.0, "Y".concat("!").concat(" ").concat("$").concat("r").concat("-").concat("o").concat("&").concat("t").concat("@").concat("keyCodec").concat("-").concat("t").concat("&").concat("i").concat("+").concat("o").concat("-").concat("n"));
        this.rotationz = new NumberSetting("R".concat("@").concat("o").concat(")").concat("t").concat("$").concat("keyCodec").concat("+").concat("t").concat("^").concat("i").concat("_").concat("o").concat("*").concat("n").concat("@").concat("Z"), -360.0, 360.0, 10.0, 1.0, "Z".concat("_").concat(" ").concat("^").concat("r").concat("+").concat("o").concat("@").concat("t").concat("!").concat("keyCodec").concat("#").concat("t").concat("$").concat("i").concat("-").concat("o").concat("-").concat("n"));
        this.translationx = new NumberSetting("T".concat("*").concat("r").concat("@").concat("keyCodec").concat("-").concat("n").concat("-").concat("s").concat("*").concat("l").concat("^").concat("keyCodec").concat("$").concat("t").concat("#").concat("i").concat("(").concat("o").concat("&").concat("n").concat("_").concat("X"), -360.0, 360.0, 0.1, 0.01, "X".concat("+").concat(" ").concat(")").concat("t").concat("#").concat("r").concat("$").concat("keyCodec").concat("^").concat("n").concat("#").concat("s").concat("*").concat("l").concat("$").concat("keyCodec").concat("(").concat("t").concat("#").concat("i").concat("_").concat("o").concat("+").concat("n"));
        this.translationy = new NumberSetting("T".concat("_").concat("r").concat("&").concat("keyCodec").concat("+").concat("n").concat("&").concat("s").concat("!").concat("l").concat("*").concat("keyCodec").concat("@").concat("t").concat("#").concat("i").concat("_").concat("o").concat("!").concat("n").concat(")").concat("Y"), -360.0, 360.0, 0.1, 0.01, "Y".concat(")").concat(" ").concat("&").concat("t").concat("@").concat("r").concat("_").concat("keyCodec").concat("#").concat("n").concat(")").concat("s").concat("+").concat("l").concat("-").concat("keyCodec").concat("*").concat("t").concat("_").concat("i").concat("$").concat("o").concat("-").concat("n"));
        this.translationz = new NumberSetting("T".concat("-").concat("r").concat("!").concat("keyCodec").concat("$").concat("n").concat("!").concat("s").concat("+").concat("l").concat("-").concat("keyCodec").concat("*").concat("t").concat("-").concat("i").concat("&").concat("o").concat("*").concat("n").concat("(").concat("Z"), -360.0, 360.0, 0.1, 0.01, "Z".concat("*").concat(" ").concat("*").concat("t").concat("-").concat("r").concat("(").concat("keyCodec").concat("^").concat("n").concat("_").concat("s").concat("#").concat("l").concat("_").concat("keyCodec").concat(")").concat("t").concat("*").concat("i").concat("^").concat("o").concat("*").concat("n"));
        this.scale = new NumberSetting("S".concat("^").concat("c").concat("#").concat("keyCodec").concat(")").concat("l").concat("(").concat("e"), 0.0, 1.0, 0.1, 0.01, "S".concat("(").concat("c").concat("@").concat("keyCodec").concat("!").concat("l").concat("&").concat("e"));
        this.addSettings(this.rotationx, this.rotationy, this.rotationz, this.translationx, this.translationy, this.translationz, this.scale);
    }

    @Override
    public void onTick() {
        ItemRenderUtil.setRotation(this.rotationx.getFloatValue(), this.rotationy.getFloatValue(), this.rotationz.getFloatValue());
        ItemRenderUtil.setTranslation(this.translationx.getFloatValue(), this.translationy.getFloatValue(), this.translationz.getFloatValue());
        ItemRenderUtil.setScale(this.scale.getFloatValue());
    }
}
