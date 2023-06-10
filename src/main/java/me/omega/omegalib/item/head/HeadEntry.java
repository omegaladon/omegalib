package me.omega.omegalib.item.head;

import java.util.List;
import java.util.UUID;

/**
 * A record representing a head entry from the minecraft-heads.com API.
 *
 * @param name    the name of the head
 * @param uuid    a uuid
 * @param texture the texture value
 * @param tags    a list of tags from the API
 * @param id      the id of the head
 */
public record HeadEntry(String name, UUID uuid, String texture, List<String> tags, int id) {
}
