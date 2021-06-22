package me.hydro.queueclient;

import lombok.Getter;
import me.hydro.common.redis.Redis;
import me.hydro.common.redis.file.HydroFile;
import me.hydro.common.redis.misc.Logger;
import me.hydro.queueclient.listeners.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class QueueClient extends JavaPlugin {

    @Getter
    private static QueueClient instance;

    private HydroFile settings;
    private String representing;
    private Redis redis;

    @Override
    public void onEnable() {
        instance = this;

        settings = new HydroFile(this, "settings");
        representing = settings.getConfig().getString("representing");

        redis = new Redis(this.settings.getConfig().getString("redis.host"),
                this.settings.getConfig().getInt("redis.port"),
                this.settings.getConfig().getBoolean("redis.auth.enabled"),
                this.settings.getConfig().getString("redis.auth.password"));

        Bukkit.getPluginManager().registerEvents(new MessageListener(), this);

        Logger.success("QueueClient is ready to go!");
    }

    @Override
    public void onDisable() {
        instance = null;
        redis.getPool().close();

        Logger.info("Disabled HydroQueue.");
    }
}
