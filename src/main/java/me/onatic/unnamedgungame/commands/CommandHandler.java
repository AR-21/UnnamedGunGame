package me.onatic.unnamedgungame.commands;

import org.bukkit.command.CommandSender;

public interface CommandHandler {
    void handle(CommandSender sender, String[] args);
}