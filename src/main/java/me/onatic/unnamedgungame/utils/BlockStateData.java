package me.onatic.unnamedgungame.utils;

import org.bukkit.Material;

public class BlockStateData {
    private Material material;
    private org.bukkit.block.data.BlockData blockData;

    public BlockStateData(Material material, org.bukkit.block.data.BlockData blockData) {
        this.material = material;
        this.blockData = blockData;
    }

    public Material getMaterial() {
        return material;
    }

    public org.bukkit.block.data.BlockData getBlockData() {
        return blockData;
    }
}