package me.onatic.unnamedgungame.items;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomItemLoader {

    private JavaPlugin plugin;
    private ItemPropertiesManager itemPropertiesManager;

    public CustomItemLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.itemPropertiesManager = new ItemPropertiesManager();
    }

    public List<ItemStack> loadItems(String directoryName) {
        File directory = new File(plugin.getDataFolder(), directoryName);
        if (!directory.exists() || !directory.isDirectory()) {
            return new ArrayList<>();
        }

        File[] itemFiles = directory.listFiles();
        List<ItemStack> items = new ArrayList<>();

        for (File itemFile : itemFiles) {
            FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(itemFile);
            ItemStack item = createItemStack(itemConfig);
            items.add(item);
        }

        return items;
    }

    private ItemStack createItemStack(FileConfiguration itemConfig) {
        String name = itemConfig.getString("name");
        System.out.println("Loaded item: " + name);
        String type = itemConfig.getString("type");
        String displayName = itemConfig.getString("displayName");
        List<String> lore = itemConfig.getStringList("lore");
        List<String> enchantments = itemConfig.getStringList("enchantments");
        boolean isConsumable = itemConfig.getBoolean("isConsumable", false);
        int charges = itemConfig.getInt("charges", 0);
        long cooldown = itemConfig.getLong("cooldown", 0L);
        long activationTime = itemConfig.getLong("activationTime", 0L);
        double damage = itemConfig.getDouble("damage", 0.0);
        int maxStackSize = itemConfig.getInt("maxStackSize", 64);
        int modelData = itemConfig.getInt("modelData", 0);
        List<String> potionEffects = itemConfig.getStringList("potionEffects");
        List<String> attributes = itemConfig.getStringList("attributes");

        ItemStack item = new ItemStack(Material.valueOf(type), maxStackSize);
        ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        if (enchantments != null) {
            for (String enchantment : enchantments) {
                String[] parts = enchantment.split(":");
                Enchantment ench = Enchantment.getByName(parts[0]);
                int level = Integer.parseInt(parts[1]);
                meta.addEnchant(ench, level, true);
            }
        }

        meta.setCustomModelData(modelData);
        item.setItemMeta(meta);

        // Store the new properties in the item's metadata
        itemPropertiesManager.setConsumable(item, isConsumable);
        itemPropertiesManager.setCharges(item, charges);
        itemPropertiesManager.setCooldown(item, cooldown);
        itemPropertiesManager.setActivationTime(item, activationTime);
        itemPropertiesManager.setDamage(item, damage);
        itemPropertiesManager.setModelData(item, modelData);
        itemPropertiesManager.setPotionEffects(item, String.join(",", potionEffects));
        itemPropertiesManager.setAttributes(item, String.join(",", attributes));
        itemPropertiesManager.setName(item, name);

        return item;
    }
}