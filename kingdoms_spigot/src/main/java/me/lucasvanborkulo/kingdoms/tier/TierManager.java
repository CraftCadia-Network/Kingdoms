package me.lucasvanborkulo.kingdoms.tier;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.user.KingdomUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TierManager {

    private Main main;

    private Map<Player, Tier> tierUserCache;


    public TierManager(Main main) {
        this.main = main;

        this.tierUserCache = new HashMap<Player, Tier>();
    }

    public Tier getTier(Player bukkitPlayer) {
        if (this.tierUserCache.containsKey(bukkitPlayer)) {
            return this.tierUserCache.get(bukkitPlayer);
        } else {
            Tier tier = Tier.TIER0;
            this.tierUserCache.put(bukkitPlayer, tier);
            return tier;
        }
    }


    //TODO: RETRIEVE TIER FROM DATABASE
    public void updateCache() {
        this.tierUserCache.clear();
        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Tier tier = Tier.TIER0;
            this.tierUserCache.put(bukkitPlayer, tier);
        }
        Commons.logAction("Updated the Tier cache!");
    }
}
