package me.tl0x.symbolicgroups.util;

import java.sql.*;

public class DatabaseUtils {

    public static Connection startDatabase() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:playergroups.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void createGroupsTable(Connection databaseConnection) {
        try {
            Statement statement = databaseConnection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS GROUPS (" +
                    "UUID TEXT PRIMARY KEY," +
                    "POWER INTEGER," +
                    "GROUPNAME TEXT)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertGroupEntry(Connection connection, String uuid, int power, String groupName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO GROUPS (UUID, POWER, GROUPNAME) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, power);
            preparedStatement.setString(3, groupName);

            preparedStatement.executeUpdate();
            System.out.println("New entry inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeEntryByUuid(Connection connection, String uuid) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM GROUPS WHERE UUID = ?")) {
            preparedStatement.setString(1, uuid);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Entry with UUID " + uuid + " has been removed.");
            } else {
                System.out.println("No entry found with UUID " + uuid + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getGroupNameFromUuid(Connection connection, String uuid) {
        String groupName = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT GROUPNAME FROM GROUPS WHERE UUID = ?")) {
            preparedStatement.setString(1, uuid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    groupName = resultSet.getString("GROUPNAME");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupName;
    }
    public static boolean doesUuidExist(Connection connection, String uuid) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM GROUPS WHERE UUID = ?")) {
            preparedStatement.setString(1, uuid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getPowerFromUuid(Connection connection, String uuid) {
        int id = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT POWER FROM GROUPS WHERE UUID = ?")) {
            preparedStatement.setString(1, uuid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("POWER");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}