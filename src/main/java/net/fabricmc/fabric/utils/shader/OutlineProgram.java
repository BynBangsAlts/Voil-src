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

public class OutlineProgram {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform1f smoothness;
    private Uniform1f thickness;
    private Uniform4f color1;
    private Uniform4f color2;
    private Uniform4f color3;
    private Uniform4f color4;
    public static final ManagedCoreShader OUTLINE = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of((String)"tulip", (String)"outline"), VertexFormats.POSITION);

    public OutlineProgram() {
        this.setup();
    }

    public void setParameters(float x, float y, float width, float height, float r, float smooth, float thick, Color c1, Color c2, Color c3, Color c4) {
        int scale = (Integer)ClientMain.mc.options.getGuiScale().getValue();
        this.radius.set(r * (float)scale);
        this.smoothness.set(smooth * (float)scale);
        this.thickness.set(thick * (float)scale);
        this.uLocation.set(x * (float)scale, -y * (float)scale + (float)(ClientMain.mc.getWindow().getScaledHeight() * scale) - height * (float)scale);
        this.uSize.set(width * (float)scale, height * (float)scale);
        this.color1.set((float)c1.getRed() / 255.0f, (float)c1.getGreen() / 255.0f, (float)c1.getBlue() / 255.0f, (float)c1.getAlpha() / 255.0f);
        this.color2.set((float)c2.getRed() / 255.0f, (float)c2.getGreen() / 255.0f, (float)c2.getBlue() / 255.0f, (float)c2.getAlpha() / 255.0f);
        this.color3.set((float)c3.getRed() / 255.0f, (float)c3.getGreen() / 255.0f, (float)c3.getBlue() / 255.0f, (float)c3.getAlpha() / 255.0f);
        this.color4.set((float)c4.getRed() / 255.0f, (float)c4.getGreen() / 255.0f, (float)c4.getBlue() / 255.0f, (float)c4.getAlpha() / 255.0f);
    }

    public void use() {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    protected void setup() {
        this.uSize = OUTLINE.findUniform2f("uSize");
        this.uLocation = OUTLINE.findUniform2f("uLocation");
        this.radius = OUTLINE.findUniform1f("radius");
        this.smoothness = OUTLINE.findUniform1f("smoothness");
        this.thickness = OUTLINE.findUniform1f("thickness");
        this.color1 = OUTLINE.findUniform4f("color1");
        this.color2 = OUTLINE.findUniform4f("color2");
        this.color3 = OUTLINE.findUniform4f("color3");
        this.color4 = OUTLINE.findUniform4f("color4");
    }
}
