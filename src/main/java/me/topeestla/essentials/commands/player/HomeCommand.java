package me.topeestla.essentials.commands.player;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public HomeCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "home", permission = "essentials.home", aliases = {"homes"}, inGameOnly = true)
    public void onCommand(CommandContext context, String[] args) {
        IUser user = context.getIUser();
        if (args.length >= 1) {
            if (user.getHomes().containsKey(args[0])) {

                String[][] format = {{"%name%", args[0]}};
                context.reply("HOME.HOME_TELEPORTED", format);
            } else {
                context.reply("HOME.HOME_NOT_FOUND", null);
            }
        } else {
            String[][] formatZ = {{"%amount%", user.getHomesNames().size() + ""}};
            context.reply("HOME.LIST_HOMES", formatZ);
            for (String name : user.getHomesNames()) {
                String[][] format = {{"%name%", name}};
                context.reply("HOME.LIST_HOMES_FORMAT", format);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
