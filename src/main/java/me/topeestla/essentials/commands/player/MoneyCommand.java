package me.topeestla.essentials.commands.player;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MoneyCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public MoneyCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "money", permission = "essentials.money", aliases = {"balance", "bal"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            if (context.hasPermission("essentials.money.others")) {
                IUser iUser = this.essentials.getUserManager().handleDataNeed(args[0]);
                if (iUser != null) {
                    String[][] format = {{"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)},
                            {"%player%", iUser.getName()},
                            {"%balance%", String.valueOf(iUser.getMoney())}};
                    context.reply("ECONOMY.BALANCE", format);
                } else {
                    context.reply("PLAYER-NOT-FOUND", null);
                }
            } else {
                context.reply("NO-PERMISSION", null);
            }
        } else {
            if (!context.isConsole()) {
                String[][] format = {{"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)},
                        {"%player%", context.getIUser().getName()},
                        {"%balance%", String.valueOf(context.getIUser().getMoney())}};
                context.reply("ECONOMY.BALANCE", format);
            } else {
                context.reply("PLAYER-NOT-FOUND", null);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
