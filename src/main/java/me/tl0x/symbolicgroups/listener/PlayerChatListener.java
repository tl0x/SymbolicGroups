package me.tl0x.symbolicgroups.listener;

import me.tl0x.symbolicgroups.SymbolicGroups;
import me.tl0x.symbolicgroups.util.DatabaseUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerChat(PlayerChatEvent e) {
        if (DatabaseUtils.doesUuidExist(SymbolicGroups.getDatabaseConnection(), e.getPlayer().getUniqueId().toString())) {
            String groupName = DatabaseUtils.getGroupNameFromUuid(SymbolicGroups.getDatabaseConnection(), e.getPlayer().getUniqueId().toString());
            e.setFormat(ChatColor.AQUA + groupName + ChatColor.WHITE + " " + e.getPlayer().getName() + ": " + e.getMessage());
        }
    }
}
