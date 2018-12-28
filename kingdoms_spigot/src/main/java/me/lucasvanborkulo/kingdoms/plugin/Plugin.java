package me.lucasvanborkulo.kingdoms.plugin;

import lombok.AccessLevel;
import lombok.Getter;

public class Plugin {

    @Getter(AccessLevel.PUBLIC) private String link;

    public Plugin(String link) {
        this.link = link;
    }

}
