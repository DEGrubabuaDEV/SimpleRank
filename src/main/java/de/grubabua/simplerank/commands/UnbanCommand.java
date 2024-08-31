package de.grubabua.simplerank.commands;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class UnbanCommand implements CommandExecutor {
    private final SimpleRank plugin;

    public UnbanCommand(SimpleRank plugin) {
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

            if (permissions.contains("ban") ||permissions.contains("administration") || player.hasPermission("operator")) {
                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "Usage: /unban <player>");
                    return true;
                }

                String targetName = args[0];

                if (Bukkit.getBanList(BanList.Type.NAME).isBanned(targetName)) {
                    Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);
                    player.sendMessage(ChatColor.GREEN + "Player " + targetName + " has been unbanned.");
                } else {
                    player.sendMessage(ChatColor.RED + "Player " + targetName + " is not banned.");
                }
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
