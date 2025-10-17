package net.fabricmc.fabric.api.scripting;

import java.awt.Color;

public class ScriptWrapper {
    private String script;
    private String name;
    private String description;
    private String plan;
    private String user;
    final int VOIL_PLUS_COLOR = -10496;
    final int VOIL_COLOR = -4144960;
    final int VOIL_MONTHLY_COLOR = -3309774;
    final int OWNER_COLOR = -65536;
    final int BETA_TESTER_COLOR = -8388480;

    public ScriptWrapper(String name, String description, String user, String plan, String script) {
        this.script = script;
        this.name = name;
        this.description = description;
        this.plan = plan;
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlan() {
        return this.plan;
    }

    public Color getColor() {
        switch (this.plan) {
            case "owner": {
                return new Color(-65536);
            }
            case "voil": {
                return new Color(-4144960);
            }
            case "voil+": {
                return new Color(-10496);
            }
            case "staff": {
                return new Color(-3309774);
            }
            case "beta": {
                return new Color(-8388480);
            }
        }
        return Color.WHITE;
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
