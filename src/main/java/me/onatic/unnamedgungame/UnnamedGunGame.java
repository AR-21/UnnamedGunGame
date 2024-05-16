package me.onatic.unnamedgungame;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.onatic.unnamedgungame.database.DatabaseManager;
import me.onatic.unnamedgungame.handlers.SuffocationDamageHandler;
import me.onatic.unnamedgungame.items.Consumables.Stim;
import me.onatic.unnamedgungame.items.CustomItemLoader;
import me.onatic.unnamedgungame.listeners.*;
import me.onatic.unnamedgungame.commands.CommandManager;
import me.onatic.unnamedgungame.database.PlayerStatsDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.sql.Statement;

public final class UnnamedGunGame extends JavaPlugin {

    public static StateFlag use_barricade;
    public static StateFlag heal_on_kill;
    private CustomItemLoader itemLoader;
    private DatabaseManager dbManager;
    private PlayerStatsDatabaseHandler playerStatsDatabaseHandler;

    @Override
    public void onLoad() {
        use_barricade = registerFlag("use-barricade", true);
        heal_on_kill = registerFlag("heal-on-kill", false);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        dbManager = new DatabaseManager();
        dbManager.connect();
        playerStatsDatabaseHandler = new PlayerStatsDatabaseHandler(dbManager);

        getServer().getPluginManager().registerEvents(new SuffocationDamageHandler(), this);
        getServer().getPluginManager().registerEvents(new BarricadeListener(), this);
        getServer().getPluginManager().registerEvents(new ProneListener(this), this);
        getServer().getPluginManager().registerEvents(new KillListener(playerStatsDatabaseHandler), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new Stim(1, 1000L, 5000L, this), this);

        this.getCommand("ugg").setExecutor(new CommandManager(this));

        try (Statement statement = dbManager.getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS player_stats (" +
                    "player_uuid TEXT PRIMARY KEY," +
                    "stat_name TEXT," +
                    "stat_value INTEGER" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Load items on plugin enable
        itemLoader = new CustomItemLoader();
        itemLoader.loadItemsFromDirectory("Items");
    }

    @Override
    public void onDisable() {
        dbManager.disconnect();
    }

    private StateFlag registerFlag(String flagName, boolean defaultValue) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag(flagName, defaultValue);
            registry.register(flag);
            return flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(flagName);
            if (existing instanceof StateFlag) {
                return (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
                return null;
            }
        }
    }
    public CustomItemLoader getItemLoader() {
        return itemLoader;
    }

    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }

    public PlayerStatsDatabaseHandler getPlayerStatsDatabaseHandler() {
        return playerStatsDatabaseHandler;
    }
}