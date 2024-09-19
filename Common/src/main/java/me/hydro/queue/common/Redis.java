package me.hydro.queue.common;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.hydro.queue.common.event.RedisMessageEvent;
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

    private final boolean authenticated;

    private ExecutorService executorService;

    private ArrayList<String> receivedResponsePacket;

    public Redis(String host, Integer port, boolean authenticated, String password) {
        this.pool = new JedisPool(host, port);

        this.host = host;
        this.port = port;
        this.password = password;
        this.authenticated = authenticated;

        this.executorService = Executors.newSingleThreadExecutor();

        this.receivedResponsePacket = new ArrayList<>();

        this.subscribe();
    }

    private void subscribe() {
        new Thread(() -> {
            final Jedis jedis = this.pool.getResource();

            if (this.authenticated) {
                jedis.auth(this.password);
            }

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
        this.executorService.execute(() -> {
            final Jedis jedis = this.pool.getResource();

            if (this.authenticated) {
                jedis.auth(this.password);
            }

            jedis.publish(channel, message);
            jedis.close();
        });
    }
}
