package me.topeestla.essentials.commands.admin;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FlyCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public FlyCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }


    @Override
    @CommandHandler(name = "fly", permission = "essentials.fly", help = {"/fly", "Toggles flying"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            if (context.hasPermission("essentials.fly.others")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    boolean enabled = !target.getAllowFlight();

                    target.setFallDistance(0f);
                    target.setAllowFlight(enabled);

                    if (!target.getAllowFlight()) {
                        target.setFlying(false);
                    }

                    String[][] format = {{"%player%", target.getName()}};
                    context.reply("FLY." + (enabled ? "ENABLE" : "DISABLE"), format);
                    this.essentials.getAudiences().player(target).sendMessage(this.essentials.getLocaleManager().getLocaleConfig().getChatComponent("FLY." + (enabled ? "ENABLE" : "DISABLE"), true, format));
                } else {
                    context.reply("PLAYER-NOT-FOUND", null);
                }
            } else {
                context.reply("NO-PERMISSION", null);
            }
        } else {
            if (!context.isConsole()) {
                IUser user = context.getIUser();
                boolean enabled = !user.getPlayer().getAllowFlight();

                user.getPlayer().setFallDistance(0f);
                user.getPlayer().setAllowFlight(enabled);

                if (!user.getPlayer().getAllowFlight()) {
                    user.getPlayer().setFlying(false);
                }

                String[][] format = {{"%player%", user.getName()}};
                context.reply("FLY." + (enabled ? "ENABLE" : "DISABLE"), format);
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
