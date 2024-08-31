package de.grubabua.simplerank.commands;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class currentrank implements CommandExecutor {
    private SimpleRank plugin;

    public currentrank(SimpleRank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        String currentRank = config.getString("rank.currentrank." + playerUUID);

        if (currentRank != null) {
            sender.sendMessage("§aYour current rank: §r" + currentRank);
        } else {
            sender.sendMessage("§cYou have no rank!");
        }

        return true;
    }
}
