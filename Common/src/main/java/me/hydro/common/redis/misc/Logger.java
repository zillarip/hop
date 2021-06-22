package me.hydro.common.redis.misc;

import org.bukkit.Bukkit;

public class Logger {

    public static void info(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&9[HydroQueue] " + m));
    }

    public static void warn(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&e[HydroQueue] " + m));
    }

    public static void err(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&c[HydroQueue] " + m));
    }

    public static void success(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&a[HydroQueue] " + m));
    }
}
