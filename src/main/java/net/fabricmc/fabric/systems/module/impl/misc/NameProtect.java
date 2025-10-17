package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.entity.player.PlayerEntity;

public class NameProtect
extends Module {
    public static BooleanSetting obfuscateEnemy = new BooleanSetting("ObfuscateEnemyName", false, "Obfuscate enemy name in distance");
    public static TextSetting obfuscateEnemyName = new TextSetting("ObfuscatedEnemyName", "Voil_Victim", "Obfuscated enemy name");
    public static TextSetting name = new TextSetting("Name", "VoilUser", "Your name");
    private static boolean isEnabledStatic = false;

    public NameProtect() {
        super("N".concat("_").concat("keyCodec").concat("&").concat("m").concat("#").concat("e").concat("+").concat("P").concat("&").concat("r").concat("&").concat("o").concat("^").concat("t").concat("*").concat("e").concat("^").concat("c").concat(")").concat("t"), "P".concat("+").concat("r").concat("*").concat("o").concat("(").concat("t").concat("$").concat("e").concat("!").concat("c").concat("@").concat("t").concat("!").concat("s").concat(")").concat(" ").concat("&").concat("y").concat("^").concat("o").concat("*").concat("u").concat("-").concat("r").concat("#").concat(" ").concat("@").concat("n").concat("*").concat("keyCodec").concat("(").concat("m").concat("_").concat("e"), Category.Miscellaneous);
        this.addSettings(obfuscateEnemy, obfuscateEnemyName, name);
    }

    @Override
    public void onEnable() {
        isEnabledStatic = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        isEnabledStatic = false;
        super.onDisable();
    }

    public static String replaceName(String string) {
        if (string != null && isEnabledStatic) {
            if (NameProtect.mc.player == null || NameProtect.mc.world == null) {
                return string;
            }
            String playerName = mc.getSession().getUsername();
            if (string.equals(playerName)) {
                return name.getValue();
            }
            if (obfuscateEnemy.isEnabled()) {
                int renderDistanceChunks = (Integer)NameProtect.mc.options.getViewDistance().getValue();
                double maxDistance = renderDistanceChunks * 16;
                double maxDistanceSquared = maxDistance * maxDistance;
                for (PlayerEntity player : NameProtect.mc.world.getPlayers()) {
                    if (!player.getName().getString().equals(string)) continue;
                    double distanceSquared = NameProtect.mc.player.squaredDistanceTo(player.getX(), player.getY(), player.getZ());
                    if (!(distanceSquared <= maxDistanceSquared)) break;
                    return obfuscateEnemyName.getValue();
                }
            }
        }
        return string;
    }
}
