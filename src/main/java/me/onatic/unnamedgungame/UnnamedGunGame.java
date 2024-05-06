package me.onatic.unnamedgungame;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.onatic.unnamedgungame.database.DatabaseManager;
import me.onatic.unnamedgungame.items.CustomItemLoader;
import me.onatic.unnamedgungame.listeners.BarricadeListener;
import me.onatic.unnamedgungame.commands.CommandManager;
import me.onatic.unnamedgungame.listeners.KillListener;
import me.onatic.unnamedgungame.listeners.ProneListener;
import me.onatic.unnamedgungame.listeners.SuffocationDamageHandler;
import me.onatic.unnamedgungame.database.PlayerStatsDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.sql.Statement;

public final class UnnamedGunGame extends JavaPlugin {

    public static StateFlag use_barricade;
    private CustomItemLoader itemLoader;
    private DatabaseManager dbManager;
    private PlayerStatsDatabaseHandler playerStatsDatabaseHandler;

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("use-barricade", true);
            registry.register(flag);
            use_barricade = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("use-barricade");
            if (existing instanceof StateFlag) {
                use_barricade = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
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