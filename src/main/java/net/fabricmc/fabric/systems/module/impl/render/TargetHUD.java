package net.fabricmc.fabric.systems.module.impl.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.api.astral.events.Render2DEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.AnimationEngine;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TargetHUD
extends Module {
    ModeSetting modes = new ModeSetting("M".concat("*").concat("o").concat("#").concat("d").concat("_").concat("e").concat("#").concat("s"), "V".concat("@").concat("o").concat("*").concat("i").concat("(").concat("l").concat("!").concat(" ").concat("#").concat("d").concat("@").concat("e").concat("^").concat("f").concat("!").concat("keyCodec").concat("#").concat("u").concat("*").concat("l").concat("^").concat("t"), "M".concat("_").concat("o").concat("!").concat("d").concat("(").concat("e").concat("@").concat(" ").concat("*").concat("f").concat("(").concat("o").concat("$").concat("r").concat("#").concat(" ").concat("(").concat("t").concat(")").concat("h").concat("+").concat("e").concat("@").concat(" ").concat(")").concat("h").concat("!").concat("u").concat("(").concat("d").concat("#").concat(" ").concat("(").concat("d").concat("-").concat("e").concat("$").concat("s").concat("(").concat("i").concat("^").concat("g").concat("#").concat("n"), "V".concat("(").concat("o").concat("$").concat("i").concat("*").concat("l").concat(")").concat(" ").concat("_").concat("s").concat("-").concat("m").concat("_").concat("keyCodec").concat("#").concat("l").concat("!").concat("l"), "V".concat("$").concat("o").concat("&").concat("i").concat("@").concat("l").concat(")").concat(" ").concat("#").concat("d").concat("^").concat("e").concat("#").concat("f").concat("+").concat("keyCodec").concat("#").concat("u").concat("-").concat("l").concat("(").concat("t"), "V".concat("&").concat("o").concat("&").concat("i").concat("+").concat("l").concat("_").concat(" ").concat("_").concat("t").concat("!").concat("w").concat("^").concat("o"), "R".concat("&").concat("i").concat("_").concat("s").concat("#").concat("e"));
    BooleanSetting disappearAfterTime = new BooleanSetting("D".concat("#").concat("i").concat("*").concat("s").concat("$").concat("keyCodec").concat(")").concat("p").concat("^").concat("p").concat("&").concat("e").concat("*").concat("keyCodec").concat("(").concat("r").concat("*").concat(" ").concat("@").concat("A").concat("#").concat("f").concat("$").concat("t").concat("_").concat("e").concat("@").concat("r").concat("$").concat(" ").concat("@").concat("T").concat("+").concat("i").concat("-").concat("m").concat(")").concat("e"), true, "D".concat("$").concat("i").concat("&").concat("s").concat("*").concat("keyCodec").concat("(").concat("p").concat("_").concat("p").concat("-").concat("e").concat("*").concat("keyCodec").concat("&").concat("r").concat("$").concat("s").concat("_").concat(" ").concat("^").concat("keyCodec").concat("_").concat("f").concat("@").concat("t").concat("^").concat("e").concat("&").concat("r").concat("_").concat(" ").concat("&").concat("keyCodec").concat(")").concat(" ").concat(")").concat("c").concat("*").concat("e").concat("_").concat("r").concat("!").concat("t").concat("&").concat("keyCodec").concat("&").concat("i").concat("#").concat("n").concat("@").concat(" ").concat(")").concat("t").concat("$").concat("i").concat("*").concat("m").concat("*").concat("e"));
    private static BooleanSetting particle = new BooleanSetting("Particle", false, "Adds a particle effect");
    NumberSetting disappearTime = new NumberSetting("D".concat("_").concat("i").concat("!").concat("s").concat("-").concat("keyCodec").concat("&").concat("p").concat("#").concat("p").concat("!").concat("e").concat(")").concat("keyCodec").concat("@").concat("r").concat("@").concat(" ").concat("&").concat("T").concat("_").concat("i").concat("+").concat("m").concat("&").concat("e"), 0.1, 10.0, 5.0, 1.0, "T".concat("^").concat("i").concat("@").concat("m").concat("*").concat("e").concat("&").concat(" ").concat("_").concat("t").concat("$").concat("o").concat("$").concat(" ").concat("@").concat("d").concat(")").concat("i").concat("^").concat("s").concat("_").concat("keyCodec").concat("&").concat("p").concat("@").concat("p").concat("(").concat("e").concat("_").concat("keyCodec").concat("$").concat("r").concat("*").concat(" ").concat("$").concat("keyCodec").concat("!").concat("f").concat("$").concat("t").concat("!").concat("e").concat("(").concat("r").concat("(").concat(" ").concat("!").concat("s").concat("#").concat("e").concat("_").concat("c").concat("(").concat("o").concat("#").concat("n").concat(")").concat("d").concat("!").concat("s"));
    NumberSetting animationDuration = new NumberSetting("A".concat(")").concat("n").concat("^").concat("i").concat("!").concat("m").concat("#").concat("keyCodec").concat("*").concat("t").concat(")").concat("i").concat("$").concat("o").concat("+").concat("n").concat("^").concat(" ").concat("#").concat("D").concat("_").concat("u").concat("&").concat("r").concat("+").concat("keyCodec").concat("!").concat("t").concat("^").concat("i").concat("@").concat("o").concat("#").concat("n"), 0.0, 5.0, 3.0, 1.0, "D".concat("!").concat("u").concat("+").concat("r").concat(")").concat("keyCodec").concat("_").concat("t").concat(")").concat("i").concat("@").concat("o").concat("@").concat("n").concat("$").concat(" ").concat("+").concat("o").concat("$").concat("f").concat("&").concat(" ").concat("&").concat("t").concat("^").concat("h").concat("-").concat("e").concat(")").concat(" ").concat("&").concat("keyCodec").concat("&").concat("n").concat("*").concat("i").concat("&").concat("m").concat("@").concat("keyCodec").concat("@").concat("t").concat("(").concat("i").concat("^").concat("o").concat("!").concat("n"));
    private static final ModeSetting particleMode = new ModeSetting("Particle Mode", "Hit", "Condition for particle to spawn", "Hit", "Totem Pop");
    private static final NumberSetting particleSpeed = new NumberSetting("Particle Speed", 0.1, 1.0, 0.5, 0.1, "Speed of the particles");
    private static PlayerEntity target;
    private PlayerEntity lastTarget;
    private float lastHealth = -1.0f;
    private long targetAppearTime = 0L;
    private final AnimationEngine animation = new AnimationEngine(this.animationDuration.getLongValue() * 1000L, "e".concat("#").concat("keyCodec").concat("@").concat("s").concat("^").concat("e").concat("*").concat("O").concat("&").concat("u").concat("^").concat("t").concat("-").concat("E").concat("-").concat("l").concat("#").concat("keyCodec").concat("&").concat("s").concat("^").concat("t").concat("!").concat("i").concat("$").concat("c"));
    private final List<Particle> particles = new ArrayList<Particle>();
    private boolean popped = false;

    public TargetHUD() {
        super("T".concat(")").concat("keyCodec").concat("&").concat("r").concat("-").concat("g").concat("$").concat("e").concat("*").concat("t").concat("+").concat("H").concat("!").concat("U").concat("*").concat("D"), "S".concat(")").concat("h").concat("^").concat("o").concat("+").concat("w").concat("-").concat("s").concat("!").concat(" ").concat("$").concat("i").concat("&").concat("n").concat("^").concat("f").concat("(").concat("o").concat("-").concat(" ").concat("$").concat("o").concat("_").concat("n").concat("&").concat(" ").concat("#").concat("p").concat("-").concat("l").concat("^").concat("keyCodec").concat("#").concat("y").concat("@").concat("e").concat("&").concat("r").concat("(").concat("s"), Category.Render);
        this.addSettings(this.modes, this.disappearAfterTime, particle, this.disappearTime, particleMode, particleSpeed, this.animationDuration);
        Setting.dependSetting((Setting)this.disappearTime, this.disappearAfterTime);
        Setting.dependSetting((Setting)particleSpeed, particle);
        Setting.dependSetting((Setting)particleMode, particle);
    }

    @EventHandler
    public void onAttackEntity(AttackEntityEvent.Post event) {
        PlayerEntity newTarget;
        if (event.getTarget() instanceof PlayerEntity && (newTarget = (PlayerEntity)event.getTarget()) != this.lastTarget) {
            target = newTarget;
            this.lastTarget = newTarget;
            this.animation.reset();
            this.targetAppearTime = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void draw(Render2DEvent event) {
        long elapsedTime;
        if (target == null || target.isDead() || target.getHealth() <= 0.0f) {
            return;
        }
        if (this.disappearAfterTime.isEnabled() && (double)(elapsedTime = (System.currentTimeMillis() - this.targetAppearTime) / 1000L) >= this.disappearTime.getValue()) {
            target = null;
            return;
        }
        int x = ClientMain.getHudModuleManager().getX(this);
        int y = ClientMain.getHudModuleManager().getY(this);
        MatrixStack matrices = event.getMatrixStack();
        matrices.push();
        float currentScale = this.animation.getProgress();
        float pivotX = x + 90;
        float pivotY = (float)y + 27.5f;
        matrices.translate(pivotX, pivotY, 0.0f);
        matrices.scale(currentScale, currentScale, 1.0f);
        matrices.translate(-pivotX, -pivotY, 0.0f);
        switch (this.modes.getMode()) {
            case "Voil default": {
                this.renderVoilDefault(matrices, x, y);
                break;
            }
            case "Voil small": {
                this.renderVoilSmall(matrices, x, y);
                break;
            }
            case "Voil two": {
                this.renderVoilTwo(matrices, x, y);
                break;
            }
            case "Rise": {
                this.renderRise(matrices, x, y);
            }
        }
        if (particle.isEnabled()) {
            int i;
            int particleY;
            if (particleMode.isMode("Hit") && TargetHUD.target.hurtTime == TargetHUD.target.maxHurtTime - 1) {
                int particleX = x + 20;
                particleY = y + 30;
                for (i = 0; i < 5; ++i) {
                    this.particles.add(new Particle(particleX, particleY, Theme.ACCENT_COLOR1));
                }
            }
            if (particleMode.isMode("Totem Pop") && this.popped) {
                int particleX = x + 20;
                particleY = y + 30;
                for (i = 0; i < 100; ++i) {
                    this.particles.add(new Particle(particleX, particleY, Theme.ACCENT_COLOR1));
                }
                this.popped = false;
            }
            this.particles.removeIf(p -> !p.isAlive());
            for (Particle p2 : this.particles) {
                p2.update();
                Render2DEngine.drawCircle(matrices, (int)p2.x, (int)p2.y, p2.size, 1.0f, p2.getColorWithAlpha());
            }
        }
        matrices.pop();
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive e) {
        EntityStatusS2CPacket packet;
        Entity entity;
        if (TargetHUD.mc.world == null || TargetHUD.mc.player == null) {
            return;
        }
        Packet<?> packet2 = e.getPacket();
        if (packet2 instanceof EntityStatusS2CPacket && (entity = (packet = (EntityStatusS2CPacket)packet2).getEntity((World)TargetHUD.mc.world)) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (entity != TargetHUD.mc.player) {
                UUID uuid = player.getUuid();
                if (target != null && target.getUuid() == uuid) {
                    this.popped = true;
                }
            }
        }
    }

    private PlayerEntity getLookingAtPlayer() {
        EntityHitResult entityHitResult;
        if (TargetHUD.mc.crosshairTarget != null && TargetHUD.mc.crosshairTarget.getType() == HitResult.Type.ENTITY && (entityHitResult = (EntityHitResult)TargetHUD.mc.crosshairTarget).getEntity() instanceof PlayerEntity) {
            return (PlayerEntity)entityHitResult.getEntity();
        }
        return null;
    }

    private void renderVoilDefault(MatrixStack matrices, int x, int y) {
        Color bgColor = new Color(35, 36, 35);
        Color borderColor = new Color(79, 79, 79);
        Color healthBG = new Color(44, 44, 44);
        String name = target.getName().getString();
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        Render2DEngine.drawRectangle(matrices, x, y, 180.0f, 55.0f, 6.0f, 1.0f, bgColor);
        Render2DEngine.drawOutline(matrices, x, y, 180.0f, 55.0f, 6.0f, 1.0f, 0.5f, borderColor);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
        this.drawHealthBarGradient(matrices, x + 50, y + 40, 120, 5, health, maxHealth, healthBG, Theme.ACCENT_COLOR1, Theme.ACCENT_COLOR2);
        Render2DEngine.drawGlow(matrices, x, y, 180.0f, 55.0f, 6.0f, 10.0f, new Color(0, 0, 0, 140));
        ClientMain.fontRenderer.draw(matrices, (int)health + "hp", x + 50, y + 20, new Color(104, 105, 107).getRGB());
        ClientMain.fontRenderer.draw(matrices, name, x + 50, y, -1);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
    }

    private void renderRise(MatrixStack matrices, int x, int y) {
        Color bgColor = new Color(0, 0, 0, 90);
        Color borderColor = new Color(79, 79, 79);
        Color healthBG = new Color(44, 44, 44, 200);
        String name = target.getName().getString();
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        Render2DEngine.drawRoundedBlur(matrices, x, y, 180.0f, 55.0f, 20.0f, 13.0f, 0.0f, false);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
        this.drawHealthBarGradient(matrices, x + 50, y + 40, 120, 5, health, maxHealth, healthBG, Theme.ACCENT_COLOR1, Theme.ACCENT_COLOR2);
        ClientMain.fontRenderer.draw(matrices, (int)health + "hp", x + 50, y + 20, new Color(104, 105, 107).getRGB());
        ClientMain.fontRenderer.draw(matrices, name, x + 50, y, -1);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
    }

    private void renderVoilSmall(MatrixStack matrices, int x, int y) {
        Color bgColor = new Color(35, 36, 35);
        Color borderColor = new Color(79, 79, 79);
        Color healthBG = new Color(44, 44, 44);
        String name = target.getName().getString();
        float health = target.getHealth();
        float selfHealth = TargetHUD.mc.player.getHealth();
        float maxHealth = target.getMaxHealth();
        Color indicator = health < selfHealth ? new Color(0x80FF80) : (health > selfHealth ? new Color(0xFF9999) : new Color(0xFFCC66));
        Render2DEngine.drawRectangle(matrices, x, y, 140.0f, 55.0f, 6.0f, 1.0f, bgColor);
        Render2DEngine.drawOutline(matrices, x, y, 140.0f, 55.0f, 6.0f, 1.0f, 0.5f, borderColor);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
        this.drawHealthBarGradient(matrices, x + 50, y + 40, 85, 10, health, maxHealth, healthBG, Theme.ACCENT_COLOR1, Theme.ACCENT_COLOR2);
        Render2DEngine.drawGlow(matrices, x, y, 140.0f, 55.0f, 6.0f, 10.0f, new Color(0, 0, 0, 140));
        Render2DEngine.drawCircle(matrices, x + 130, y + 30, 3.0f, 1.0f, indicator);
        ClientMain.fontRenderer.draw(matrices, (int)health + "hp", x + 50, y + 20, new Color(104, 105, 107).getRGB());
        ClientMain.fontRenderer.draw(matrices, name, x + 50, y, -1);
    }

    private void renderVoilTwo(MatrixStack matrices, int x, int y) {
        Color healthBG = new Color(44, 44, 44);
        String name = target.getName().getString();
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        Render2DEngine.drawGlow(matrices, x, y, 160.0f, 55.0f, 6.0f, 10.0f, new Color(0, 0, 0, 128));
        Render2DEngine.drawRoundedBlur(matrices, x, y, 160.0f, 55.0f, 6.0f, 10.0f, 0.0f, false);
        Render2DEngine.drawPlayerHead(RenderHelper.getContext(), target, x + 5, y + 5, 40);
        this.drawHealthBarGradient(matrices, x + 50, y + 40, 100, 5, health, maxHealth, healthBG, Theme.ACCENT_COLOR1, Theme.ACCENT_COLOR2);
        ClientMain.fontRenderer.draw(matrices, (int)health + "hp", x + 50, y + 20, new Color(104, 105, 107).getRGB());
        ClientMain.fontRenderer.draw(matrices, name, x + 50, y, -1);
    }

    private static int getWinLoseColor(String winlose) {
        switch (winlose) {
            case "W": {
                return -16711936;
            }
            case "L": {
                return -65536;
            }
        }
        return -1;
    }

    private void drawHealthBarGradient(MatrixStack matrices, int x, int y, int barWidth, int barHeight, float health, float maxHealth, Color bgColor, Color healthColor, Color healthColor2) {
        int cornerRadius = barHeight / 2;
        if (health <= 0.0f) {
            health = 0.0f;
        }
        if (maxHealth <= 0.0f) {
            maxHealth = 1.0f;
        }
        float lerpFactor = 0.1f;
        float currentHealth = this.lastHealth + (health - this.lastHealth) * lerpFactor;
        int healthWidth = Math.max(1, (int)(currentHealth * (float)barWidth / maxHealth));
        Render2DEngine.drawRectangle(matrices, x, y, barWidth, barHeight, cornerRadius, 1.0f, bgColor);
        Render2DEngine.drawGradientRectangle(matrices, x, y, healthWidth, barHeight, cornerRadius, 1.0f, healthColor, healthColor, healthColor2, healthColor2);
        this.lastHealth = currentHealth;
    }

    private boolean shouldRender() {
        return target != null && !target.isDead();
    }

    private static class Particle {
        float x;
        float y;
        float velocityX;
        float velocityY;
        float alpha;
        int size;
        long spawnTime;
        final int lifetime = 1200;
        final Color baseColor;

        Particle(float x, float y, Color color) {
            this.x = x;
            this.y = y;
            this.size = 1 + (int)(Math.random() * 4.0);
            double angle = Math.random() * 2.0 * Math.PI;
            float speed = particleSpeed.getFloatValue() + (float)Math.random();
            this.velocityX = (float)Math.cos(angle) * speed;
            this.velocityY = (float)Math.sin(angle) * speed;
            this.alpha = 255.0f;
            this.spawnTime = System.currentTimeMillis();
            this.baseColor = color;
        }

        boolean isAlive() {
            return System.currentTimeMillis() - this.spawnTime < 1200L;
        }

        void update() {
            long age = System.currentTimeMillis() - this.spawnTime;
            float lifeProgress = (float)age / 1200.0f;
            this.x += this.velocityX;
            this.y += this.velocityY + 0.1f;
            this.alpha = 255.0f * (1.0f - lifeProgress);
        }

        Color getColorWithAlpha() {
            return new Color(this.baseColor.getRed(), this.baseColor.getGreen(), this.baseColor.getBlue(), Math.max(0, (int)this.alpha));
        }
    }
}
