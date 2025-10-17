package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.AttackEvent;
import net.fabricmc.fabric.api.astral.events.BlockBreakEvent;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.api.astral.events.ShutdownEvent;
import net.fabricmc.fabric.api.scripting.ScriptAPI;
import net.fabricmc.fabric.managers.PlaytimeManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MinecraftClient.class})
public class MinecraftClientMixin {
    PlaytimeManager playtimeManager = new PlaytimeManager();
    private int tickCounter = 0;

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void onUpdate(CallbackInfo ci) {
        if (ClientMain.getInstance().isSelfDestructed) {
            return;
        }
        ClientMain.EVENTBUS.post(EventUpdate.get());
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void onTick(CallbackInfo ci) {
        if (PlaytimeManager.timerOn) {
            ++this.tickCounter;
            if (this.tickCounter >= 20) {
                this.playtimeManager.addTime();
                this.tickCounter = 0;
            }
        }
        ScriptAPI.getScriptHooks().triggerEvent("onTick", ScriptAPI.getScriptEngine());
        ClientMain.getInstance().onTick();
    }

    @Inject(method={"doItemUse"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPreItemUse(CallbackInfo ci) {
        if (ClientMain.EVENTBUS.post(ItemUseEvent.Pre.get()).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"doAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPreAttack(CallbackInfoReturnable<Boolean> cir) {
        if (ClientMain.EVENTBUS.post(AttackEvent.Pre.get()).isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method={"doAttack"}, at={@At(value="TAIL")}, cancellable=true)
    private void onPostAttack(CallbackInfoReturnable<Boolean> cir) {
        if (ClientMain.EVENTBUS.post(AttackEvent.Post.get()).isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method={"doItemUse"}, at={@At(value="TAIL")}, cancellable=true)
    private void onPostItemUse(CallbackInfo ci) {
        if (ClientMain.EVENTBUS.post(ItemUseEvent.Post.get()).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"handleBlockBreaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPreBlockBreak(boolean breaking, CallbackInfo ci) {
        if (ClientMain.EVENTBUS.post(BlockBreakEvent.Pre.get()).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"handleBlockBreaking"}, at={@At(value="TAIL")}, cancellable=true)
    private void onPostBlockBreak(boolean breaking, CallbackInfo ci) {
        if (ClientMain.EVENTBUS.post(BlockBreakEvent.Post.get()).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    private void onClose(CallbackInfo ci) {
        ClientMain.EVENTBUS.post(ShutdownEvent.get());
    }
}
