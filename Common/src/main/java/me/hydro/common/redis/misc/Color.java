package me.hydro.common.redis.misc;

import org.bukkit.ChatColor;

public class Color {

    public static String translate(String toTranslate) {
        return ChatColor.translateAlternateColorCodes('&', toTranslate);
    }
}
