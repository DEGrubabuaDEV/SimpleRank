package de.grubabua.simplerank.commands;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class KickCommand implements CommandExecutor {
    private final SimpleRank plugin;

    public KickCommand(SimpleRank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String rawRank = plugin.getConfig().getString("rank.currentrank." + playerUUID);

        if (rawRank != null || player.hasPermission("operator")) {
            String rank = ChatColor.stripColor(rawRank);
            List<String> permissions = plugin.getConfig().getStringList("rank.permissions." + rank);

            if (permissions.contains("kick") ||permissions.contains("administration") || player.hasPermission("operator")) {
                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "Usage: /kick <player>");
                    return true;
                }

                String targetName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetName);

                if (targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }

                targetPlayer.kickPlayer(ChatColor.RED + "You have been kicked");
                player.sendMessage(ChatColor.GREEN + "Player " + targetPlayer.getName() + " has been kicked.");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have a rank assigned.");
            return true;
        }
    }
}
