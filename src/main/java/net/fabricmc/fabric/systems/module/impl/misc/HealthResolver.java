package net.fabricmc.fabric.systems.module.impl.misc;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoCrystal;
import net.fabricmc.fabric.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.world.GameMode;

public class HealthResolver
extends Module {
    private ModeSetting mode = new ModeSetting("M".concat("&").concat("o").concat("+").concat("d").concat("$").concat("e"), "T".concat("&").concat("keyCodec").concat("$").concat("elementCodec"), "U".concat("!").concat("p").concat("@").concat("d").concat("&").concat("keyCodec").concat("_").concat("t").concat("+").concat("e").concat("#").concat("s").concat("*").concat(" ").concat("+").concat("h").concat("+").concat("e").concat("&").concat("keyCodec").concat("!").concat("l").concat("+").concat("t").concat("_").concat("h").concat("#").concat(" ").concat("*").concat("o").concat("^").concat("f").concat("^").concat(" ").concat(")").concat("p").concat("_").concat("l").concat("^").concat("keyCodec").concat("&").concat("y").concat("*").concat("e").concat("&").concat("r").concat("*").concat("s").concat("@").concat(" ").concat("_").concat("f").concat("!").concat("r").concat("*").concat("o").concat("-").concat("m").concat("$").concat(" ").concat("@").concat("t").concat(")").concat("keyCodec").concat("-").concat("elementCodec"), "C".concat("!").concat("keyCodec").concat("@").concat("l").concat("(").concat("c").concat("#").concat("u").concat("*").concat("l").concat(")").concat("keyCodec").concat("@").concat("t").concat("&").concat("e"), "T".concat("+").concat("keyCodec").concat("$").concat("elementCodec"));
    public ConcurrentHashMap<LivingEntity, Float> calculatedDamage = new ConcurrentHashMap();
    public ConcurrentHashMap<LivingEntity, Float> serverCalcHealth = new ConcurrentHashMap();

    public HealthResolver() {
        super("H".concat("!").concat("e").concat("&").concat("keyCodec").concat("@").concat("l").concat(")").concat("t").concat("-").concat("h").concat("(").concat("R").concat("&").concat("e").concat("^").concat("s").concat("!").concat("o").concat("^").concat("l").concat("!").concat("v").concat("@").concat("e").concat("^").concat("r"), "U".concat("&").concat("p").concat("#").concat("d").concat("&").concat("keyCodec").concat("&").concat("t").concat("#").concat("e").concat(")").concat("s").concat("^").concat(" ").concat("$").concat("h").concat(")").concat("e").concat("&").concat("keyCodec").concat("(").concat("l").concat("*").concat("t").concat(")").concat("h").concat("-").concat(" ").concat("@").concat("o").concat("@").concat("f").concat("_").concat(" ").concat("^").concat("p").concat("-").concat("l").concat("^").concat("keyCodec").concat("&").concat("y").concat("&").concat("e").concat(")").concat("r").concat("(").concat("s").concat("$").concat(" ").concat("$").concat("f").concat("_").concat("r").concat("(").concat("o").concat("&").concat("m").concat("&").concat(" ").concat("(").concat("t").concat("_").concat("keyCodec").concat("*").concat("elementCodec"), Category.Miscellaneous);
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        this.calculatedDamage.clear();
        this.serverCalcHealth.clear();
    }

    @EventHandler
    private void onAttack(AttackEntityEvent.Pre event) {
        if (!AutoCrystal.nullCheck() || HealthResolver.mc.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
            return;
        }
        Entity ent = event.target;
        if (ent instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity)ent;
            if (PlayerUtils.isBlockedByShield((LivingEntity)HealthResolver.mc.player, entity, false)) {
                return;
            }
            if (!this.serverCalcHealth.containsKey(entity)) {
                this.serverCalcHealth.put(entity, Float.valueOf(entity.getMaxHealth()));
            }
        }
    }

    @Override
    public void onTick() {
        if (HealthResolver.mc.world == null) {
            return;
        }
        for (Entity e : HealthResolver.mc.world.getEntities()) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)e;
                switch (this.mode.getMode()) {
                    case "Calculate": {
                        if (!this.serverCalcHealth.containsKey(le)) break;
                        if ((double)this.serverCalcHealth.get(le).floatValue() <= 0.0) {
                            this.serverCalcHealth.put(le, Float.valueOf(le.getMaxHealth()));
                            break;
                        }
                        le.setHealth(this.serverCalcHealth.get(le).floatValue());
                        break;
                    }
                    case "Tab": {
                        Collection<ScoreboardObjective> sb = HealthResolver.mc.world.getScoreboard().getObjectives();

                        for (ScoreboardObjective so : sb) {
                            Object2IntMap<ScoreboardObjective> map = so.getScoreboard().getScoreHolderObjectives((ScoreHolder) le);

                            for (Object2IntMap.Entry<ScoreboardObjective> entry : map.object2IntEntrySet()) {
                                ScoreboardObjective key = entry.getKey();
                                int value = entry.getIntValue();

                                if (!key.getDisplayName().getString().equals("HEALTH_MODULE_LIST")) continue;

                                le.setHealth((float) value);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event) {
        LivingEntity le;
        Entity e;
        if (HealthResolver.mc.world == null) {
            return;
        }
        if (event.packet instanceof EntitySpawnS2CPacket && (e = HealthResolver.mc.world.getEntityById(((EntitySpawnS2CPacket)event.packet).getEntityId())) instanceof LivingEntity && this.serverCalcHealth.containsKey(le = (LivingEntity)e)) {
            this.serverCalcHealth.put(le, Float.valueOf(le.getMaxHealth()));
        }
        if (event.packet instanceof EntitiesDestroyS2CPacket) {
            IntListIterator intListIterator = ((EntitiesDestroyS2CPacket)event.packet).getEntityIds().iterator();
            while (intListIterator.hasNext()) {
                int id = (Integer)intListIterator.next();
                Entity e2 = HealthResolver.mc.world.getEntityById(id);
                if (!(e2 instanceof LivingEntity)) continue;
                this.serverCalcHealth.remove((LivingEntity)e2);
            }
        }
    }
}
