package me.hydro.common;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.hydro.common.event.RedisMessageEvent;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
public class Redis {

    private JedisPool pool;

    private String host;
    private Integer port;
    private String password;
    private final boolean enabled;

    private ExecutorService executorService;

    private ArrayList<String> receivedResponsePacket;

    /**
     * Creates the Redis manager.
     *
     * @author Hydrogen
     * @since 3.0
     */
    public Redis(String host, Integer port, boolean enabled, String password) {
        this.pool = new JedisPool(host, port);

        this.host = host;
        this.port = port;
        this.password = password;
        this.enabled = enabled;

        this.executorService = Executors.newSingleThreadExecutor();

        this.receivedResponsePacket = new ArrayList<>();

        subscribe();
    }

    private void subscribe() {
        new Thread(() -> {
            final Jedis jedis = new Jedis(this.host, this.port);

            if (enabled) jedis.auth(this.password);

            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    Bukkit.getPluginManager().callEvent(new RedisMessageEvent(channel, message));
                }
            }, "ALLOW", "DENY", "STATUS");
        }, "RedisPubSub").start();
    }

    @SneakyThrows
    public void message(String channel, String message) {
        executorService.execute(() -> {
            final Jedis jedis = new Jedis(this.host, this.port);
            if (enabled) jedis.auth(password);

            jedis.publish(channel, message);
            jedis.close();
        });
    }
}
