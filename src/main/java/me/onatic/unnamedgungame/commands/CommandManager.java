package me.onatic.unnamedgungame.commands;

import me.onatic.unnamedgungame.UnnamedGunGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    private final UnnamedGunGame plugin;

    public CommandManager(UnnamedGunGame unnamedGunGame) {
        this.plugin = unnamedGunGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ugg")) {
            if (args.length == 0) {
                sendCommandList(sender);
                return true;
            }

            if (sender.hasPermission("unnamedgungame.admin")) {
                switch (args[0].toLowerCase()) {
                    case "give":
                        GiveCommand giveCommand = new GiveCommand(plugin);
                        giveCommand.handle(sender, args);
                        break;
                    case "announce":
                        // Your announce command logic here
                        break;
                    default:
                        sendCommandList(sender);
                        break;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }

            return true;
        }

        return false;
    }

    private void sendCommandList(CommandSender sender) {
        sender.sendMessage("List of available commands:");
        sender.sendMessage("/ugg give <player> <item> <quantity>");
        // Add more commands here
    }
}