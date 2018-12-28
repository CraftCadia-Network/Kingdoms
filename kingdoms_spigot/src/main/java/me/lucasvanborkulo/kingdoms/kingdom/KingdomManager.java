package me.lucasvanborkulo.kingdoms.kingdom;

import lombok.AccessLevel;
import lombok.Getter;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.user.KingdomUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomManager {

    private Main main;

    @Getter(AccessLevel.PUBLIC) public HashMap<KingdomUser, Kingdom> kingdoms = new HashMap<KingdomUser, Kingdom>();

    public KingdomManager(Main main) {
        this.main = main;
    }

    public void updateCache() {
        this.kingdoms.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            
        }
    }

    public Integer getPluginsLeft(KingdomUser kingdomUser) {
        if (kingdoms.containsKey(kingdomUser)) {
            Integer installed = kingdoms.get(kingdomUser).getInstalledPlugins().size();
            Integer allowed = kingdomUser.getKingdomTier().getMaxPlugins();
            Integer pluginsLeft = allowed - installed;
            return pluginsLeft;
        } else {
            return 0;
        }
    }
}
