package me.onatic.unnamedgungame.commands;

import me.onatic.unnamedgungame.UnnamedGunGame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class StatsCommand implements CommandHandler {

    private final UnnamedGunGame plugin;

    public StatsCommand(UnnamedGunGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        Map<String, Integer> stats = plugin.getDatabaseManager().getAllStats(playerUUID);

        StringBuilder statsMessage = new StringBuilder("Your stats:\n");
        for (Map.Entry<String, Integer> stat : stats.entrySet()) {
            statsMessage.append(stat.getKey()).append(": ").append(stat.getValue()).append("\n");
        }

        player.sendMessage(statsMessage.toString());
    }
}