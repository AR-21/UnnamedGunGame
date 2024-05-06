package me.onatic.unnamedgungame.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.onatic.unnamedgungame.BlockData;
import me.onatic.unnamedgungame.BlockStateData;
import me.onatic.unnamedgungame.UnnamedGunGame;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BarricadeListener implements Listener {

    private static final String BARRICADE_ITEM_NAME = "Deployable Barricade";
    private boolean deployableBarricadeUsed = false;
    private static final long COOLDOWN = 20L * 15;

    private Map<Player, Boolean> cooldowns = new HashMap<>();
    private Map<Player, Long> soundCooldowns = new HashMap<>();

    private static final BlockData[][][][] STRUCTURES = new BlockData[][][][]{
            // First structure
            {
                    // SOUTH/NORTH variation
                    {
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false),
                                    new BlockData(Material.AIR, BlockFace.NORTH, false),
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true),
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, false),
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.EAST, true),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                            }
                    },
                    // EAST/WEST variation
                    {
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false),
                                    new BlockData(Material.AIR, BlockFace.NORTH, false),
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, true),
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, false),
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.EAST, true),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                            }
                    }
            },
            // Second structure
            {
                    // SOUTH/NORTH variation
                    {
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, false)
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true)
                            }
                    },
                    // EAST/WEST variation
                    {
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, true),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, true)
                            }
                    }
            },
            // Third structure
            {
                    // SOUTH/NORTH variation
                    {
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.EAST, true)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true)
                            }
                    },
                    // EAST/WEST variation
                    {
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, true)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)
                            },
                            {
                                    new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false),
                                    new BlockData(Material.OAK_STAIRS, BlockFace.NORTH, true)
                            }
                    }
            }
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();


        // Check if the player is holding the barricade item
        if (item != null && item.getType() == Material.STICK && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().contains(BARRICADE_ITEM_NAME)) {

                // Check if the player is in a region with the use-barricade flag
                if (!canUseBarricade(player.getLocation())) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You can't use that here"));
                    return;
                }
                // Check if the player is on cooldown
                if (cooldowns.containsKey(player)) {
                        long lastSoundPlayed = soundCooldowns.getOrDefault(player, 0L);
                        if (System.currentTimeMillis() - lastSoundPlayed >= 1000) {
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                            soundCooldowns.put(player, System.currentTimeMillis());
                        }
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "Not Ready!"));
                    event.setCancelled(true); // Cancel the event to prevent the barricade from being thrown
                } else {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        player.launchProjectile(Egg.class);
                        item.setAmount(item.getAmount() - 1);
                        player.getInventory().setItemInMainHand(item);
                        event.setCancelled(true);
                        deployableBarricadeUsed = true;
                        cooldowns.put(player, true);

                        player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                        // Schedule a task to remove the player from the cooldown list after the cooldown period
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldowns.remove(player);
                            }
                        }.runTaskLater(JavaPlugin.getPlugin(UnnamedGunGame.class), COOLDOWN);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (deployableBarricadeUsed == true) {
            if (event.getEntity() instanceof Egg) {
                Egg egg = (Egg) event.getEntity();
                if (egg.getShooter() instanceof Player) {
                    Player player = (Player) egg.getShooter();
                    // Check if the player is in a region with the use-barricade flag
                    if (!canUseBarricade(player.getLocation())) {
                        return;
                    }
                    Vector direction = player.getLocation().getDirection();
                    BlockFace facing = getFacingDirection(direction);
                    int structureIndex = new Random().nextInt(STRUCTURES.length);
                    int variationIndex = (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) ? 0 : 1;
                    createBarricade(event.getEntity().getLocation(), facing, structureIndex, variationIndex);
                    deployableBarricadeUsed = false;
                }
            }
        }
    }

    private BlockFace getFacingDirection(Vector direction) {
        double rotation = (Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())) + 360) % 360;
        if (0 <= rotation && rotation < 45.0 || 315.0 <= rotation) {
            return BlockFace.SOUTH;
        } else if (45.0 <= rotation && rotation < 135.0) {
            return BlockFace.WEST;
        } else if (135.0 <= rotation && rotation < 225.0) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }

    private void createBarricade(Location location, BlockFace facing, int structureIndex, int variationIndex) {
        BlockData[][] structure = STRUCTURES[structureIndex][variationIndex];
        Map<Location, BlockStateData> originalBlocks = new HashMap<>();
        for (int x = 0; x < structure.length; x++) {
            for (int y = 0; y < structure[x].length; y++) {
                BlockData blockData = structure[x][y];
                Block block;
                switch (facing) {
                    case NORTH:
                    case SOUTH:
                        block = location.getWorld().getBlockAt(location.getBlockX() + x - 2, location.getBlockY() + y, location.getBlockZ());
                        break;
                    case EAST:
                    case WEST:
                        block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + y, location.getBlockZ() + x - 2);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + facing);
                }
                if (block.getType() == Material.AIR) {
                    // Save the block below the barricade
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            Block belowBlock = location.getWorld().getBlockAt(block.getX() + dx, block.getY() - 2, block.getZ() + dz);
                            originalBlocks.put(belowBlock.getLocation(), new BlockStateData(belowBlock.getType(), belowBlock.getBlockData()));
                        }
                    }

                    originalBlocks.put(block.getLocation(), new BlockStateData(block.getType(), block.getBlockData()));
                    block.setType(blockData.getMaterial());
                    if (block.getBlockData() instanceof Stairs) {
                        Stairs stairs = (Stairs) block.getBlockData();
                        stairs.setFacing(blockData.getBlockFace());
                        stairs.setHalf(blockData.isUpsideDown() ? Bisected.Half.TOP : Bisected.Half.BOTTOM);
                        block.setBlockData(stairs);
                    }
                }
            }
        }
        startBarricadeTimer(originalBlocks);
    }

    private void startBarricadeTimer(Map<Location, BlockStateData> originalBlocks) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(getClass()), () -> {
            for (Map.Entry<Location, BlockStateData> entry : originalBlocks.entrySet()) {
                Block block = entry.getKey().getBlock();
                BlockStateData blockStateData = entry.getValue();
                block.setType(blockStateData.getMaterial());
                block.setBlockData(blockStateData.getBlockData());
            }
            originalBlocks.clear();
        }, 20L * 10); // 10 seconds
    }

    public ItemStack createBarricadeItem() {
        ItemStack barricadeItem = new ItemStack(Material.STICK);
        ItemMeta meta = barricadeItem.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + BARRICADE_ITEM_NAME);
        barricadeItem.setItemMeta(meta);
        return barricadeItem;
    }
private boolean canUseBarricade(Location location) {
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();
    ApplicableRegionSet applicableRegions = query.getApplicableRegions(BukkitAdapter.adapt(location));

    for (ProtectedRegion region : applicableRegions) {
        StateFlag.State flagState = region.getFlag(UnnamedGunGame.use_barricade);
        if (flagState == StateFlag.State.ALLOW) {
            return true;
        }
    }
    return false;
}

}