package me.hydro.queue.command;

import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
import me.hydro.queue.api.QueueManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class LeaveQueueCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NO_PERMISSION());
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = PlayerData.players.get(player.getUniqueId());

        if (!QueueManager.isQueued(data)) {
            player.sendMessage(Messages.NOT_QUEUED());
            return true;
        }

        Messages.LEFT_QUEUE(QueueManager.getQueued(data).getName());
        QueueManager.removeFromQueue(data);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
