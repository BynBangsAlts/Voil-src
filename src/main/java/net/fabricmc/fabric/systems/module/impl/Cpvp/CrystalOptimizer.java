package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class CrystalOptimizer
extends Module {
    public CrystalOptimizer() {
        super("C".concat("^").concat("r").concat("_").concat("y").concat("@").concat("s").concat("@").concat("t").concat("^").concat("keyCodec").concat("*").concat("l").concat("-").concat("O").concat("&").concat("p").concat("+").concat("t").concat("(").concat("i").concat("(").concat("m").concat("_").concat("i").concat("*").concat("z").concat("$").concat("e").concat("*").concat("r"), "B".concat("+").concat("r").concat(")").concat("e").concat("@").concat("keyCodec").concat("^").concat("k").concat("-").concat("s").concat("+").concat(" ").concat("@").concat("c").concat(")").concat("r").concat(")").concat("y").concat("&").concat("s").concat("*").concat("t").concat("&").concat("keyCodec").concat("-").concat("l").concat("!").concat("s").concat("+").concat(" ").concat("*").concat("c").concat("*").concat("l").concat("*").concat("i").concat("!").concat("e").concat("!").concat("n").concat("*").concat("t").concat("(").concat(" ").concat("#").concat("s").concat("!").concat("i").concat("+").concat("d").concat("-").concat("e"), Category.CrystalPvP);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean isTool(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ToolItem) || itemStack.getItem() instanceof HoeItem) {
            return false;
        }
        ToolMaterial material = ((ToolItem)itemStack.getItem()).getMaterial();
        return material == ToolMaterials.DIAMOND || material == ToolMaterials.NETHERITE;
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
                        if (!(weakness == null || strength != null && strength.getAmplifier() > weakness.getAmplifier() || CrystalOptimizer.this.isTool(mc.player.getMainHandStack()))) {
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
}
