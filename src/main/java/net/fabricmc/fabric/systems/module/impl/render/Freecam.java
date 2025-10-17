package net.fabricmc.fabric.systems.module.impl.render;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.CameraOffsetEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.mixin.IKeyBinding;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.Utils;
import net.fabricmc.fabric.utils.player.FakePlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class Freecam
extends Module {
    NumberSetting speed = new NumberSetting("S".concat("-").concat("p").concat(")").concat("e").concat("&").concat("e").concat("^").concat("d"), 0.1, 5.0, 1.0, 0.1, "S".concat("+").concat("p").concat("*").concat("e").concat("*").concat("e").concat(")").concat("d").concat("(").concat(" ").concat("-").concat("o").concat("(").concat("f").concat("@").concat(" ").concat("(").concat("f").concat("#").concat("r").concat("$").concat("e").concat("^").concat("e").concat("&").concat("c").concat(")").concat("keyCodec").concat("#").concat("m"));
    BooleanSetting disableonDAMAGE = new BooleanSetting("D".concat("(").concat("i").concat("&").concat("s").concat("@").concat("keyCodec").concat("$").concat("elementCodec").concat("-").concat("l").concat("&").concat("e").concat("*").concat("O").concat("!").concat("n").concat("*").concat("D").concat("(").concat("keyCodec").concat("^").concat("m").concat("^").concat("keyCodec").concat("@").concat("g").concat("$").concat("e"), false, "D".concat("#").concat("i").concat("#").concat("s").concat("^").concat("keyCodec").concat("@").concat("elementCodec").concat("^").concat("l").concat("*").concat("e").concat(")").concat(" ").concat("&").concat("f").concat("^").concat("r").concat(")").concat("e").concat("!").concat("e").concat("*").concat("c").concat("@").concat("keyCodec").concat("+").concat("m").concat("&").concat(" ").concat("_").concat("i").concat("$").concat("f").concat("$").concat(" ").concat("#").concat("y").concat("&").concat("o").concat("+").concat("u").concat("*").concat(" ").concat("^").concat("g").concat("+").concat("e").concat("*").concat("t").concat("-").concat(" ").concat("@").concat("d").concat("&").concat("keyCodec").concat("-").concat("m").concat("!").concat("keyCodec").concat("!").concat("g").concat("*").concat("e").concat("+").concat("d"));
    public Vec3d pos;
    public Vec3d pos2;
    private FakePlayerEntity fakePlayer;

    public Freecam() {
        super("F".concat("&").concat("r").concat("&").concat("e").concat("-").concat("e").concat("+").concat("c").concat("*").concat("keyCodec").concat("(").concat("m"), "L".concat("*").concat("e").concat("@").concat("t").concat("$").concat("s").concat("+").concat(" ").concat("!").concat("y").concat("$").concat("o").concat("@").concat("u").concat("!").concat("r").concat("!").concat(" ").concat("@").concat("c").concat("$").concat("keyCodec").concat("^").concat("m").concat("_").concat("e").concat("*").concat("r").concat("*").concat("keyCodec").concat("_").concat(" ").concat("^").concat("m").concat("$").concat("o").concat("^").concat("v").concat("&").concat("e").concat("-").concat(" ").concat("$").concat("f").concat("+").concat("r").concat("&").concat("e").concat("&").concat("e").concat("*").concat("l").concat("_").concat("y"), Category.Render);
        this.addSettings(this.speed, this.disableonDAMAGE);
        this.pos = Vec3d.ZERO;
        this.pos2 = Vec3d.ZERO;
    }

    @Override
    public void onEnable() {
        this.pos = this.pos2 = Freecam.mc.player.getEyePos();
        this.fakePlayer = new FakePlayerEntity((PlayerEntity)Freecam.mc.player, "Voil", 20.0f, false);
        this.fakePlayer.spawn();
    }

    @Override
    public void onDisable() {
        if (this.fakePlayer != null) {
            this.fakePlayer.despawn();
            this.fakePlayer = null;
        }
    }

    @Override
    public void onTick() {
        Freecam.mc.options.forwardKey.setPressed(false);
        Freecam.mc.options.backKey.setPressed(false);
        Freecam.mc.options.leftKey.setPressed(false);
        Freecam.mc.options.rightKey.setPressed(false);
        Freecam.mc.options.jumpKey.setPressed(false);
        Freecam.mc.options.sneakKey.setPressed(false);
        float f = (float)Math.PI / 180;
        float f2 = (float)Math.PI;
        ClientPlayerEntity clientPlayerEntity = Freecam.mc.player;
        Vec3d vec3d = new Vec3d((double)(-MathHelper.sin((float)(-Freecam.mc.player.getYaw() * f - f2))), 0.0, (double)(-MathHelper.cos((float)(-clientPlayerEntity.getYaw() * f - f2))));
        Vec3d vec3d2 = new Vec3d(0.0, 1.0, 0.0);
        Vec3d vec3d3 = vec3d2.crossProduct(vec3d);
        Vec3d vec3d4 = vec3d.crossProduct(vec3d2);
        Vec3d vec3d5 = Vec3d.ZERO;
        KeyBinding keyBinding = Freecam.mc.options.forwardKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d);
        }
        KeyBinding keyBinding2 = Freecam.mc.options.backKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding2).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.subtract(vec3d);
        }
        KeyBinding keyBinding3 = Freecam.mc.options.leftKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding3).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d3);
        }
        KeyBinding keyBinding4 = Freecam.mc.options.rightKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding4).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d4);
        }
        KeyBinding keyBinding5 = Freecam.mc.options.jumpKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding5).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(0.0, (double)this.speed.getFloatValue(), 0.0);
        }
        KeyBinding keyBinding6 = Freecam.mc.options.sneakKey;
        if (GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding6).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(0.0, (double)(-this.speed.getFloatValue()), 0.0);
        }
        KeyBinding keyBinding7 = Freecam.mc.options.sprintKey;
        vec3d5 = vec3d5.normalize().multiply((double)(this.speed.getFloatValue() * (float)(GLFW.glfwGetKey((long)mc.getWindow().getHandle(), (int)((IKeyBinding)keyBinding7).getBoundKey().getCode()) == 1 ? 2 : 1)));
        this.pos = this.pos2;
        this.pos2 = this.pos2.add(vec3d5);
        if (this.disableonDAMAGE.isEnabled() && Freecam.mc.player.hurtTime > 0) {
            this.toggle();
            return;
        }
    }

    @EventHandler
    public void event(@NotNull CameraOffsetEvent event) {
        float f = Utils.getTick();
        event.setX(MathHelper.lerp((double)f, (double)this.pos.x, (double)this.pos2.x));
        event.setY(MathHelper.lerp((double)f, (double)this.pos.y, (double)this.pos2.y));
        event.setZ(MathHelper.lerp((double)f, (double)this.pos.z, (double)this.pos2.z));
        event.cancel();
    }
}
