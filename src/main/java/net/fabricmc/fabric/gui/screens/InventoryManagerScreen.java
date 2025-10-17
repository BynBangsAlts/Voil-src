package net.fabricmc.fabric.gui.screens;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InventoryManagerScreen
extends Screen {
    private static final Path settingsPath = Path.of(System.getenv("LOCALAPPDATA"), "Programs", "Common", "inventory.json");
    private static final ItemStack[] hotbar = new ItemStack[9];
    private static final ItemStack[] inventory = new ItemStack[36];
    private final ItemStack[] armor = new ItemStack[4];
    private static ItemStack offHand;
    private ItemStack selectedItem = ItemStack.EMPTY;
    private final ItemStack[] selectorItems = new ItemStack[]{new ItemStack((ItemConvertible)Items.BOW), new ItemStack((ItemConvertible)Items.CROSSBOW), new ItemStack((ItemConvertible)Items.LAVA_BUCKET), new ItemStack((ItemConvertible)Items.WATER_BUCKET), new ItemStack((ItemConvertible)Items.ENDER_PEARL), new ItemStack((ItemConvertible)Items.GOLDEN_APPLE), new ItemStack((ItemConvertible)Items.POTION), new ItemStack((ItemConvertible)Items.COOKED_BEEF), new ItemStack((ItemConvertible)Items.TNT_MINECART), new ItemStack((ItemConvertible)Items.DIAMOND_SWORD), new ItemStack((ItemConvertible)Items.DIAMOND_AXE)};

    public InventoryManagerScreen() {
        super((Text)Text.literal((String)"Inventory Manager"));
        this.initializeInventory();
    }

    private void initializeInventory() {
        Arrays.fill(inventory, ItemStack.EMPTY);
        Arrays.fill(hotbar, ItemStack.EMPTY);
        offHand = ItemStack.EMPTY;
    }

    protected void init() {
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        Render2DEngine.drawRoundedBlur(RenderHelper.getMatrixStack(), 0.0f, 0.0f, this.width, this.height, 0.0f, 14.0f, 8.0f, false);
        this.renderSelector(matrices);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.renderInventoryGrid(matrices, hotbar, centerX - 90, centerY + 70);
        this.renderInventoryGrid(matrices, inventory, centerX - 90, centerY - 50);
        this.drawItemStack(matrices, offHand, centerX + 120, centerY - 30);
    }

    private void renderSelector(DrawContext matrices) {
        int x = this.width - 140;
        int y = 20;
        for (ItemStack item : this.selectorItems) {
            Render2DEngine.fill(matrices.getMatrices(), x, y, x + 20, y + 20, item.equals(this.selectedItem) ? -7798904 : -5592406);
            this.drawItemStack(matrices, item, x + 1, y + 1);
            y += 26;
        }
    }

    private void renderInventoryGrid(DrawContext matrices, ItemStack[] slots, int startX, int startY) {
        for (int i = 0; i < slots.length; ++i) {
            int x = startX + i % 9 * 20;
            int y = startY + i / 9 * 20;
            Render2DEngine.fill(matrices.getMatrices(), x, y, x + 18, y + 18, -13421773);
            this.drawItemStack(matrices, slots[i], x, y);
        }
    }

    private void drawItemStack(DrawContext matrices, ItemStack itemStack, int x, int y) {
        if (!itemStack.isEmpty()) {
            matrices.drawItem(itemStack, x, y);
        }
    }

    public void onSlotClick(int slotIndex, boolean isArmorSlot) {
        if (this.selectedItem.isEmpty() || this.isItemAlreadyInInventory(this.selectedItem)) {
            return;
        }
        if (isArmorSlot) {
            this.armor[slotIndex] = this.selectedItem;
        } else {
            InventoryManagerScreen.inventory[slotIndex] = this.selectedItem;
        }
        this.selectedItem = ItemStack.EMPTY;
    }

    private boolean isItemAlreadyInInventory(ItemStack item) {
        return Arrays.stream(inventory).anyMatch(stack -> ItemStack.areItemsEqual((ItemStack)stack, (ItemStack)item));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int selectorX = this.width - 140;
        for (int i = 0; i < this.selectorItems.length; ++i) {
            int selectorY = 20 + i * 26;
            if (!(mouseX >= (double)selectorX) || !(mouseX <= (double)(selectorX + 20)) || !(mouseY >= (double)selectorY) || !(mouseY <= (double)(selectorY + 20))) continue;
            this.selectedItem = this.selectorItems[i].copy();
            return true;
        }
        int invStartX = this.width / 2 - 90;
        int invStartY = this.height / 2 - 110;
        for (int i = 0; i < inventory.length; ++i) {
            int x = invStartX + i % 9 * 20;
            int y = invStartY + i / 9 * 20;
            if (!(mouseX >= (double)x) || !(mouseX <= (double)(x + 18)) || !(mouseY >= (double)y) || !(mouseY <= (double)(y + 18))) continue;
            this.onSlotClick(i, false);
            return true;
        }
        int armorStartX = this.width / 2 + 100;
        int armorStartY = this.height / 2 - 110;
        for (int i = 0; i < this.armor.length; ++i) {
            int x = armorStartX + i * 20;
            int y = armorStartY;
            if (!(mouseX >= (double)x) || !(mouseX <= (double)(x + 18)) || !(mouseY >= (double)y) || !(mouseY <= (double)(y + 18))) continue;
            this.onSlotClick(i, true);
            return true;
        }
        int hotbarStartX = this.width / 2 - 90;
        int hotbarStartY = this.height / 2 + 70;
        for (int i = 0; i < hotbar.length; ++i) {
            int x = hotbarStartX + i * 20;
            int y = hotbarStartY;
            if (!(mouseX >= (double)x) || !(mouseX <= (double)(x + 18)) || !(mouseY >= (double)y) || !(mouseY <= (double)(y + 18))) continue;
            if (!this.selectedItem.isEmpty() && !this.isItemAlreadyInInventory(this.selectedItem)) {
                InventoryManagerScreen.hotbar[i] = this.selectedItem.copy();
                this.selectedItem = ItemStack.EMPTY;
            }
            return true;
        }
        int offhandX = this.width / 2 + 100;
        int offhandY = this.height / 2 - 40;
        if (mouseX >= (double)offhandX && mouseX <= (double)(offhandX + 18) && mouseY >= (double)offhandY && mouseY <= (double)(offhandY + 18)) {
            if (!this.selectedItem.isEmpty() && !this.isItemAlreadyInInventory(this.selectedItem)) {
                offHand = this.selectedItem.copy();
                this.selectedItem = ItemStack.EMPTY;
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void close() {
        this.saveInventory();
        super.close();
    }

    private void saveInventory() {
        JsonObject root = new JsonObject();
        root.add("hotbar", (JsonElement)this.serializeItemArray(hotbar));
        root.add("inventory", (JsonElement)this.serializeItemArray(inventory));
        root.add("offHand", (JsonElement)this.serializeItem(offHand));
        try {
            Files.createDirectories(settingsPath.getParent(), new FileAttribute[0]);
            Files.writeString(settingsPath, (CharSequence)root.toString(), new OpenOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonArray serializeItemArray(ItemStack[] items) {
        JsonArray array = new JsonArray();
        for (ItemStack stack : items) {
            array.add((JsonElement)this.serializeItem(stack));
        }
        return array;
    }

    private JsonObject serializeItem(ItemStack stack) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", stack.getItem().toString());
        obj.addProperty("count", (Number)stack.getCount());
        return obj;
    }

    public static void loadInventory() {
        if (!Files.exists(settingsPath, new LinkOption[0])) {
            return;
        }
        try {
            String json = Files.readString(settingsPath);
            JsonObject root = JsonParser.parseString((String)json).getAsJsonObject();
            InventoryManagerScreen.deserializeItemArray(root.getAsJsonArray("hotbar"), hotbar);
            InventoryManagerScreen.deserializeItemArray(root.getAsJsonArray("inventory"), inventory);
            offHand = InventoryManagerScreen.deserializeItem(root.getAsJsonObject("offHand"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserializeItemArray(JsonArray array, ItemStack[] items) {
        for (int i = 0; i < array.size(); ++i) {
            items[i] = InventoryManagerScreen.deserializeItem(array.get(i).getAsJsonObject());
        }
    }

    public static ItemStack deserializeItem(JsonObject obj) {
        String id = obj.get("id").getAsString();
        int count = obj.get("count").getAsInt();
        Item item = (Item)Registries.ITEM.get(Identifier.of((String)id));
        if (item == null) {
            throw new IllegalArgumentException("Invalid item id: " + id);
        }
        return new ItemStack((ItemConvertible)item, count);
    }
}
