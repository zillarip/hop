package me.hydro.queue.misc;

import me.hydro.common.redis.misc.Color;
import me.hydro.queue.HydroQueue;
import org.bukkit.entity.Player;

public class Messages {

    public static String JOINED_QUEUE(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.joined-queue")
                .replace("{queue}", queue));
    }

    public static String LEFT_QUEUE(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.left-queue")
                .replace("{queue}", queue));
    }

    public static String SENDING(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.sending")
                .replace("{queue}", queue));
    }

    public static String ALREADY_QUEUED() {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.already-queued"));
    }

    public static String NOT_QUEUED() {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.not-queued"));
    }

    public static String FAILURE(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.failed")
                .replace("{queue}", queue));
    }

    public static String NO_PERMISSION() {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.no-perm"));
    }

    public static String UNKNOWN(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.queue-unknown")
                .replace("{queue}", queue));
    }

    public static String RESTRICTED(String queue) {
        return Color.translate(HydroQueue.getInstance().getSettings().getConfig().getString("messages.restricted-queue")
                .replace("{queue}", queue));
    }

    // Multi-lines
    public static void REMINDER(Player player, int pos, int size) {
        HydroQueue.getInstance().getSettings().getConfig().getStringList("messages.reminder").forEach(line -> {
            line = Color.translate(line);
            player.sendMessage(line.replace("{pos}", pos + "").replace("{size}", size + ""));
        });
    }

    public static void REMINDER_FAIL(Player player, String reason, int pos, int size) {
        HydroQueue.getInstance().getSettings().getConfig().getStringList("messages.reminder-" + reason).forEach(line -> {
            line = Color.translate(line);
            player.sendMessage(line.replace("{pos}", pos + "").replace("{size}", size + ""));
        });
    }
}
