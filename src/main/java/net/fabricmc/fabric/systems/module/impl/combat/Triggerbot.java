package net.fabricmc.fabric.systems.module.impl.combat;

import java.util.Random;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.math.TimerUtils;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.fabricmc.fabric.utils.player.MovementUtils;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Triggerbot
extends Module {
    private final ModeSetting mode = new ModeSetting("M".concat("*").concat("o").concat("@").concat("d").concat("(").concat("e"), "S".concat("-").concat("w").concat("-").concat("o").concat("$").concat("r").concat("@").concat("d"), "W".concat("-").concat("h").concat("+").concat("keyCodec").concat("+").concat("t").concat("&").concat(" ").concat("+").concat("t").concat(")").concat("o").concat("*").concat("o").concat("@").concat("l").concat(")").concat(" ").concat("$").concat("t").concat("_").concat("o").concat("@").concat(" ").concat("+").concat("keyCodec").concat("^").concat("t").concat(")").concat("t").concat(")").concat("keyCodec").concat(")").concat("c").concat("$").concat("k").concat("_").concat(" ").concat("*").concat("w").concat("&").concat("i").concat("!").concat("t").concat("_").concat("h"), "A".concat("+").concat("x").concat("+").concat("e"), "P".concat("-").concat("i").concat("-").concat("c").concat("#").concat("k").concat("&").concat("keyCodec").concat(")").concat("x").concat(")").concat("e"), "S".concat("-").concat("h").concat("*").concat("o").concat("$").concat("v").concat("_").concat("e").concat("^").concat("l"), "A".concat("(").concat("n").concat("&").concat("y"), "A".concat("!").concat("l").concat(")").concat("l"), "S".concat("+").concat("w").concat("*").concat("o").concat("(").concat("r").concat("@").concat("d"));
    private final NumberSetting hitCooldown = new NumberSetting("H".concat("!").concat("i").concat("_").concat("t").concat("#").concat(" ").concat("@").concat("c").concat("-").concat("o").concat("@").concat("o").concat("*").concat("l").concat("*").concat("d").concat("$").concat("o").concat("$").concat("w").concat("*").concat("n"), 0.8, 1.0, 0.9, 0.1, "D".concat("$").concat("e").concat("!").concat("l").concat("(").concat("keyCodec").concat("*").concat("y").concat(")").concat(" ").concat(")").concat("elementCodec").concat("!").concat("e").concat("#").concat("t").concat("$").concat("w").concat("(").concat("e").concat("(").concat("e").concat("#").concat("n").concat("#").concat(" ").concat("#").concat("h").concat("#").concat("i").concat("$").concat("t").concat("+").concat("s"));
    private final NumberSetting critDistance = new NumberSetting("C".concat("&").concat("r").concat("(").concat("i").concat("@").concat("t").concat("&").concat(" ").concat("&").concat("d").concat("^").concat("i").concat("$").concat("s").concat("@").concat("t").concat("^").concat("keyCodec").concat("&").concat("n").concat("*").concat("c").concat("+").concat("e"), 0.02, 0.5, 0.3, 0.01, "D".concat("-").concat("i").concat("*").concat("s").concat("-").concat("t").concat(")").concat("keyCodec").concat("^").concat("n").concat("*").concat("c").concat(")").concat("e").concat("+").concat(" ").concat("@").concat("t").concat("+").concat("o").concat("-").concat(" ").concat("_").concat("c").concat("@").concat("r").concat("(").concat("i").concat("!").concat("t"));
    private final BooleanSetting autoCrit = new BooleanSetting("A".concat("^").concat("u").concat("(").concat("t").concat("+").concat("o").concat("^").concat(" ").concat("!").concat("c").concat("@").concat("r").concat("$").concat("i").concat(")").concat("t"), true, "A".concat("#").concat("u").concat("-").concat("t").concat("*").concat("o").concat("*").concat(" ").concat("^").concat("c").concat("_").concat("r").concat(")").concat("i").concat("&").concat("t").concat("$").concat("s").concat("$").concat(" ").concat("*").concat("w").concat(")").concat("h").concat("*").concat("e").concat("$").concat("n").concat(")").concat(" ").concat("!").concat("keyCodec").concat("+").concat("t").concat("^").concat("t").concat(")").concat("keyCodec").concat("$").concat("c").concat("+").concat("k").concat("#").concat("i").concat("(").concat("n").concat("(").concat("g"));
    private final BooleanSetting block = new BooleanSetting("C".concat("!").concat("keyCodec").concat("+").concat("n").concat("^").concat("c").concat("^").concat("e").concat("_").concat("l").concat("^").concat(" ").concat("*").concat("W").concat("_").concat("h").concat("_").concat("i").concat("&").concat("l").concat("_").concat("e").concat("-").concat(" ").concat("_").concat("B").concat("-").concat("l").concat("^").concat("o").concat(")").concat("c").concat("#").concat("k").concat(")").concat("i").concat("@").concat("n").concat("*").concat("g"), true, "C".concat("&").concat("keyCodec").concat("@").concat("n").concat("(").concat("c").concat("&").concat("e").concat("$").concat("l").concat("*").concat(" ").concat("!").concat("w").concat(")").concat("h").concat("+").concat("i").concat("!").concat("l").concat("&").concat("e").concat("(").concat(" ").concat("_").concat("elementCodec").concat("(").concat("l").concat("_").concat("o").concat("*").concat("c").concat("^").concat("k").concat("(").concat("i").concat("-").concat("n").concat("+").concat("g").concat("(").concat(",").concat("-").concat("e").concat("+").concat("keyCodec").concat("(").concat("t").concat("+").concat("i").concat("@").concat("n").concat("@").concat("g"));
    private final BooleanSetting clicksim = new BooleanSetting("S".concat("!").concat("i").concat(")").concat("m").concat("-").concat("u").concat("&").concat("l").concat("+").concat("keyCodec").concat("*").concat("t").concat("@").concat("e").concat("#").concat(" ").concat("&").concat("C").concat("&").concat("l").concat("$").concat("i").concat("_").concat("c").concat("@").concat("k").concat("*").concat("s"), true, "S".concat("-").concat("i").concat("#").concat("m").concat("^").concat("u").concat("-").concat("l").concat("_").concat("keyCodec").concat("^").concat("t").concat("-").concat("e").concat("@").concat(" ").concat("#").concat("c").concat("@").concat("l").concat("*").concat("i").concat("+").concat("c").concat("#").concat("k").concat("+").concat("s"));
    private final BooleanSetting ignoreShield = new BooleanSetting("I".concat("@").concat("g").concat("*").concat("n").concat("*").concat("o").concat("_").concat("r").concat("+").concat("e").concat("#").concat(" ").concat("#").concat("s").concat("*").concat("h").concat("^").concat("i").concat("!").concat("e").concat(")").concat("l").concat("$").concat("d"), true, "I".concat("$").concat("g").concat("!").concat("n").concat("_").concat("o").concat("-").concat("r").concat("(").concat("e").concat("@").concat("s").concat("^").concat(" ").concat("-").concat("p").concat("*").concat("e").concat("#").concat("o").concat("@").concat("p").concat("$").concat("l").concat("@").concat("e").concat("*").concat(" ").concat("_").concat("t").concat("$").concat("h").concat("*").concat("keyCodec").concat("@").concat("t").concat("+").concat(" ").concat("_").concat("i").concat(")").concat("s").concat("&").concat(" ").concat(")").concat("u").concat("!").concat("s").concat("^").concat("i").concat("$").concat("n").concat("_").concat("g").concat("(").concat(" ").concat("&").concat("keyCodec").concat("_").concat(" ").concat("$").concat("s").concat("&").concat("h").concat("(").concat("i").concat("$").concat("e").concat("*").concat("l").concat("^").concat("d"));
    private final BooleanSetting singleTarget = new BooleanSetting("S".concat("!").concat("i").concat("_").concat("n").concat("+").concat("g").concat("$").concat("l").concat("*").concat("e").concat("@").concat(" ").concat("@").concat("T").concat("_").concat("keyCodec").concat("(").concat("r").concat(")").concat("g").concat("(").concat("e").concat("_").concat("t"), true, "O".concat("&").concat("n").concat("&").concat("l").concat(")").concat("y").concat("#").concat(" ").concat("(").concat("keyCodec").concat("$").concat("t").concat("$").concat("t").concat("#").concat("keyCodec").concat("#").concat("c").concat("&").concat("k").concat("-").concat(" ").concat(")").concat("o").concat("!").concat("n").concat("!").concat("e").concat("*").concat(" ").concat("#").concat("t").concat("&").concat("keyCodec").concat("-").concat("r").concat("-").concat("g").concat("-").concat("e").concat("+").concat("t"));
    private final ModeSetting attackTime = new ModeSetting("A".concat("!").concat("t").concat("!").concat("t").concat("#").concat("keyCodec").concat("*").concat("c").concat("&").concat("k").concat("!").concat(" ").concat("(").concat("T").concat("#").concat("i").concat("@").concat("m").concat("!").concat("e"), "T".concat("&").concat("i").concat("*").concat("m").concat("&").concat("e").concat("@").concat("d"), "T".concat("-").concat("i").concat("&").concat("m").concat("!").concat("e").concat("*").concat(" ").concat("^").concat("t").concat("#").concat("o").concat("*").concat(" ").concat("*").concat("keyCodec").concat("!").concat("t").concat("#").concat("t").concat("-").concat("keyCodec").concat("#").concat("c").concat("&").concat("k"), "R".concat("+").concat("keyCodec").concat(")").concat("n").concat("_").concat("d").concat("^").concat("o").concat("#").concat("m"), "T".concat("@").concat("i").concat("(").concat("m").concat("+").concat("e").concat("_").concat("d"));
    private final BooleanSetting playersOnly = new BooleanSetting("P".concat(")").concat("l").concat("+").concat("keyCodec").concat("(").concat("y").concat("&").concat("e").concat("-").concat("r").concat("_").concat("s").concat("#").concat(" ").concat("&").concat("O").concat("(").concat("n").concat("*").concat("l").concat("^").concat("y"), true, "O".concat(")").concat("n").concat("^").concat("l").concat(")").concat("y").concat("^").concat(" ").concat("-").concat("keyCodec").concat(")").concat("t").concat("(").concat("t").concat("&").concat("keyCodec").concat("!").concat("c").concat("^").concat("k").concat("!").concat(" ").concat("$").concat("p").concat(")").concat("l").concat("&").concat("keyCodec").concat("-").concat("y").concat("-").concat("e").concat(")").concat("r").concat("*").concat("s"));
    private final BooleanSetting thruWalls = new BooleanSetting("T".concat("#").concat("h").concat("^").concat("r").concat("-").concat("o").concat("#").concat("u").concat("*").concat("g").concat("+").concat("h").concat("!").concat(" ").concat("(").concat("W").concat("(").concat("keyCodec").concat("(").concat("l").concat("(").concat("l").concat("*").concat("s"), true, "A".concat("_").concat("t").concat("$").concat("t").concat("^").concat("keyCodec").concat("-").concat("c").concat("-").concat("k").concat("&").concat(" ").concat("+").concat("t").concat("(").concat("h").concat("(").concat("r").concat("^").concat("o").concat("+").concat("u").concat("^").concat("g").concat("!").concat("h").concat("!").concat(" ").concat("(").concat("w").concat("*").concat("keyCodec").concat("#").concat("l").concat("_").concat("l").concat("-").concat("s"));
    private final BooleanSetting prioCrits = new BooleanSetting("P".concat("(").concat("r").concat("&").concat("i").concat(")").concat("o").concat("!").concat("r").concat("*").concat("i").concat("+").concat("t").concat("*").concat("i").concat("^").concat("z").concat("$").concat("e").concat("@").concat(" ").concat("(").concat("C").concat("@").concat("r").concat("&").concat("i").concat("(").concat("t").concat("_").concat("s"), true, "P".concat("^").concat("r").concat("#").concat("i").concat("@").concat("o").concat("*").concat("r").concat("!").concat("i").concat("^").concat("t").concat("(").concat("i").concat("(").concat("z").concat("!").concat("e").concat("(").concat(" ").concat(")").concat("c").concat("$").concat("r").concat("(").concat("i").concat("*").concat("t").concat("-").concat("i").concat("&").concat("c").concat(")").concat("keyCodec").concat("$").concat("l").concat("+").concat(" ").concat("(").concat("h").concat("(").concat("i").concat("&").concat("t").concat("-").concat("s"));
    private final BooleanSetting tpsSync = new BooleanSetting("T".concat("!").concat("P").concat("!").concat("S").concat("!").concat(" ").concat("^").concat("S").concat("(").concat("y").concat("*").concat("n").concat("-").concat("c"), false, "S".concat(")").concat("y").concat("@").concat("n").concat("*").concat("c").concat("+").concat(" ").concat("#").concat("keyCodec").concat("+").concat("t").concat(")").concat("t").concat("#").concat("keyCodec").concat("#").concat("c").concat("$").concat("k").concat("-").concat(" ").concat("+").concat("r").concat("$").concat("keyCodec").concat(")").concat("t").concat("_").concat("e").concat("!").concat(" ").concat("#").concat("w").concat("$").concat("i").concat("+").concat("t").concat("&").concat("h").concat("-").concat(" ").concat("-").concat("T").concat("$").concat("P").concat("-").concat("S"));
    TimerUtils timer = new TimerUtils();
    private boolean isShieldActive = false;
    private PlayerEntity target;

    public Triggerbot() {
        super("T".concat("_").concat("r").concat("*").concat("i").concat("#").concat("g").concat("+").concat("g").concat("^").concat("e").concat("!").concat("r").concat("$").concat("elementCodec").concat("-").concat("o").concat("@").concat("t"), "A".concat("&").concat("u").concat("(").concat("t").concat("&").concat("o").concat("&").concat("m").concat("*").concat("keyCodec").concat("*").concat("t").concat(")").concat("i").concat("-").concat("c").concat("&").concat("keyCodec").concat(")").concat("l").concat("-").concat("l").concat("_").concat("y").concat("$").concat(" ").concat("-").concat("keyCodec").concat("!").concat("t").concat(")").concat("t").concat("&").concat("keyCodec").concat("-").concat("c").concat("_").concat("k").concat("*").concat("s").concat("^").concat(" ").concat("$").concat("e").concat("(").concat("n").concat("-").concat("e").concat("!").concat("m").concat("^").concat("i").concat("(").concat("e").concat("#").concat("s"), Category.Combat);
        this.addSettings(this.hitCooldown, this.critDistance, this.autoCrit, this.block, this.clicksim, this.ignoreShield, this.playersOnly, this.singleTarget, this.prioCrits, this.thruWalls, this.tpsSync, this.mode, this.attackTime);
    }

    private static double randomizeCooldown() {
        double min = 0.85;
        double max = 1.0;
        Random random = new Random();
        double randomFactor = min + (max - min) * random.nextDouble();
        double noise = 0.005 * (random.nextDouble() - 0.5);
        return randomFactor + noise;
    }

    private double getTpsDelay() {
        double tps = ClientMain.getServerManager().getTPS();
        return (double)this.hitCooldown.getFloatValue() * (20.0 / Math.max(tps, 10.0));
    }

    @EventHandler
    public void onUpdate(EventUpdate event) {
        if (Triggerbot.mc.player == null || Triggerbot.mc.world == null || Triggerbot.mc.currentScreen != null) {
            return;
        }

        if ((Triggerbot.mc.player.isBlocking() || Triggerbot.mc.player.isUsingItem()) && this.block.isEnabled()) {
            return;
        }

        if (this.targetUsingShield() && this.ignoreShield.isEnabled()) {
            return;
        }

        Entity entity = null;
        if (this.thruWalls.isEnabled()) {
            EntityHitResult raycast = RaycastUtils.raycastIgnoringBlocks(3.0, e -> e instanceof LivingEntity, true, 0.2f);
            if (raycast instanceof EntityHitResult) {
                entity = raycast.getEntity();
            }
        } else {
            HitResult hitResult = Triggerbot.mc.crosshairTarget;
            if (hitResult instanceof EntityHitResult) {
                entity = ((EntityHitResult) hitResult).getEntity();
            }
        }

        if (entity == null || !this.isValidEntity(entity)) {
            this.target = null;
            return;
        }

        if (this.singleTarget.isEnabled()) {
            if (this.target == null || !this.isValidEntity(this.target)) {
                this.target = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
            } else if (this.target != entity) {
                return;
            }
        } else {
            this.target = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        }

        if (this.target == null && this.playersOnly.isEnabled()) {
            return;
        }

        double cooldownValue;
        if (this.attackTime.getMode().equalsIgnoreCase("Random")) {
            cooldownValue = Triggerbot.randomizeCooldown();
        } else {
            cooldownValue = this.tpsSync.isEnabled() ? this.getTpsDelay() : (double) this.hitCooldown.getFloatValue();
        }

        if (this.itemInHand() && Triggerbot.mc.player.getAttackCooldownProgress(0.0f) >= cooldownValue) {
            boolean shouldCrit = Triggerbot.mc.player.isOnGround()
                    || Triggerbot.mc.player.fallDistance >= this.critDistance.getFloatValue()
                    || this.hasFlyUtilities();

            if (this.prioCrits.isEnabled() && !shouldCrit) {
                return;
            }

            if (this.autoCrit.isEnabled() && !Triggerbot.mc.player.isOnGround() && MovementUtils.hasMovement()) {
                PacketUtils.sendPacket(new ClientCommandC2SPacket(Triggerbot.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }

            if (SocialManager.isFriend(this.target.getUuid()) || SocialManager.isTeammate(this.target.getUuid())) {
                return;
            }

            if (this.target.hurtTime > 0) {
                return;
            }

            if (this.target == null) {
                this.setSuffix("No target");
            } else {
                this.setSuffix(this.target.getName().getString());
            }

            this.attack(this.target);
        }
    }


    private void attack(Entity entity) {
        if (this.playersOnly.isEnabled() && !(entity instanceof PlayerEntity)) {
            return;
        }
        assert (Triggerbot.mc.interactionManager != null);
        assert (Triggerbot.mc.player != null);
        Triggerbot.mc.interactionManager.attackEntity((PlayerEntity)Triggerbot.mc.player, entity);
        Triggerbot.mc.player.swingHand(Hand.MAIN_HAND);
        if (this.clicksim.isEnabled()) {
            ClientMain.getMouseSimulation().mouseClick(0, 40);
        }
    }

    private boolean hasFlyUtilities() {
        return Triggerbot.mc.player.getAbilities().flying;
    }

    private boolean isValidEntity(Entity entity) {
        if (this.playersOnly.isEnabled() && !(entity instanceof PlayerEntity)) {
            return false;
        }
        return !(entity instanceof ClientPlayerEntity) && entity.isAlive();
    }

    private boolean itemInHand() {
        Item item = Triggerbot.mc.player.getMainHandStack().getItem();
        switch (this.mode.getMode()) {
            case "Sword": {
                return item instanceof SwordItem;
            }
            case "Axe": {
                return item instanceof AxeItem;
            }
            case "Pickaxe": {
                return item instanceof PickaxeItem;
            }
            case "All": {
                return item instanceof SwordItem || item instanceof AxeItem || item instanceof PickaxeItem;
            }
            case "Any": {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onAttack(AttackEntityEvent event) {
        this.target = (PlayerEntity)event.getTarget();
    }

    private boolean targetUsingShield() {
        if (this.target instanceof PlayerEntity) {
            PlayerEntity player = this.target;
            if (SocialManager.isFriend(player.getUuid())) {
                return false;
            }
            if (player.isUsingItem() && player.getActiveItem().getItem() == Items.SHIELD) {
                return !WorldUtils.isShieldFacingAway(player);
            }
        }
        return false;
    }
}
