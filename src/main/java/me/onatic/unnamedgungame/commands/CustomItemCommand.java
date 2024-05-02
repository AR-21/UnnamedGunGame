package me.onatic.unnamedgungame.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.onatic.unnamedgungame.UnnamedGunGame;
import me.onatic.unnamedgungame.items.CustomItemLoader;
import me.onatic.unnamedgungame.items.ItemPropertiesManager;

import java.util.List;

public class CustomItemCommand implements CommandExecutor {

    private UnnamedGunGame plugin;
    private ItemPropertiesManager itemPropertiesManager;

    public CustomItemCommand(UnnamedGunGame plugin, ItemPropertiesManager itemPropertiesManager) {
        this.plugin = plugin;
        this.itemPropertiesManager = itemPropertiesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if the command has the correct number of arguments
        if (args.length < 3) {
            sender.sendMessage("Usage: /ugg give <player> <item> <quantity>");
            return true;
        }

        // Print all online players' names to the console
        Bukkit.getOnlinePlayers().forEach(player -> System.out.println(player.getName()));

        // Print the player name being passed to the getPlayerExact() method
        System.out.println("Player name: " + args[1].trim());

        // Get the target player
        Player targetPlayer = Bukkit.getPlayerExact(args[1].trim());
        if (targetPlayer == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        // Get the item name
        String itemName = args[2];

        // Get the quantity
        int quantity;
        try {
            quantity = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid quantity.");
            return true;
        }

        // Load the custom item
        CustomItemLoader itemLoader = plugin.getItemLoader();
        List<ItemStack> items = itemLoader.loadItems("Items");

        ItemStack customItem = items.stream()
                .filter(item -> {
                    String itemNameDebug = (String) itemPropertiesManager.getProperty(item, "name");
                    System.out.println("Retrieved item: " + itemName);
                    return itemNameDebug != null && itemName.equals(args[2]);
                })
                .findFirst()
                .orElse(null);

        if (customItem == null) {
            System.out.println("Item not found." + itemName);
            sender.sendMessage("Item not found.");
            return true;
        }

        // Set the quantity of the item
        customItem.setAmount(quantity);

        // Give the item to the player
        targetPlayer.getInventory().addItem(customItem);
        sender.sendMessage("You have given " + targetPlayer.getName() + " a " + itemName + "!");

        return true;
    }
}