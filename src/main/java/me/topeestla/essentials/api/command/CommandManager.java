package me.topeestla.essentials.api.command;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.command.annotation.CommandHandler;
import me.topeestla.essentials.api.command.annotation.CompleterHandler;
import me.topeestla.essentials.api.command.data.BaseCommand;
import me.topeestla.essentials.api.command.data.BaseCommandExecutor;
import me.topeestla.essentials.api.command.data.BukkitCommand;
import me.topeestla.essentials.api.command.data.BukkitCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

/**
 * A simple CommandManager based on "CommandFramework" reworked by TopeEstLa for SeasonSky.
 *
 * @author TopeEstLa
 */
public class CommandManager {

    private final LiteEssentials plugin;
    private CommandMap spigotCommandMap;
    private final Map<String, Entry<Method, BaseCommand>> commandMap = new HashMap<>();

    private final BaseCommandExecutor commandExecutor;

    private final String PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName();
    private final String VERSION = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf(".") + 1);

    public CommandManager(LiteEssentials essentials) {
        this.plugin = essentials;
        this.commandExecutor = new BaseCommandExecutor(this);

        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();

            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                spigotCommandMap = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | NoSuchFieldException | IllegalAccessException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register a Command and TabCompleter of an BaseCommand object
     *
     * @param command
     */
    public void registerCommands(BaseCommand command) {
        this.plugin.getDebugLogger().log(Level.WARNING, "Registering command of class" + command.getClass().getName());

        for (Method method : command.getClass().getMethods()) {
            if (method.getAnnotation(CommandHandler.class) != null) {
                CommandHandler handler = method.getAnnotation(CommandHandler.class);
                this.plugin.getDebugLogger().log(Level.WARNING, "Registering command " + handler.name());

                if (method.getParameterTypes().length > 3) {
                    this.plugin.getDebugLogger().log(Level.WARNING, "The command " + handler.name() + " has too many arguments.");
                    continue;
                }

                this.registerCommand(handler, handler.name(), method, command);
                for (String alias : handler.aliases()) {
                    this.registerCommand(handler, alias, method, command);
                }
            } else if (method.getAnnotation(CompleterHandler.class) != null) {
                CompleterHandler handler = method.getAnnotation(CompleterHandler.class);
                this.plugin.getDebugLogger().log(Level.WARNING, "Registering completer for command " + handler.name());

                if (method.getParameterTypes().length > 3) {
                    this.plugin.getLogger().log(Level.SEVERE, "The completer " + handler.name() + " has too many arguments.");
                    continue;
                }

                if (method.getReturnType() != List.class) {
                    this.plugin.getLogger().log(Level.SEVERE,"Unable to register tab completer " + handler.name() + ". Unexpected return type");
                    continue;
                }

                this.registerCompleter(handler.name(), method, command);
                for (String alias : handler.aliases()) {
                    this.registerCompleter(alias, method, command);
                }
            } else {
                continue;
            }
        }
    }

    /**
     * Unregister a Command and TabCompleter of an BaseCommand object
     *
     * @param obj
     */
    public void unregisterCommands(BaseCommand obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(CommandHandler.class) != null) {
                CommandHandler command = m.getAnnotation(CommandHandler.class);

                this.unregisterCommand(command.name());
                for (String alias : command.aliases()) {
                    this.unregisterCommand(alias);
                }

                continue;
            }
        }
    }

    /**
     * Register the Help of the Plugins
     */
    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                Command cmd = spigotCommandMap.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help,
                "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    /**
     * Register a command of an BaseCommand object
     *
     * @param commandHandler
     * @param label
     * @param method
     * @param command
     */
    private void registerCommand(CommandHandler commandHandler, String label, Method method, BaseCommand command) {

        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(method, command));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(method, command));

        String cmdLabel = label.split("\\.")[0].toLowerCase();

        if (spigotCommandMap.getCommand(cmdLabel) == null) {
            Command cmd = new BukkitCommand(cmdLabel, this.commandExecutor, plugin);
            spigotCommandMap.register(plugin.getName(), cmd);
        }

        if (!commandHandler.help()[1].equalsIgnoreCase("") && cmdLabel.equals(label)) {
            spigotCommandMap.getCommand(cmdLabel).setDescription(commandHandler.help()[1]);
        }

        if (!commandHandler.help()[0].equalsIgnoreCase("") && cmdLabel.equals(label)) {
            spigotCommandMap.getCommand(cmdLabel).setUsage(commandHandler.help()[0]);
        }
    }

    /**
     * Register a completer of an BaseCommand object
     *
     * @param label
     * @param method
     * @param command
     */
    private void registerCompleter(String label, Method method, BaseCommand command) {
        String cmdLabel = label.split("\\.")[0].toLowerCase();

        if (spigotCommandMap.getCommand(cmdLabel) == null) {
            return;
        }

        if (spigotCommandMap.getCommand(cmdLabel) instanceof BukkitCommand) {

            BukkitCommand bukkitCommand = (BukkitCommand) spigotCommandMap.getCommand(cmdLabel);

            if (bukkitCommand.getCompleter() == null) {
                bukkitCommand.setCompleter(new BukkitCompleter());
            }

            bukkitCommand.getCompleter().addCompleter(label, method, command);

        } else if (spigotCommandMap.getCommand(cmdLabel) instanceof PluginCommand) {
            try {

                Object commandObj = spigotCommandMap.getCommand(cmdLabel);
                Field field = commandObj.getClass().getDeclaredField("completer");
                field.setAccessible(true);

                if (field.get(commandObj) == null) {

                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, method, command);
                    field.set(commandObj, completer);
                } else if (field.get(commandObj) instanceof BukkitCompleter) {

                    BukkitCompleter completer = (BukkitCompleter) field.get(commandObj);
                    completer.addCompleter(label, method, command);
                } else {
                    this.plugin.getLogger().log(Level.WARNING, "Unable to register tab completer " + label + ". Unexpected return type");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void unregisterCommand(String label) {
        commandMap.remove(label.toLowerCase());
        commandMap.remove(this.plugin.getName() + ':' + label.toLowerCase());

        String cmdLabel = label.split("\\.")[0].toLowerCase();

        final Class<?> serverClass;

        try {
            serverClass = Class.forName("org.bukkit.craftbukkit." + VERSION + ".CraftServer");

            final Field field = serverClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            final SimpleCommandMap commandMap = (SimpleCommandMap) field.get(Bukkit.getServer());

            final Field field2 = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field2.setAccessible(true);
            final Map<String, Command> knownCommands = (Map) field2.get(commandMap);

            knownCommands.remove(cmdLabel);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Entry<Method, BaseCommand>> getCommandMap() {
        return commandMap;
    }

    public LiteEssentials getPlugin() {
        return plugin;
    }
}
