package de.grubabua.simplerank.namechange;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class NameChangeEvents implements Listener {

    private SimpleRank plugin;

    public NameChangeEvents(SimpleRank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        String currentRank = config.getString("rank.currentrank." + playerUUID);

        if (currentRank == null) {
            currentRank = config.getString("rank.default");
            if (currentRank != null) {
                config.set("rank.currentrank." + playerUUID, currentRank);
                plugin.saveConfig();
            }
        }

        if (currentRank != null) {
            String rankcolor = currentRank.substring(0, 2);
            String coloredRank = ChatColor.translateAlternateColorCodes('&', currentRank);
            String showRank = coloredRank + " §8| " + rankcolor + player.getName();
            String tagRank = coloredRank + " §8| ";

            player.setDisplayName(showRank);
            player.setPlayerListName(showRank);

            event.setJoinMessage(showRank + "§e just joined");

            int priority = config.getInt("rank.priority." + ChatColor.stripColor(currentRank));
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager != null) {
                Scoreboard board = manager.getMainScoreboard();

                Team team = board.getTeam("priority_" + priority);
                if (team == null) {
                    team = board.registerNewTeam("priority_" + priority);
                }

                team.setPrefix(tagRank);
                team.setColor(ChatColor.getByChar(rankcolor.charAt(1)));

                team.addEntry(player.getName());
                player.setScoreboard(board);
            }
        }
    }

    public void resetPlayerNametag(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard board = manager.getNewScoreboard();
            player.setScoreboard(board);
        }
    }
}
