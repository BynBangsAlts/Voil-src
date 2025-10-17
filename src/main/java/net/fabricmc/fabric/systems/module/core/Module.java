package net.fabricmc.fabric.systems.module.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.KeybindSetting;
import net.fabricmc.fabric.gui.setting.Setting;
import net.fabricmc.fabric.managers.NotificationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class Module {
    protected static MinecraftClient mc = MinecraftClient.getInstance();
    private final List<Setting> settings = new ArrayList<Setting>();
    private CharSequence displayName;
    private String name;
    private String description;
    private Object suffix;
    private KeybindSetting keybind;
    private boolean enabled;
    private Category category;
    private boolean expanded;
    private boolean hidden;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.displayName = name;
        this.keybind = new KeybindSetting("Keybind", 0, true, "Keybind to activate the module");
        this.settings.add(this.keybind);
    }

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean isNull() {
        return Objects.isNull(Module.mc.world) || Objects.isNull(Module.mc.player);
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public void addSettings(Setting ... settingz) {
        this.settings.addAll(Arrays.asList(settingz));
    }

    public void onEnable() {
        ClientMain.EVENTBUS.subscribe(this);
    }

    public void draw(MatrixStack matrices) {
    }

    public void onDisable() {
        ClientMain.EVENTBUS.unsubscribe(this);
    }

    public void onTick() {
    }

    public void onWorldRender(MatrixStack matrices) {
    }

    public String getName() {
        return this.name.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public Object getSuffix() {
        return this.suffix;
    }

    public void setSuffix(Object suffix) {
        this.suffix = suffix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return this.keybind.getKey();
    }

    public void setKey(int key) {
        this.keybind.setKey(key);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean toggled) {
        if (this.enabled == toggled) {
            return;
        }
        this.enabled = toggled;
        if (this.enabled) {
            this.onEnable();
            ClientMain.EVENTBUS.subscribe(this);
            ClientMain.getNotificationManager().sendNotification("Toggled", "Toggled " + this.getName() + " on", NotificationManager.NotificationPosition.BOTTOM_RIGHT);
        } else {
            this.onDisable();
            ClientMain.EVENTBUS.unsubscribe(this);
            ClientMain.getNotificationManager().sendNotification("Toggled", "Toggled " + this.getName() + " off", NotificationManager.NotificationPosition.BOTTOM_RIGHT);
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CharSequence getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(CharSequence displayName) {
        this.displayName = displayName;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    protected void sendMsg(String message) {
        if (Module.mc.player == null) {
            return;
        }
        Module.mc.player.sendMessage(Text.of((String)message.replace("&", "\u00a7")));
    }
}
