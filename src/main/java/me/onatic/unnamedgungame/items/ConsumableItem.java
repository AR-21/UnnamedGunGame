package me.onatic.unnamedgungame.items;

import me.onatic.unnamedgungame.UnnamedGunGame;
import org.bukkit.entity.Player;

public abstract class ConsumableItem {
    private String name;
    private int quantity;
    private final UnnamedGunGame plugin;

    public ConsumableItem(String name, int quantity, UnnamedGunGame unnamedGunGame) {
        this.name = name;
        this.quantity = quantity;
        this.plugin = unnamedGunGame;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    protected UnnamedGunGame getPlugin() {
        return plugin;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // This method will be implemented by each specific consumable item
    public abstract void use(Player player);

    public void consume(Player player) {
        if (quantity > 0) {
            quantity--;
        } else {
            System.out.println("You have no more " + name + " left to use.");
        }
    }
}