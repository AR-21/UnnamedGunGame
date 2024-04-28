package me.onatic.unnamedgungame.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private Map<CommandSender, Countdown> countdowns = new HashMap<>();
    private static Countdown broadcastCountdown;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ugg") || label.equalsIgnoreCase("unnamedgungame")) {
            if (args.length == 0) {
                sendCommandList(sender);
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "countdown":
                    if (args.length > 1 && args[1].equalsIgnoreCase("cancel")) {
                        Countdown countdown = countdowns.get(sender);
                        if (countdown != null) {
                            countdown.cancel();
                            countdowns.remove(sender);
                            sender.sendMessage("Your countdown has been cancelled.");
                        } else {
                            sender.sendMessage("You don't have an active countdown.");
                        }
                        return true;
                    }

                    Countdown countdown = countdowns.get(sender);
                    if (countdown != null) {
                        countdown.cancel();
                    }

                    try {
                        countdown = new Countdown(sender, args);
                        if (countdown.isBroadcast()) {
                            if (broadcastCountdown != null) {
                                broadcastCountdown.cancel();
                            }
                            broadcastCountdown = countdown;
                        }
                        countdown.start();
                        countdowns.put(sender, countdown);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("Correct format: /ugg countdown <time> [broadcast]");
                    }
                    break;

                // Add more cases for other subcommands

                default:
                    sendCommandList(sender);
                    break;
            }

            return true;
        }

        return false;
    }
    private void sendCommandList(CommandSender sender) {
        sender.sendMessage("List of available commands:");
        sender.sendMessage("/ugg countdown <Hours> <Minutes> <Seconds> ");
        // Add more commands here
    }

    public static Countdown getBroadcastCountdown() {
        return broadcastCountdown;
    }

    public static void setBroadcastCountdown(Countdown countdown) {
        broadcastCountdown = countdown;
    }
}