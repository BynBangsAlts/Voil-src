package net.fabricmc.fabric.gui.clickgui.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.GUI;
import net.fabricmc.fabric.gui.clickgui.component.Component;
import net.fabricmc.fabric.gui.setting.CurveSetting;
import net.fabricmc.fabric.gui.theme.Theme;
import net.fabricmc.fabric.utils.render.Bounds;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class CurveComponent
extends Component {
    private final CurveSetting setting;
    private final GUI parent;
    private CurveSetting.Point draggedPoint = null;
    private float dragOffsetX;
    private float dragOffsetY;

    public CurveComponent(CurveSetting setting, GUI parent, float x, float y) {
        super(setting);
        this.setting = setting;
        this.parent = parent;
        this.setPosition(0.0f, 0.0f);
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int i;
        float width = 180.0f;
        float height = 80.0f;
        float curveX = this.x + (float)this.parent.x + 450.0f;
        float curveY = this.y + 5.0f;
        ClientMain.fontRenderer.draw(matrices, this.setting.getName(), curveX + 2.0f, curveY - 20.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        Render2DEngine.renderRoundedQuad(matrices, curveX, curveY, curveX + width, curveY + height, 6.0, 10.0, new Color(20, 20, 20, 200));
        for (int i2 = 0; i2 <= 4; ++i2) {
            float gx = curveX + (float)i2 * (width / 4.0f);
            float gy = curveY + (float)i2 * (height / 4.0f);
            Render2DEngine.drawLine(matrices, gx, curveY, gx, curveY + height, 0.5f, new Color(60, 60, 60, 90));
            Render2DEngine.drawLine(matrices, curveX, gy, curveX + width, gy, 0.5f, new Color(60, 60, 60, 90));
        }
        List<CurveSetting.Point> points = this.setting.getControlPoints();
        ArrayList<Float[]> screenPoints = new ArrayList<Float[]>();
        for (CurveSetting.Point point : points) {
            float px = curveX + point.x * width;
            float py = curveY + (1.0f - point.y) * height;
            screenPoints.add(new Float[]{Float.valueOf(px), Float.valueOf(py)});
        }
        for (i = 0; i < screenPoints.size() - 1; ++i) {
            Float[] p1 = (Float[])screenPoints.get(i);
            Float[] p2 = (Float[])screenPoints.get(i + 1);
            Render2DEngine.drawLine(matrices, p1[0].floatValue(), p1[1].floatValue(), p2[0].floatValue(), p2[1].floatValue(), 2.0f, Color.WHITE);
        }
        for (i = 0; i < screenPoints.size(); ++i) {
            Float[] p = (Float[])screenPoints.get(i);
            boolean hovered = CurveComponent.isHovered(p[0].floatValue() - 5.0f, p[1].floatValue() - 5.0f, p[0].floatValue() + 5.0f, p[1].floatValue() + 5.0f, mouseX, mouseY);
            Render2DEngine.drawGlow(matrices, p[0].floatValue(), p[1].floatValue() - 5.0f, 10.0f, 10.0f, 5.0f, 10.0f, new Color(170, 110, 255, 150));
            Render2DEngine.drawCircle(matrices, p[0].floatValue(), p[1].floatValue() - 5.0f, 5.0f, 1.0f, hovered ? new Color(170, 110, 255) : Color.WHITE);
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, String.valueOf(i + 1), p[0].floatValue() - 3.0f, p[1].floatValue() - 14.0f, Theme.NORMAL_TEXT_COLOR.getRGB());
        }
        if (CurveComponent.isHovered(this.x, this.y + 2.0f, this.x + 20.0f, this.y + 2.0f + 20.0f, mouseX, mouseY)) {
            this.renderTooltip(matrices, this.setting.getDescription(), mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float width = 180.0f;
        float height = 80.0f;
        float curveX = this.x + (float)this.parent.x + 450.0f;
        float curveY = this.y;
        List<CurveSetting.Point> points = this.setting.getControlPoints();
        for (CurveSetting.Point point : points) {
            float px = curveX + point.x * width;
            float py = curveY + (1.0f - point.y) * height;
            if (!CurveComponent.isHovered(px - 5.0f, py - 5.0f, px + 5.0f, py + 5.0f, (int)mouseX, (int)mouseY)) continue;
            this.draggedPoint = point;
            this.dragOffsetX = (float)(mouseX - (double)px);
            this.dragOffsetY = (float)(mouseY - (double)py);
            break;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.draggedPoint != null) {
            float width = 180.0f;
            float height = 80.0f;
            float curveX = this.x + (float)this.parent.x + 450.0f;
            float curveY = this.y;
            float newX = MathHelper.clamp((float)((float)(mouseX - (double)curveX - (double)this.dragOffsetX) / width), (float)0.0f, (float)1.0f);
            float newY = MathHelper.clamp((float)((float)(mouseY - (double)curveY - (double)this.dragOffsetY) / height), (float)0.0f, (float)1.0f);
            this.draggedPoint.x = newX;
            this.draggedPoint.y = 1.0f - newY;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.draggedPoint = null;
    }

    private void renderTooltip(MatrixStack matrices, String description, int mouseX, int mouseY) {
        int maxLineLength = 40;
        String[] lines = description.split("(?<=\\G.{" + maxLineLength + "})");
        int tooltipWidth = 0;
        for (String line : lines) {
            int lineWidth = (int)(ClientMain.fontRenderer.getStringWidth(Text.of((String)line)) + 6.0f);
            tooltipWidth = Math.max(tooltipWidth, lineWidth);
        }
        int tooltipHeight = lines.length * 12 + 3;
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY - tooltipHeight - 3;
        Render2DEngine.renderRoundedQuad(matrices, tooltipX, tooltipY, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 2.0, 10.0, Color.darkGray);
        for (int i = 0; i < lines.length; ++i) {
            ClientMain.fontRenderer.drawString(matrices, ClientMain.mc.textRenderer, lines[i], (float)(tooltipX + 3), (float)(tooltipY + 6 + i * 12), Theme.NORMAL_TEXT_COLOR.getRGB());
        }
    }

    @Override
    public Bounds getBounds() {
        float width = 180.0f;
        float height = 100.0f;
        float curveX = this.x + (float)this.parent.x + 450.0f;
        float curveY = this.y + 5.0f;
        return new Bounds(curveX, curveY, width, height);
    }
}
