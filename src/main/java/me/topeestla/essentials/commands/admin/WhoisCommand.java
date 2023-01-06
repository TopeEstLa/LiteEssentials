package me.topeestla.essentials.commands.admin;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WhoisCommand extends BaseCommand {

    private final LiteEssentials essentials;

    public WhoisCommand(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    public void onCommand(CommandContext context, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Player player, String[] args) {
        return null;
    }
}
