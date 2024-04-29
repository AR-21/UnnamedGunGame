package me.onatic.unnamedgungame.listeners;

import me.onatic.unnamedgungame.BlockData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.util.Vector;

import java.util.Random;

public class BarricadeListener implements Listener {

    private static final String BARRICADE_ITEM_NAME = "Deployable Barricade";

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

        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(BARRICADE_ITEM_NAME)) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    player.launchProjectile(Egg.class);
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItemInMainHand(item);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg) {
            Egg egg = (Egg) event.getEntity();
            if (egg.getShooter() instanceof Player) {
                Player player = (Player) egg.getShooter();
                Vector direction = player.getLocation().getDirection();
                BlockFace facing = getFacingDirection(direction);
                int structureIndex = new Random().nextInt(STRUCTURES.length);
                int variationIndex = (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) ? 0 : 1;
                createBarricade(event.getEntity().getLocation(), facing, structureIndex, variationIndex);
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
        for (int x = 0; x < structure.length; x++) {
            for (int y = 0; y < structure[x].length; y++) {
                BlockData blockData = structure[x][y];
                Block block;
                switch (facing) {
                    case NORTH:
                    case SOUTH:
                        block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ());
                        break;
                    case EAST:
                    case WEST:
                        block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + y, location.getBlockZ() + x);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + facing);
                }
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

    public ItemStack createBarricadeItem() {
        ItemStack barricadeItem = new ItemStack(Material.STICK);
        ItemMeta meta = barricadeItem.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + BARRICADE_ITEM_NAME);
        barricadeItem.setItemMeta(meta);
        return barricadeItem;
    }
}