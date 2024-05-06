package me.onatic.unnamedgungame.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomItemLoader {

    public ItemStack loadItem(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Material type = Material.valueOf(config.getString("type"));
        String name = config.getString("name");
        String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("displayName"));
        List<String> lore = config.getStringList("lore");
        List<String> enchantments = config.getStringList("enchantments");

        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);

            for (String enchantment : enchantments) {
                String[] parts = enchantment.split(":");
                Enchantment ench = Enchantment.getByName(parts[0]);
                int level = Integer.parseInt(parts[1]);
                if (ench != null) {
                    meta.addEnchant(ench, level, true);
                }
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public List<ItemStack> loadItemsFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }

        List<ItemStack> items = new ArrayList<>();
        for (File file : files) {
            ItemStack item = loadItem(file.getPath());
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }
}