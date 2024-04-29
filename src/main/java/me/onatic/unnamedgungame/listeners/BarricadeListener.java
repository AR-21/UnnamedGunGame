package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.BlockData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import me.onatic.unnamedgungame.UnnamedGunGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarricadeListener implements Listener {

    public ItemStack createBarricadeItem() {
        ItemStack barricade = new ItemStack(Material.STICK);
        ItemMeta meta = barricade.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Deployable Barricade");
        barricade.setItemMeta(meta);
        return barricade;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getHand() == EquipmentSlot.HAND ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();

        if (itemInHand.getType() == Material.STICK && itemInHand.getItemMeta().getDisplayName().contains("Deployable Barricade")) {
            player.launchProjectile(Egg.class);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        event.getEgg().setVelocity(event.getEgg().getVelocity().multiply(0.5));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg) {
            Egg egg = (Egg) event.getEntity();
            if (egg.getShooter() instanceof Player) {
                Player player = (Player) egg.getShooter();
                ItemStack itemInHand = player.getInventory().getItemInMainHand();

                if (itemInHand.getType() == Material.STICK && itemInHand.getItemMeta().getDisplayName().contains("Deployable Barricade")) {
                    Block baseBlock = egg.getLocation().getBlock();

                    BlockData[][][][] structures = {
                            {
                                    {
                                            {new BlockData(Material.AIR, BlockFace.NORTH, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false), new BlockData(Material.OAK_SLAB, BlockFace.NORTH, true), new BlockData(Material.OAK_SLAB, BlockFace.NORTH, false)},
                                    },
                                    {
                                            {new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, false), new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false), new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false), new BlockData(Material.OAK_PLANKS, BlockFace.NORTH, false)},
                                    }
                            },
                            {
                                    {
                                            {new BlockData(Material.AIR, BlockFace.NORTH, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false)},
                                    },
                                    {
                                            {new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.AIR, null, false), new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false), new BlockData(Material.AIR, null, false), new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false)},
                                    }
                            },
                            {
                                    {
                                            {new BlockData(Material.AIR, BlockFace.NORTH, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false), new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.OAK_STAIRS, BlockFace.SOUTH, false)},
                                    },
                                    {
                                            {new BlockData(Material.OAK_STAIRS, BlockFace.WEST, true), new BlockData(Material.AIR, null, false), new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false), new BlockData(Material.AIR, null, false), new BlockData(Material.OAK_STAIRS, BlockFace.EAST, false)},
                                    }
                            },
                    };

                    Random random = new Random();
                    BlockData[][][] structure = structures[random.nextInt(structures.length)];

                    BlockData[][][] rotatedStructure = rotateStructure(structure, player.getFacing());

                    // Create a list to store the locations of the blocks that were placed
                    List<Location> placedBlocks = new ArrayList<>();

                    for (int x = 0; x < rotatedStructure.length; x++) {
                        for (int y = 0; y < rotatedStructure[0].length; y++) {
                            for (int z = 0; z < rotatedStructure[0][0].length; z++) {
                                Block currentBlock = baseBlock.getRelative(x - 1, y, z - 1);

                                if (currentBlock.getType() == Material.AIR) {
                                    BlockData blockData = rotatedStructure[x][y][z];
                                    currentBlock.setType(blockData.getMaterial());

                                    if (currentBlock.getType().name().endsWith("_STAIRS")) {
                                        Stairs stairs = (Stairs) currentBlock.getBlockData();
                                        stairs.setFacing(blockData.getBlockFace());
                                        stairs.setHalf(blockData.isUpsideDown() ? Stairs.Half.BOTTOM : Stairs.Half.TOP);
                                        currentBlock.setBlockData(stairs);
                                    }

                                    // Add the location of the block to the list
                                    placedBlocks.add(currentBlock.getLocation());
                                }
                            }
                        }
                    }

                    // Schedule a task to remove the barricade after an amount of ticks
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Location location : placedBlocks) {
                                location.getBlock().setType(Material.AIR);
                            }
                        }
                    }.runTaskLater(JavaPlugin.getPlugin(UnnamedGunGame.class), 300);
                }
            }
        }
    }

    private BlockData[][][] rotateStructure(BlockData[][][] structure, BlockFace playerDirection) {
        BlockData[][][] rotatedStructure = new BlockData[structure.length][structure[0].length][structure[0][0].length];

        for (int x = 0; x < structure.length; x++) {
            for (int y = 0; y < structure[0].length; y++) {
                for (int z = 0; z < structure[0][0].length; z++) {
                    BlockData blockData = structure[x][y][z];

                    if (blockData.getMaterial().name().endsWith("_STAIRS")) {
                        BlockFace originalDirection = blockData.getBlockFace();
                        BlockFace newDirection;

                        switch (playerDirection) {
                            case EAST:
                                newDirection = rotateClockwise(originalDirection);
                                break;
                            case SOUTH:
                                newDirection = rotateClockwise(rotateClockwise(originalDirection));
                                break;
                            case WEST:
                                newDirection = rotateCounterClockwise(originalDirection);
                                break;
                            default: // NORTH
                                newDirection = originalDirection;
                                break;
                        }

                        rotatedStructure[x][y][z] = new BlockData(blockData.getMaterial(), newDirection, blockData.isUpsideDown());
                    } else {
                        rotatedStructure[x][y][z] = blockData;
                    }
                }
            }
        }

        return rotatedStructure;
    }

    private BlockFace rotateClockwise(BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.NORTH;
            default:
                return face;
        }
    }

    private BlockFace rotateCounterClockwise(BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.NORTH;
            default:
                return face;
        }
    }
}