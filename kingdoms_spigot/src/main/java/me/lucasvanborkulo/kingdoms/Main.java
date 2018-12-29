package me.lucasvanborkulo.kingdoms;

import me.lucasvanborkulo.kingdoms.commands.Kingdoms;
import me.lucasvanborkulo.kingdoms.gui.GUIManager;
import me.lucasvanborkulo.kingdoms.listeners.GUIClick;
import me.lucasvanborkulo.kingdoms.listeners.PlayerJoin;
import me.lucasvanborkulo.kingdoms.multicraft.MulticraftDriver;
import me.lucasvanborkulo.kingdoms.mysql.MySQLDriver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    /*
     * Drivers
     */

    public MulticraftDriver multicraftDriver;
    public MySQLDriver mySQLDriver;

    /*
     * Managers
     */

    public GUIManager guiManager;

    /*
     * This is triggered once the server starts.
     */

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.registerCommands();
        this.registerEvents();

        this.mySQLDriver = new MySQLDriver(this);
        this.multicraftDriver = new MulticraftDriver(this);
        this.guiManager = new GUIManager(this);
    }

    /*
     * This is triggered once the server stops.
     */

    @Override
    public void onDisable() {
        this.multicraftDriver = null;
        this.mySQLDriver = null;
        this.guiManager = null;
    }

    /*
     * This is used for registering commands.
     */

    private void registerCommands() {
        Bukkit.getPluginCommand("kingdoms").setExecutor(new Kingdoms(this));
    }

    /*
     * This is used for registering events.
     */

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new GUIClick(this), this);
    }
}
