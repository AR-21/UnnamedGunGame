package me.onatic.unnamedgungame.commands;

import me.onatic.unnamedgungame.UnnamedGunGame;
import me.onatic.unnamedgungame.items.BarricadeItemHandler;
import me.onatic.unnamedgungame.items.Consumables.Stim;
import me.onatic.unnamedgungame.listeners.BarricadeListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandHandler {
    private final UnnamedGunGame plugin;

    public GiveCommand(UnnamedGunGame unnamedGunGame) {
        this.plugin = unnamedGunGame;
    }
    @Override
    public void handle(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Please specify a player, an item to give, and the amount.");
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        String itemName = args[2].toLowerCase();
        int amount = 1;

        if (args.length >= 4) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount.");
                return;
            }
        }

        switch (itemName) {
            case "barricade":
                BarricadeItemHandler barricadeItemHandler = new BarricadeItemHandler();
                ItemStack barricadeItem = barricadeItemHandler.createBarricadeItem();
                barricadeItem.setAmount(amount);
                targetPlayer.getInventory().addItem(barricadeItem);
                sendConfirmationMessage(sender, targetPlayer.getName(), amount, "deployable barricades");
                break;
            case "stim":
                Stim.createStimForPlayer(targetPlayer, amount, 1000L, 2.5, plugin);
                Stim stim = Stim.getStimForPlayer(targetPlayer);
                ItemStack stimItem = stim.toItemStack();
                stimItem.setAmount(amount);
                targetPlayer.getInventory().addItem(stimItem);

                sendConfirmationMessage(sender, targetPlayer.getName(), amount, "Stim");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid item.");
                break;
        }
    }

    private void sendConfirmationMessage(CommandSender sender, String playerName, int amount, String itemName) {
        sender.sendMessage(String.format(ChatColor.DARK_GREEN + "Gave %s %d %s", playerName, amount, itemName));
    }
}