package me.lucasvanborkulo.kingdoms.redis;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import redis.clients.jedis.Jedis;

public class RedisDriver {

    private Main main;
    private Jedis jedis;

    /*
     * Redis credentials
     */

    private String host_address;
    private Integer host_port;
    private String password;

    /*
     * Redis settings
     */

    private Integer timeout = 5000;

    public RedisDriver(Main main) {
        this.main = main;
        this.initialize();
    }

    private void initialize() {
        Commons.logAction("Initializing the Redis driver...");
        try {
            this.jedis = new Jedis(this.host_address, this.host_port, this.timeout);
            this.jedis.auth(this.password);

        } catch (Exception exception) {
            Commons.logAction("Failed to initialize the Redis driver!");
            return;
        }
        Commons.logAction("Successfully initialized the Redis driver!");
    }
}
