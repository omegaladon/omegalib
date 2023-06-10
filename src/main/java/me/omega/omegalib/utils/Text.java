package me.omega.omegalib.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for working with text-related operations.
 */
@UtilityClass
public class Text {

    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F\\d]{6}");

    /**
     * Converts a {@code String} into a {@link TextComponent} using the {@link LegacyComponentSerializer}.
     * <p>Supports legacy color codes and hex colors formatted as {@code &#123456}.</p>
     *
     * @param text the text to convert
     * @return a {@link TextComponent} representing the given text
     */
    public static TextComponent componentOf(@NonNull String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    /**
     * Returns a colored version of the input text by replacing hexadecimal color codes with their corresponding
     * {@link net.md_5.bungee.api.ChatColor} values.
     *
     * @param text the text to color
     * @return the colored text
     */
    public static String colored(@NonNull String text) {
        for (Matcher match = hexPattern.matcher(text); match.find(); match = hexPattern.matcher(text)) {
            String color = text.substring(match.start(), match.end());
            text = text.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
