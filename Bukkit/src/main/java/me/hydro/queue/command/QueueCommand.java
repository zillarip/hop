package me.hydro.queue.command;

import me.hydro.queue.common.misc.Color;
import me.hydro.queue.Hop;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.Queue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("queue.admin")) {
            sender.sendMessage(Messages.noPermission());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Color.translate("&cUsage: /queue <queue>"));
            return true;
        }

        final String queueName = args[0].toLowerCase();

        if (!Hop.getInstance().getQueues().getConfig().contains("queues." + queueName)) {
            sender.sendMessage(Messages.unknown(args[0]));
            return true;
        }

        final Queue queue = Queue.getQueues().get(queueName);

        sender.sendMessage(Color.translate("&7&m-------------------------------------"));
        sender.sendMessage(Color.translate("&3&lQueue Info for '" + queue.getId() + "'"));
        sender.sendMessage(Color.translate("&7"));
        sender.sendMessage(Color.translate("&7Queued: &f" + queue.getQueued().size()));
        sender.sendMessage(Color.translate("&7Display name: &f" + queue.getName()));
        sender.sendMessage(Color.translate("&7BungeeCord server: &f" + queue.getBungee()));
        sender.sendMessage(Color.translate("&7Restricted: &f" + (queue.isRestricted() ? "Yes &7(&fqueue.join." + queue.getId() + "&7)" : "No")));
        sender.sendMessage(Color.translate("&7&m-------------------------------------"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(Queue.getQueues().keySet());
    }
}
