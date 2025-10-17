package net.fabricmc.fabric.gui.setting;

import net.fabricmc.fabric.gui.setting.Setting;

public class NumberSetting
extends Setting {
    private double min;
    private final double max;
    private final double increment;
    private double value;
    private String description;

    public NumberSetting(String name, double min, double max, double defaultValue, double increment, String description) {
        super(name);
        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.increment = increment;
        this.description = description;
    }

    public double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return this.value;
    }

    public long getLongValue() {
        return (long)this.value;
    }

    public void setValue(double value) {
        value = this.clamp(value, this.min, this.max);
        this.value = value = (double)Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
    }

    public void setMin(double min) {
        this.min = min;
        this.setValue(this.value);
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public float getFloatValue() {
        return (float)this.value;
    }

    public void increment(boolean positive) {
        if (positive) {
            this.setValue(this.getValue() + this.getIncrement());
        } else {
            this.setValue((double)this.getFloatValue() - this.getIncrement());
        }
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double getIncrement() {
        return this.increment;
    }

    public int getIValue() {
        return (int)this.value;
    }
}
