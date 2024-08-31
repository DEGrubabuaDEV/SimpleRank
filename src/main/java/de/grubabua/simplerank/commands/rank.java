package de.grubabua.simplerank.commands;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class rank implements CommandExecutor {
    private SimpleRank plugin;

    public rank(SimpleRank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /rank <create|give|demote|list|delete|givepermission|seepermission|setstandard> [arguments]");
            return true;
        }

        String commandType = args[0].toLowerCase();
        Player player = sender instanceof Player ? (Player) sender : null;
        FileConfiguration config = plugin.getConfig();

        UUID playerUUID = player != null ? player.getUniqueId() : null;
        String rawRank = player != null ? config.getString("rank.currentrank." + playerUUID) : null;
        String permissionrank = rawRank != null ? ChatColor.stripColor(rawRank) : null;
        List<String> rankpermissions = permissionrank != null ? config.getStringList("rank.permissions." + permissionrank) : null;

        boolean isAdmin = player != null && (player.hasPermission("operator") || (rankpermissions != null && rankpermissions.contains("administration")));

        if (isAdmin) {
            switch (commandType) {
                case "create":
                    if (args.length < 3) {
                        sender.sendMessage("§cUsage: /rank create [rank] [priority]");
                        return true;
                    }
                    String rankName = args[1].replace("&", "§");
                    String plainRankName = ChatColor.stripColor(rankName);

                    int priority;
                    try {
                        priority = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§cThe priority must be a valid number.");
                        return true;
                    }

                    if (!config.contains("rank.ranks." + plainRankName)) {
                        config.set("rank.ranks." + plainRankName, rankName);
                        config.set("rank.priority." + plainRankName, priority);
                        plugin.saveConfig();
                        sender.sendMessage("§aRank " + rankName + " §acreated with priority " + priority + "!");
                    } else {
                        sender.sendMessage("§cThis rank already exists");
                    }
                    return true;

                case "give":
                    if (args.length < 3) {
                        sender.sendMessage("§cUsage: /rank give [player] [rank]");
                        return true;
                    }
                    String playerName = args[1];
                    String rankToGive = args[2];
                    Player targetPlayer = Bukkit.getPlayer(playerName);

                    if (targetPlayer == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }

                    String storedRankToGive = config.getString("rank.ranks." + rankToGive);
                    if (storedRankToGive == null) {
                        sender.sendMessage("§cRank not found!");
                        return true;
                    }

                    UUID targetUUID = targetPlayer.getUniqueId();
                    config.set("rank.currentrank." + targetUUID, storedRankToGive);
                    plugin.saveConfig();
                    sender.sendMessage("§aRank '" + storedRankToGive + "§a' was given to " + targetPlayer.getName());
                    targetPlayer.kickPlayer("§cRank Update! Please rejoin.");
                    return true;

                case "demote":
                    if (args.length < 2) {
                        sender.sendMessage("§cUsage: /rank demote [player]");
                        return true;
                    }
                    playerName = args[1];
                    targetPlayer = Bukkit.getPlayer(playerName);

                    if (targetPlayer == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }

                    targetUUID = targetPlayer.getUniqueId();
                    if (config.contains("rank.currentrank." + targetUUID)) {
                        config.set("rank.currentrank." + targetUUID, null);
                        plugin.saveConfig();
                        sender.sendMessage("§aPlayer " + targetPlayer.getName() + " has been demoted.");
                        targetPlayer.kickPlayer("§cRank Update! Please rejoin.");
                    } else {
                        sender.sendMessage("§cThis player does not have a rank.");
                    }
                    return true;

                case "list":
                    if (config.contains("rank.ranks")) {
                        Set<String> ranks = config.getConfigurationSection("rank.ranks").getKeys(false);
                        if (!ranks.isEmpty()) {
                            sender.sendMessage("§aExisting Ranks:");
                            for (String rank : ranks) {
                                sender.sendMessage("§a- " + config.getString("rank.ranks." + rank) + " (Priority: " + config.getInt("rank.priority." + rank) + ")");
                            }
                        } else {
                            sender.sendMessage("§cNo ranks have been created yet.");
                        }
                    } else {
                        sender.sendMessage("§cNo ranks have been created yet.");
                    }
                    return true;

                case "delete":
                    if (args.length < 2) {
                        sender.sendMessage("§cUsage: /rank delete [rank]");
                        return true;
                    }
                    String rankToDelete = args[1];
                    if (config.contains("rank.ranks." + rankToDelete)) {
                        config.set("rank.ranks." + rankToDelete, null);
                        config.set("rank.priority." + rankToDelete, null);
                        plugin.saveConfig();
                        sender.sendMessage("§aRank " + rankToDelete + " has been deleted.");
                    } else {
                        sender.sendMessage("§cRank not found!");
                    }
                    return true;

                case "givepermission":
                    if (args.length < 3) {
                        sender.sendMessage("§cUsage: /rank givepermission [rank] [permission]");
                        return true;
                    }
                    String rankNameForPermission = args[1];
                    String permissionToAdd = args[2].toLowerCase();

                    if (!config.contains("rank.ranks." + rankNameForPermission)) {
                        sender.sendMessage("§cRank not found!");
                        return true;
                    }

                    if (!permissionToAdd.matches("build|ban|kick|administration")) {
                        sender.sendMessage("§cInvalid permission! Use one of the following: build, interact, hit, ban, kick, administration");
                        return true;
                    }

                    List<String> permissions = config.getStringList("rank.permissions." + rankNameForPermission);
                    if (!permissions.contains(permissionToAdd)) {
                        permissions.add(permissionToAdd);
                        config.set("rank.permissions." + rankNameForPermission, permissions);
                        plugin.saveConfig();
                        sender.sendMessage("§aPermission '" + permissionToAdd + "' has been added to rank '" + rankNameForPermission + "'.");
                    } else {
                        sender.sendMessage("§cThis permission is already assigned to this rank.");
                    }
                    return true;

                case "seepermission":
                    if (args.length < 2) {
                        sender.sendMessage("§cUsage: /rank seepermission [rank]");
                        return true;
                    }
                    String rankNameToSeePermissions = args[1];

                    if (!config.contains("rank.ranks." + rankNameToSeePermissions)) {
                        sender.sendMessage("§cRank not found!");
                        return true;
                    }

                    List<String> permissions2 = config.getStringList("rank.permissions." + rankNameToSeePermissions);
                    if (!permissions2.isEmpty()) {
                        sender.sendMessage("§aPermissions for rank '" + rankNameToSeePermissions + "':");
                        for (String perm : permissions2) {
                            sender.sendMessage("§a- " + perm);
                        }
                    } else {
                        sender.sendMessage("§cThis rank has no permissions assigned.");
                    }
                    return true;

                case "setstandard":
                    if (args.length < 2) {
                        sender.sendMessage("§cUsage: /rank setstandard [rank]");
                        return true;
                    }
                    String plainRank = args[1];
                    String coloredRank = config.getString("rank.ranks." + plainRank);
                    if (coloredRank == null) {
                        sender.sendMessage("§cRank not found!");
                        return true;
                    }
                    config.set("rank.default", coloredRank);
                    plugin.saveConfig();
                    sender.sendMessage("§aStandard rank set to '" + coloredRank + "'.");
                    return true;

                default:
                    sender.sendMessage("§cUnknown command type. Usage: /rank <create|give|demote|list|delete|givepermission|seepermission|setstandard> [arguments]");
                    return true;
            }
        } else {
            sender.sendMessage("§cRank management for server operators or roles with administration permission");
        }
        return false;
    }
}
