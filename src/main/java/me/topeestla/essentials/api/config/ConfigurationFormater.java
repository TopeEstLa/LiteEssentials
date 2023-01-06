package me.topeestla.essentials.api.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider for configuration, here you can get values from configuration
 *
 * @author NeiiZun, Edit TopeEstLa
 */

public class ConfigurationFormater {

    private FileConfiguration configuration;

    /**
     * @param configuration configFile class
     */
    public ConfigurationFormater(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @param path         path of YML string
     * @param formatColors true if you want to color the text
     * @param format       format Strings to another String (ex. {name} -> KayzunYT} you can create an double Array String[][]{{"key", "value"}, {"key", "value"}}
     * @return formatted String
     */
    public String getString(String path, boolean formatColors, String[][] format) {
        String result = this.configuration.getString(path);

        if (formatColors) {
            result = MiniMessageManager.getMiniMessageString(result);
        }


        if (format != null) {
            for (String[] formatTo : format) {
                String from = formatTo[0];
                String to = formatTo[1];

                result = result.replace(from, to);
            }
        }
        return result;
    }

    /**
     * @param path         path of YML String list
     * @param formatColors true if you want to color the text
     * @param format       format Strings to another String (ex. {name} -> TopeEstLa} you can create an double Array String[][]{{"key", "value"}, {"key", "value"}}
     * @return formatted String
     */
    public List<String> getStringList(String path, boolean formatColors, String[][] format) {
        List<String> result = this.configuration.getStringList(path);

        for (int i = 0; i < result.size(); i++) {
            if (formatColors) {
                result.set(i, MiniMessageManager.getMiniMessageString(result.get(i)));
            }

            if (format != null) {
                for (String[] formatTo : format) {
                    String from = formatTo[0];
                    String to = formatTo[1];

                    result.set(i, result.get(i).replace(from, to));
                }
            }
        }
        return result;
    }

    /**
     * @param path         path of YML String list
     * @param formatColors true if you want to color the text
     * @param format       format Strings to another String (ex. {name} -> TopeEstLa} you can create an double Array String[][]{{"key", "value"}, {"key", "value"}}
     * @return Component
     */
    public Component getChatComponent(String path, boolean formatColors, String[][] format) {
        String result = this.configuration.getString(path);
        Component component = null;

        if (formatColors) {
            if (format != null) {
                for (String[] formatTo : format) {
                    String from = formatTo[0];
                    String to = formatTo[1];

                    result = result.replace(from, to);
                }
            }

            component = MiniMessageManager.getMiniMessageComponent(result);
        }

        return component;
    }

    /**
     * @param path         path of YML String list
     * @param formatColors true if you want to color the text
     * @param format       format Strings to another String (ex. {name} -> TopeEstLa} you can create an double Array String[][]{{"key", "value"}, {"key", "value"}}
     * @return Component List
     */
    public List<Component> getChatComponentList(String path, boolean formatColors, String[][] format) {
        List<String> result = this.configuration.getStringList(path);
        List<Component> componentList = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            if (format != null) {
                for (String[] formatTo : format) {
                    String from = formatTo[0];
                    String to = formatTo[1];

                    result.set(i, result.get(i).replace(from, to));
                }
            }

            if (formatColors) {
                componentList.add(MiniMessageManager.getMiniMessageComponent(result.get(i)));
            }
        }

        return componentList;
    }

    /**
     * @param configuration
     */
    public void setConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @return {@link FileConfiguration}
     */
    public FileConfiguration getConfiguration() {
        return configuration;
    }
}