package me.lucasvanborkulo.server.tier;

import lombok.AccessLevel;
import lombok.Getter;

public enum Tier {

    TIER0(5, 2, 512), TIER1(10, 6, 1024), TIER2(30, 11, 1024*2), TIER3(50, 16, 1024*3), TIER4(70, 22, 1024*4), TIER5(90, 30, 1024*5), TIER6(110, 100, 1024*6), TIER7(130, 100, 1024*7), TIER8(150, 100, 1024*8);

    @Getter(AccessLevel.PUBLIC) Integer maxPlayers;
    @Getter(AccessLevel.PUBLIC) Integer maxPlugins;
    @Getter(AccessLevel.PUBLIC) Integer memory;


    Tier(Integer maxPlayers, Integer maxPlugins, Integer memory) {
        this.maxPlayers = maxPlayers;
        this.maxPlugins = maxPlugins;
        this.memory = memory;
    }

}
