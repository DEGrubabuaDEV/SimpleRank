package de.grubabua.simplerank.tabcompleter;


import de.grubabua.simplerank.SimpleRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class rank implements TabCompleter {
    private SimpleRank plugin;

    public rank(SimpleRank plugin) {this.plugin = plugin;}
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String rawRank = plugin.getConfig().getString("rank.currentrank." + playerUUID);
        String permissionrank = ChatColor.stripColor(rawRank);
        List<String> rankpermissions = plugin.getConfig().getStringList("rank.permissions." + permissionrank);

        String[] commandtypes = {"create", "give", "demote", "list", "delete", "givepermission", "seepermission", "setstandard"};
        String[] permissions = {"build", "ban", "kick", "administration"};
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("operator") || rankpermissions.contains("administration")){
            if (args.length == 1) {
                String input = args[0].toLowerCase();

                for (String commandtype : commandtypes) {
                    if (commandtype.toLowerCase().startsWith(input)) {
                        suggestions.add(commandtype);
                    }
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("setstandard")) {
                String input = args[1].toLowerCase();
                List<String> tabcranks = new ArrayList<>();
                Set<String> ranks = plugin.getConfig().getConfigurationSection("rank.ranks").getKeys(false);

                for (String rank : ranks) {
                    tabcranks.add(rank);
                }

                for (String tabcrank : tabcranks) {
                    if (tabcrank.toLowerCase().startsWith(input)) {
                        suggestions.add(tabcrank);
                    }
                }
            }
            else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                String input = args[2].toLowerCase();
                List<String> tabranks = new ArrayList<>();
                Set<String> ranks = plugin.getConfig().getConfigurationSection("rank.ranks").getKeys(false);

                for (String rank : ranks) {
                    tabranks.add(rank);
                }

                for (String tabrank : tabranks) {
                    if (tabrank.toLowerCase().startsWith(input)) {
                        suggestions.add(tabrank);
                    }
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("givepermission") || args[0].equalsIgnoreCase("seepermission")) {
                String input = args[1].toLowerCase();
                List<String> tabranks = new ArrayList<>();
                Set<String> ranks = plugin.getConfig().getConfigurationSection("rank.ranks").getKeys(false);

                for (String rank : ranks) {
                    tabranks.add(rank);
                }

                for (String tabrank : tabranks) {
                    if (tabrank.toLowerCase().startsWith(input)) {
                        suggestions.add(tabrank);
                    }
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("givepermission")) {
                String input = args[2].toLowerCase();

                for (String permission : permissions) {
                    if (permission.toLowerCase().startsWith(input)) {
                        suggestions.add(permission);
                    }
                }
            }
        }
        return suggestions;
    }
}
