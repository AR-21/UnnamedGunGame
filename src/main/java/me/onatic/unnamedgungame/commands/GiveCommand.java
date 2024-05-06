package me.onatic.unnamedgungame.commands;

import me.onatic.unnamedgungame.UnnamedGunGame;
import me.onatic.unnamedgungame.items.CustomItemLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveCommand {

    public void execute(CommandSender sender, String[] args) {
        if (args.length > 2) {
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage("Player not found.");
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount. Please enter a number.");
                return;
            }

            // Use the CustomItemLoader instance from UnnamedGunGame
            CustomItemLoader itemLoader = UnnamedGunGame.getPlugin(UnnamedGunGame.class).getItemLoader();
            List<ItemStack> customItems = itemLoader.loadItemsFromDirectory("Items");

            if (customItems != null) {
                for (ItemStack customItem : customItems) {
                    ItemStack itemToGive = customItem.clone();
                    itemToGive.setAmount(amount);
                    targetPlayer.getInventory().addItem(itemToGive);
                }
                sender.sendMessage("You have given " + targetPlayer.getName() + " " + amount + " of each item.");
            } else {
                sender.sendMessage("Failed to load custom items.");
            }
        } else {
            sender.sendMessage("This command requires a player and an amount.");
        }
    }
}