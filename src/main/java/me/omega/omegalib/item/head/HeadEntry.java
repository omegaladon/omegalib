package me.omega.omegalib.item.head;

import java.util.List;
import java.util.UUID;

public record HeadEntry(String name, UUID uuid, String texture, List<String> tags, int id) {
}
