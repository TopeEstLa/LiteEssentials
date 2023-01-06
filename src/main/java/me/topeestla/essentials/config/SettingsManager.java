package me.topeestla.essentials.config;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingsManager {

    private final LiteEssentials essentials;
    private FileConfiguration settingsFileConfig;

    private Map<String, Integer> multipleHomes;

    public SettingsManager(LiteEssentials essentials) {
        this.essentials = essentials;
        this.settingsFileConfig = essentials.getConfig();
    }

    public void loadSettings() {
        this.loadMultipleHomes();
    }

    public void loadMultipleHomes() {
        this.multipleHomes = new HashMap<>();
        for (String str : this.settingsFileConfig.getStringList("home.multiple")) {
            String[] st = str.split(":");
            String name = st[0];
            int time = Integer.parseInt(st[1]);
            this.multipleHomes.put(name, Integer.valueOf(time));
        }
    }

    public int getHomeLimit(final IUser user) {
        int limit = 1;
        if (user.getPlayer().hasPermission("essentials.sethome.multiple")) {
            limit = this.settingsFileConfig.getInt("home.limit.default");
        }

        for (Map.Entry<String, Integer> entry : this.multipleHomes.entrySet()) {
            if (user.getPlayer().hasPermission("essentials.sethome.multiple." + entry.getKey()))
                limit = Math.max(entry.getValue(), limit);
        }

        if (user.getPlayer().hasPermission("essentials.sethome.unlimited")) {
            limit = 1000000;
        }

        return limit;
    }

    public LiteEssentials getEssentials() {
        return essentials;
    }

    public FileConfiguration getSettingsFileConfig() {
        return settingsFileConfig;
    }
}
