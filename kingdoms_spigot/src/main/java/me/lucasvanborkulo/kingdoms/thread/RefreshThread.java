package me.lucasvanborkulo.kingdoms.thread;

import me.lucasvanborkulo.kingdoms.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class RefreshThread extends BukkitRunnable {

    private Main main;

    public RefreshThread(Main main) {
        this.main = main;
    }

    public void run() {
        this.main.tierManager.updateCache();
        this.main.kingdomUserManager.updateCache();
    }
}
