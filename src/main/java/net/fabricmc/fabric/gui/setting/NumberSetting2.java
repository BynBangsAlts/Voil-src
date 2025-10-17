package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.gui.setting.Setting;

public class NumberSetting2
extends Setting {
    private double min;
    private final double max;
    private final double increment;
    private double minValue;
    private double maxValue;
    private String description;

    public NumberSetting2(String name, double min, double max, double defaultMinValue, double defaultMaxValue, double increment, String description) {
        super(name);
        this.min = min;
        this.max = max;
        this.minValue = defaultMinValue;
        this.maxValue = defaultMaxValue;
        this.increment = increment;
        this.description = description;
    }

    private double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = this.clamp(minValue, this.min, this.maxValue);
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = this.clamp(maxValue, this.minValue, this.max);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double getRandomValue() {
        return Math.random() * (this.maxValue - this.minValue) + this.minValue;
    }

    public float getFloatValue() {
        return (float)this.getRandomValue();
    }

    public double getIncrement() {
        return this.increment;
    }
}
