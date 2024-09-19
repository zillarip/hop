package me.hydro.queueclient;

import lombok.Getter;
import me.hydro.queue.common.Redis;
import me.hydro.queue.common.file.HopFile;
import me.hydro.queue.common.misc.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class QueueClient extends JavaPlugin {

    @Getter
    private static QueueClient instance;

    private HopFile settings;
    private String representing;
    private Redis redis;

    @Override
    public void onEnable() {
        instance = this;

        this.settings = new HopFile(this, "settings");
        this.representing = settings.getConfig().getString("representing");

        this.redis = new Redis(this.settings.getConfig().getString("redis.host"),
                this.settings.getConfig().getInt("redis.port"),
                this.settings.getConfig().getBoolean("redis.auth.enabled"),
                this.settings.getConfig().getString("redis.auth.password"));

        Bukkit.getPluginManager().registerEvents(new MessageListener(), this);

        Logger.success("Ready");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.redis.getPool().close();

        Logger.info("Disabled HopClient");
    }
}
