package me.lucasvanborkulo.kingdoms.listeners;

import me.lucasvanborkulo.kingdoms.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClick implements Listener {

    private Main main;

    public GUIClick(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {

    }
}
