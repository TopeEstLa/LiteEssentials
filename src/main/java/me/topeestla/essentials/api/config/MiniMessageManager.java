package me.topeestla.essentials.api.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

/**
 * @author TopeEstLa
 */
public class MiniMessageManager {

    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();

    public static String getMiniMessageString(String text) {
        String formattedText = text;
        formattedText = ChatColor.translateAlternateColorCodes('&', formattedText);
        formattedText = MiniMessageManager.LEGACY_COMPONENT_SERIALIZER.serialize(MiniMessageManager.MINI_MESSAGE.deserialize(formattedText));
        return formattedText;
    }

    public static Component getMiniMessageComponent(String text) {
        String formattedText = text;
        formattedText = ChatColor.translateAlternateColorCodes('&', formattedText);
        return MiniMessageManager.MINI_MESSAGE.deserialize(formattedText);
    }
}
