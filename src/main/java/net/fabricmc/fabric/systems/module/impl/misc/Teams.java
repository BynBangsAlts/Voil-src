package net.fabricmc.fabric.systems.module.impl.misc;

import java.util.regex.Pattern;
import net.fabricmc.fabric.managers.SocialManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Teams
extends Module {
    private static final Pattern COLOR_PATTERN = Pattern.compile("\u00a7[0-9A-Fk-o]", 2);

    public Teams() {
        super("T".concat("^").concat("e").concat("(").concat("keyCodec").concat(")").concat("m").concat("@").concat("s"), "M".concat("@").concat("keyCodec").concat("!").concat("k").concat("!").concat("e").concat("+").concat("s").concat("+").concat(" ").concat("^").concat("y").concat("_").concat("o").concat("_").concat("u").concat(")").concat(" ").concat("_").concat("n").concat("!").concat("o").concat("&").concat("t").concat("(").concat(" ").concat(")").concat("keyCodec").concat("_").concat("t").concat("+").concat("t").concat("^").concat("keyCodec").concat(")").concat("c").concat("+").concat("k").concat("@").concat(" ").concat("#").concat("y").concat("(").concat("o").concat("+").concat("u").concat(")").concat("r").concat("*").concat(" ").concat("#").concat("t").concat("(").concat("e").concat("_").concat("keyCodec").concat("-").concat("m").concat("#").concat("m").concat("#").concat("keyCodec").concat("-").concat("t").concat("$").concat("e").concat("&").concat("s"), Category.Miscellaneous);
    }

    @Override
    public void onEnable() {
        SocialManager.clearTeammates();
    }

    @Override
    public void onDisable() {
        SocialManager.clearTeammates();
    }

    @Override
    public void onTick() {
        if (Teams.mc.world == null || Teams.mc.player == null) {
            return;
        }
        Teams.mc.world.getPlayers().stream().filter(this::isSameTeam).forEach(player -> SocialManager.addTeammate(player.getUuid()));
    }

    public boolean isSameTeam(LivingEntity entity) {
        if (Teams.mc.player.isTeammate((Entity)entity)) {
            return true;
        }
        Text clientDisplayName = Teams.mc.player.getDisplayName();
        Text targetDisplayName = entity.getDisplayName();
        if (clientDisplayName == null || targetDisplayName == null) {
            return false;
        }
        return this.checkName(clientDisplayName, targetDisplayName) || this.checkPrefix(targetDisplayName, clientDisplayName) || entity instanceof PlayerEntity && this.checkArmor((PlayerEntity)entity);
    }

    private boolean checkName(Text clientDisplayName, Text targetDisplayName) {
        Style targetStyle = targetDisplayName.getStyle();
        Style clientStyle = clientDisplayName.getStyle();
        return targetStyle.getColor() != null && clientStyle.getColor() != null && targetStyle.getColor().equals((Object)clientStyle.getColor());
    }

    private boolean checkPrefix(Text targetDisplayName, Text clientDisplayName) {
        String targetName = Teams.stripMinecraftColorCodes(targetDisplayName.getString());
        String clientName = Teams.stripMinecraftColorCodes(clientDisplayName.getString());
        String[] targetSplit = targetName.split(" ");
        String[] clientSplit = clientName.split(" ");
        return targetSplit.length > 1 && clientSplit.length > 1 && targetSplit[0].equals(clientSplit[0]);
    }

    private boolean checkArmor(PlayerEntity player) {
        return this.matchesArmorColor(player, 3) || this.matchesArmorColor(player, 2);
    }

    private boolean matchesArmorColor(PlayerEntity player, int armorSlot) {
        ItemStack ownStack = Teams.mc.player.getInventory().getArmorStack(armorSlot);
        ItemStack otherStack = player.getInventory().getArmorStack(armorSlot);
        Integer ownColor = this.getArmorColor(ownStack);
        Integer otherColor = this.getArmorColor(otherStack);
        return ownColor != null && ownColor.equals(otherColor);
    }

    private Integer getArmorColor(ItemStack stack) {
        if (stack != null && stack.isIn(ItemTags.DYEABLE)) {
            return DyedColorComponent.getColor((ItemStack)stack, (int)-6265536);
        }
        return null;
    }

    public static String stripMinecraftColorCodes(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
