package me.topeestla.essentials.commands.admin;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import me.topeestla.essentials.api.entity.IUser;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SeenCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public SeenCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    @CommandHandler(name = "seen", permission = "essentials.seen", help = {"/seen <player>", "Check when a player was last seen"})
    public void onCommand(CommandContext context, String[] args) {
        if (args.length >= 1) {
            IUser targetUser = this.essentials.getUserManager().handleDataNeed(args[0]);

            if (targetUser != null) {

                String online = targetUser.getPlayer() != null ? this.essentials.getLocaleManager().getLocaleConfig().getString("LOGIN_STATE.ONLINE", true, null) : this.essentials.getLocaleManager().getLocaleConfig().getString("LOGIN_STATE.OFFLINE", true, null);

                String[][] format = {{"%player%", targetUser.getName()}, {"%state%", online},
                        {"%time%", DurationFormatUtils.formatDuration(System.currentTimeMillis() - targetUser.getLastLogin().getTime(), "H'h 'm'm 's's'")},
                        {"%uuid%", targetUser.getUniqueId().toString()},
                        {"%ip%", targetUser.getIP()}};

                context.replies("SEEN", format);
            } else {
                context.reply("PLAYER-NOT-FOUND", null);
            }
        } else {
            context.getSender().sendMessage("§cUsage: /seen <player>");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }

}
