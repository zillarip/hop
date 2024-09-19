package me.hydro.queue.common.file;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HopFile {

    private final File file;

    @Getter private FileConfiguration config;

    @SneakyThrows
    public HopFile(Plugin plugin, String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName + ".yml");

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        }

        (this.config = new YamlConfiguration()).load(this.file);
    }

    @SneakyThrows
    public void save() {
        this.config.save(this.file);
        this.reload();
    }

    @SneakyThrows
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
}
