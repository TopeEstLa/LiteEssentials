package me.topeestla.essentials.api.command.data;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

    private final LiteEssentials essentials;
    private final CommandSender sender;
    private final IUser iUser;
    private boolean isConsole;

    public CommandContext(LiteEssentials essentials, CommandSender sender, IUser iUser) {
        this.essentials = essentials;
        this.sender = sender;
        this.iUser = iUser;

        this.isConsole = true;

        if (sender instanceof Player) {
            this.isConsole = false;
        }
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public IUser getIUser() {
        return this.iUser;
    }

    public boolean isConsole() {
        return this.isConsole;
    }

    public boolean hasPermission(String permission) {
        if (this.isConsole) {
            return true;
        } else {
            return this.sender.hasPermission(permission);
        }
    }

    public void reply(String path, String[][] format) {
        if (this.isConsole) {
            this.sender.sendMessage(this.essentials.getLocaleManager().getLocaleConfig().getString(path, true, format));
        } else {
            this.iUser.sendMessage(this.essentials, this.essentials.getLocaleManager().getLocaleConfig().getChatComponent(path, true, format));
        }
    }

    public void replies(String path, String[][] format) {
        if (this.isConsole) {
            for (String message : this.essentials.getLocaleManager().getLocaleConfig().getStringList(path, true, format)) {
                this.sender.sendMessage(message);
            }
        } else {
            this.iUser.sendMessages(this.essentials, this.essentials.getLocaleManager().getLocaleConfig().getChatComponentList(path, true, format));
        }
    }
}
