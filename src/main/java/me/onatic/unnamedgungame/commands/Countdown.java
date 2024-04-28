package me.onatic.unnamedgungame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown {

    private int totalSeconds;
    private boolean broadcast;
    private BukkitRunnable task;
    private CommandSender sender;

    public Countdown(CommandSender sender, String[] args) {
        this.sender = sender;

        if (args.length < 2) {
            sender.sendMessage("Usage: /ugg countdown <time> [broadcast]");
            throw new IllegalArgumentException("Invalid arguments");
        }

        for (int i = 1; i < args.length; i++) {
            if (args[i].endsWith("h")) {
                int hours = Integer.parseInt(args[i].substring(0, args[i].length() - 1));
                this.totalSeconds += hours * 3600;
            } else if (args[i].endsWith("m")) {
                int minutes = Integer.parseInt(args[i].substring(0, args[i].length() - 1));
                this.totalSeconds += minutes * 60;
            } else if (args[i].endsWith("s")) {
                int seconds = Integer.parseInt(args[i].substring(0, args[i].length() - 1));
                this.totalSeconds += seconds;
            } else if (args[i].equalsIgnoreCase("broadcast")) {
                this.broadcast = true;
                if (!sender.hasPermission("countdown.broadcast")) {
                    throw new IllegalArgumentException("You do not have permission to broadcast the countdown");
                }
            } else {
                throw new IllegalArgumentException("Invalid time format: " + args[i]);
            }
        }
    }

    public void start() {
        task = new BukkitRunnable() {
            int remainingSeconds = totalSeconds;

            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    sendMessage("");
                    sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "GO!");
                    this.cancel();
                } else {
                    ChatColor color = ChatColor.WHITE;
                    if (remainingSeconds == 3) {
                        color = ChatColor.RED;
                    } else if (remainingSeconds == 2) {
                        color = ChatColor.GOLD;
                    } else if (remainingSeconds == 1) {
                        color = ChatColor.GREEN;
                    }

                    if ((remainingSeconds <= 10) ||
                        (remainingSeconds <= 30 && remainingSeconds % 5 == 0) ||
                        (remainingSeconds <= 60 && remainingSeconds % 10 == 0) ||
                        (remainingSeconds > 60 && remainingSeconds % 60 == 0)) {
                        sendMessage(color + "" + remainingSeconds + " seconds remaining");
                    }
                    remainingSeconds--;
                }
            }
        };

        Plugin plugin = Bukkit.getPluginManager().getPlugin("UnnamedGunGame");
        task.runTaskTimer(plugin, 0L, 20L);
    }

    private void sendMessage(String message) {
        if (broadcast) {
            Bukkit.broadcastMessage(message);
        } else {
            sender.sendMessage(message);
        }
    }
    public boolean isBroadcast() {
        return broadcast;
    }
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (this == CommandManager.getBroadcastCountdown()) {
            CommandManager.setBroadcastCountdown(null);
        }
    }
}