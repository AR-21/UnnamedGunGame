package me.onatic.unnamedgungame.handlers;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class RespawnHandler {

    public RespawnHandler() {
    }

    public Location getRespawnLocationInRegion(Player player, String regionName) {
        World world = player.getWorld();
        RegionManager regionManager = getRegionManager(world);
        ProtectedRegion region = regionManager.getRegion(regionName);

        if (region != null) {
            Location minPoint = new Location(world, region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
            Location maxPoint = new Location(world, region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());

            Location randomLocation;
            do {
                randomLocation = getRandomLocation(minPoint, maxPoint);
            } while (!isLocationSuitable(randomLocation));

            return randomLocation;
        } else {
            player.sendMessage("The specified region does not exist.");
            return null;
        }
    }

    private RegionManager getRegionManager(World world) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
    }

    private Location getRandomLocation(Location min, Location max) {
        Random random = new Random();
        double x = min.getX() + (max.getX() - min.getX()) * random.nextDouble();
        double y = min.getY() + (max.getY() - min.getY()) * random.nextDouble();
        double z = min.getZ() + (max.getZ() - min.getZ()) * random.nextDouble();
        return new Location(min.getWorld(), x, y, z);
    }

    private boolean isLocationSuitable(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        World world = location.getWorld();

        // Check if the two blocks above the location are air
        boolean isAboveAir = world.getBlockAt(x, y, z).getType().isAir() && world.getBlockAt(x, y + 1, z).getType().isAir();

        // Check if any of the two blocks below the location are non-air
        boolean isBelowNonAir = !world.getBlockAt(x, y - 1, z).getType().isAir() || !world.getBlockAt(x, y - 2, z).getType().isAir();

        return isAboveAir && isBelowNonAir;
    }
}