package me.lucasvanborkulo.kingdoms.listeners;

import me.lucasvanborkulo.kingdoms.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Main main;

    public PlayerJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        this.main.mySQLDriver.insertPlayer(bukkitPlayer);
    }
}
