package me.onatic.unnamedgungame;

import me.onatic.unnamedgungame.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnnamedGunGame extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("ugg").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}