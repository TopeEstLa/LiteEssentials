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

public class MoneyTakeCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public MoneyTakeCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "money.take", permission = "essentials.money.take", aliases = {"takemoney", "takebal", "eco.take", "economy.take"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 2) {
            IUser target = this.essentials.getUserManager().handleDataNeed(args[0]);
            if (target != null) {
                if (NumberUtils.isNumber(args[1])) {
                    double amount = Double.parseDouble(args[1]);
                    if (target.getMoney() >= amount) {
                        target.removeMoney(amount);


                        String[][] format = {
                                {"%player%", target.getName()},
                                {"%balance%", String.valueOf(target.getMoney())},
                                {"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)}
                        };

                        context.reply("ECONOMY.BALANCE_UPDATE", format);

                        if (target.getPlayer() == null) {
                            this.essentials.getUserManager().getUserFactory().updateUserAsync(target);
                            return;
                        }

                        target.sendMessages(this.essentials, "ECONOMY.BALANCE_UPDATE", format);
                    } else {
                        context.reply("ECONOMY.BALANCE_NOT_ENOUGH", null);
                    }
                } else {
                    context.reply("NEED-NUMBER", null);
                }
            } else {
                context.reply("PLAYER-NOT-FOUND", null);
            }
        } else {
            context.getSender().sendMessage("§cUsage: /money set <player> <amount>");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
