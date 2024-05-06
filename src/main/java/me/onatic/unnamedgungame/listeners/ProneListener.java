package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.UnnamedGunGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.block.data.type.Snow;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProneListener implements Listener {

    private final UnnamedGunGame plugin;

    public ProneListener(UnnamedGunGame unnamedGunGame) {
        this.plugin = unnamedGunGame;
    }

    private final Map<UUID, Long> lastSneakTime = new HashMap<>();
    private final Map<UUID, Block> barrierBlocks = new HashMap<>();
    private final Map<UUID, Long> lastProneToggleTime = new HashMap<>();
    private static final long PRONE_TOGGLE_COOLDOWN = 1000; // 1 second cooldown

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return; // Only execute the logic when the player is sneaking

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Block blockBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material blockType = blockBelow.getType();

            // If the player cannot go prone on the block, return
            if (!canGoProne(blockType, blockBelow, player)) {
                return;
            }

            long time = System.currentTimeMillis();
            long lastTime = lastSneakTime.getOrDefault(player.getUniqueId(), 0L);
            lastSneakTime.put(player.getUniqueId(), time);
            if (time - lastTime >= 500) return; // If the player double-taps sneak within 0.5 seconds, toggle their prone state

            long lastToggleTime = lastProneToggleTime.getOrDefault(player.getUniqueId(), 0L);
            if (time - lastToggleTime < PRONE_TOGGLE_COOLDOWN) return; // If the player has toggled prone within the cooldown period, don't toggle their prone state

            lastProneToggleTime.put(player.getUniqueId(), time); // Update the time when the player last toggled prone

            if (barrierBlocks.containsKey(player.getUniqueId())) {
                System.out.println("Player is prone, unproning them"); // Add this line
                unpronePlayer(player);
            } else {
                player.setWalkSpeed(0.1F); // If the player is not prone, reduce their speed
                placeBarrierBlock(player); // Place a barrier block above their head
            }
        }, 1L); // Delay of 1 server tick
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block barrierBlock = barrierBlocks.get(player.getUniqueId());
        if (barrierBlock == null) return; // If the player is not prone, don't do anything

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        Block blockFrom = from.getBlock();
        Block blockTo = to.getBlock();

        // If the player is moving from a block they can go prone on to a block they cannot go prone on, unprone them
        Material blockToType = blockTo.getType();
        if (!canGoProne(blockToType, blockTo, player)) {
            unpronePlayer(player);
            return;
        }

        // Update the position of the barrier block
        barrierBlock.setType(Material.AIR);
        placeBarrierBlock(player); // Place a barrier block above their head
    }

    @EventHandler
    public void onPlayerVelocityChange(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        Vector velocity = event.getVelocity();

        System.out.println("Player's vertical velocity: " + velocity.getY()); // New debug message

        if (velocity.getY() > 0.2) {
            System.out.println("Player's vertical velocity exceeds the jump threshold"); // New debug message

            if (barrierBlocks.containsKey(player.getUniqueId())) {
                System.out.println("Player is prone, unproning them"); // Existing debug message
                unpronePlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        unpronePlayer(player);
    }

    private void unpronePlayer(Player player) {
        player.setWalkSpeed(0.2F);
        Block barrierBlock = barrierBlocks.remove(player.getUniqueId());
        if (barrierBlock != null) barrierBlock.setType(Material.AIR);
    }

    private boolean canGoProne(Material blockType, Block block,  Player player) {
        System.out.println("Block type: " + blockType); // Add this line
        if (player.getLocation().getBlock().getType().isOccluding()) {
            return false;
        }
        if (blockType == Material.AIR) {
            // Check the block slightly below the player's feet
            Block blockBelow = block.getLocation().subtract(0, 0.45, 0).getBlock();
            double blockBelowHeight = blockBelow.getBoundingBox().getHeight();
            if (!(blockBelowHeight > 0.55)) return false; // If the block's height is 1.0, allow the player to go prone
            System.out.println("Block below height: " + blockBelowHeight); // Add this line
        }
        if (blockType.name().endsWith("_FENCE") || blockType.name().endsWith("_WALL") || blockType == Material.STRING || blockType.name().endsWith("_GLASS_PANE")) return false;
        if (blockType == Material.SNOW) {
            // Check the number of layers of the snow block
            Snow snow = (Snow) block.getBlockData();
            if (snow.getLayers() > 3) return false;
        }
        double blockHeight = block.getBoundingBox().getHeight();
        if (blockHeight > 0.45 && blockHeight < 0.55) return false; // If the block's height is within the range of 0.4 to 0.6, prevent the player from going prone
        return true;
    }

    private void placeBarrierBlock(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 1.5); // Place the barrier block 2 blocks above the player's current location
        Block barrierBlock = location.getBlock();
        if (barrierBlock.getType() == Material.AIR && barrierBlock.getRelative(BlockFace.UP).getType() == Material.AIR) {
            barrierBlock.setType(Material.BARRIER);
            barrierBlocks.put(player.getUniqueId(), barrierBlock);
        } else {
            player.setWalkSpeed(0.2F); // If there is no space for the barrier block, don't slow down the player
        }
    }
}