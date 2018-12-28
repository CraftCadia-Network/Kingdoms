package me.lucasvanborkulo.kingdoms.gui;

import me.lucasvanborkulo.kingdoms.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIManager {

    private Main main;

    public GUIManager(Main main) {
        this.main = main;
    }

    public void openPluginsGUI(Player player) {
        Inventory GUI = Bukkit.createInventory(null, 9*2, "PLACEHOLDER");
    }

}
