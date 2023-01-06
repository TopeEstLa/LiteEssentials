package me.topeestla.essentials.api.command.data;

import me.topeestla.essentials.api.command.CommandManager;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TopeEstLa
 */
public class BaseCommandExecutor implements CommandExecutor {

    private final CommandManager commandManager;

    public BaseCommandExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    /**
     * Handles commands. Used in the onCommand method in your JavaPlugin class
     *
     * @param sender The {@link CommandSender} parsed from
     *               onCommand
     * @param cmd    The {@link Command} parsed from onCommand
     * @param label  The label parsed from onCommand
     * @param args   The arguments parsed from onCommand
     * @return Always returns true for simplicity's sake in onCommand
     */
    public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append("." + args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            IUser user = null;
            if (this.commandManager.getCommandMap().containsKey(cmdLabel)) {
                Method method = this.commandManager.getCommandMap().get(cmdLabel).getKey();
                Object methodObject = this.commandManager.getCommandMap().get(cmdLabel).getValue();

                CommandHandler command = method.getAnnotation(CommandHandler.class);

                if (!command.permission().equals("") && !sender.hasPermission(command.permission())) {
                    sender.sendMessage("§cVous n'avez pas la permission pour utiliser cette commande.");
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage("§cCette commande est disponible uniquement dans le jeu.");
                    return true;
                } else if (sender instanceof Player) {
                     user = this.commandManager.getPlugin().getUserManager().getUserCache().getUser(sender.getName());

                    if (user == null) {
                        sender.sendMessage("§cVous n'avez pas la permission pour utiliser cette commande.");
                        return true;
                    }
                }

                try {
                    int subCommand = cmdLabel.split("\\.").length - 1;
                    String[] modArgs = new String[args.length - subCommand];

                    for (int y = 0; y < args.length - subCommand; y++) {
                        modArgs[y] = args[y + subCommand];
                    }


                    CommandContext context = new CommandContext(this.commandManager.getPlugin(), sender, user);

                    method.invoke(methodObject, context, modArgs);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        sender.sendMessage("§cCette commande n'existe pas.");
        return true;
    }
}
