package me.tl0x.symbolicgroups.commands;

import me.tl0x.symbolicgroups.SymbolicGroups;
import me.tl0x.symbolicgroups.util.DatabaseUtils;
import me.tl0x.symbolicgroups.util.GroupUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Objects;

public class GroupCommand implements CommandExecutor {

    // It's going to be a big one
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        if (args.length == 0) {
            this.sendHelp((Player) commandSender);
        }
        if (args.length == 2) {
            if (args[0].equals("add")) {
                Player player = this.getPlayerByName(args[1]);
                if (player != null) {
                    this.addPlayerToGroup((Player) commandSender, player, GroupUtils.getGroupName(player));
                }
            }
            if (args[0].equals("remove")) {
                Player player = this.getPlayerByName(args[1]);
                if (player != null) {
                    this.removePlayerFromGroup((Player) commandSender, player);
                }
            }
            else {
                commandSender.sendMessage("Invalid command usage.");
            }
        }

        return false;
    }

    public void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "======Group Help======");
        player.sendMessage(ChatColor.GOLD + "/group add <player>: Add a player to your group (requires leader).");
        player.sendMessage(ChatColor.GOLD + "/group remove <player>: Removes a player from your group (requires leader).");
        player.sendMessage(ChatColor.GOLD + "/group list: Lists all players in your group.");
        player.sendMessage(ChatColor.GOLD + "/group chat <chat message>: Sends a chat message within your group.");
        player.sendMessage(ChatColor.GOLD + "/group create <group name>: Creates group");
        player.sendMessage(ChatColor.GOLD + "======================");
    }
    public Player getPlayerByName(String name) {
        return Bukkit.getPlayerExact(name);
    }

    // TODO: MAKE SURE PLAYERS CANT JOIN TWO GROUPS AT ONCE
    // TODO: ADD ERROR CHECKING FOR DUPLICATE ENTRIES

    public boolean addPlayerToGroup(Player commandSender, Player player, String groupName) {
        if (!GroupUtils.isGroupLeader(player)) {
            player.sendMessage("You are not group leader!");
            return false;
        }
        Connection connection = SymbolicGroups.getDatabaseConnection();
        if (DatabaseUtils.doesUuidExist(connection, player.getUniqueId().toString())) {
            commandSender.sendMessage("They are already in a group!");
            return false;
        }
        DatabaseUtils.insertGroupEntry(connection, player.getUniqueId().toString(), 0, groupName);
        return true;
    }

    public boolean removePlayerFromGroup(Player commandSender, Player player) {
        if (!GroupUtils.isGroupLeader(player)) {
            player.sendMessage("You are not group leader!");
            return false;
        }
        Connection connection = SymbolicGroups.getDatabaseConnection();
        if (!Objects.equals(DatabaseUtils.getGroupNameFromUuid(connection, commandSender.getUniqueId().toString()), DatabaseUtils.getGroupNameFromUuid(connection, player.getUniqueId().toString()))) {
            player.sendMessage("That player is not in your group!");
            return false;
        }
        DatabaseUtils.removeEntryByUuid(SymbolicGroups.getDatabaseConnection(), player.getUniqueId().toString());
        return true;
    }

    public boolean createGroup(Player leader, String groupName) {
        return false;
    }
}
