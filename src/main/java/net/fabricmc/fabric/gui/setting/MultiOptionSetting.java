package net.fabricmc.fabric.gui.setting;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.fabric.gui.setting.Setting;

public class MultiOptionSetting
extends Setting {
    private final List<String> options;
    private final Set<String> selectedOptions;
    private String description;

    public MultiOptionSetting(String name, String description, String ... options) {
        super(name);
        this.description = description;
        this.options = List.of(options);
        this.selectedOptions = new HashSet<String>();
    }

    public List<String> getOptions() {
        return this.options.stream().map(option -> option.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "")).toList();
    }

    public Set<String> getSelectedOptions() {
        return this.selectedOptions.stream().map(option -> option.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "")).collect(Collectors.toSet());
    }

    public void select(String option) {
        this.selectedOptions.add(option);
    }

    public void unselect(String option) {
        this.selectedOptions.remove(option);
    }

    public String getDescription() {
        return this.description.replaceAll("[&@#$%!\\^\\*\\(\\)\\-_+=]", "");
    }

    public OptionEntry getOption(String optionName) {
        return this.options.stream().filter(option -> option.equalsIgnoreCase(optionName)).map(x$0 -> new OptionEntry((String)x$0)).findFirst().orElse(null);
    }

    public boolean isEnabled(String optionName) {
        return this.selectedOptions.contains(optionName);
    }

    public class OptionEntry {
        private final String option;

        public OptionEntry(String option) {
            this.option = option;
        }

        public boolean isEnabled() {
            return MultiOptionSetting.this.selectedOptions.contains(this.option);
        }

        public void select() {
            MultiOptionSetting.this.selectedOptions.add(this.option);
        }

        public void unselect() {
            MultiOptionSetting.this.selectedOptions.remove(this.option);
        }

        public String getName() {
            return this.option;
        }
    }
}
