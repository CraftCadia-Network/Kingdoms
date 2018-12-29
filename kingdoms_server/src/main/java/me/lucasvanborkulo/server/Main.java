package me.lucasvanborkulo.server;

import me.lucasvanborkulo.server.events.PlayerJoin;
import me.lucasvanborkulo.server.events.PlayerQuit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Starting this kingdom...");
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down this kingdom...");
    }
}
