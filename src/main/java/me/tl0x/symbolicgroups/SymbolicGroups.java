package me.tl0x.symbolicgroups;

import me.tl0x.symbolicgroups.commands.GroupCommand;
import me.tl0x.symbolicgroups.listener.PlayerChatListener;
import me.tl0x.symbolicgroups.util.DatabaseUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.Objects;

public final class SymbolicGroups extends JavaPlugin {

    private static final Connection databaseConnection = DatabaseUtils.startDatabase();

    @Override
    public void onEnable() {
        DatabaseUtils.createGroupsTable(databaseConnection);
        registerEvents();
        registerCommands();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("group")).setExecutor(new GroupCommand());
    }

    public static Connection getDatabaseConnection() {
        return databaseConnection;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
