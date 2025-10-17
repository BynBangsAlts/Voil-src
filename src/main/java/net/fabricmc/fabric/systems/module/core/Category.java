package net.fabricmc.fabric.systems.module.core;

public enum Category {
    Client("Client"),
    CrystalPvP("CrystalPvP"),
    Combat("Combat"),
    Movement("Movement"),
    Player("Player"),
    Render("Render"),
    Miscellaneous("Miscellaneous");

    public String name;

    private Category(String name) {
        this.name = name;
    }
}
