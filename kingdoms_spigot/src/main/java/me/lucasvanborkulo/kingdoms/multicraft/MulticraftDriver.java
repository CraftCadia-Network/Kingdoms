package me.lucasvanborkulo.kingdoms.multicraft;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private void initialize() {
        Commons.logAction("Initializing the Multicraft driver...");
        this.api_url = this.main.getConfig().getString("api_url");
        this.key = this.main.getConfig().getString("api_key");
        this.driver_url = this.main.getConfig().getString("driver_url");

        if (this.testConnection()) {
            Commons.logAction("Successfully initialized the Multicraft driver!");
        } else {
            Commons.logAction("Failed to initialize the Multicraft driver!");
        }
    }

    public boolean testConnection() {
        try {
            String testUrl = this.driver_url + "/test_connection.php";
            Document doc = Jsoup.connect(testUrl).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer createServer(Player creator, String name) {
            String request_url = this.driver_url + "/create_server.php?api=" + this.key + "=&url=" + this.api_url + "&server_name=" + name;
            Document doc = null;
            try {
                doc = Jsoup.connect(request_url).get();
            } catch (IOException e) {
                Commons.logAction("Failed to connect to the driver url!");
            }

            String id = doc.html();
            id = id.replaceAll("[^0-9]", "");

            this.main.mySQLDriver.setValue(creator, "tier", Tier.TIER0.toString());
            Tier tier = Tier.valueOf((String) this.main.mySQLDriver.getValue(creator, "tier"));

            id = id.substring(1);

            Integer server_id = Integer.parseInt(id);

            this.setMemory(server_id, tier);

            return server_id;

    }

    public void startServer(Integer server_id) {
        String request_url = this.driver_url + "/start_server.php?api=" + this.key + "=&url=" + this.api_url + "&server_id=" + server_id;
        Document doc = null;
        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }
    }

    private void setMemory(Integer serverID, Tier tier) {
        String request_url = this.driver_url + "/set_server_ram.php?api=" + this.key + "&url=" + this.api_url + "&server_id=" + serverID + "&memory=" + tier.getMemory();
        Document doc = null;
        try {
            Jsoup.connect(request_url).get();
        } catch (IOException e) {
            Commons.logAction("Failed to connect to the driver url!");
        }
    }

    public Integer getServerID(Player bukkitPlayer) {
        Integer server_id = Integer.valueOf((String) this.main.mySQLDriver.getValue(bukkitPlayer, "server_id"));
        return server_id;
    }


    public boolean hasServer(Player player) {
        Integer id = Integer.parseInt((String) this.main.mySQLDriver.getValue(player, "server_id"));
        return id>-1;
    }

    public void deleteServer() {

    }

    public void createUser() {

    }

    public void deleteUser() {

    }

}
