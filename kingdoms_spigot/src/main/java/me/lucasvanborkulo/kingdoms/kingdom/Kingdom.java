package me.lucasvanborkulo.kingdoms.kingdom;

import lombok.AccessLevel;
import lombok.Getter;
import me.lucasvanborkulo.kingdoms.plugin.Plugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Kingdom {

    @Getter(AccessLevel.PUBLIC) private String ip;
    @Getter(AccessLevel.PUBLIC) private Integer port;
    @Getter(AccessLevel.PUBLIC) private Player owner;
    @Getter(AccessLevel.PUBLIC) private ArrayList<Plugin> installedPlugins;

    public Kingdom(String ip, Integer port, Player owner) {
        this.ip = ip;
        this.port = port;
        this.owner = owner;
        this.installedPlugins = new ArrayList<Plugin>();
    }

}
