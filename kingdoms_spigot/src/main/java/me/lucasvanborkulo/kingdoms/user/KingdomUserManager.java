package me.lucasvanborkulo.kingdoms.user;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KingdomUserManager {

    private Map<Player, KingdomUser> kingdomUserCache;

    private Main main;

    public KingdomUserManager(Main main) {
        this.main = main;

        this.kingdomUserCache = new HashMap<Player, KingdomUser>();
    }

    public KingdomUser getKingdomUser(Player bukkitPlayer) {
        if (this.kingdomUserCache.containsKey(bukkitPlayer)) {
            return this.kingdomUserCache.get(bukkitPlayer);
        } else {
            KingdomUser kingdomUser = new KingdomUser(bukkitPlayer);
            this.kingdomUserCache.put(bukkitPlayer, kingdomUser);
            return kingdomUser;
        }
    }

    public void updateCache() {
        this.kingdomUserCache.clear();
        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            KingdomUser kingdomUser = new KingdomUser(bukkitPlayer);
            kingdomUser.setKingdomTier(this.main.tierManager.getTier(bukkitPlayer));
            this.kingdomUserCache.put(bukkitPlayer, kingdomUser);
        }
        Commons.logAction("Updated the KingdomUser cache!");
    }
}
