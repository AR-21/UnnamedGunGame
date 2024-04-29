package me.onatic.unnamedgungame;

import me.onatic.unnamedgungame.listeners.BarricadeListener;
import me.onatic.unnamedgungame.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnnamedGunGame extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new BarricadeListener(), this);
        this.getCommand("ugg").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}