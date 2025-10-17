package net.fabricmc.fabric.managers;

import java.util.List;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.ShutdownEvent;
import net.fabricmc.fabric.security.Networking;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.Client.ArrayList;
import net.fabricmc.fabric.systems.module.impl.Client.HUD;
import net.fabricmc.fabric.systems.module.impl.Client.MoveFix;
import net.fabricmc.fabric.systems.module.impl.Client.Notifications;
import net.fabricmc.fabric.systems.module.impl.Client.Sounds;
import net.fabricmc.fabric.systems.module.impl.Client.Spoofer;
import net.fabricmc.fabric.systems.module.impl.Client.ThemeModule;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AirAnchor;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoAnchor;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoCrystal;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoDoubleHand;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoHitCrystal;
import net.fabricmc.fabric.systems.module.impl.Cpvp.AutoTotem;
import net.fabricmc.fabric.systems.module.impl.Cpvp.CrystalOptimizer;
import net.fabricmc.fabric.systems.module.impl.Cpvp.KeyPearl;
import net.fabricmc.fabric.systems.module.impl.Cpvp.TotemHit;
import net.fabricmc.fabric.systems.module.impl.combat.AimAssist;
import net.fabricmc.fabric.systems.module.impl.combat.AntiStun;
import net.fabricmc.fabric.systems.module.impl.combat.AutoCart;
import net.fabricmc.fabric.systems.module.impl.combat.AutoClicker;
import net.fabricmc.fabric.systems.module.impl.combat.AutoCombo;
import net.fabricmc.fabric.systems.module.impl.combat.AutoCrossbow;
import net.fabricmc.fabric.systems.module.impl.combat.AutoDrain;
import net.fabricmc.fabric.systems.module.impl.combat.AutoJumpReset;
import net.fabricmc.fabric.systems.module.impl.combat.AutoPot;
import net.fabricmc.fabric.systems.module.impl.combat.AutoRefill;
import net.fabricmc.fabric.systems.module.impl.combat.AutoStun;
import net.fabricmc.fabric.systems.module.impl.combat.AutoWater;
import net.fabricmc.fabric.systems.module.impl.combat.AutoWeb;
import net.fabricmc.fabric.systems.module.impl.combat.Backtrack;
import net.fabricmc.fabric.systems.module.impl.combat.BedMacro;
import net.fabricmc.fabric.systems.module.impl.combat.BowAssist;
import net.fabricmc.fabric.systems.module.impl.combat.Hitboxes;
import net.fabricmc.fabric.systems.module.impl.combat.MaceStun;
import net.fabricmc.fabric.systems.module.impl.combat.MaceSwap;
import net.fabricmc.fabric.systems.module.impl.combat.OptimalEating;
import net.fabricmc.fabric.systems.module.impl.combat.Reach;
import net.fabricmc.fabric.systems.module.impl.combat.Triggerbot;
import net.fabricmc.fabric.systems.module.impl.misc.AntiBot;
import net.fabricmc.fabric.systems.module.impl.misc.AutoTool;
import net.fabricmc.fabric.systems.module.impl.misc.ChestStealer;
import net.fabricmc.fabric.systems.module.impl.misc.FakePlayer;
import net.fabricmc.fabric.systems.module.impl.misc.FastUse;
import net.fabricmc.fabric.systems.module.impl.misc.FlagDetector;
import net.fabricmc.fabric.systems.module.impl.misc.HealthResolver;
import net.fabricmc.fabric.systems.module.impl.misc.KillInsults;
import net.fabricmc.fabric.systems.module.impl.misc.MiddleClickFriend;
import net.fabricmc.fabric.systems.module.impl.misc.NameProtect;
import net.fabricmc.fabric.systems.module.impl.misc.Plugins;
import net.fabricmc.fabric.systems.module.impl.misc.Prevent;
import net.fabricmc.fabric.systems.module.impl.misc.SelfDestruct;
import net.fabricmc.fabric.systems.module.impl.misc.Teams;
import net.fabricmc.fabric.systems.module.impl.movement.AutoHeadHitter;
import net.fabricmc.fabric.systems.module.impl.movement.Blink;
import net.fabricmc.fabric.systems.module.impl.movement.BridgeAssist;
import net.fabricmc.fabric.systems.module.impl.movement.FakeLag;
import net.fabricmc.fabric.systems.module.impl.movement.FastFall;
import net.fabricmc.fabric.systems.module.impl.movement.Fly;
import net.fabricmc.fabric.systems.module.impl.movement.InventoryMove;
import net.fabricmc.fabric.systems.module.impl.movement.MaceDodge;
import net.fabricmc.fabric.systems.module.impl.movement.NoJumpCooldown;
import net.fabricmc.fabric.systems.module.impl.movement.Scaffold;
import net.fabricmc.fabric.systems.module.impl.movement.SnapTap;
import net.fabricmc.fabric.systems.module.impl.movement.Speed;
import net.fabricmc.fabric.systems.module.impl.movement.Sprint;
import net.fabricmc.fabric.systems.module.impl.player.AutoArmor;
import net.fabricmc.fabric.systems.module.impl.player.AutoCraft;
import net.fabricmc.fabric.systems.module.impl.player.ElytraSwap;
import net.fabricmc.fabric.systems.module.impl.player.InventoryManager;
import net.fabricmc.fabric.systems.module.impl.player.NoFall;
import net.fabricmc.fabric.systems.module.impl.player.NoRotate;
import net.fabricmc.fabric.systems.module.impl.player.NoSlow;
import net.fabricmc.fabric.systems.module.impl.player.PearlChase;
import net.fabricmc.fabric.systems.module.impl.render.ArmorInfo;
import net.fabricmc.fabric.systems.module.impl.render.BlockESP;
import net.fabricmc.fabric.systems.module.impl.render.Breadcrumbs;
import net.fabricmc.fabric.systems.module.impl.render.Cape;
import net.fabricmc.fabric.systems.module.impl.render.Chams;
import net.fabricmc.fabric.systems.module.impl.render.ESP;
import net.fabricmc.fabric.systems.module.impl.render.Freecam;
import net.fabricmc.fabric.systems.module.impl.render.HitCircle;
import net.fabricmc.fabric.systems.module.impl.render.HotkeyHUD;
import net.fabricmc.fabric.systems.module.impl.render.JumpCircle;
import net.fabricmc.fabric.systems.module.impl.render.NameTags;
import net.fabricmc.fabric.systems.module.impl.render.Radar;
import net.fabricmc.fabric.systems.module.impl.render.SwingAnimation;
import net.fabricmc.fabric.systems.module.impl.render.TargetHUD;
import net.fabricmc.fabric.systems.module.impl.render.Trajectories;
import net.fabricmc.fabric.systems.settings.SettingsManager;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();
    private List<Module> modules = new java.util.ArrayList<Module>();

    public ModuleManager() {
        this.init();
    }

    private void init() {
        this.add(new ArrayList());
        this.add(new SwingAnimation());
        this.add(new ESP());
        this.add(new HUD());
        this.add(new ThemeModule());
        this.add(new HitCircle());
        this.add(new TargetHUD());
        this.add(new Cape());
        this.add(new BlockESP());
        this.add(new AutoCrystal());
        this.add(new AutoTotem());
        this.add(new CrystalOptimizer());
        this.add(new AutoDoubleHand());
        this.add(new AirAnchor());
        this.add(new AutoAnchor());
        this.add(new KeyPearl());
        this.add(new AutoCrystal());
        this.add(new AutoHitCrystal());
        this.add(new AimAssist());
        this.add(new AutoPot());
        this.add(new NoJumpCooldown());
        this.add(new AutoStun());
        this.add(new Hitboxes());
        this.add(new Reach());
        this.add(new Triggerbot());
        this.add(new AutoJumpReset());
        this.add(new AutoCombo());
        this.add(new AutoWater());
        this.add(new OptimalEating());
        this.add(new Backtrack());
        this.add(new AutoCart());
        this.add(new AutoRefill());
        this.add(new AntiBot());
        this.add(new AutoTool());
        this.add(new BowAssist());
        this.add(new Speed());
        this.add(new InventoryMove());
        this.add(new Sprint());
        this.add(new Fly());
        this.add(new BridgeAssist());
        this.add(new FastFall());
        this.add(new FakeLag());
        this.add(new Blink());
        this.add(new AutoHeadHitter());
        this.add(new AutoCrossbow());
        this.add(new Trajectories());
        this.add(new TargetHUD());
        this.add(new Chams());
        this.add(new SwingAnimation());
        this.add(new AutoWeb());
        this.add(new Plugins());
        this.add(new SelfDestruct());
        this.add(new Spoofer());
        this.add(new FakePlayer());
        this.add(new NameTags());
        this.add(new NameProtect());
        this.add(new MiddleClickFriend());
        this.add(new Prevent());
        this.add(new ChestStealer());
        this.add(new FastUse());
        this.add(new KillInsults());
        this.add(new AutoDrain());
        this.add(new NoSlow());
        this.add(new NoFall());
        this.add(new InventoryManager());
        this.add(new AutoArmor());
        this.add(new NoRotate());
        this.add(new SnapTap());
        this.add(new Freecam());
        this.add(new Scaffold());
        this.add(new TotemHit());
        this.add(new AutoCraft());
        this.add(new MaceSwap());
        this.add(new MaceStun());
        this.add(new MaceDodge());
        this.add(new AntiStun());
        this.add(new ArmorInfo());
        this.add(new HotkeyHUD());
        this.add(new BedMacro());
        this.add(new ElytraSwap());
        this.add(new Sounds());
        this.add(new Breadcrumbs());
        this.add(new Notifications());
        this.add(new Chams());
        this.add(new FlagDetector());
        this.add(new Trajectories());
        this.add(new Teams());
        this.add(new Radar());
        this.add(new AutoClicker());
        this.add(new JumpCircle());
        this.add(new HealthResolver());
        this.add(new MoveFix());
        this.add(new PearlChase());
    }

    public void add(Module m) {
        this.modules.add(m);
    }

    public void remove(Module m) {
        this.modules.remove(m);
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().toString().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public void unexpandAll() {
        for (Module m : this.modules) {
            if (!m.isExpanded()) continue;
            m.setExpanded(false);
        }
    }

    public static boolean isAnyModuleExpanded() {
        for (Module m : INSTANCE.getModules()) {
            if (!m.isExpanded()) continue;
            return true;
        }
        return false;
    }

    public java.util.ArrayList<Module> getModulesByCategory(Category category) {
        java.util.ArrayList<Module> modules = new java.util.ArrayList<Module>();
        for (Module m : this.modules) {
            Category moduleCategory = m.getCategory();
            if (moduleCategory == null || !moduleCategory.equals((Object)category)) continue;
            modules.add(m);
        }
        return modules;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : this.modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public List<Module> getEnabledModules() {
        java.util.ArrayList<Module> enabled = new java.util.ArrayList<Module>();
        for (Module m : this.getModules()) {
            if (!m.isEnabled()) continue;
            enabled.add(m);
        }
        return enabled;
    }

    @EventHandler
    private void onShutdown(ShutdownEvent e) {
        Networking.instance.sendSettings(SettingsManager.getSettings());
        Networking.instance.close();
    }
}
