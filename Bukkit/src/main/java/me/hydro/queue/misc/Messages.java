package me.hydro.queue.misc;

import me.hydro.queue.common.misc.Color;
import me.hydro.queue.Hop;
import org.bukkit.entity.Player;

public class Messages {

    public static String joinedQueue(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.joined-queue")
                .replace("{queue}", queue));
    }

    public static String leftQueue(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.left-queue")
                .replace("{queue}", queue));
    }

    public static String sending(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.sending")
                .replace("{queue}", queue));
    }

    public static String alreadyQueued() {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.already-queued"));
    }

    public static String notQueued() {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.not-queued"));
    }

    public static String failure(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.failed")
                .replace("{queue}", queue));
    }

    public static String noPermission() {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.no-perm"));
    }

    public static String unknown(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.queue-unknown")
                .replace("{queue}", queue));
    }

    public static String restricted(String queue) {
        return Color.translate(Hop.getInstance().getSettings().getConfig().getString("messages.restricted-queue")
                .replace("{queue}", queue));
    }

    // Multi-lines
    public static void reminder(Player player, int pos, int size) {
        Hop.getInstance().getSettings().getConfig().getStringList("messages.reminder").forEach(line -> {
            line = Color.translate(line);
            player.sendMessage(line.replace("{pos}", pos + "").replace("{size}", size + ""));
        });
    }

    public static void reminderFail(Player player, String reason, int pos, int size) {
        Hop.getInstance().getSettings().getConfig().getStringList("messages.reminder-" + reason).forEach(line -> {
            line = Color.translate(line);
            player.sendMessage(line.replace("{pos}", pos + "").replace("{size}", size + ""));
        });
    }
}
