package me.onatic.unnamedgungame.items;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemPropertiesManager {

    private Map<ItemStack, Map<String, Object>> itemProperties;

    public ItemPropertiesManager() {
        this.itemProperties = new HashMap<>();
    }

    public void setProperties(ItemStack item, Map<String, Object> properties) {
        this.itemProperties.put(item, properties);
    }

    public Map<String, Object> getProperties(ItemStack item) {
        return this.itemProperties.get(item);
    }

    // Add methods for each property here
    public void setProperty(ItemStack item, String key, Object value) {
        Map<String, Object> properties = this.itemProperties.get(item);
        if (properties == null) {
            properties = new HashMap<>();
            this.itemProperties.put(item, properties);
        }
        properties.put(key, value);
    }

    public Object getProperty(ItemStack item, String key) {
        Map<String, Object> properties = this.itemProperties.get(item);
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }

    public boolean isConsumable(ItemStack item) {
        return (boolean) getProperty(item, "isConsumable");
    }

    public void setConsumable(ItemStack item, boolean isConsumable) {
        setProperty(item, "isConsumable", isConsumable);
    }

    public int getCharges(ItemStack item) {
        return (int) getProperty(item, "charges");
    }

    public void setCharges(ItemStack item, int charges) {
        setProperty(item, "charges", charges);
    }

    public long getCooldown(ItemStack item) {
        return (long) getProperty(item, "cooldown");
    }

    public void setCooldown(ItemStack item, long cooldown) {
        setProperty(item, "cooldown", cooldown);
    }

    public long getActivationTime(ItemStack item) {
        return (long) getProperty(item, "activationTime");
    }

    public void setActivationTime(ItemStack item, long activationTime) {
        setProperty(item, "activationTime", activationTime);
    }

    public double getDamage(ItemStack item) {
        return (double) getProperty(item, "damage");
    }

    public void setDamage(ItemStack item, double damage) {
        setProperty(item, "damage", damage);
    }

    public int getModelData(ItemStack item) {
        return (int) getProperty(item, "modelData");
    }

    public void setModelData(ItemStack item, int modelData) {
        setProperty(item, "modelData", modelData);
    }

    public String getPotionEffects(ItemStack item) {
        return (String) getProperty(item, "potionEffects");
    }

    public void setPotionEffects(ItemStack item, String potionEffects) {
        setProperty(item, "potionEffects", potionEffects);
    }

    public String getAttributes(ItemStack item) {
        return (String) getProperty(item, "attributes");
    }

    public void setAttributes(ItemStack item, String attributes) {
        setProperty(item, "attributes", attributes);
    }
    public String getName(ItemStack item) {
        return (String) getProperty(item, "name");
    }

    public void setName(ItemStack item, String name) {
        setProperty(item, "name", name);
    }
}