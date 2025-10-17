package net.fabricmc.fabric.gui.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.gui.setting.Setting;
import net.minecraft.util.math.MathHelper;

public class CurveSetting
extends Setting {
    private final List<Point> controlPoints = new ArrayList<Point>();
    private final String name;
    private String description;

    public CurveSetting(String name, String description, Point ... points) {
        super(name);
        this.name = name;
        this.description = description;
        if (points != null && points.length > 0) {
            this.controlPoints.addAll(Arrays.asList(points));
        } else {
            this.controlPoints.add(new Point(0.0f, 0.0f));
            this.controlPoints.add(new Point(0.5f, 1.0f));
            this.controlPoints.add(new Point(1.0f, 0.0f));
        }
    }

    public List<Point> getControlPoints() {
        return Collections.unmodifiableList(this.controlPoints);
    }

    public float evaluate(float t) {
        t = MathHelper.clamp((float)t, (float)0.0f, (float)1.0f);
        if (this.controlPoints.size() < 2) {
            return 0.0f;
        }
        for (int i = 0; i < this.controlPoints.size() - 1; ++i) {
            Point p1 = this.controlPoints.get(i);
            Point p2 = this.controlPoints.get(i + 1);
            if (!(t >= p1.x) || !(t <= p2.x)) continue;
            float localT = (t - p1.x) / (p2.x - p1.x);
            return this.lerp(p1.y, p2.y, localT);
        }
        return this.controlPoints.get((int)(this.controlPoints.size() - 1)).y;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    @Override
    public String getName() {
        return this.name.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public static class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
