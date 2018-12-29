package me.lucasvanborkulo.kingdoms.multicraft;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MulticraftDriver {

    private Main main;

    /*
     * API Credentials
     */

    private String api_url;
    private String key;
    private String driver_url;

    public MulticraftDriver(Main main) {
        this.main = main;
        this.initialize();
    }

    /*
     * This is used for initializing the Multicraft driver.
     */

    private void initialize() {
        Commons.logAction("Initializing the Multicraft driver...");

        this.api_url = this.main.getConfig().getString("api_url");
        this.key = this.main.getConfig().getString("api_key");
        this.driver_url = this.main.getConfig().getString("driver_url");

        if (this.testConnection()) {
            Commons.logAction("Successfully initialized the Multicraft driver!");
        } else {
            Commons.logAction("Failed to initialize the Multicraft driver!");
            Bukkit.getPluginManager().disablePlugin(this.main);
        }
    }

    /*
     * This is used for testing the connection to the PHP api.
     */

    public boolean testConnection() {
        try {
            String driverURL = this.driver_url + "/test_connection.php";
            String multicraftURL = this.api_url;

            Document driverDoc = Jsoup.connect(driverURL).get();
            Document multicraftDoc = Jsoup.connect(multicraftURL).get();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * This is used for creating a Multicraft server.
     */

    public Integer createServer(Player creator, String name) {
            String request_url = this.driver_url + "/create_server.php?api=" + this.key + "=&url=" + this.api_url + "&server_name=" + name;

            Document doc = null;

            try {
                doc = Jsoup.connect(request_url).get();
            } catch (IOException e) {
                Commons.logAction("Failed to connect to the driver url!");
            }

            String htmlOutput = doc.html();

            String idString = htmlOutput.replaceAll("[^0-9]", "").substring(1);

            this.main.mySQLDriver.setValue(creator, "tier", Tier.TIER0.toString());

            Integer serverID = Integer.parseInt(idString);

            this.startServer(serverID);

            return serverID;
    }

    /*
     * This is used for starting a Multicraft server by the server ID.
     */

    public void startServer(Integer serverID) {
        String request_url = this.driver_url + "/start_server.php?api=" + this.key + "=&url=" + this.api_url + "&server_id=" + serverID;
        Document doc = null;

        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }

        this.updateServer(this.main.mySQLDriver.getCreatorByServerID(serverID));
    }

    /*
     * This is used for updating the server settings based on the Tier.
     */

    private void updateServer(OfflinePlayer creator) {
        String tierString = (String) this.main.mySQLDriver.getValue(creator, "tier");

        Tier tier = Tier.valueOf(tierString);

        Integer serverID = getServerID(creator);

        this.setMemory(serverID, tier);
        this.setMaxPlayers(serverID, this.main.mySQLDriver.getTierByServerID(serverID));
    }

    /*
     * This is used for setting the maximum amount of ram usage.
     */

    private void setMemory(Integer serverID, Tier tier) {
        String request_url = this.driver_url + "/set_server_ram.php?api=" + this.key + "&url=" + this.api_url + "&server_id=" + serverID + "&memory=" + tier.getMemory();
        Document doc = null;
        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }
    }

    /*
     * This is used for setting the maximum player count.
     */

    private void setMaxPlayers(Integer serverID, Tier tier) {
        String request_url = this.driver_url + "/set_max_players.php?api=" + this.key + "&url=" + this.api_url + "&server_id=" + serverID + "&max_players=" + tier.getMaxPlayers();
        Commons.logAction(request_url);
        Document doc = null;
        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }
    }

    /*
     * This is used for creating a Multicraft server.
     */

    public Integer getServerID(OfflinePlayer bukkitPlayer) {
        Integer serverID = Integer.valueOf((String) this.main.mySQLDriver.getValue(bukkitPlayer, "server_id"));
        return serverID;
    }

    /*
     * This is used for deleting a Multicraft server.
     */

    public void deleteServer() {

    }

    /*
     * This is used for checking if a Player has a server.
     */

    public boolean hasServer(OfflinePlayer bukkitPlayer) {
        Integer id = Integer.parseInt((String) this.main.mySQLDriver.getValue(bukkitPlayer, "server_id"));
        return id>-1;
    }

    /*
     * This is used for creating a Multicraft user.
     */

    public void createUser(Player bukkitPlayer, boolean sendMessage) {
        String generatedUsername = this.generateUsername(bukkitPlayer);
        String generatedPassword = this.generatePassword();
        String request_url = this.driver_url + "/create_user.php?api=" + this.key + "&url=" + this.api_url + "&username=" + generatedUsername + "&password=" + generatedPassword;
        Commons.logAction(request_url);
        Document doc = null;
        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }

        if (sendMessage) {
            String link = this.api_url.replaceAll("/api.php", "");
            Commons.sendMessage(bukkitPlayer, "Your web panel username: " + generatedUsername);
            Commons.sendMessage(bukkitPlayer, "Your web panel password: " + generatedPassword);
            Commons.sendMessage(bukkitPlayer, "The web panel link: " + link);
        }
    }

    /*
     * This is used for deleting a Multicraft user.
     */

    public void deleteUser() {

    }

    /*
     * This is used for generating an unique Multicraft username based on the Minecraft user.
     */

    private String generateUsername(OfflinePlayer bukkitPlayer) {
        String uuid = bukkitPlayer.getUniqueId().toString();
        byte[] uuidBytes = null;
        try {
           uuidBytes = uuid.getBytes("UTF-8");
        } catch (UnsupportedEncodingException exception) {
            Commons.logAction("Encoding failure!");
        }

        MessageDigest md5 = null;
        MessageDigest sha256 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            Commons.logAction("MessageDigest failure!");
        }

        byte[] md5Digested = md5.digest(uuidBytes);
        byte[] sha256Digested = sha256.digest(md5Digested);

        String result = new String(sha256Digested);

        result = result.substring(0, 16);

        return result;
    }

    /*
     * This is used for generating a Multicraft password based on a random number.
     */

    private String generatePassword() {
        Random random = new Random();
        String numberString = random.nextInt(2000000000) + "";
        byte[] numberBytes = null;

        MessageDigest md5 = null;
        MessageDigest sha256 = null;

        try {
            numberBytes = numberString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException exception){
            Commons.logAction("Encoding failure!");
        }

        byte[] md5Digested = md5.digest(numberBytes);
        byte[] sha256Digested = sha256.digest(md5Digested);

        String result = new String(sha256Digested);

        result = result.substring(0, 16);

        return result;
    }
}
