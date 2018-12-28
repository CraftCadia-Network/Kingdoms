package me.lucasvanborkulo.kingdoms.mysql;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MySQLDriver {

    private Main main;

    private Connection connection;

    private String mysql_host;
    private Integer mysql_port;
    private String mysql_database;
    private String mysql_user;
    private String mysql_password;
    private String mysql_table_name;


    public MySQLDriver(Main main) {
        this.main = main;
        this.initialize();
    }

    private void initialize() {
        Commons.logAction("Initializing the MySQL driver...");

        this.mysql_host = this.main.getConfig().getString("mysql_host");
        this.mysql_port = this.main.getConfig().getInt("mysql_port");
        this.mysql_database = this.main.getConfig().getString("mysql_database");
        this.mysql_user = this.main.getConfig().getString("mysql_user");
        this.mysql_password = this.main.getConfig().getString("mysql_password");
        this.mysql_table_name = this.main.getConfig().getString("mysql_table_name");

        if (this.connect()) {
            Commons.logAction("Successfully initialized the MySQL driver!");
        } else {
            Commons.logAction("Failed to initialize the MySQL driver!");

        }
        this.createTableIfNotExists();
    }

    public boolean connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.mysql_host + ":" + this.mysql_port + "/" + this.mysql_database, this.mysql_user, this.mysql_password);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void createTableIfNotExists() {
        Commons.logAction("Creating a table called '" + this.mysql_table_name + "'....");
        try {
            String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + this.mysql_table_name + "` (\n" +
                    "`uuid` varchar(255) NOT NULL," +
                    "`tier` varchar(255)," +
                    "`server_name` varchar(255)," +
                    "`server_id` varchar(255)," +
                    "PRIMARY KEY( `uuid` )" +
                    ");";

            Statement stmt = this.connection.createStatement();
            stmt.execute(sqlCreate);
        } catch (Exception exception) {
            Commons.logAction("Failed to create the table!");
            return;
        }
        Commons.logAction("Successfully created the '" + this.mysql_table_name + "' table!");
    }

    private boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Player Found");
                return true;
            }
            Bukkit.broadcastMessage(ChatColor.RED + "Player NOT Found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            System.out.print(1);
            if (playerExists(uuid) != true) {
                PreparedStatement insert = this.connection
                        .prepareStatement("INSERT INTO " + this.mysql_table_name + " (uuid,tier,server_name,server_id) VALUES (?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, Tier.TIER0.toString());
                insert.setString(3, "none");
                insert.setInt(4, -1);
                insert.executeUpdate();

                Bukkit.broadcastMessage(ChatColor.GREEN + "Player Inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setValue(String key, String columnName, String value) {

    }

    public Object getValue(Player bukkitPlayer, String key) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE uuid=?");
            statement.setString(1, bukkitPlayer.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setValue(Player bukkitPlayer, String key, String value) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("UPDATE " + this.mysql_table_name + " SET " + key + "=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, bukkitPlayer.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
