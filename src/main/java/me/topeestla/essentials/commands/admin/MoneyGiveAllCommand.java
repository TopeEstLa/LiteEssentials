package me.topeestla.essentials.commands.admin;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MoneyGiveAllCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public MoneyGiveAllCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }


    @Override
    @CommandHandler(name = "money.giveall", permission = "essentials.money.giveall", aliases = {"eco.giveall", "economy.giveall"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            if (NumberUtils.isNumber(args[0])) {
                double amount = Double.parseDouble(args[0]);
                for (Player player : essentials.getServer().getOnlinePlayers()) {
                    IUser user = this.essentials.getUserManager().getUserCache().getUser(player.getName());
                    user.addMoney(amount);
                    String[][] format = {
                            {"%player%", user.getName()},
                            {"%balance%", String.valueOf(user.getMoney())},
                            {"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)}
                    };

                    user.sendMessages(this.essentials, "ECONOMY.BALANCE_UPDATE", format);
                }
                String[][] format = {
                        {"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)},
                        {"%amount%", String.valueOf(amount)}
                };

                context.reply("ECONOMY.GIVE-ALL", format);
            } else {
                context.reply("NEED-NUMBER", null);
            }
        } else {
            context.getSender().sendMessage("§cUsage: /money giveall <amount>");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
