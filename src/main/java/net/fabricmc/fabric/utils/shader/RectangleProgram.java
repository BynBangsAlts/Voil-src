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

public class RectangleProgram {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform1f smoothness;
    private Uniform4f color1;
    private Uniform4f color2;
    private Uniform4f color3;
    private Uniform4f color4;
    public static final ManagedCoreShader RECTANGLE = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of((String)"tulip", (String)"rectangle"), VertexFormats.POSITION);

    public RectangleProgram() {
        this.setup();
    }

    public void setParameters(float x, float y, float width, float height, float r, float smooth, Color c1, Color c2, Color c3, Color c4) {
        if (c1 == null) c1 = new Color(40, 70, 50, 255);
        if (c2 == null) c2 = c1;
        if (c3 == null) c3 = c1;
        if (c4 == null) c4 = c1;
        int scale = (Integer)ClientMain.mc.options.getGuiScale().getValue();
        this.radius.set(r * (float)scale);
        this.smoothness.set(smooth * (float)scale);
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
        this.uSize = RECTANGLE.findUniform2f("uSize");
        this.uLocation = RECTANGLE.findUniform2f("uLocation");
        this.radius = RECTANGLE.findUniform1f("radius");
        this.smoothness = RECTANGLE.findUniform1f("smoothness");
        this.color1 = RECTANGLE.findUniform4f("color1");
        this.color2 = RECTANGLE.findUniform4f("color2");
        this.color3 = RECTANGLE.findUniform4f("color3");
        this.color4 = RECTANGLE.findUniform4f("color4");
    }
}
