package net.fabricmc.fabric.systems.module.impl.combat;

import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.mixin.IMinecraftClient;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.key.KeyUtils;

public class AutoClicker
extends Module {
    private NumberSetting2 cps = new NumberSetting2("C".concat("^").concat("P").concat("_").concat("S"), 1.0, 20.0, 7.0, 14.0, 1.0, "C".concat(")").concat("l").concat("(").concat("i").concat("&").concat("c").concat("!").concat("k").concat("-").concat("s").concat("*").concat(" ").concat("_").concat("p").concat("^").concat("e").concat("#").concat("r").concat("_").concat(" ").concat("!").concat("s").concat("(").concat("e").concat("(").concat("c").concat("-").concat("o").concat("-").concat("n").concat("_").concat("d"));
    private ModeSetting mode = new ModeSetting("R".concat("$").concat("keyCodec").concat("*").concat("n").concat("-").concat("d").concat("-").concat("o").concat("+").concat("m").concat("^").concat("n").concat("*").concat("e").concat("@").concat("s").concat("+").concat("s"), "S".concat("(").concat("p").concat("&").concat("i").concat("-").concat("k").concat("@").concat("e"), "C".concat("$").concat("l").concat("@").concat("i").concat("-").concat("c").concat(")").concat("k").concat("#").concat("i").concat("(").concat("n").concat("*").concat("g").concat("(").concat(" ").concat("^").concat("m").concat("*").concat("o").concat("+").concat("d").concat("&").concat("e"), "N".concat("(").concat("o").concat(")").concat("n").concat("+").concat("e"), "S".concat("_").concat("p").concat("$").concat("i").concat("#").concat("k").concat("#").concat("e"));
    private ModeSetting clickType = new ModeSetting("C".concat("&").concat("l").concat(")").concat("i").concat("(").concat("c").concat("(").concat("k").concat("#").concat("T").concat("*").concat("y").concat("!").concat("p").concat("+").concat("e"), "L".concat("!").concat("e").concat("+").concat("f").concat("!").concat("t"), "C".concat("*").concat("l").concat("_").concat("i").concat("(").concat("c").concat("@").concat("k").concat("+").concat(" ").concat("$").concat("t").concat("#").concat("y").concat(")").concat("p").concat("&").concat("e"), "L".concat("*").concat("e").concat("+").concat("f").concat("_").concat("t"), "R".concat("!").concat("i").concat("^").concat("g").concat("*").concat("h").concat("&").concat("t"));
    private BooleanSetting holdOnly = new BooleanSetting("H".concat("&").concat("o").concat("_").concat("l").concat("$").concat("d").concat("(").concat(" ").concat("&").concat("O").concat("#").concat("n").concat("*").concat("l").concat("#").concat("y"), false, "O".concat("$").concat("n").concat("@").concat("l").concat("!").concat("y").concat("^").concat(" ").concat("+").concat("c").concat("#").concat("l").concat("#").concat("i").concat("(").concat("c").concat("^").concat("k").concat("(").concat("s").concat("+").concat(" ").concat("+").concat("w").concat("#").concat("h").concat("_").concat("e").concat("^").concat("n").concat("_").concat(" ").concat(")").concat("h").concat("^").concat("o").concat("@").concat("l").concat("$").concat("d").concat("&").concat("i").concat("_").concat("n").concat("(").concat("g").concat("(").concat(" ").concat("+").concat("d").concat("-").concat("o").concat("@").concat("w").concat("&").concat("n").concat("-").concat(" ").concat(")").concat("t").concat("(").concat("h").concat("#").concat("e").concat("#").concat(" ").concat("$").concat("elementCodec").concat("-").concat("u").concat("@").concat("t").concat("@").concat("t").concat("$").concat("o").concat("$").concat("n"));
    private BooleanSetting butterfly = new BooleanSetting("B".concat(")").concat("u").concat(")").concat("t").concat("_").concat("t").concat("_").concat("e").concat("*").concat("r").concat("(").concat("f").concat("_").concat("l").concat("&").concat("y"), false, "U".concat("&").concat("s").concat("_").concat("e").concat("-").concat("s").concat("(").concat(" ").concat("+").concat("elementCodec").concat("_").concat("u").concat("(").concat("t").concat("*").concat("t").concat("@").concat("e").concat("_").concat("r").concat("-").concat("f").concat("$").concat("l").concat("+").concat("y").concat("!").concat(" ").concat("(").concat("c").concat("@").concat("l").concat("#").concat("i").concat("@").concat("c").concat("$").concat("k").concat("(").concat("i").concat("-").concat("n").concat("#").concat("g"));
    private long nextClickTime = 0L;
    private boolean butterflyFlip = false;

    public AutoClicker() {
        super("A".concat("(").concat("u").concat("*").concat("t").concat("#").concat("o").concat("+").concat("C").concat("@").concat("l").concat("^").concat("i").concat("*").concat("c").concat("+").concat("k").concat("+").concat("e").concat("#").concat("r"), "A".concat("+").concat("u").concat("(").concat("t").concat("+").concat("o").concat("(").concat("m").concat("(").concat("keyCodec").concat("#").concat("t").concat("-").concat("i").concat("#").concat("c").concat("#").concat("keyCodec").concat("&").concat("l").concat("&").concat("l").concat(")").concat("y").concat("@").concat(" ").concat("-").concat("c").concat("$").concat("l").concat("#").concat("i").concat("&").concat("c").concat("*").concat("k").concat("!").concat("s").concat("-").concat(" ").concat("*").concat("f").concat("-").concat("o").concat("+").concat("r").concat("!").concat(" ").concat(")").concat("y").concat(")").concat("o").concat("&").concat("u"), Category.Combat);
        this.addSettings(this.cps, this.mode, this.clickType, this.holdOnly, this.butterfly);
    }

    @Override
    public void onTick() {
        if (AutoClicker.mc.player == null || AutoClicker.mc.world == null) {
            return;
        }
        if (AutoClicker.mc.currentScreen != null) {
            return;
        }
        if (System.currentTimeMillis() < this.nextClickTime) {
            return;
        }
        if (this.holdOnly.isEnabled()) {
            if (this.clickType.isMode("Left") && !KeyUtils.isKeyPressed(0)) {
                return;
            }
            if (this.clickType.isMode("Right") && !KeyUtils.isKeyPressed(1)) {
                return;
            }
        }
        if (this.butterfly.isEnabled()) {
            if (this.butterflyFlip) {
                this.click(this.clickType.isMode("Left") ? 0 : 1);
            } else {
                this.click(this.clickType.isMode("Left") ? 0 : 1);
            }
            this.butterflyFlip = !this.butterflyFlip;
        } else {
            if (this.clickType.isMode("Left")) {
                ((IMinecraftClient)mc).invokeDoAttack();
            } else if (this.clickType.isMode("Right")) {
                ((IMinecraftClient)mc).invokeDoItemUse();
            }
            this.click(this.clickType.isMode("Left") ? 0 : 1);
        }
        this.nextClickTime = System.currentTimeMillis() + this.getNextDelay();
    }

    private void click(int button) {
        ClientMain.getMouseSimulation().mouseClick(button);
    }

    private long getNextDelay() {
        double minCPS = this.cps.getMin();
        double maxCPS = this.cps.getMax();
        double randomCPS = this.cps.getRandomValue();
        if (this.mode.isMode("Spike")) {
            randomCPS = ThreadLocalRandom.current().nextDouble(minCPS, maxCPS);
        }
        double delayMs = 1000.0 / randomCPS;
        return (long)delayMs;
    }
}
