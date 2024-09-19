package me.hydro.queue.common.misc;

import org.bukkit.Bukkit;

public class Logger {

    public static void info(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&9[Hop] " + m));
    }

    public static void warn(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&e[Hop] " + m));
    }

    public static void err(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&c[Hop] " + m));
    }

    public static void success(String m) {
        Bukkit.getConsoleSender().sendMessage(Color.translate("&a[Hop] " + m));
    }
}
