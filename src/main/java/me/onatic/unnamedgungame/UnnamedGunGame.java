package me.onatic.unnamedgungame;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.onatic.unnamedgungame.listeners.BarricadeListener;
import me.onatic.unnamedgungame.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnnamedGunGame extends JavaPlugin {

    public static StateFlag use_barricade;
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
        getServer().getPluginManager().registerEvents(new BarricadeListener(), this);
        this.getCommand("ugg").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}