package me.topeestla.essentials;

import me.topeestla.essentials.api.command.CommandManager;
import me.topeestla.essentials.commands.admin.*;
import me.topeestla.essentials.commands.player.*;
import me.topeestla.essentials.config.LocaleManager;
import me.topeestla.essentials.config.SettingsManager;
import me.topeestla.essentials.providers.EconomyProvider;
import me.topeestla.essentials.user.UserManager;
import me.topeestla.essentials.user.listener.UserListener;
import me.topeestla.essentials.utils.DebugLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class LiteEssentials extends JavaPlugin {

    private DebugLogger debugLogger;
    private CommandManager commandManager;

    private LocaleManager localeManager;
    private SettingsManager settingsManager;
    private BukkitAudiences audiences;

    private UserManager userManager;


    @Override
    public void onLoad() {
        super.onLoad();

        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            getServer().getServicesManager().register(Economy.class, new EconomyProvider(this), this, ServicePriority.Normal);
        } catch (ClassNotFoundException e) {
            //Ignored not hook into vault
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.audiences = BukkitAudiences.create(this);

        this.localeManager = new LocaleManager(this);
        this.settingsManager = new SettingsManager(this);
        this.settingsManager.loadSettings();

        this.debugLogger = new DebugLogger(this);
        this.commandManager = new CommandManager(this);
        this.userManager = new UserManager(this);

        this.localeManager.loadConfig();

        this.userManager.init();

        this.commandManager.registerCommands(new SeenCommand(this));

        this.commandManager.registerCommands(new FlyCommand(this));

        //Admin economy commands
        this.commandManager.registerCommands(new MoneyGiveCommand(this));
        this.commandManager.registerCommands(new MoneyTakeCommand(this));
        this.commandManager.registerCommands(new MoneySetCommand(this));
        this.commandManager.registerCommands(new MoneyClearCommand(this));
        this.commandManager.registerCommands(new MoneyGiveAllCommand(this));


        //Player economy commands
        this.commandManager.registerCommands(new MoneyCommand(this));
        this.commandManager.registerCommands(new PayCommand(this));

        //Player home commands
        this.commandManager.registerCommands(new HomeCommand(this));
        this.commandManager.registerCommands(new SetHomeCommand(this));
        this.commandManager.registerCommands(new DelHomeCommand(this));

        this.getServer().getPluginManager().registerEvents(new UserListener(this), this);
    }

    @Override
    public void onDisable() {
    }


    public SettingsManager getSettings() {
        return settingsManager;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public BukkitAudiences getAudiences() {
        return audiences;
    }

    public DebugLogger getDebugLogger() {
        return debugLogger;
    }

    public UserManager getUserManager() {
        return userManager;
    }

}
