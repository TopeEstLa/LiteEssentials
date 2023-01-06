package me.topeestla.essentials.api.command.data;

import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * An abstract class that represents a base command.
 * @author TopeEstLa
 */
public abstract class BaseCommand {

    /**
     * The command method.
     * @param sender
     * @param user
     * @param args
     */
    public abstract void onCommand(CommandContext context, String[] args);

    /**
     * The completer method.
     * @param sender
     * @param player
     * @param args
     * @return
     */
    public abstract List<String> onTabComplete(CommandSender sender, Player player, String[] args);

}
