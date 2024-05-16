package me.onatic.unnamedgungame.items;

import me.onatic.unnamedgungame.utils.BlockData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BarricadeItemHandler {

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

    public ItemStack createBarricadeItem() {
        ItemStack barricadeItem = new ItemStack(Material.STICK);
        ItemMeta meta = barricadeItem.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + BARRICADE_ITEM_NAME);
        barricadeItem.setItemMeta(meta);
        return barricadeItem;
    }

    public BlockData[][][][] getStructures() {
        return STRUCTURES;
    }

    public String getBarricadeItemName() {
        return BARRICADE_ITEM_NAME;
    }
    // Your other methods here...
}