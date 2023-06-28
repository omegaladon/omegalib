package me.omega.omegalib.messenger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.omega.omegalib.utils.Text;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class Messenger {

    public static void message(Audience player, Message message, Object... replacements) {
        message(player, EmptyMessageType.EMPTY, message, replacements);
    }

    public static void message(Audience player, MessageType type, Message message, Object... replacements) {
        message(player, type, message.getMessage(), replacements);
    }

    public static void message(Audience player, String message, Object... replacements) {
        message(player, EmptyMessageType.EMPTY, message, replacements);
    }

    public static void message(Audience player, MessageType type, String message, Object... replacements) {
        player.sendMessage(Text.componentOf(type.getPrefix() + MessageFormat.format(message, replacements)));
    }

    public static void sound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player, sound, volume, pitch);
    }

    public static void sound(Player player, SoundType sound) {
        sound(player, sound.getSound(), sound.getVolume(), sound.getPitch());
    }

    public static void actionbar(Player player, String message, Object... replacements) {
        player.sendActionBar(Text.componentOf(MessageFormat.format(message, replacements)));
    }

    @AllArgsConstructor
    @Getter
    private enum EmptyMessageType implements MessageType {
        EMPTY("<gray>");

        private final String prefix;
    }

}
