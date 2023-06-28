package me.omega.omegalib.data.codec;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemstackSerializer {

    public static ItemStack deserialize(String data) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.loadFromString(data);
            return yamlConfiguration.getItemStack("item");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serialize(ItemStack item) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("item", item);
        return yamlConfiguration.saveToString();
    }
}
