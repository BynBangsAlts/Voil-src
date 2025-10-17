package net.fabricmc.fabric.utils.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
 
import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.utils.shader.shim.ManagedCoreShader;
import net.fabricmc.fabric.utils.shader.shim.ShaderEffectManager;
import net.fabricmc.fabric.utils.shader.shim.Uniform1f;
import net.fabricmc.fabric.utils.shader.shim.Uniform2f;
import net.fabricmc.fabric.utils.shader.shim.Uniform4f;

public class CircleProgram {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform1f smoothness;
    private Uniform4f color1;
    public static final ManagedCoreShader CIRCLE = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of((String)"tulip", (String)"circle"), VertexFormats.POSITION);

    public CircleProgram() {
        this.setup();
    }

    public void setParameters(float x, float y, float r, float smooth, Color c) {
        this.radius.set(r);
        this.smoothness.set(smooth);
        this.uLocation.set(x, (float)ClientMain.mc.getWindow().getScaledHeight() - y - r);
        this.uSize.set(r * 2.0f, r * 2.0f);
        this.color1.set((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, (float)c.getAlpha() / 255.0f);
    }

    public void use() {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    protected void setup() {
        this.uSize = CIRCLE.findUniform2f("size");
        this.uLocation = CIRCLE.findUniform2f("uLocation");
        this.radius = CIRCLE.findUniform1f("radius");
        this.smoothness = CIRCLE.findUniform1f("smoothness");
        this.color1 = CIRCLE.findUniform4f("color1");
    }
}
