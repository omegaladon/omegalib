package me.omega.omegalib.item;

import lombok.NonNull;
import lombok.ToString;
import me.omega.omegalib.menu.MenuAction;
import me.omega.omegalib.menu.MenuItem;
import me.omega.omegalib.utils.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ToString
public class ItemBuilder extends GenericItemBuilder {

    public static MenuItem EMPTY = new ItemBuilder(Material.AIR).buildGuiItem();

    public ItemBuilder(@NonNull Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder name(@NonNull String name) {
        getMeta().displayName(Text.legacyComponentOf(name));
        update();
        return this;
    }

    public ItemBuilder addLore(@NonNull String... strings) {
        if (getMeta().lore() == null) {
            getMeta().lore(new ArrayList<>());
        }
        List<Component> lore = getMeta().lore();
        for (String line : strings) {
            for (String split : line.split("\n")) {
                lore.add(Text.legacyComponentOf(split));
            }
        }
        getMeta().lore(lore);
        update();
        return this;
    }

    public ItemBuilder setLore(@NonNull String... strings) {
        List<Component> lore = new ArrayList<>();
        for (String line : strings) {
            for (String split : line.split("\n")) {
                lore.add(Text.legacyComponentOf(split));
            }
        }
        getMeta().lore(lore);
        update();
        return this;
    }

    public ItemBuilder clearLore() {
        getMeta().lore(new ArrayList<>());
        update();
        return this;
    }

    public ItemBuilder material(@NonNull Material material) {
        getItemStack().setType(material);
        return this;
    }

    public ItemBuilder flag(@NonNull ItemFlag... flags) {
        Map<Enchantment, Integer> enchantments = getItemStack().getEnchantments();
        getMeta().addItemFlags(flags);
        update();
        getItemStack().addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemBuilder unflag(ItemFlag... flags) {
        Map<Enchantment, Integer> enchantments = getItemStack().getEnchantments();
        getMeta().removeItemFlags(flags);
        update();
        getItemStack().addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemBuilder flagAll() {
        flag(ItemFlag.values());
        return this;
    }

    public ItemBuilder unflagAll() {
        unflag(ItemFlag.values());
        return this;
    }

    public ItemBuilder unbreakable() {
        getMeta().setUnbreakable(!getMeta().isUnbreakable());
        update();
        return this;
    }

    public ItemBuilder amount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(@NonNull Enchantment enchantment, int level) {
        if (level > Short.MAX_VALUE) {
            level = Short.MAX_VALUE;
        }
        getItemStack().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchant(@NonNull Enchantment enchantment) {
        getItemStack().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder unenchant(@NonNull Enchantment enchantment) {
        getItemStack().removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder customModelData(int data) {
        getMeta().setCustomModelData(data);
        update();
        return this;
    }

    public ItemBuilder empty() {
        flagAll();
        unbreakable();
        name(" ");
        clearLore();
        return this;
    }

    public MenuItem buildGuiItem() {
        return buildGuiItem(null, null);
    }

    public MenuItem buildGuiItem(MenuAction<InventoryClickEvent> action) {
        return buildGuiItem(null, action);
    }

    public MenuItem buildGuiItem(ClickType clickType, MenuAction<InventoryClickEvent> action) {
        return new MenuItem(getItemStack(), clickType, action);
    }

}