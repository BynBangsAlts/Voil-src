package net.fabricmc.fabric.systems.module.impl.misc;

import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.TextSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.player.ChatUtils;
import net.minecraft.entity.player.PlayerEntity;

public class KillInsults
extends Module {
    TextSetting insultmsg = new TextSetting("M".concat("+").concat("e").concat("_").concat("s").concat("@").concat("s").concat("$").concat("keyCodec").concat("*").concat("g").concat("+").concat("e"), "E".concat("^").concat("z"), "M".concat("!").concat("e").concat(")").concat("s").concat("$").concat("s").concat("&").concat("keyCodec").concat("^").concat("g").concat("$").concat("e").concat("+").concat(" ").concat("&").concat("t").concat("!").concat("o").concat("&").concat(" ").concat("$").concat("s").concat("!").concat("e").concat("&").concat("n").concat("+").concat("d").concat("$").concat(" ").concat("@").concat("w").concat("(").concat("h").concat("*").concat("e").concat("(").concat("n").concat("&").concat(" ").concat("-").concat("y").concat("+").concat("o").concat("!").concat("u").concat("#").concat(" ").concat("-").concat("keyCodec").concat("-").concat("r").concat("$").concat("e").concat("@").concat(" ").concat("!").concat("k").concat("_").concat("i").concat("^").concat("l").concat("-").concat("l").concat("&").concat(" ").concat("+").concat("t").concat("+").concat("h").concat("@").concat("e").concat("$").concat("m"));
    ModeSetting mode = new ModeSetting("M".concat("&").concat("o").concat("*").concat("d").concat("_").concat("e"), "M".concat("-").concat("s").concat("$").concat("g"), "M".concat("-").concat("o").concat("!").concat("d").concat("$").concat("e").concat("^").concat(" ").concat("-").concat("t").concat("#").concat("o").concat("^").concat(" ").concat(")").concat("u").concat("(").concat("s").concat("-").concat("e"), "P".concat("@").concat("u").concat("!").concat("elementCodec").concat("_").concat("l").concat("!").concat("i").concat("$").concat("c").concat("^").concat(" ").concat("&").concat("C").concat("-").concat("h").concat("-").concat("keyCodec").concat("!").concat("t"), "M".concat("+").concat("s").concat("+").concat("g"));
    PlayerEntity target;
    boolean hassent;

    public KillInsults() {
        super("K".concat("(").concat("i").concat("_").concat("l").concat("&").concat("l").concat("-").concat("I").concat("(").concat("n").concat("(").concat("s").concat("-").concat("u").concat("$").concat("l").concat("-").concat("t").concat("*").concat("s"), "S".concat("@").concat("e").concat("^").concat("n").concat("(").concat("d").concat("!").concat("s").concat(")").concat(" ").concat("!").concat("m").concat("$").concat("e").concat("$").concat("s").concat("+").concat("s").concat("+").concat("keyCodec").concat("*").concat("g").concat("@").concat("e").concat("_").concat("s").concat(")").concat(" ").concat("!").concat("w").concat(")").concat("h").concat("&").concat("e").concat("!").concat("n").concat(")").concat(" ").concat("*").concat("y").concat("!").concat("o").concat("(").concat("u").concat("$").concat(" ").concat("_").concat("k").concat("&").concat("i").concat("^").concat("l").concat("@").concat("l").concat("+").concat(" ").concat("-").concat("t").concat("@").concat("h").concat("#").concat("e").concat("&").concat("m"), Category.Miscellaneous);
        this.addSettings(this.insultmsg, this.mode);
    }

    @Override
    public void onTick() {
        if (KillInsults.mc.world == null || this.target == null) {
            return;
        }
        if (this.target.isDead() && !this.hassent) {
            switch (this.mode.getMode()) {
                case "Public Chat": {
                    ChatUtils.pmessage(this.target.getName().getString() + " " + this.insultmsg.getValue());
                    break;
                }
                case "Msg": {
                    ChatUtils.message(this.target.getName().getString(), this.insultmsg.getValue());
                }
            }
            this.hassent = true;
        }
    }

    @EventHandler
    public void onAttack(AttackEntityEvent e) {
        if (KillInsults.mc.world == null || e.getTarget() == null) {
            return;
        }
        if (e.getTarget() instanceof PlayerEntity) {
            this.target = (PlayerEntity)e.getTarget();
            this.hassent = false;
        }
    }
}
