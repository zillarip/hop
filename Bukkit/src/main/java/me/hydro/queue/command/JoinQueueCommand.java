package me.hydro.queue.command;

import me.hydro.queue.api.ManagerHandle;
import me.hydro.queue.common.misc.Color;
import me.hydro.queue.Hop;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.Queue;
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

        final Player player = (Player) sender;
        final PlayerData data = PlayerData.getPlayers().get(player.getUniqueId());

        if (Hop.getInstance().getSettings().getConfig().getBoolean("permission-by-default")
                && !player.hasPermission("queue.join")) {
            player.sendMessage(Messages.noPermission());
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(Color.translate("&cUsage: /joinqueue <queue>"));
            return true;
        }

        final String queue = args[0].toLowerCase();

        if (!Hop.getInstance().getQueues().getConfig().contains("queues." + queue)) {
            player.sendMessage(Messages.unknown(args[0]));
            return true;
        }

        if (!ManagerHandle.getImplementation().getQueue(queue).hasPermission(player)) {
            player.sendMessage(Messages.restricted(queue));
            return true;
        }

        if (ManagerHandle.getImplementation().isQueued(data)) {
            player.sendMessage(Messages.alreadyQueued());
            return true;
        }

        // TODO: queue priorities

        final boolean bypass = player.hasPermission("queue.bypass");

        data.setQueuedFor(queue);

        ManagerHandle.getImplementation().addToQueue(data, queue, bypass ? 0 : -1);
        player.sendMessage(Messages.joinedQueue(ManagerHandle.getImplementation().getQueue(queue).getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Queue.getQueues().values().stream().filter(q -> q.hasPermission(sender))
                .map(Queue::getId).collect(Collectors.toList());
    }
}
