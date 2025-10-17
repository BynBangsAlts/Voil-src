package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.CommandSuggest;
import net.fabricmc.fabric.api.astral.events.JoinWorldEvent;
import net.fabricmc.fabric.command.Command;
import net.fabricmc.fabric.command.CommandManager;
import net.fabricmc.fabric.managers.PlaytimeManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method={"onCommandSuggestions"}, at={@At(value="TAIL")})
    public void onCmdSuggest(CommandSuggestionsS2CPacket packet, CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        ClientMain.EVENTBUS.post(new CommandSuggest(packet));
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    public void onJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        PlaytimeManager.timerOn = true;
        ClientMain.EVENTBUS.post(new JoinWorldEvent());
    }

    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    public void sendChatMessage(String msg, CallbackInfo ci) {
        if (!ClientMain.getInstance().isSelfDestructed) {
            StringBuilder CMD = new StringBuilder();
            for (int i = 1; i < msg.toCharArray().length; ++i) {
                CMD.append(msg.toCharArray()[i]);
            }
            String[] args = CMD.toString().split(" ");
            if (msg.startsWith(ClientMain.getCommandPrefix())) {
                for (Command command : CommandManager.INSTANCE.getCmds()) {
                    if (!args[0].equalsIgnoreCase(command.getName()) && !command.getAliases().contains(command.getName())) continue;
                    command.onCmd(msg, args);
                    ci.cancel();
                    break;
                }
            }
        }
    }
}
