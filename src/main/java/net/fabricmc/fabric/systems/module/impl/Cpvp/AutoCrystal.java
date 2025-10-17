package net.fabricmc.fabric.systems.module.impl.Cpvp;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EndCrystalExplosionMcPlayerEvent;
import net.fabricmc.fabric.api.astral.events.EntitySpawnEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.crystal.CrystalUtils;
import net.fabricmc.fabric.utils.key.KeyUtils;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.fabricmc.fabric.utils.world.EntityUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AutoCrystal
extends Module {
    public NumberSetting placeTicks = new NumberSetting("P".concat("+").concat("l").concat("&").concat("keyCodec").concat("#").concat("c").concat("&").concat("e").concat("^").concat("T").concat("_").concat("i").concat("^").concat("c").concat("-").concat("k").concat("+").concat("s"), 0.0, 10.0, 0.0, 0.1, "D".concat("@").concat("e").concat("@").concat("l").concat("*").concat("keyCodec").concat("+").concat("y").concat("(").concat(" ").concat("_").concat("elementCodec").concat("#").concat("e").concat("!").concat("f").concat("(").concat("o").concat("_").concat("r").concat("@").concat("e").concat("+").concat(" ").concat("_").concat("p").concat("&").concat("l").concat("^").concat("keyCodec").concat("+").concat("c").concat("!").concat("i").concat("^").concat("n").concat("(").concat("g").concat(")").concat(" ").concat("!").concat("c").concat("_").concat("r").concat("!").concat("y").concat("(").concat("s").concat("^").concat("t").concat("(").concat("keyCodec").concat("$").concat("l"));
    public NumberSetting breakTicks = new NumberSetting("B".concat("-").concat("r").concat("@").concat("e").concat("@").concat("keyCodec").concat("@").concat("k").concat(")").concat("T").concat("#").concat("i").concat("(").concat("c").concat("@").concat("k").concat("&").concat("s"), 0.0, 10.0, 0.0, 0.1, "D".concat("+").concat("e").concat("@").concat("l").concat("*").concat("keyCodec").concat("*").concat("y").concat("!").concat(" ").concat("#").concat("elementCodec").concat("@").concat("e").concat("-").concat("f").concat("&").concat("o").concat("$").concat("r").concat("+").concat("e").concat("^").concat(" ").concat("*").concat("elementCodec").concat("&").concat("r").concat("+").concat("e").concat("-").concat("keyCodec").concat("&").concat("k").concat("*").concat("i").concat("-").concat("n").concat("$").concat("g").concat("#").concat(" ").concat("@").concat("c").concat("#").concat("r").concat("#").concat("y").concat("_").concat("s").concat("@").concat("t").concat("!").concat("keyCodec").concat("-").concat("l"));
    private ModeSetting modeSetting = new ModeSetting("M".concat("!").concat("o").concat("+").concat("d").concat("#").concat("e"), "N".concat(")").concat("o").concat("@").concat("r").concat("+").concat("m").concat("&").concat("keyCodec").concat("^").concat("l"), "M".concat("+").concat("o").concat("*").concat("d").concat("+").concat("e").concat("!").concat(" ").concat("*").concat("t").concat("@").concat("o").concat("^").concat(" ").concat("*").concat("u").concat(")").concat("s").concat("$").concat("e"), "B".concat("!").concat("l").concat("#").concat("keyCodec").concat(")").concat("t").concat("^").concat("keyCodec").concat("@").concat("n").concat("+").concat("t"), "N".concat("(").concat("o").concat("+").concat("r").concat("^").concat("m").concat("!").concat("keyCodec").concat("^").concat("l"));
    private final BooleanSetting activeOnRightClick = new BooleanSetting("O".concat("*").concat("n").concat("*").concat(" ").concat("_").concat("k").concat("#").concat("e").concat("^").concat("y"), true, "O".concat("$").concat("n").concat("+").concat("l").concat("#").concat("y").concat("(").concat(" ").concat("!").concat("keyCodec").concat("#").concat("c").concat("#").concat("t").concat("*").concat("i").concat("#").concat("v").concat("#").concat("keyCodec").concat(")").concat("t").concat("$").concat("e").concat("@").concat("s").concat("!").concat(" ").concat("#").concat("o").concat("(").concat("n").concat(")").concat(" ").concat("^").concat("k").concat("_").concat("e").concat("*").concat("y"));
    private final BooleanSetting clickSimulation = new BooleanSetting("C".concat(")").concat("l").concat("+").concat("i").concat("(").concat("c").concat("(").concat("k").concat("@").concat(" ").concat("!").concat("S").concat("!").concat("i").concat("-").concat("m").concat("(").concat("u").concat("!").concat("l").concat(")").concat("keyCodec").concat("&").concat("t").concat("#").concat("i").concat("-").concat("o").concat("-").concat("n"), false, "S".concat("&").concat("i").concat("^").concat("m").concat("^").concat("u").concat("+").concat("l").concat("&").concat("keyCodec").concat("*").concat("t").concat("&").concat("e").concat("*").concat(" ").concat("+").concat("c").concat("(").concat("l").concat("_").concat("i").concat("-").concat("c").concat("*").concat("k").concat("^").concat("s").concat("&").concat(" ").concat(")").concat("w").concat("@").concat("h").concat("_").concat("i").concat("^").concat("l").concat("*").concat("e").concat("&").concat(" ").concat("#").concat("p").concat("-").concat("l").concat("$").concat("keyCodec").concat("*").concat("c").concat("!").concat("i").concat("@").concat("n").concat("!").concat("g").concat("!").concat("/").concat("*").concat("elementCodec").concat("_").concat("r").concat("$").concat("e").concat("$").concat("keyCodec").concat("$").concat("k").concat("!").concat("i").concat("#").concat("n").concat("#").concat("g"));
    private final BooleanSetting hurtTimePredict = new BooleanSetting("D".concat("*").concat("keyCodec").concat("#").concat("m").concat("*").concat("keyCodec").concat("#").concat("g").concat("(").concat("e").concat("&").concat(" ").concat(")").concat("t").concat("^").concat("i").concat("^").concat("c").concat("(").concat("k"), false, "P".concat("&").concat("r").concat("$").concat("e").concat(")").concat("d").concat("-").concat("i").concat("$").concat("c").concat("&").concat("t").concat("$").concat("s").concat("@").concat(" ").concat("+").concat("h").concat(")").concat("u").concat("^").concat("r").concat("&").concat("t").concat("_").concat(" ").concat("@").concat("t").concat("-").concat("i").concat("@").concat("m").concat("+").concat("e").concat(")").concat(" ").concat("#").concat("t").concat("$").concat("o").concat("@").concat(" ").concat("_").concat("d").concat("!").concat("o").concat("_").concat("u").concat("_").concat("elementCodec").concat("^").concat("l").concat("+").concat("e").concat("!").concat(" ").concat("(").concat("t").concat("@").concat("keyCodec").concat("&").concat("p").concat("&").concat(" ").concat("#").concat("t").concat(")").concat("h").concat("-").concat("e").concat("^").concat(" ").concat("*").concat("t").concat("@").concat("keyCodec").concat(")").concat("r").concat("#").concat("g").concat("_").concat("e").concat("$").concat("t"));
    public static final BooleanSetting nobounce = new BooleanSetting("noBounce", true, "Prevents bouncing crystals");
    private final BooleanSetting stopOnKill = new BooleanSetting("S".concat("$").concat("t").concat("@").concat("o").concat("*").concat("p").concat("!").concat("O").concat("!").concat("n").concat("$").concat("K").concat("$").concat("i").concat("@").concat("l").concat("+").concat("l"), true, "S".concat("_").concat("t").concat("(").concat("o").concat("@").concat("p").concat(")").concat("s").concat("$").concat(" ").concat("^").concat("c").concat("^").concat("r").concat("-").concat("y").concat("^").concat("s").concat("+").concat("t").concat("$").concat("keyCodec").concat("*").concat("l").concat("-").concat("l").concat(")").concat("i").concat(")").concat("n").concat("@").concat("g").concat("_").concat(" ").concat("@").concat("i").concat("^").concat("f").concat(")").concat(" ").concat("_").concat("s").concat("@").concat("o").concat("&").concat("m").concat("*").concat("e").concat("-").concat("o").concat("-").concat("n").concat("&").concat("e").concat("(").concat(" ").concat("*").concat("d").concat("^").concat("i").concat("-").concat("e").concat("+").concat("s"));
    private final BooleanSetting antiweakness = new BooleanSetting("A".concat("^").concat("n").concat("&").concat("t").concat("@").concat("i").concat("$").concat("W").concat("*").concat("e").concat("(").concat("keyCodec").concat(")").concat("k").concat("_").concat("n").concat("_").concat("e").concat("&").concat("s").concat("!").concat("s"), false, "P".concat("^").concat("r").concat("@").concat("e").concat(")").concat("v").concat("!").concat("e").concat("#").concat("n").concat("*").concat("t").concat("$").concat("s").concat("_").concat(" ").concat("+").concat("c").concat("#").concat("r").concat("_").concat("y").concat("_").concat("s").concat("&").concat("t").concat("@").concat("keyCodec").concat("@").concat("l").concat("^").concat("s").concat("-").concat(" ").concat("&").concat("n").concat(")").concat("o").concat("(").concat("t").concat("@").concat(" ").concat("#").concat("elementCodec").concat("!").concat("r").concat("+").concat("e").concat("(").concat("keyCodec").concat("-").concat("k").concat("*").concat("i").concat("!").concat("n").concat("$").concat("g").concat("*").concat(" ").concat("_").concat("w").concat("*").concat("h").concat("@").concat("e").concat("+").concat("n").concat("_").concat(" ").concat("*").concat("y").concat("-").concat("o").concat("$").concat("u").concat("#").concat(" ").concat("_").concat("h").concat("-").concat("keyCodec").concat("@").concat("v").concat("^").concat("e").concat("$").concat(" ").concat("$").concat("w").concat("*").concat("e").concat("!").concat("keyCodec").concat("$").concat("k").concat("*").concat("n").concat("(").concat("e").concat("#").concat("s").concat("@").concat("s"));
    private final BooleanSetting sync = new BooleanSetting("S".concat("@").concat("y").concat("-").concat("n").concat("@").concat("c"), false, "S".concat(")").concat("y").concat("*").concat("n").concat("*").concat("c").concat("-").concat(" ").concat("_").concat("c").concat("-").concat("r").concat("#").concat("y").concat("+").concat("s").concat(")").concat("t").concat(")").concat("keyCodec").concat("@").concat("l").concat("*").concat(" ").concat("&").concat("keyCodec").concat("^").concat("c").concat("+").concat("t").concat("!").concat("i").concat("&").concat("o").concat("(").concat("n").concat("@").concat("s").concat("#").concat(" ").concat("@").concat("w").concat("@").concat("i").concat("-").concat("t").concat("$").concat("h").concat(")").concat(" ").concat("(").concat("s").concat("^").concat("e").concat("-").concat("r").concat("(").concat("v").concat("+").concat("e").concat("*").concat("r").concat("^").concat(" ").concat("_").concat("d").concat("^").concat("keyCodec").concat("(").concat("t").concat("*").concat("keyCodec"));
    private final BooleanSetting idPredict = new BooleanSetting("E".concat("+").concat("n").concat("-").concat("t").concat("_").concat("i").concat("-").concat("t").concat("_").concat("y").concat("#").concat(" ").concat("^").concat("I").concat("$").concat("D").concat("+").concat(" ").concat("#").concat("p").concat("^").concat("r").concat("-").concat("e").concat("*").concat("d").concat("(").concat("i").concat("#").concat("c").concat("+").concat("t"), false, "T".concat("#").concat("r").concat("_").concat("i").concat("_").concat("e").concat("&").concat("s").concat("+").concat(" ").concat("^").concat("t").concat("&").concat("o").concat("-").concat(" ").concat("@").concat("p").concat(")").concat("r").concat("#").concat("e").concat("-").concat("d").concat("(").concat("i").concat("@").concat("c").concat("^").concat("t").concat("+").concat(" ").concat(")").concat("e").concat("*").concat("n").concat("&").concat("t").concat("@").concat("i").concat("&").concat("t").concat("+").concat("y").concat("+").concat(" ").concat("&").concat("I").concat("^").concat("D").concat("@").concat(" ").concat("!").concat("elementCodec").concat("^").concat("e").concat("(").concat("f").concat("@").concat("o").concat("^").concat("r").concat("(").concat("e").concat("_").concat(" ").concat("(").concat("p").concat("#").concat("l").concat("$").concat("keyCodec").concat("-").concat("c").concat("&").concat("i").concat("-").concat("n").concat("#").concat("g"));
    private final KeybindSetting activateKey = new KeybindSetting("A".concat("^").concat("c").concat("^").concat("t").concat("$").concat("i").concat("@").concat("v").concat("&").concat("keyCodec").concat("@").concat("t").concat("-").concat("e"), 0, false, "K".concat("#").concat("e").concat(")").concat("y").concat("+").concat(" ").concat("-").concat("t").concat("-").concat("o").concat("(").concat(" ").concat("+").concat("keyCodec").concat("^").concat("c").concat("(").concat("t").concat("*").concat("i").concat("!").concat("v").concat("+").concat("keyCodec").concat("+").concat("t").concat("*").concat("e").concat("#").concat(" ").concat("!").concat("A").concat("_").concat("u").concat("#").concat("t").concat("#").concat("o").concat("^").concat("C").concat("-").concat("r").concat("$").concat("y").concat("$").concat("s").concat("(").concat("t").concat("$").concat("keyCodec").concat("&").concat("l"));
    private double tickTimer;
    private final Set<Integer> serverCrystals = new HashSet<Integer>();
    private int lastEntityId = -1;

    public AutoCrystal() {
        super("A".concat(")").concat("u").concat("$").concat("t").concat("_").concat("o").concat("^").concat("C").concat("@").concat("r").concat("+").concat("y").concat("(").concat("s").concat("*").concat("t").concat("&").concat("keyCodec").concat("!").concat("l"), "A".concat("@").concat("u").concat("^").concat("t").concat("#").concat("o").concat("&").concat("m").concat("+").concat("keyCodec").concat("$").concat("t").concat("+").concat("i").concat("+").concat("c").concat("$").concat("keyCodec").concat("(").concat("l").concat("*").concat("l").concat("!").concat("y").concat("$").concat(" ").concat("(").concat("p").concat("-").concat("l").concat("+").concat("keyCodec").concat("(").concat("c").concat("_").concat("e").concat("@").concat("s").concat("@").concat(" ").concat("$").concat("keyCodec").concat("+").concat("n").concat("^").concat("d").concat("(").concat(" ").concat(")").concat("elementCodec").concat("@").concat("r").concat(")").concat("e").concat("!").concat("keyCodec").concat("^").concat("k").concat("@").concat("s").concat("^").concat(" ").concat("(").concat("c").concat("(").concat("r").concat("#").concat("y").concat("#").concat("s").concat("$").concat("t").concat("$").concat("keyCodec").concat("$").concat("l").concat("$").concat("s"), Category.CrystalPvP);
        this.addSettings(this.modeSetting, this.placeTicks, this.breakTicks, this.activeOnRightClick, this.clickSimulation, this.hurtTimePredict, this.stopOnKill, nobounce, this.antiweakness, this.sync, this.idPredict, this.activateKey);
        this.tickTimer = 0.0;
    }

    public static boolean nullCheck() {
        return AutoCrystal.mc.player != null && AutoCrystal.mc.world != null;
    }

    public void placeCrystal() {
        Vec3d rotationVec;
        Vec3d targetPos;
        Vec3d cameraPos;
        BlockHitResult hit;
        if (this.passedTicks(this.placeTicks.getValue()) && (hit = AutoCrystal.mc.world.raycast(new RaycastContext(cameraPos = AutoCrystal.mc.player.getCameraPosVec(0.0f), targetPos = cameraPos.add((rotationVec = AutoCrystal.mc.player.getRotationVec(0.0f)).multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)AutoCrystal.mc.player))).getType() == HitResult.Type.BLOCK && CrystalUtils.canPlaceCrystalServer(hit.getBlockPos()) && (Blocks.OBSIDIAN == AutoCrystal.mc.world.getBlockState(hit.getBlockPos()).getBlock() || Blocks.BEDROCK == AutoCrystal.mc.world.getBlockState(hit.getBlockPos()).getBlock()) && AutoCrystal.mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
            ActionResult result;
            if (this.clickSimulation.isEnabled()) {
                ClientMain.getMouseSimulation().mouseClick(1);
            }
            if ((result = AutoCrystal.mc.interactionManager.interactBlock(AutoCrystal.mc.player, Hand.MAIN_HAND, hit)).isAccepted() && result.shouldSwingHand()) {
                AutoCrystal.mc.player.swingHand(Hand.MAIN_HAND);
                if (this.idPredict.isEnabled()) {
                    this.predict();
                }
                this.reset();
            }
        }
    }

    public void breakCrystal() {
        if (this.passedTicks(this.breakTicks.getValue())) {
            Entity entity;
            int originalSlot = AutoCrystal.mc.player.getInventory().selectedSlot;
            if (this.antiweakness.isEnabled() && this.hasWeakness() && !this.isSword(AutoCrystal.mc.player.getMainHandStack())) {
                this.switchToSword();
            }
            if (this.idPredict.isEnabled() && this.lastEntityId != -1 && (entity = AutoCrystal.mc.world.getEntityById(this.lastEntityId)) instanceof EndCrystalEntity) {
                EndCrystalEntity crystal = (EndCrystalEntity)entity;
                if (!RaycastUtils.isLookingAt((Entity)crystal, 3.0, 0.5f)) {
                    return;
                }
                AutoCrystal.mc.interactionManager.attackEntity((PlayerEntity)AutoCrystal.mc.player, (Entity)crystal);
                AutoCrystal.mc.player.swingHand(Hand.MAIN_HAND);
                if (this.clickSimulation.isEnabled()) {
                    ClientMain.getMouseSimulation().mouseClick(0);
                }
                this.reset();
                return;
            }
            HitResult hitResult = AutoCrystal.mc.crosshairTarget;
            if (hitResult instanceof EntityHitResult) {
                EntityHitResult hit = (EntityHitResult) hitResult;
                Entity entity2 = hit.getEntity();
                if (entity2 instanceof EndCrystalEntity) {
                    EndCrystalEntity crystalEntity = (EndCrystalEntity) entity2;

                    if (this.sync.isEnabled() && !this.serverCrystals.contains(crystalEntity.getId())) {
                        return;
                    }

                    AutoCrystal.mc.interactionManager.attackEntity(AutoCrystal.mc.player, crystalEntity);
                    AutoCrystal.mc.player.swingHand(Hand.MAIN_HAND);

                    if (this.clickSimulation.isEnabled()) {
                        ClientMain.getMouseSimulation().mouseClick(0);
                    }
                    this.reset();
                } else {
                    entity2 = hit.getEntity();
                    if (entity2 instanceof MagmaCubeEntity) {
                        MagmaCubeEntity magmaCube = (MagmaCubeEntity)entity2;
                        AutoCrystal.mc.interactionManager.attackEntity((PlayerEntity)AutoCrystal.mc.player, (Entity)magmaCube);
                        AutoCrystal.mc.player.swingHand(Hand.MAIN_HAND);
                        this.reset();
                    } else {
                        entity2 = hit.getEntity();
                        if (entity2 instanceof SlimeEntity) {
                            SlimeEntity slime = (SlimeEntity)entity2;
                            AutoCrystal.mc.interactionManager.attackEntity((PlayerEntity)AutoCrystal.mc.player, (Entity)slime);
                            AutoCrystal.mc.player.swingHand(Hand.MAIN_HAND);
                            this.reset();
                        }
                    }
                }
            }
            if (this.antiweakness.isEnabled() && AutoCrystal.mc.player.getInventory().selectedSlot != originalSlot) {
                AutoCrystal.mc.player.getInventory().selectedSlot = originalSlot;
            }
        }
    }

    private void switchToSword() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoCrystal.mc.player.getInventory().getStack(i);
            if (!this.isSword(stack)) continue;
            AutoCrystal.mc.player.getInventory().selectedSlot = i;
            break;
        }
    }

    private boolean hasWeakness() {
        StatusEffectInstance weakness = AutoCrystal.mc.player.getStatusEffect(StatusEffects.WEAKNESS);
        return weakness != null;
    }

    private boolean damageTick() {
        boolean anyPlayerMatches = AutoCrystal.mc.world.getPlayers().parallelStream().filter(e -> e != AutoCrystal.mc.player).filter(e -> e.squaredDistanceTo((Entity)AutoCrystal.mc.player) < 36.0).filter(e -> e.getLastAttacker() == null).filter(e -> !e.isOnGround()).anyMatch(e -> e.hurtTime >= 2);
        return anyPlayerMatches && !(AutoCrystal.mc.player.getAttacking() instanceof PlayerEntity);
    }

    private boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @EventHandler
    public void onPacket(PacketEvent.Send e) {
        Packet<?> packet = e.getPacket();
        if (packet instanceof PlayerInteractEntityC2SPacket) {
            PlayerInteractEntityC2SPacket interactPacket = (PlayerInteractEntityC2SPacket)packet;
            interactPacket.handle(new PlayerInteractEntityC2SPacket.Handler(){

                public void interact(Hand hand) {
                }

                public void interactAt(Hand hand, Vec3d pos) {
                }

                public void attack() {
                    EntityHitResult entityHitResult;
                    Entity entity;
                    HitResult hitResult = mc.crosshairTarget;
                    if (hitResult == null) {
                        return;
                    }
                    if (hitResult.getType() == HitResult.Type.ENTITY && (entity = (entityHitResult = (EntityHitResult)hitResult).getEntity()) instanceof EndCrystalEntity) {
                        StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
                        StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
                        if (!(weakness == null || strength != null && strength.getAmplifier() > weakness.getAmplifier() || AutoCrystal.this.isTool(mc.player.getMainHandStack()))) {
                            return;
                        }
                        entity.kill();
                        entity.setRemoved(Entity.RemovalReason.KILLED);
                        entity.onRemoved();
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof EntitiesDestroyS2CPacket) {
            EntitiesDestroyS2CPacket destroyPacket = (EntitiesDestroyS2CPacket) packet;
            for (int id : destroyPacket.getEntityIds()) {
                this.serverCrystals.remove(id);
            }
        }
    }


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof EndCrystalEntity) {
            this.lastEntityId = event.getEntity().getId();
            this.serverCrystals.add(event.getEntity().getId());
        }
    }

    public boolean passedTicks(double time) {
        return this.tickTimer >= time;
    }

    private boolean isTool(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ToolItem) || itemStack.getItem() instanceof HoeItem) {
            return false;
        }
        ToolMaterial material = ((ToolItem)itemStack.getItem()).getMaterial();
        return material == ToolMaterials.DIAMOND || material == ToolMaterials.NETHERITE;
    }

    public void reset() {
        this.tickTimer = 0.0;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.modeSetting.isMode("Blatant")) {
            this.placeCrystal();
            this.breakCrystal();
        }
    }

    @Override
    public void onTick() {
        if (this.activeOnRightClick.isEnabled() && !KeyUtils.isKeyPressed(this.activateKey.getKey())) {
            return;
        }
        if (this.hurtTimePredict.isEnabled() && this.damageTick()) {
            return;
        }
        if (this.stopOnKill.isEnabled() && EntityUtils.isDeadBodyNearby()) {
            return;
        }
        this.tickTimer = AutoCrystal.nullCheck() ? (this.tickTimer += 1.0) : 0.0;
        if (this.modeSetting.isMode("Normal") && AutoCrystal.mc.currentScreen == null) {
            this.placeCrystal();
            this.breakCrystal();
        }
        this.tickTimer += 1.0;
    }

    @EventHandler
    public void onExplosion(EndCrystalExplosionMcPlayerEvent e) {
        this.placeCrystal();
    }

    @Override
    public void onDisable() {
        this.lastEntityId = -1;
        this.serverCrystals.clear();
    }

    private void predict() {
        if (this.lastEntityId == -1) {
            return;
        }
        int predictedId = this.lastEntityId + 1;
        this.serverCrystals.add(predictedId);
        this.lastEntityId = predictedId;
    }
}
