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

public class MoneyClearCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public MoneyClearCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }


    @Override
    @CommandHandler(name = "money.clear", permission = "essentials.money.clear", aliases = {"money.c", "money.reset", "eco.clear", "economy.clear", "eco.reset", "economy.reset"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            IUser target = this.essentials.getUserManager().handleDataNeed(args[0]);
            if (target != null) {
                    target.setMoney(0);

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
