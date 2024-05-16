package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.handlers.RespawnHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final RespawnHandler respawnHandler;

    public PlayerRespawnListener() {
        this.respawnHandler = new RespawnHandler();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String regionName = "pvp"; // replace with your region name
        Location respawnLocation = respawnHandler.getRespawnLocationInRegion(player, regionName);
        if (respawnLocation != null) {
            event.setRespawnLocation(respawnLocation);
        }
    }
}