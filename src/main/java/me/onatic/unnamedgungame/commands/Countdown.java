package me.onatic.unnamedgungame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

public class Countdown {

    private int totalSeconds;
    private boolean broadcast;
    private BukkitRunnable task;
    private CommandSender sender;
    private Player target;
    private boolean requestAccepted = false; // New field

    // Constructor for when no target player is specified
    public Countdown(CommandSender sender, String[] args) {
        this(sender, null, args); // Call the other constructor with 'null' as the target player
    }

    // Constructor for when a target player is specified
    public Countdown(CommandSender sender, Player target, String[] args) {
        this.sender = sender;
        this.target = target;

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

        if (this.totalSeconds <= 0) {
            throw new IllegalArgumentException("Invalid time: " + args[1]);
        }

        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            TextComponent message = new TextComponent("You have received a countdown request. ");
            TextComponent accept = new TextComponent("[ACCEPT]");
            accept.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            accept.setBold(true); // Make the text bold
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ugg countdown accept"));
            TextComponent deny = new TextComponent("[DENY]");
            deny.setColor(net.md_5.bungee.api.ChatColor.RED);
            deny.setBold(true); // Make the text bold
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ugg countdown deny"));
            message.addExtra(accept);
            message.addExtra(" ");
            message.addExtra(deny);
            targetPlayer.spigot().sendMessage(message);
        } else if (args[args.length - 1].equalsIgnoreCase("broadcast")) {
            this.broadcast = true;
            if (!sender.hasPermission("countdown.broadcast")) {
                throw new IllegalArgumentException("You do not have permission to broadcast the countdown");
            }
        }
    }

    public void start() {
        if (target instanceof Player && !requestAccepted) {
            sender.sendMessage("The countdown request has not been accepted by the target player.");
            return;
        }

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
            if (target != null) {
                target.sendMessage(message);
            }
        }
    }

    public boolean isBroadcast() {
        return broadcast;
    }

public void acceptRequest() {
    if (this.requestAccepted) {
        return;
    }
    this.requestAccepted = true;
    if (target instanceof Player || broadcast || totalSeconds > 0) {
        start();
    }
    sender.sendMessage("Countdown request has been accepted.");
    if (target instanceof Player) {
        target.sendMessage("You have accepted the countdown request.");
    }
}

public void cancel() {
    if (task != null) {
        task.cancel();
        task = null;
    }
    if (this == CommandManager.getBroadcastCountdown()) {
        CommandManager.setBroadcastCountdown(null);
    }
    sender.sendMessage("Countdown request has been denied.");
    if (target instanceof Player) {
        target.sendMessage("You have denied the countdown request.");
    }
}
}