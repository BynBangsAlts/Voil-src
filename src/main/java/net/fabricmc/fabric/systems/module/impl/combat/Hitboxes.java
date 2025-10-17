package net.fabricmc.fabric.systems.module.impl.combat;

import java.awt.Color;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class Hitboxes
extends Module {
    public NumberSetting size = new NumberSetting("S".concat("$").concat("i").concat("$").concat("z").concat("#").concat("e"), 0.1, 3.0, 0.3, 0.1, "S".concat("-").concat("i").concat("(").concat("z").concat("@").concat("e").concat("^").concat(" ").concat("^").concat("o").concat("&").concat("f").concat("$").concat(" ").concat("&").concat("h").concat("@").concat("i").concat("+").concat("t").concat("$").concat("elementCodec").concat("$").concat("o").concat("#").concat("x").concat("-").concat("e").concat("+").concat("s"));
    public BooleanSetting renderExpanded = new BooleanSetting("R".concat("&").concat("e").concat("+").concat("n").concat("(").concat("d").concat("@").concat("e").concat("+").concat("r").concat("#").concat(" ").concat("(").concat("E").concat(")").concat("x").concat("_").concat("p").concat("_").concat("keyCodec").concat("!").concat("n").concat("+").concat("d").concat("@").concat("e").concat("-").concat("d"), true, "R".concat("_").concat("e").concat("(").concat("n").concat("*").concat("d").concat("^").concat("e").concat(")").concat("r").concat("_").concat("S").concat("@").concat(" ").concat("_").concat("e").concat("*").concat("x").concat("#").concat("p").concat("*").concat("keyCodec").concat("-").concat("n").concat("@").concat("d").concat("*").concat("e").concat("_").concat("d").concat("!").concat(" ").concat("(").concat("h").concat("!").concat("i").concat("#").concat("t").concat("+").concat("elementCodec").concat("+").concat("o").concat("*").concat("x").concat("&").concat("e").concat("+").concat(" ").concat("+").concat("s").concat("!").concat("i").concat("-").concat("z").concat("-").concat("e"));

    public Hitboxes() {
        super("H".concat("@").concat("i").concat("^").concat("t").concat("-").concat("elementCodec").concat("#").concat("o").concat("(").concat("x").concat(")").concat("e").concat("$").concat("s"), "E".concat(")").concat("x").concat("&").concat("p").concat("#").concat("keyCodec").concat("-").concat("n").concat("(").concat("d").concat("!").concat("s").concat("&").concat(" ").concat("_").concat("e").concat("@").concat("n").concat("&").concat("e").concat("_").concat("m").concat(")").concat("y").concat("*").concat(" ").concat("-").concat("h").concat("&").concat("i").concat("_").concat("t").concat("!").concat("elementCodec").concat("+").concat("o").concat("*").concat("x").concat("&").concat("e").concat("&").concat("s"), Category.Combat);
        this.addSettings(this.size, this.renderExpanded);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.isEnabled()) {
            for (Entity entity : Hitboxes.mc.world.getEntities()) {
                PlayerEntity player;
                if (!(entity instanceof PlayerEntity) || entity == Hitboxes.mc.player || !Hitboxes.mc.player.canSee((Entity)(player = (PlayerEntity)entity)) || !this.renderExpanded.isEnabled()) continue;
                float expandedWidth = player.getWidth() + this.size.getFloatValue();
                float expandedHeight = player.getHeight() + this.size.getFloatValue();
                Color fillColor = new Color(255, 0, 0, 50);
                Color outlineColor = new Color(255, 0, 0, 255);
                Render3DEngine.drawEntityBox(player, fillColor, outlineColor, matrices, expandedWidth, expandedHeight);
            }
        }
    }
}
