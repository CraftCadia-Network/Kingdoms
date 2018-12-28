package me.lucasvanborkulo.kingdoms.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class KingdomUser {

    @Getter(AccessLevel.PUBLIC) private OfflinePlayer bukkitPlayer;
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private Tier kingdomTier;

    public KingdomUser(OfflinePlayer bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.kingdomTier = Tier.TIER0;
    }
}
