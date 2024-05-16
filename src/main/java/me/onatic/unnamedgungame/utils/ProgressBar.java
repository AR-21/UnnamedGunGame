package me.onatic.unnamedgungame.utils;

import org.bukkit.ChatColor;

public class ProgressBar {

    public String generateProgressBar(int progress, int totalBlocks, ChatColor backgroundColor, ChatColor foregroundColor, char symbol, String keyword, long remainingTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(backgroundColor + keyword + " "); // Add keyword at the start
        for (int i = 0; i < totalBlocks; i++) {
            if (i < progress) {
                sb.append(foregroundColor + String.valueOf(symbol)); // Color the progress bar
            } else {
                sb.append(backgroundColor + String.valueOf(symbol)); // Color the empty part
            }
        }
        sb.append(backgroundColor + " "); // Add space
        sb.append(backgroundColor + String.format("%.1fs", (double) remainingTime / 1000)); // Add remaining time in seconds
        return sb.toString();
    }
}