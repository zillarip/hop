package me.hydro.queue.command;

import me.hydro.queue.api.ManagerHandle;
import me.hydro.queue.misc.Messages;
import me.hydro.queue.api.PlayerData;
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
            sender.sendMessage(Messages.noPermission());
            return true;
        }

        final Player player = (Player) sender;
        final PlayerData data = PlayerData.getPlayers().get(player.getUniqueId());

        if (!ManagerHandle.getImplementation().isQueued(data)) {
            player.sendMessage(Messages.notQueued());
            return true;
        }

        player.sendMessage(Messages.leftQueue(ManagerHandle.getImplementation().getQueued(data).getName()));

        ManagerHandle.getImplementation().removeFromQueue(data);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
