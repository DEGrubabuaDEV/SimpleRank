package de.grubabua.simplerank.permission;

import de.grubabua.simplerank.SimpleRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


import java.util.List;
import java.util.UUID;

public class PermissionEvents implements Listener {
    private final SimpleRank plugin;
    public boolean onblockbreak = false;
    public PermissionEvents(SimpleRank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        onblockbreak = true;
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String rawRank = plugin.getConfig().getString("rank.currentrank." + playerUUID);

        if (rawRank != null || player.hasPermission("operator")) {
            String rank = ChatColor.stripColor(rawRank);
            List<String> permissions = plugin.getConfig().getStringList("rank.permissions." + rank);
            if (permissions.contains("build") || permissions.contains("administration") || player.hasPermission("operator")) {
                return;
            } else {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String rawRank = plugin.getConfig().getString("rank.currentrank." + playerUUID);

        if (rawRank != null || player.hasPermission("operator")) {
            String rank = ChatColor.stripColor(rawRank);
            List<String> permissions = plugin.getConfig().getStringList("rank.permissions." + rank);
            if (permissions.contains("build") || permissions.contains("administration") || player.hasPermission("operator")) {
                return;
            } else {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

}

