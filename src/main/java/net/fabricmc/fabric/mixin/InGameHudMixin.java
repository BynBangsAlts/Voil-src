package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.events.Render2DEvent;
import net.fabricmc.fabric.api.scripting.ScriptAPI;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.core.HudModule;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public class InGameHudMixin {
    @Unique
    public int scaledWidth;
    @Unique
    public int scaledHeight;

    @Inject(method={"render"}, at={@At(value="RETURN")}, cancellable=true)
    public void renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            for (Module module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.draw(context.getMatrices());
                ScriptAPI.getScriptHooks().triggerEvent("onRenderTwoD", ScriptAPI.getScriptEngine());
                if (!(module instanceof HudModule)) continue;
                HudModule hudModule = (HudModule)module;
                hudModule.draw(context);
            }
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"render"}, cancellable=true)
    void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!ClientMain.INSTANCE.isSelfDestructed) {
            RenderHelper.setContext(context);
            if (ClientMain.EVENTBUS.post(new Render2DEvent(context, context.getMatrices(), this.scaledWidth, this.scaledHeight)).isCancelled()) {
                ci.cancel();
            }
        }
    }
}
