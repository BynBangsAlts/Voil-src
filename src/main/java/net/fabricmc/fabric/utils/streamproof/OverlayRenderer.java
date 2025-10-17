package net.fabricmc.fabric.utils.streamproof;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.streamproof.Kernel32;
import net.fabricmc.fabric.utils.streamproof.MinHook;
import net.fabricmc.fabric.utils.streamproof.MinHookManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.apache.commons.compress.utils.IOUtils;

public class OverlayRenderer {
    private static PointerByReference reference;
    private static boolean framebufferDirty;
    private static Framebuffer overlayFramebuffer;

    public static void markFramebufferDirty() {
        framebufferDirty = true;
    }

    public static void beginDraw() {
        if (overlayFramebuffer == null) {
            return;
        }
        overlayFramebuffer.beginWrite(false);
        framebufferDirty = true;
    }

    public static void beginEmptyDraw() {
        MinecraftClient.getInstance().getFramebuffer().endWrite();
    }

    public static void endDraw() {
        if (overlayFramebuffer == null) {
            return;
        }
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        framebufferDirty = true;
    }

    public static void onResolutionChanged(MinecraftClient client) {
        if (overlayFramebuffer == null) {
            return;
        }
        overlayFramebuffer.resize(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
    }

    public static void beginFrame() {
        if (overlayFramebuffer == null) {
            return;
        }
        overlayFramebuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        overlayFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
    }

    private static void renderFrame() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (overlayFramebuffer != null && framebufferDirty) {
            framebufferDirty = false;
            GlStateManager._disableDepthTest();
            GlStateManager._enableBlend();
            GlStateManager._blendFunc((int)770, (int)771);
            GlStateManager._viewport((int)0, (int)0, (int)client.getWindow().getFramebufferWidth(), (int)client.getWindow().getFramebufferHeight());
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            ShaderProgram shaderProgram = Objects.requireNonNull(minecraftClient.gameRenderer.blitScreenProgram, "Blit shader not loaded");
            shaderProgram.addSampler("DiffuseSampler", (Object)overlayFramebuffer.getColorAttachment());
            shaderProgram.bind();
            Tessellator tessellator = RenderSystem.renderThreadTesselator();
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float f = minecraftClient.getWindow().getFramebufferWidth();
            float g = minecraftClient.getWindow().getFramebufferHeight();
            float h = (float)OverlayRenderer.overlayFramebuffer.viewportWidth / (float)OverlayRenderer.overlayFramebuffer.textureWidth;
            float i = (float)OverlayRenderer.overlayFramebuffer.viewportHeight / (float)OverlayRenderer.overlayFramebuffer.textureHeight;
            bufferBuilder.vertex(0.0f, g, 0.0f).texture(0.0f, 0.0f).color(50, 50, 50, 255);
            bufferBuilder.vertex(f, g, 0.0f).texture(h, 0.0f).color(50, 50, 50, 255);
            bufferBuilder.vertex(f, 0.0f, 0.0f).texture(h, i).color(50, 50, 50, 255);
            bufferBuilder.vertex(0.0f, 0.0f, 0.0f).texture(0.0f, i).color(50, 50, 50, 255);
            BufferRenderer.draw((BuiltBuffer)bufferBuilder.end());
            shaderProgram.unbind();
        }
    }

    public static void setupLibs() {
        String arch = System.getProperty("os.arch").toLowerCase();
        boolean is64 = arch.equals("x86_64") || arch.equals("amd64") || arch.equals("x64") || arch.equals("ia64");
        InputStream libFile = ClientMain.class.getResourceAsStream(is64 ? "/assets/tulip/lib/MinHook.x64.dll" : "/assets/tulip/lib/MinHook.x86.dll");
        if (libFile == null) {
            System.out.println("Failed to load dependency DLL");
            return;
        }
        File nativeDir = new File(FabricLoader.getInstance().getGameDir().toAbsolutePath().toString().concat("/native"));
        File copyLibFile = new File(FabricLoader.getInstance().getGameDir().toAbsolutePath().toString().concat("/native/MinHook.dll"));
        nativeDir.mkdir();
        try {
            FileOutputStream fos = new FileOutputStream(copyLibFile);
            copyLibFile.createNewFile();
            IOUtils.copy((InputStream)libFile, (OutputStream)fos);
            fos.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("jna.library.path", nativeDir.getAbsolutePath());
    }

    public static void init(MinecraftClient client) {
        overlayFramebuffer = new SimpleFramebuffer(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
        RenderSystem.clearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        overlayFramebuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        overlayFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        Pointer module = Kernel32.INSTANCE.GetModuleHandleA("opengl32.dll");
        Pointer proc = Kernel32.INSTANCE.GetProcAddress(module, "wglSwapBuffers");
        try {
            MinHook minhook = MinHookManager.GetInstance();
            minhook.MH_Initialize();
            reference = new PointerByReference();
            minhook.MH_CreateHook(proc, hDc -> {
                OverlayRenderer.renderFrame();
                Function origFunction = Function.getFunction((Pointer)reference.getValue(), (int)63);
                return (Boolean)origFunction.invoke(Boolean.class, new Object[]{hDc});
            }, reference);
            minhook.MH_EnableHook(proc);
        }
        catch (Exception e) {
            overlayFramebuffer = null;
        }
    }

    static {
        framebufferDirty = false;
        overlayFramebuffer = null;
    }
}
