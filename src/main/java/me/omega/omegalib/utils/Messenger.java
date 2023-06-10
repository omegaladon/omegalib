package me.omega.omegalib.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * A utility class for sending notifications to players in game.
 */
public class Messenger {

    /**
     * Sends a message to a player. The message supports legacy color codes, as well as hex codes formatted as
     * {@code &#123456}.
     *
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void message(@NonNull Audience player, @NonNull String message) {
        player.sendMessage(Text.componentOf(message));
    }

    /**
     * Sends a message of a specified {@link MessageType} to a player. The message will be prefixed with the type's
     * prefix.
     *
     * @param player  the player to send the message to
     * @param type    the type of the message (e.g. SUCCESS or ERROR)
     * @param message the message to send
     */
    public static void message(@NonNull Audience player, @NonNull MessageType type, @NonNull String message) {
        player.sendMessage(Text.componentOf(type.getPrefix() + " " + message));
    }

    /**
     * Plays a sound to a player.
     *
     * @param player the player to play the sound to
     * @param sound  the sound to play
     * @param volume the volume of the sound (between 0 and 1)
     * @param pitch  the pitch of the sound (between 0 and 2)
     */
    public static void sound(@NonNull Player player, @NonNull Sound sound, float volume, float pitch) {
        player.playSound(player, sound, volume, pitch);
    }

    /**
     * Plays a sound to a player at default volume and pitch.
     *
     * @param player the player to play the sound to
     * @param sound  the sound to play
     */
    public static void sound(@NonNull Player player, @NonNull Sound sound) {
        sound(player, sound, 1, 1);
    }

    /**
     * An enum representing the type of message to send.
     */
    @AllArgsConstructor
    @Getter
    public enum MessageType {
        SUCCESS("ⓘ"),
        ERROR("⚠");

        private final String prefix;
    }

}
