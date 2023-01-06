package me.topeestla.essentials.commands.player;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public SetHomeCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "sethome", permission = "essentials.sethome", aliases = {"homeset"}, inGameOnly = true)
    public void onCommand(CommandContext context, String[] args) {
        IUser user = context.getIUser();

        if (args.length >= 1) {
            if (this.essentials.getSettings().getHomeLimit(user) >= user.getHomes().size()) {
                user.addHome(args[0], user.getPlayer().getLocation());
                String[][] format = {{"%name%", args[0]}};
                context.reply("HOME.HOME_SET", format);
            } else {
                context.reply("HOME.LIMIT_REACHED", null);
            }
        } else {
            user.addHome("home", user.getPlayer().getLocation());
            String[][] format = {{"%name%", "home"}};
            context.reply("HOME.HOME_SET", format);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
