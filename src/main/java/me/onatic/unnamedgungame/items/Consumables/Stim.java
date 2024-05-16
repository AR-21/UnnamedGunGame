package me.onatic.unnamedgungame.items.Consumables;

import me.onatic.unnamedgungame.UnnamedGunGame;
import me.onatic.unnamedgungame.items.ConsumableItem;
import me.onatic.unnamedgungame.utils.ProgressBar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Stim extends ConsumableItem implements Listener {
    private static HashMap<UUID, Stim> stimMap = new HashMap<>();

    private long cooldown; // Cooldown in milliseconds
    private long lastUsed; // Time when the item was last used
    private long usageTime; // Usage time in milliseconds
    private long startUsageTime; // Time when the player starts using the item
    private boolean isInUse = false;

    public Stim(int quantity, long cooldownInSeconds, long usageTimeInSeconds, UnnamedGunGame unnamedGunGame) {
        super("Stim", quantity, unnamedGunGame);
        this.cooldown = cooldownInSeconds * 1000; // Convert seconds to milliseconds
        this.lastUsed = 0;
        this.usageTime = usageTimeInSeconds * 1000; // Convert seconds to milliseconds
        this.startUsageTime = 0;
    }

    public static Stim createStimForPlayer(Player player, int quantity, long cooldown, double usageTimeInSeconds, UnnamedGunGame unnamedGunGame) {
        Stim stim = new Stim(quantity, 0, 0, unnamedGunGame); // set usageTime to 0 initially
        stim.setCooldownInSeconds(3); // Set cooldown to 30 seconds
        stim.setUsageTimeInSeconds(usageTimeInSeconds); // set usageTime in seconds
        stimMap.put(player.getUniqueId(), stim);
        return stim;
    }

    public static Stim getStimForPlayer(Player player) {
        return stimMap.get(player.getUniqueId());
    }

    public static void removeStimForPlayer(Player player) {
        stimMap.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.SLIME_BALL && item.getItemMeta().getDisplayName().contains("Stim")) {
            Stim stim = getStimForPlayer(player);
            if (stim != null) {
                stim.startUse(player);
            }
            event.setCancelled(true); // Cancel the event so the item isn't used in the default way
        }
    }

    public void startUse(Player player) {
        if (isInUse) {
            player.sendMessage("You are already using a stim. Please wait until it finishes.");
            return;
        }
        if (System.currentTimeMillis() - lastUsed < cooldown) {
            player.sendMessage("You can't use this item yet. Please wait for the cooldown.");
            return;
        }
        isInUse = true;
        startUsageTime = System.currentTimeMillis();

        new BukkitRunnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startUsageTime;
                if (elapsedTime < usageTime) {
                    updateProgressBar(player, usageTime - elapsedTime);
                } else {
                    use(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 1L);
    }

    private void updateProgressBar(Player player, long remainingTime) {
        long elapsedTime = usageTime - remainingTime;
        int progress = (int) ((elapsedTime / (double) usageTime) * 20);
        ProgressBar progressBar = new ProgressBar();
        String progressBarString = progressBar.generateProgressBar(progress, 20, ChatColor.DARK_GRAY, ChatColor.BLUE, '|', "Using", remainingTime);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(progressBarString));
    }

    @Override
    public void use(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startUsageTime >= usageTime) {
            // Heal the player
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
            // Update the last used time
            lastUsed = currentTime;
            isInUse = false;
            // Consume the item
            super.consume(player);
        } else {
            long remainingTime = usageTime - (currentTime - startUsageTime);
            updateProgressBar(player, remainingTime);
        }
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.SLIME_BALL); // Replace with the material you want
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Stim");
        item.setItemMeta(meta);
        return item;
    }

    public void setUsageTimeInSeconds(double seconds) {
        this.usageTime = (long) (seconds * 1000);
    }
    public void setCooldownInSeconds(long seconds) {
        this.cooldown = seconds * 1000; // Convert seconds to milliseconds
    }
}