package me.topeestla.essentials.config;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.config.ConfigurationFormater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LocaleManager {

    private final LiteEssentials essentials;

    private FileConfiguration localeFileConfig;
    private ConfigurationFormater localeConfig;

    public LocaleManager(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    public void loadConfig() {
        File localeFolder = new File(this.essentials.getDataFolder().getAbsolutePath() + "/locale");

        if (!localeFolder.exists() || localeFolder.listFiles().length < 1) {
            this.essentials.saveResource("locale/fr_FR.yml", false);
            this.localeFileConfig = YamlConfiguration.loadConfiguration(new File(this.essentials.getDataFolder().getAbsolutePath() + "/locale/fr_FR.yml"));
            this.localeConfig = new ConfigurationFormater(this.localeFileConfig);
        } else {
            String locale = this.essentials.getConfig().getString("locale");
            this.localeFileConfig = YamlConfiguration.loadConfiguration(new File(this.essentials.getDataFolder().getAbsolutePath() + "/locale/" + locale + ".yml"));
            this.localeConfig = new ConfigurationFormater(this.localeFileConfig);
        }
    }

    public FileConfiguration getLocaleFileConfig() {
        return localeFileConfig;
    }

    public ConfigurationFormater getLocaleConfig() {
        return localeConfig;
    }
}
