package me.omega.omegalib.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.text.WordUtils;

import java.util.regex.Pattern;


@UtilityClass
public class Text {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static TextComponent componentOf(@NonNull String text) {
        return (TextComponent) miniMessage.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent legacyComponentOf(@NonNull String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static String wrap(String text, int maxChars, String join) {
        return WordUtils.wrap(text, maxChars, join, false);
    }

    public static String getProgressBar(double current, double max, int totalBars, char symbol, String completedColor, String notCompletedColor) {
        double percent = (current / max);
        int progressBars = (int) (totalBars * percent);

        return String.format("%s%s", completedColor, new String(new char[progressBars]).replace('\0', symbol)) +
                String.format("%s%s", notCompletedColor, new String(new char[totalBars - progressBars]).replace('\0', symbol));
    }

}
