package me.omega.omegalib.item;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import lombok.NonNull;
import lombok.ToString;
import me.omega.omegalib.item.head.HeadEntry;
import me.omega.omegalib.item.head.Heads;
import me.omega.omegalib.utils.Http;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ToString
public class HeadBuilder extends GenericItemBuilder {

    private static final Map<String, HeadBuilder> SKULL_CACHE = new HashMap<>();

    public HeadBuilder() {
        super(new ItemStack(Material.PLAYER_HEAD));
    }

    public HeadBuilder database(int id) {
        if (!Heads.isLoaded()) {
            throw new IllegalArgumentException("The head database is not loaded. Load it with Heads.load().");
        }
        Optional<HeadEntry> entry = Heads.getHeadEntry(id);
        if (entry.isEmpty()) {
            throw new IllegalArgumentException("The ID " + id + " is not in the head database.");
        }
        return texture(entry.get().name(), entry.get().uuid(), entry.get().texture());
    }

    public HeadBuilder owner(@NonNull Player player) {
        return owner(player.getName(), player.getUniqueId());
    }

    public HeadBuilder owner(@NonNull String name, @NonNull UUID uuid) {

        if (SKULL_CACHE.containsKey(name)) {
            return SKULL_CACHE.get(name);
        }
        Optional<String> texture = getTextureValue(name);
        if (texture.isEmpty()) {
            return this;
        }

        HeadBuilder builder = texture(name, uuid, texture.get());
        SKULL_CACHE.put(name, builder);
        return builder;
    }

    public HeadBuilder texture(@NonNull String name, @NonNull UUID uuid, @NonNull String texture) {
        NBTItem nbtItem = new NBTItem(getItemStack());
        NBTCompound skull = nbtItem.addCompound("SkullOwner");
        skull.setString("Name", name);
        skull.setString("Id", uuid.toString());

        NBTListCompound properties = skull.addCompound("Properties").getCompoundList("textures").addCompound();
        properties.setString("Value", texture);

        setItemStack(nbtItem.getItem());
        return this;
    }

    private static Optional<String> getTextureValue(String playerName) {
        try {
            Optional<Object> playerValues =
                    Http.getUrlContent("https://api.mojang.com/users/profiles/minecraft/" + playerName, false);
            if (playerValues.isEmpty()) {
                return Optional.empty();
            }
            JsonObject playerValue = (JsonObject) playerValues.get();
            String uuid = playerValue.get("id").getAsString();

            Optional<Object> playerProperties = Http.getUrlContent("https://sessionserver.mojang" +
                                                                   ".com/session/minecraft/profile/" + uuid,
                                                                   false);
            if (playerProperties.isEmpty()) {
                return Optional.empty();
            }
            JsonObject playerProperty = (JsonObject) playerProperties.get();
            String textureValue = playerProperty.get("properties").getAsJsonArray().get(0).getAsJsonObject().get(
                    "value").getAsString();
            return Optional.of(textureValue);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
