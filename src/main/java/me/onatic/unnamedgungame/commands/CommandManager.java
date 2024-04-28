package me.onatic.unnamedgungame.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
                    Countdown countdown = countdowns.get(sender);
                    if (countdown != null) {
                        countdown.cancel();
                    }

                    try {
                        if (args.length > 2 && args[args.length - 2].equalsIgnoreCase("request")) {
                            Player target = Bukkit.getPlayer(args[args.length - 1]);
                            if (target == null) {
                                sender.sendMessage("Player not found.");
                                return true;
                            }
                            countdown = new Countdown(sender, target, Arrays.copyOf(args, args.length - 2));
                            countdowns.put(sender, countdown);
                        } else {
                            countdown = new Countdown(sender, args);
                            if (countdown.isBroadcast()) {
                                if (broadcastCountdown != null) {
                                    broadcastCountdown.cancel();
                                }
                                broadcastCountdown = countdown;
                            }
                            countdown.start();
                            countdowns.put(sender, countdown);
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("Correct format: /ugg countdown <time> [broadcast]");
                    }
                    break;

                case "accept":
                    countdown = countdowns.get(sender);
                    if (countdown != null) {
                        countdown.acceptRequest();
                    } else {
                        sender.sendMessage("No countdown request to accept.");
                    }
                    break;

                case "deny":
                    countdown = countdowns.get(sender);
                    if (countdown != null) {
                        countdown.cancel();
                        countdowns.remove(sender);
                    } else {
                        sender.sendMessage("No countdown request to deny.");
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