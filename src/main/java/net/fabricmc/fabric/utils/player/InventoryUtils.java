package net.fabricmc.fabric.utils.player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.mixin.IClientPlayerInteractionManagerAccessor;
import net.fabricmc.fabric.utils.player.FindItemResult;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.BlastFurnaceScreenHandler;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public abstract class InventoryUtils {
    private static final Action ACTION = new Action();
    public static int previousSlot = -1;
    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;
    public static final int OFFHAND = 40;
    public static final int MAIN_START = 9;
    public static final int MAIN_END = 35;
    public static final int ARMOR_START = 36;
    public static final int ARMOR_END = 39;
    public static Map<ArmorItem.Type, Integer> slotNumByType = new HashMap<ArmorItem.Type, Integer>();

    public static boolean useItem(Item item) {
        PlayerInventory inventory = ClientMain.mc.player.getInventory();
        for (int i = 0; i <= 8; ++i) {
            if (!inventory.getStack(i).isOf(item)) continue;
            inventory.selectedSlot = i;
            return true;
        }
        return false;
    }

    public static int find(Class<? extends ToolItem> toolClass, boolean hotbar) {
        if (ClientMain.mc.player == null) {
            return -1;
        }
        PlayerInventory inventory = ClientMain.mc.player.getInventory();
        int start = hotbar ? 0 : 0;
        int end = hotbar ? 9 : inventory.main.size();
        for (int i = start; i < end; ++i) {
            ItemStack stack = inventory.getStack(i);
            if (!(stack.getItem() instanceof ToolItem) || !toolClass.isInstance(stack.getItem())) continue;
            return i;
        }
        return -1;
    }

    public static boolean swap(Item item) {
        PlayerInventory inv = ClientMain.mc.player.getInventory();
        for (int i = 0; i <= 8; ++i) {
            if (!inv.getStack(i).isOf(item)) continue;
            inv.selectedSlot = i;
            return true;
        }
        return false;
    }

    public static boolean hasItem(Item item) {
        PlayerInventory inv = ClientMain.mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            if (!inv.getStack(i).isOf(item)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasItemInInv(Predicate<Item> item) {
        PlayerInventory inv = ClientMain.mc.player.getInventory();
        boolean count = false;
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            return true;
        }
        return false;
    }

    public static Integer findPotion(int slot, int slotto, StatusEffects statusEffect) {
        for (int i = slot; i < slotto; ++i) {
            ItemStack itemStack = ClientMain.mc.player.getInventory().getStack(i);
            if (itemStack.getItem() != Items.SPLASH_POTION || statusEffect != null && !InventoryUtils.hasStatusEffect(itemStack, statusEffect)) continue;
            return i;
        }
        return null;
    }

    public static boolean hasStatusEffect(ItemStack itemStack, StatusEffects type) {
        if (itemStack.getItem() instanceof SplashPotionItem) {
            for (StatusEffectInstance statusEffectInstance : ((PotionContentsComponent)itemStack.get(DataComponentTypes.POTION_CONTENTS)).getEffects()) {
                if (statusEffectInstance.getEffectType() != type) continue;
                return true;
            }
        }
        return false;
    }

    public static FindItemResult find(Item ... items) {
        return InventoryUtils.find((ItemStack itemStack) -> {
            for (Item item : items) {
                if (itemStack.getItem() != item) continue;
                return true;
            }
            return false;
        });
    }

    public static boolean isArmorBetter(ItemStack armorStack) {
        ItemStack currentArmor = ClientMain.mc.player.getInventory().getArmorStack(slotNumByType.get(((ArmorItem)armorStack.getItem()).getType()).intValue());
        if (currentArmor == null || !(currentArmor.getItem() instanceof ArmorItem)) {
            return true;
        }
        return InventoryUtils.isArmorBetter(currentArmor, armorStack, (RegistryWrapper<Enchantment>)((RegistryWrapper)ClientMain.mc.player.getRegistryManager().get(RegistryKeys.ENCHANTMENT)));
    }

    public static boolean isArmorBetter(ItemStack fromArmorStack, ItemStack thenArmorStack, RegistryWrapper<Enchantment> registry) {
        ArmorItem fromArmorItem;
        Item item;
        block10: {
            block9: {
                item = fromArmorStack.getItem();
                if (!(item instanceof ArmorItem)) break block9;
                fromArmorItem = (ArmorItem)item;
                item = thenArmorStack.getItem();
                if (item instanceof ArmorItem) break block10;
            }
            return false;
        }
        ArmorItem thenArmorItem = (ArmorItem)item;
        float fromPoints = (float)fromArmorItem.getProtection() + fromArmorItem.getToughness();
        float thenPoints = (float)thenArmorItem.getProtection() + thenArmorItem.getToughness();
        if (fromPoints < thenPoints) {
            return true;
        }
        fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.PROTECTION, registry);
        fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.UNBREAKING, registry);
        thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.PROTECTION, registry);
        thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.UNBREAKING, registry);
        if ((fromPoints += (float)(InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.MENDING, registry) * 2)) == (thenPoints += (float)(InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.MENDING, registry) * 2))) {
            switch (fromArmorItem.getType()) {
                case HELMET: {
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.AQUA_AFFINITY, registry);
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.RESPIRATION, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.AQUA_AFFINITY, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.RESPIRATION, registry);
                    break;
                }
                case BOOTS: {
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.DEPTH_STRIDER, registry);
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.SWIFT_SNEAK, registry);
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.SOUL_SPEED, registry);
                    fromPoints += (float)InventoryUtils.getEnchantmentLevel(fromArmorStack, (RegistryKey<Enchantment>)Enchantments.FEATHER_FALLING, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.DEPTH_STRIDER, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.SWIFT_SNEAK, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.SOUL_SPEED, registry);
                    thenPoints += (float)InventoryUtils.getEnchantmentLevel(thenArmorStack, (RegistryKey<Enchantment>)Enchantments.FEATHER_FALLING, registry);
                }
            }
        }
        return fromPoints < thenPoints;
    }

    public static int getEnchantmentLevel(ItemStack armorStack, RegistryKey<Enchantment> enchantmentKey, RegistryWrapper<Enchantment> registry) {
        RegistryEntry.Reference enchantmentEntry = registry.getOrThrow(enchantmentKey);
        if (enchantmentEntry != null && EnchantmentHelper.hasEnchantments((ItemStack)armorStack)) {
            return EnchantmentHelper.getLevel((RegistryEntry)enchantmentEntry, (ItemStack)armorStack);
        }
        return 0;
    }

    public static FindItemResult find(Predicate<ItemStack> isGood) {
        return InventoryUtils.find(isGood, 0, ClientMain.mc.player.getInventory().size());
    }

    public static FindItemResult find(Predicate<ItemStack> isGood, int start, int end) {
        int slot = -1;
        int count = 0;
        for (int i = start; i <= end; ++i) {
            ItemStack stack = ClientMain.mc.player.getInventory().getStack(i);
            if (!isGood.test(stack)) continue;
            if (slot == -1) {
                slot = i;
            }
            count += stack.getCount();
        }
        return new FindItemResult(slot, count);
    }

    public static void setInvSlot(int slot) {
        ClientMain.mc.player.getInventory().selectedSlot = slot;
        ((IClientPlayerInteractionManagerAccessor)ClientMain.mc.interactionManager).callSyncSelectedSlot();
    }

    public static FindItemResult findFastestTool(BlockState state) {
        float bestScore = -1.0f;
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            float score = ClientMain.mc.player.getInventory().getStack(i).getMiningSpeedMultiplier(state);
            if (!(score > bestScore)) continue;
            bestScore = score;
            slot = i;
        }
        return new FindItemResult(slot, 1);
    }

    public static boolean isHolding(Item item) {
        return InventoryUtils.isHolding(item, Hand.MAIN_HAND);
    }

    public static boolean isHolding(Item item, Hand hand) {
        return ClientMain.mc.player.getStackInHand(hand).isOf(item);
    }

    public static boolean nameContains(String contains) {
        return InventoryUtils.nameContains(contains, Hand.MAIN_HAND);
    }

    public static boolean nameContains(String contains, Hand hand) {
        ItemStack item = ClientMain.mc.player.getStackInHand(hand);
        return item != null && item.getTranslationKey().toLowerCase().contains(contains.toLowerCase());
    }

    public static boolean nameContains(String contains, ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains(contains.toLowerCase()) && stack.getName().getString().contains(contains);
    }

    public static void forEachItem(Consumer<ItemStack> run) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = ClientMain.mc.player.getInventory().getStack(i);
            if (item == null || item.isEmpty()) continue;
            run.accept(item);
        }
    }

    public static ItemStack[] getContents() {
        PlayerInventory inv = ClientMain.mc.player.getInventory();
        ItemStack[] stacks = new ItemStack[]{};
        for (int i = 0; i < 9; ++i) {
            stacks[i] = inv.getStack(i);
        }
        return stacks;
    }

    public static void switchItemTo(Item item, Hand hand) {
        int slot = InventoryUtils.findItemSlot(item);
        if (slot != -1) {
            InventoryUtils.swap(Item.byRawId((int)slot));
            ClientMain.mc.player.getInventory().selectedSlot = slot;
            ClientMain.mc.interactionManager.clickSlot(ClientMain.mc.player.playerScreenHandler.syncId, 36 + slot, 0, SlotActionType.PICKUP, (PlayerEntity)ClientMain.mc.player);
            ClientMain.mc.player.setStackInHand(hand, ClientMain.mc.player.getInventory().getStack(slot));
        }
    }

    public static int findItemSlot(Item item) {
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = ClientMain.mc.player.getInventory().getStack(i);
            if (stack.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static FindItemResult findItemSlot(Predicate<ItemStack> predicate) {
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = ClientMain.mc.player.getInventory().getStack(i);
            if (!predicate.test(stack)) continue;
            return new FindItemResult(i, stack.getCount());
        }
        return null;
    }

    public static int indexToId(int i) {
        if (ClientMain.mc.player == null) {
            return -1;
        }
        ScreenHandler handler = ClientMain.mc.player.currentScreenHandler;
        if (handler instanceof PlayerScreenHandler) {
            return InventoryUtils.survivalInventory(i);
        }
        if (handler instanceof GenericContainerScreenHandler) {
            return InventoryUtils.genericContainer(i, ((GenericContainerScreenHandler)handler).getRows());
        }
        if (handler instanceof CraftingScreenHandler) {
            return InventoryUtils.craftingTable(i);
        }
        if (handler instanceof FurnaceScreenHandler) {
            return InventoryUtils.furnace(i);
        }
        if (handler instanceof BlastFurnaceScreenHandler) {
            return InventoryUtils.furnace(i);
        }
        if (handler instanceof SmokerScreenHandler) {
            return InventoryUtils.furnace(i);
        }
        if (handler instanceof Generic3x3ContainerScreenHandler) {
            return InventoryUtils.generic3x3(i);
        }
        if (handler instanceof EnchantmentScreenHandler) {
            return InventoryUtils.enchantmentTable(i);
        }
        if (handler instanceof BrewingStandScreenHandler) {
            return InventoryUtils.brewingStand(i);
        }
        if (handler instanceof MerchantScreenHandler) {
            return InventoryUtils.villager(i);
        }
        if (handler instanceof BeaconScreenHandler) {
            return InventoryUtils.beacon(i);
        }
        if (handler instanceof AnvilScreenHandler) {
            return InventoryUtils.anvil(i);
        }
        if (handler instanceof HopperScreenHandler) {
            return InventoryUtils.hopper(i);
        }
        if (handler instanceof ShulkerBoxScreenHandler) {
            return InventoryUtils.genericContainer(i, 3);
        }
        if (handler instanceof CartographyTableScreenHandler) {
            return InventoryUtils.cartographyTable(i);
        }
        if (handler instanceof GrindstoneScreenHandler) {
            return InventoryUtils.grindstone(i);
        }
        if (handler instanceof LecternScreenHandler) {
            return InventoryUtils.lectern();
        }
        if (handler instanceof LoomScreenHandler) {
            return InventoryUtils.loom(i);
        }
        if (handler instanceof StonecutterScreenHandler) {
            return InventoryUtils.stonecutter(i);
        }
        return -1;
    }

    public static Action click() {
        InventoryUtils.ACTION.type = SlotActionType.PICKUP;
        return ACTION;
    }

    public static Action move() {
        InventoryUtils.ACTION.type = SlotActionType.PICKUP;
        InventoryUtils.ACTION.two = true;
        return ACTION;
    }

    private static int survivalInventory(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 36 + i;
        }
        if (InventoryUtils.isArmor(i)) {
            return 5 + (i - 36);
        }
        return i;
    }

    private static int genericContainer(int i, int rows) {
        if (InventoryUtils.isHotbar(i)) {
            return (rows + 3) * 9 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return rows * 9 + (i - 9);
        }
        return -1;
    }

    private static int craftingTable(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 37 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return i + 1;
        }
        return -1;
    }

    private static int furnace(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 30 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }

    private static int generic3x3(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 36 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return i;
        }
        return -1;
    }

    private static int enchantmentTable(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 29 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 2 + (i - 9);
        }
        return -1;
    }

    private static int brewingStand(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 32 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 5 + (i - 9);
        }
        return -1;
    }

    private static int villager(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 30 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }

    private static int beacon(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 28 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 1 + (i - 9);
        }
        return -1;
    }

    private static int anvil(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 30 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }

    private static int hopper(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 32 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 5 + (i - 9);
        }
        return -1;
    }

    private static int cartographyTable(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 30 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }

    private static int grindstone(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 30 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }

    private static int lectern() {
        return -1;
    }

    private static int loom(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 31 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 4 + (i - 9);
        }
        return -1;
    }

    private static int stonecutter(int i) {
        if (InventoryUtils.isHotbar(i)) {
            return 29 + i;
        }
        if (InventoryUtils.isMain(i)) {
            return 2 + (i - 9);
        }
        return -1;
    }

    public static boolean isHotbar(int i) {
        return i >= 0 && i <= 8;
    }

    public static boolean isMain(int i) {
        return i >= 9 && i <= 35;
    }

    public static boolean isArmor(int i) {
        return i >= 36 && i <= 39;
    }

    static {
        slotNumByType.put(ArmorItem.Type.HELMET, 3);
        slotNumByType.put(ArmorItem.Type.CHESTPLATE, 2);
        slotNumByType.put(ArmorItem.Type.LEGGINGS, 1);
        slotNumByType.put(ArmorItem.Type.BOOTS, 0);
    }

    public static class Action {
        private SlotActionType type = null;
        private boolean two = false;
        private int from = -1;
        private int to = -1;
        private int data = 0;
        private boolean isRecursive = false;

        private Action() {
        }

        public Action fromId(int id) {
            this.from = id;
            return this;
        }

        public Action from(int index) {
            return this.fromId(InventoryUtils.indexToId(index));
        }

        public Action fromHotbar(int i) {
            return this.from(0 + i);
        }

        public Action fromOffhand() {
            return this.from(40);
        }

        public Action fromMain(int i) {
            return this.from(9 + i);
        }

        public Action fromArmor(int i) {
            return this.from(36 + (3 - i));
        }

        public void toId(int id) {
            this.to = id;
            this.run();
        }

        public void to(int index) {
            this.toId(InventoryUtils.indexToId(index));
        }

        public void toHotbar(int i) {
            this.to(0 + i);
        }

        public void toOffhand() {
            this.to(40);
        }

        public void toMain(int i) {
            this.to(9 + i);
        }

        public void toArmor(int i) {
            this.to(36 + (3 - i));
        }

        public void slotId(int id) {
            this.from = this.to = id;
            this.run();
        }

        public void slot(int index) {
            this.slotId(InventoryUtils.indexToId(index));
        }

        public void slotHotbar(int i) {
            this.slot(0 + i);
        }

        public void slotOffhand() {
            this.slot(40);
        }

        public void slotMain(int i) {
            this.slot(9 + i);
        }

        public void slotArmor(int i) {
            this.slot(36 + (3 - i));
        }

        public void run() {
            boolean hadEmptyCursor = ClientMain.mc.player.currentScreenHandler.getCursorStack().isEmpty();
            if (this.type != null && this.from != -1 && this.to != -1) {
                this.click(this.from);
                if (this.two) {
                    this.click(this.to);
                }
            }
            SlotActionType preType = this.type;
            boolean preTwo = this.two;
            int preFrom = this.from;
            int preTo = this.to;
            this.type = null;
            this.two = false;
            this.from = -1;
            this.to = -1;
            this.data = 0;
            if (!this.isRecursive && hadEmptyCursor && preType == SlotActionType.PICKUP && preTwo && preFrom != -1 && preTo != -1 && !ClientMain.mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                this.isRecursive = true;
                InventoryUtils.click().slotId(preFrom);
                this.isRecursive = false;
            }
        }

        private void click(int id) {
            ClientMain.mc.interactionManager.clickSlot(ClientMain.mc.player.currentScreenHandler.syncId, id, this.data, this.type, (PlayerEntity)ClientMain.mc.player);
        }
    }
}
