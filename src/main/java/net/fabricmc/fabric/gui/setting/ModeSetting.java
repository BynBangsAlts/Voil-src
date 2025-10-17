package net.fabricmc.fabric.gui.setting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.fabric.gui.setting.Setting;

public class ModeSetting
extends Setting {
    private final List<String> modes;
    private String mode;
    private int index;
    private String description;

    public ModeSetting(String name, String defaultMode, String description, String ... modes) {
        super(name);
        this.mode = defaultMode;
        this.description = description;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return this.mode.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void setMode(String mode) {
        this.mode = mode;
        this.index = this.modes.indexOf(mode);
    }

    public List<String> getModes() {
        return this.modes.stream().map(mode -> mode.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "")).collect(Collectors.toList());
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.mode = this.modes.get(index);
    }

    public void cycle() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
            this.mode = this.modes.get(this.index);
        } else if (this.index >= this.modes.size() - 1) {
            this.index = 0;
            this.mode = this.modes.get(0);
        }
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public void cycleBack() {
        if (this.index > 0 && this.index <= this.modes.size()) {
            --this.index;
            this.mode = this.modes.get(this.index);
        } else if (this.index == 0) {
            this.index = 0;
        }
    }

    public boolean isMode(String mode) {
        return Objects.equals(this.mode.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", ""), mode.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", ""));
    }
}
