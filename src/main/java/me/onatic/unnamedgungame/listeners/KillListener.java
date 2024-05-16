package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.UnnamedGunGame;
import me.onatic.unnamedgungame.database.PlayerStatsDatabaseHandler;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.Location;
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
        Player deadPlayer = event.getEntity();

        if (killer != null) {
            UUID killerUUID = killer.getUniqueId();
            playerStatsDatabaseHandler.incrementKillStat(killerUUID);

            // Check if the player is in a region with the heal-on-kill flag
            if (canHealOnKill(killer.getLocation())) {
                System.out.println("Before healing, health: " + killer.getHealth());

                double healthToAdd = 10.0;
                double newHealth = killer.getHealth() + healthToAdd;
                double maxHealth = killer.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();

                if (newHealth > maxHealth) {
                    killer.setHealth(maxHealth);
                } else {
                    killer.setHealth(newHealth);
                }

                System.out.println("After healing, health: " + killer.getHealth());
            }
        }
        UUID deadPlayerUUID = deadPlayer.getUniqueId();
        playerStatsDatabaseHandler.incrementDeathStat(deadPlayerUUID);
    }
    
    private boolean canHealOnKill(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet applicableRegions = query.getApplicableRegions(BukkitAdapter.adapt(location));

        for (ProtectedRegion region : applicableRegions) {
            StateFlag.State flagState = region.getFlag(UnnamedGunGame.heal_on_kill);
            if (flagState == StateFlag.State.ALLOW) {
                return true;
            }
        }
        return false;
    }
}