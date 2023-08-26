package me.tl0x.symbolicgroups.util;

import me.tl0x.symbolicgroups.SymbolicGroups;
import org.bukkit.entity.Player;

public class GroupUtils {

    public static String getGroupName(Player player) {
        return DatabaseUtils.getGroupNameFromUuid(SymbolicGroups.getDatabaseConnection(), player.getUniqueId().toString());
    }

    public static boolean isGroupLeader(Player player) {
        return DatabaseUtils.getPowerFromUuid(SymbolicGroups.getDatabaseConnection(), player.getUniqueId().toString()) == 3;
    }
}
