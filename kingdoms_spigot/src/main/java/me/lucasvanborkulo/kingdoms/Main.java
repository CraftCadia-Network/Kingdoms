package me.lucasvanborkulo.kingdoms;

import me.lucasvanborkulo.kingdoms.commands.Kingdoms;
import me.lucasvanborkulo.kingdoms.gui.GUIManager;
import me.lucasvanborkulo.kingdoms.listeners.PlayerJoin;
import me.lucasvanborkulo.kingdoms.multicraft.MulticraftDriver;
import me.lucasvanborkulo.kingdoms.mysql.MySQLDriver;
import me.lucasvanborkulo.kingdoms.redis.RedisDriver;
import me.lucasvanborkulo.kingdoms.thread.ThreadManager;
import me.lucasvanborkulo.kingdoms.tier.TierManager;
import me.lucasvanborkulo.kingdoms.user.KingdomUserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {

    /*
     * Drivers
     */

    public MulticraftDriver multicraftDriver;
    public MySQLDriver mySQLDriver;
    public RedisDriver redisDriver;

    /*
     * Managers
     */

    public GUIManager guiManager;
    public ThreadManager threadManager;
    public TierManager tierManager;
    public KingdomUserManager kingdomUserManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.multicraftDriver = new MulticraftDriver(this);
        this.mySQLDriver = new MySQLDriver(this);
        this.redisDriver = new RedisDriver(this);


        this.guiManager = new GUIManager(this);
        this.threadManager = new ThreadManager(this);
        this.tierManager = new TierManager(this);
        this.kingdomUserManager = new KingdomUserManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);

        Bukkit.getPluginCommand("kingdoms").setExecutor(new Kingdoms(this));
    }

    @Override
    public void onDisable() {
        this.multicraftDriver = null;
        this.mySQLDriver = null;
        this.redisDriver = null;

        this.guiManager = null;
        this.kingdomUserManager = null;
    }
}
