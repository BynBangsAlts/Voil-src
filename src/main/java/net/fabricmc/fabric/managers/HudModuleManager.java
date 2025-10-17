package net.fabricmc.fabric.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.impl.Client.HUD;
import net.fabricmc.fabric.systems.module.impl.render.HotkeyHUD;
import net.fabricmc.fabric.systems.module.impl.render.TargetHUD;

public class HudModuleManager {
    private final List<Module> modules = new ArrayList<Module>();
    private final Map<String, int[]> positions = new HashMap<String, int[]>();

    public HudModuleManager() {
        this.init();
    }

    private void init() {
        this.add(new TargetHUD(), 300, 300);
        this.add(new HUD(), 2, 30);
        this.add(new HotkeyHUD(), 2, 20);
    }

    public void add(Module module, int x, int y) {
        this.modules.add(module);
        this.positions.put(module.getClass().getSimpleName(), new int[]{x, y});
    }

    public void setX(Module module, int x) {
        String id = module.getClass().getSimpleName();
        int[] position = this.positions.get(id);
        if (position != null) {
            position[0] = x;
        }
    }

    public void setY(Module module, int y) {
        String id = module.getClass().getSimpleName();
        int[] position = this.positions.get(id);
        if (position != null) {
            position[1] = y;
        }
    }

    public int getX(Module module) {
        String id = module.getClass().getSimpleName();
        int[] position = this.positions.get(id);
        if (position != null) {
            return position[0];
        }
        return 0;
    }

    public int getY(Module module) {
        String id = module.getClass().getSimpleName();
        int[] position = this.positions.get(id);
        if (position != null) {
            return position[1];
        }
        return 0;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public boolean isHud(Module module) {
        return this.modules.contains(module);
    }

    public boolean isEnabled(Module module) {
        return ModuleManager.INSTANCE.getModuleByClass(module.getClass()).isEnabled();
    }
}
