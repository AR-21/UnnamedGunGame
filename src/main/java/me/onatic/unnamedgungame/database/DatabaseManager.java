package me.onatic.unnamedgungame.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager {
    private Connection connection;

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:player_stats.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void insertStat(UUID playerUUID, String statName, int statValue) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player_stats (player_uuid, stat_name, stat_value) VALUES (?, ?, ?)")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, statName);
            statement.setInt(3, statValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStat(UUID playerUUID, String statName, int statValue) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE player_stats SET stat_value = ? WHERE player_uuid = ? AND stat_name = ?")) {
            statement.setInt(1, statValue);
            statement.setString(2, playerUUID.toString());
            statement.setString(3, statName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getStat(UUID playerUUID, String statName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT stat_value FROM player_stats WHERE player_uuid = ? AND stat_name = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, statName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("stat_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteStat(UUID playerUUID, String statName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_stats WHERE player_uuid = ? AND stat_name = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, statName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getAllStats(UUID playerUUID) {
        Map<String, Integer> stats = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT stat_name, stat_value FROM player_stats WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stats.put(resultSet.getString("stat_name"), resultSet.getInt("stat_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public void resetStats(UUID playerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_stats WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}