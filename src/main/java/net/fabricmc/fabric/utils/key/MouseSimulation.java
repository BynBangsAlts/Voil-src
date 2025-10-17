package net.fabricmc.fabric.utils.key;

import java.util.HashMap;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEvent;
import net.fabricmc.fabric.api.astral.events.BlockBreakEvent;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.api.astral.events.WorldRenderEvent;
import net.fabricmc.fabric.mixin.IMouseAccessorMixin;
import net.fabricmc.fabric.utils.key.KeyUtils;

public class MouseSimulation {
    private final HashMap<Integer, Integer> mouseButtons = new HashMap();
    private boolean cancelLeft = false;
    private boolean cancelRight = false;

    public boolean isFakeMousePressed(int keyCode) {
        return this.mouseButtons.containsKey(keyCode);
    }

    public IMouseAccessorMixin getMouse() {
        return (IMouseAccessorMixin)ClientMain.mc.mouse;
    }

    public void mouseClick(int keyCode, int frames) {
        if (!this.isFakeMousePressed(keyCode)) {
            if (!this.cancelRight) {
                boolean bl = this.cancelRight = keyCode == 1;
            }
            if (!this.cancelLeft) {
                this.cancelLeft = keyCode == 0;
            }
            this.mouseButtons.put(keyCode, frames);
            this.getMouse().callOnMouseButton(ClientMain.mc.getWindow().getHandle(), keyCode, 1, 0);
        }
    }

    public void mouseClick(int keyCode) {
        this.mouseClick(keyCode, 1);
    }

    public void mouseRelease(int keyCode) {
        if (this.isFakeMousePressed(keyCode)) {
            this.getMouse().callOnMouseButton(ClientMain.mc.getWindow().getHandle(), keyCode, 0, 0);
            this.mouseButtons.remove(keyCode);
        }
    }

    private void checkMouse(int keyCode) {
        if (this.isFakeMousePressed(keyCode)) {
            int ticksLeft = this.mouseButtons.get(keyCode);
            if (ticksLeft > 0) {
                this.mouseButtons.replace(keyCode, ticksLeft - 1);
            } else {
                this.mouseRelease(keyCode);
            }
        }
    }

    @EventHandler(priority=200)
    private void onWorldRender(WorldRenderEvent event) {
        this.checkMouse(0);
        this.checkMouse(1);
    }

    @EventHandler(priority=200)
    private void onItemUse(ItemUseEvent.Pre event) {
        if (this.cancelRight) {
            event.cancel();
            this.cancelRight = ClientMain.mc.options.useKey.isPressed() && !KeyUtils.isKeyPressed(1);
        }
    }

    @EventHandler(priority=200)
    private void onAttack(AttackEvent.Pre event) {
        if (this.cancelLeft) {
            event.cancel();
            this.cancelLeft = ClientMain.mc.options.attackKey.isPressed() && !KeyUtils.isKeyPressed(0);
        }
    }

    @EventHandler(priority=200)
    private void onBlockBreak(BlockBreakEvent.Pre event) {
        if (this.cancelLeft) {
            event.cancel();
            this.cancelLeft = ClientMain.mc.options.attackKey.isPressed() && !KeyUtils.isKeyPressed(0);
        }
    }
}
