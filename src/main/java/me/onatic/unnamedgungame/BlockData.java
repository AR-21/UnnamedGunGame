package me.onatic.unnamedgungame;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockData {
    private Material material;
    private BlockFace blockFace;
    private boolean isUpsideDown;

    public BlockData(Material material, BlockFace blockFace, boolean isUpsideDown) {
        this.material = material;
        this.blockFace = blockFace;
        this.isUpsideDown = isUpsideDown;
    }

    public Material getMaterial() {
        return material;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public boolean isUpsideDown() {
        return isUpsideDown;
    }
}