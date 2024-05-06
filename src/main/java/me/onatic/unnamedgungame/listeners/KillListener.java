package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.database.PlayerStatsDatabaseHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class KillListener implements Listener {

    private final PlayerStatsDatabaseHandler playerStatsDatabaseHandler;

    public KillListener(PlayerStatsDatabaseHandler playerStatsDatabaseHandler) {
        this.playerStatsDatabaseHandler = playerStatsDatabaseHandler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            UUID killerUUID = killer.getUniqueId();
            playerStatsDatabaseHandler.incrementKillStat(killerUUID);
            System.out.println("Before healing, health: " + killer.getHealth());
            killer.setHealth(20.0);
            System.out.println("After healing, health: " + killer.getHealth());
        }
    }
}