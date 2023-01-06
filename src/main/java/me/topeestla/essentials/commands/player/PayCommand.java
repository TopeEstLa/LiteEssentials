package me.topeestla.essentials.commands.player;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

public class PayCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public PayCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "pay", permission = "essentials.pay", aliases = {"money.pay"}, inGameOnly = true)
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            IUser iUser = context.getIUser();
            IUser target = this.essentials.getUserManager().handleDataNeed(args[0]);

            if (target != null) {
                if (NumberUtils.isNumber(args[1])) {
                    if (iUser.getMoney() >= Double.parseDouble(args[1])) {
                        iUser.removeMoney(Double.parseDouble(args[1]));
                        target.addMoney(Double.parseDouble(args[1]));

                        String[][] format = {{"%currency%", this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null)},
                                {"%to%", target.getName()},
                                {"%from%", iUser.getName()},
                                {"%balance%", String.valueOf(iUser.getMoney())}};
                        context.reply("ECONOMY.PAY_SUCCESS", format);

                        if (target.getPlayer() == null) {
                            this.essentials.getUserManager().getUserFactory().updateUserAsync(target);
                            return;
                        }

                        target.sendMessage(this.essentials, "ECONOMY.RECEIVE_SUCCESS", format);
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
            context.getSender().sendMessage("§cUsage: /pay <player> <amount>");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
