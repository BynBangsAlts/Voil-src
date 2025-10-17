package net.fabricmc.fabric.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Random;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.security.checks.SecurityManager;
import net.fabricmc.fabric.systems.module.impl.render.SwingAnimation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;

public class Utils {
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]|,.<>/?";
    private static final Random RANDOM = new Random();

    public static char randomSymbols() {
        int index = RANDOM.nextInt(SYMBOLS.length());
        return SYMBOLS.charAt(index);
    }

    public static boolean isWeaponBlocking(LivingEntity entity) {
        return entity.isUsingItem() && Utils.canWeaponBlock(entity);
    }

    public static boolean canWeaponBlock(LivingEntity entity) {
        return ModuleManager.INSTANCE.getModuleByClass(SwingAnimation.class).isEnabled() && (entity.getMainHandStack().getItem() instanceof SwordItem || entity.getMainHandStack().getItem() instanceof AxeItem) && entity.getOffHandStack().getItem() instanceof ShieldItem || (entity.getOffHandStack().getItem() instanceof SwordItem || entity.getOffHandStack().getItem() instanceof AxeItem) && entity.getMainHandStack().getItem() instanceof ShieldItem;
    }

    public static File getVoilJar() throws URISyntaxException {
        return new File(SecurityManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public static float getTick() {
        return ClientMain.mc.getRenderTickCounter().getTickDelta(false);
    }
}
