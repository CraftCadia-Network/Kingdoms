package me.lucasvanborkulo.kingdoms.mysql;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MySQLDriver {

    private Main main;

    private Connection connection;

    /*
     * MySQL Credentials
     */

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

    /*
     * This is used for initializing the MySQLs driver.
     */

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
            Bukkit.getPluginManager().disablePlugin(this.main);
        }
        this.createTableIfNotExists();
    }

    /*
     * This is used for connecting to the database
     */

    public boolean connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.mysql_host + ":" + this.mysql_port + "/" + this.mysql_database, this.mysql_user, this.mysql_password);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /*
     * This is used for creating a table if it doesn't exist.
     */

    public void createTableIfNotExists() {
        Commons.logAction("Attempting to create a table called '" + this.mysql_table_name + "'...");
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

    /*
     * This is used for checking if the database contains a Player.
     */

    private boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * This is used for inserting a Player into a database.
     */

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * This is used for getting a value from the database.
     */

    public Object getValue(OfflinePlayer bukkitPlayer, String key) {
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

    /*
     * This is used for getting the server tier by the server ID.
     */

    public Tier getTierByServerID(Integer server_id) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE server_id=?");
            statement.setString(1, server_id + "");
            ResultSet results = statement.executeQuery();
            results.next();
            String tierString = results.getString("tier");
            return Tier.valueOf(tierString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * This is used for getting the creator by the server ID.
     */

    public OfflinePlayer getCreatorByServerID(Integer server_id) {
        try {
            PreparedStatement statement = this.connection
                    .prepareStatement("SELECT * FROM " + this.mysql_table_name + " WHERE server_id=?");
            statement.setString(1, server_id + "");
            ResultSet results = statement.executeQuery();
            results.next();
            String uuidString = results.getString("uuid");
            return Bukkit.getOfflinePlayer(uuidString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * This is used for setting a value in the Database.
     */

    public void setValue(OfflinePlayer bukkitPlayer, String key, String value) {
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
