package net.fabricmc.fabric.utils.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.utils.shader.shim.ManagedCoreShader;
import net.fabricmc.fabric.utils.shader.shim.SamplerUniform;
import net.fabricmc.fabric.utils.shader.shim.ShaderEffectManager;
import net.fabricmc.fabric.utils.shader.shim.Uniform1f;
import net.fabricmc.fabric.utils.shader.shim.Uniform2f;
import org.lwjgl.opengl.GL30;

public class BlurProgram {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform2f inputResolution;
    private Uniform1f brightness;
    private Uniform1f quality;
    private SamplerUniform sampler;
    private Framebuffer input;
    public static final ManagedCoreShader BLUR = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of((String)"tulip", (String)"blur"), VertexFormats.POSITION);

    public BlurProgram() {
        this.setup();
    }

    public void setParameters(float x, float y, float width, float height, float r, float blurStrength, float blurOpacity) {
        if (this.input == null) {
            this.input = new SimpleFramebuffer(ClientMain.mc.getWindow().getScaledWidth(), ClientMain.mc.getWindow().getScaledHeight(), false, MinecraftClient.IS_SYSTEM_MAC);
        }
        float i = (float)ClientMain.mc.getWindow().getScaleFactor();
        this.radius.set(r * i);
        this.uLocation.set(x * i, -y * i + (float)ClientMain.mc.getWindow().getScaledHeight() * i - height * i);
        this.uSize.set(width * i, height * i);
        this.brightness.set(blurOpacity);
        this.quality.set(blurStrength);
        this.sampler.set(this.input.getColorAttachment());
    }

    public void use() {
        Framebuffer buffer = ClientMain.mc.getFramebuffer();
        if (this.input.textureWidth != buffer.textureWidth || this.input.textureHeight != buffer.textureHeight) {
            this.input.resize(buffer.textureWidth, buffer.textureHeight, MinecraftClient.IS_SYSTEM_MAC);
        }
        this.input.beginWrite(false);
        GL30.glBindFramebuffer((int)36008, (int)buffer.fbo);
        GL30.glBlitFramebuffer((int)0, (int)0, (int)buffer.textureWidth, (int)buffer.textureHeight, (int)0, (int)0, (int)buffer.textureWidth, (int)buffer.textureHeight, (int)16384, (int)9729);
        buffer.beginWrite(false);
        this.inputResolution.set(buffer.textureWidth, buffer.textureHeight);
        this.sampler.set(this.input.getColorAttachment());
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    protected void setup() {
        this.inputResolution = BLUR.findUniform2f("InputResolution");
        this.brightness = BLUR.findUniform1f("Brightness");
        this.quality = BLUR.findUniform1f("Quality");
        this.uSize = BLUR.findUniform2f("uSize");
        this.uLocation = BLUR.findUniform2f("uLocation");
        this.radius = BLUR.findUniform1f("radius");
        this.sampler = BLUR.findSampler("InputSampler");
    }
}
