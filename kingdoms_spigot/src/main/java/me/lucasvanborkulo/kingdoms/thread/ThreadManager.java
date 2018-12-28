package me.lucasvanborkulo.kingdoms.thread;

import me.lucasvanborkulo.kingdoms.Main;

public class ThreadManager {

    private Main main;

    private RefreshThread refreshThread;

    private Integer tickspeed;

    private Integer refreshDelay;

    public ThreadManager(Main main) {
        this.main = main;

        this.tickspeed = 20;

        this.refreshDelay = this.main.getConfig().getInt("cache_refresh_delay");

        this.refreshThread = new RefreshThread(this.main);

        this.refreshThread.runTaskTimerAsynchronously(this.main, 0, this.tickspeed*this.refreshDelay);
    }
}
