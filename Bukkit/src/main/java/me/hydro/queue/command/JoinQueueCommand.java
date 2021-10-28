package me.hydro.queue.command;

import me.hydro.common.misc.Color;
import me.hydro.queue.HydroQueue;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
import me.hydro.queue.api.QueueManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class JoinQueueCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.noPermission());
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = PlayerData.players.get(player.getUniqueId());

        if (HydroQueue.getInstance().getSettings().getConfig().getBoolean("permission-by-default")
                && !player.hasPermission("queue.join")) {
            player.sendMessage(Messages.noPermission());
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(Color.translate("&cUsage: /joinqueue <queue>"));
            return true;
        }

        String queue = args[0].toLowerCase();

        if (!HydroQueue.getInstance().getQueues().getConfig().contains("queues." + queue)) {
            player.sendMessage(Messages.unknown(args[0]));
            return true;
        }

        if (QueueManager.isQueued(data)) {
            player.sendMessage(Messages.alreadyQueued());
            return true;
        }

        // TODO: queue priorities

        boolean bypass = player.hasPermission("queue.bypass");

        QueueManager.addToQueue(data, queue, bypass ? 0 : -1);
        player.sendMessage(Messages.joinedQueue(QueueManager.getQueue(queue).getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Queue.queues.stream().filter(q -> q.hasPermission(sender)).map(Queue::getId).collect(Collectors.toList());
    }
}
