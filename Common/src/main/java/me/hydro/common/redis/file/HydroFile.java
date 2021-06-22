package me.hydro.common.redis.file;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HydroFile {

    private File file;
    @Getter private FileConfiguration config;

    @SneakyThrows
    public HydroFile(Plugin plugin, String fileName) {
        file = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        }

        (this.config = new YamlConfiguration()).load(this.file);
    }

    @SneakyThrows
    public void save() {
        config.save(this.file);
        reload();
    }

    @SneakyThrows
    public void reload() {
        config = YamlConfiguration.loadConfiguration(this.file);
    }
}
