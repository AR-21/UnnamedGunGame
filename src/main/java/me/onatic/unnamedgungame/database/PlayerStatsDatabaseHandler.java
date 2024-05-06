package me.onatic.unnamedgungame.database;

import java.util.UUID;

public class PlayerStatsDatabaseHandler {
    private final DatabaseManager databaseManager;

    public PlayerStatsDatabaseHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void incrementKillStat(UUID playerUUID) {
        int currentKills = databaseManager.getStat(playerUUID, "kills");
        databaseManager.updateStat(playerUUID, "kills", currentKills + 1);
    }

    public void incrementDeathStat(UUID playerUUID) {
        int currentDeaths = databaseManager.getStat(playerUUID, "deaths");
        databaseManager.updateStat(playerUUID, "deaths", currentDeaths + 1);
    }

    public void incrementAssistStat(UUID playerUUID) {
        int currentAssists = databaseManager.getStat(playerUUID, "assists");
        databaseManager.updateStat(playerUUID, "assists", currentAssists + 1);
    }


}