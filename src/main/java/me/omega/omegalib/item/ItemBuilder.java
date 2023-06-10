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

/**
 * A basic builder for {@link ItemStack}s. Provides methods and a fluent API to build and update item names, lore,
 * flags, and more.
 * <p/>
 * This class also provides methods to create {@link MenuItem}s from built or prebuilt itemstacks.
 */
@ToString
public class ItemBuilder extends GenericItemBuilder {

    /**
     * Creates an item builder with a {@link Material}.
     *
     * @param material the {@link Material} to build on
     */
    public ItemBuilder(@NonNull Material material) {
        this(new ItemStack(material));
    }

    /**
     * Creates an item builder with an {@link ItemStack}.
     *
     * @param itemStack the {@link ItemStack} to build on
     */
    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Sets the name of the itemstack.
     *
     * @param name the new name
     * @return this item builder, for chaining
     */
    public ItemBuilder name(@NonNull String name) {
        getMeta().displayName(Text.componentOf(name));
        update();
        return this;
    }

    /**
     * Adds to the lore of the itemstack.
     *
     * @param strings any new lore to add
     * @return this item builder, for chaining
     */
    public ItemBuilder addLore(@NonNull String... strings) {
        if (getMeta().lore() == null) {
            getMeta().lore(new ArrayList<>());
        }
        List<Component> lore = getMeta().lore();
        for (String line : strings) {
            lore.add(Text.componentOf(line));
        }
        getMeta().lore(lore);
        update();
        return this;
    }

    /**
     * Sets the lore of the itemstack.
     *
     * @param strings the new lore
     * @return this item builder, for chaining
     */
    public ItemBuilder setLore(@NonNull String... strings) {
        List<Component> lore = new ArrayList<>();
        for (String line : strings) {
            lore.add(Text.componentOf(line));
        }
        getMeta().lore(lore);
        update();
        return this;
    }

    /**
     * Clears the lore of the itemstack.
     *
     * @return this item builder, for chaining
     */
    public ItemBuilder clearLore() {
        getMeta().lore(new ArrayList<>());
        update();
        return this;
    }

    /**
     * Sets the {@link Material} of the itemstack.
     *
     * @param material the new material
     * @return this item builder, for chaining
     */
    public ItemBuilder material(@NonNull Material material) {
        getItemStack().setType(material);
        return this;
    }

    /**
     * Adds {@link ItemFlag}s to the itemstack.
     *
     * @param flags the new flags to add
     * @return this item builder, for chaining
     */
    public ItemBuilder flag(@NonNull ItemFlag... flags) {
        Map<Enchantment, Integer> enchantments = getItemStack().getEnchantments();
        getMeta().addItemFlags(flags);
        update();
        getItemStack().addUnsafeEnchantments(enchantments);
        return this;
    }

    /**
     * Removes {@link ItemFlag}s from the itemstack.
     *
     * @param flags the flags to remove
     * @return this item builder, for chaining
     */
    public ItemBuilder unflag(ItemFlag... flags) {
        Map<Enchantment, Integer> enchantments = getItemStack().getEnchantments();
        getMeta().removeItemFlags(flags);
        update();
        getItemStack().addUnsafeEnchantments(enchantments);
        return this;
    }

    /**
     * Adds all {@link ItemFlag}s to the itemstack.
     *
     * @return this item builder, for chaining
     */
    public ItemBuilder flagAll() {
        flag(ItemFlag.values());
        return this;
    }

    /**
     * Removes all {@link ItemFlag}s from the itemstack.
     *
     * @return this item builder, for chaining
     */
    public ItemBuilder unflagAll() {
        unflag(ItemFlag.values());
        return this;
    }

    /**
     * Toggles the unbreakable state of the itemstack.
     *
     * @return this item builder, for chaining
     */
    public ItemBuilder unbreakable() {
        getMeta().setUnbreakable(!getMeta().isUnbreakable());
        update();
        return this;
    }

    /**
     * Sets the amount of the itemstack.
     *
     * @param amount the new amount
     * @return this item builder, for chaining
     */
    public ItemBuilder amount(int amount) {
        getItemStack().setAmount(amount);
        return this;
    }

    /**
     * Enchants the itemstack with an {@link Enchantment}. If the level is greater than {@link Short#MAX_VALUE}, it will
     * be set to {@link Short#MAX_VALUE}.
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return this item builder, for chaining
     */
    public ItemBuilder enchant(@NonNull Enchantment enchantment, int level) {
        if (level > Short.MAX_VALUE) {
            level = Short.MAX_VALUE;
        }
        getItemStack().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Enchants the itemstack with an {@link Enchantment} with a level of 1.
     *
     * @param enchantment the enchantment to add
     * @return this item builder, for chaining
     */
    public ItemBuilder enchant(@NonNull Enchantment enchantment) {
        getItemStack().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Removes an {@link Enchantment} from the itemstack.
     *
     * @param enchantment the enchantment to remove
     * @return this item builder, for chaining
     */
    public ItemBuilder unenchant(@NonNull Enchantment enchantment) {
        getItemStack().removeEnchantment(enchantment);
        return this;
    }

    /**
     * Sets the custom model data of the itemstack.
     *
     * @param data the new custom model data
     * @return this item builder, for chaining
     */
    public ItemBuilder customModelData(int data) {
        getMeta().setCustomModelData(data);
        update();
        return this;
    }

    /**
     * Effectively makes the itemstack "empty". This is done by setting the name to " ", clearing the lore, setting
     * unbreakable to true, and adding all flags.
     *
     * @return this item builder, for chaining
     */
    public ItemBuilder empty() {
        flagAll();
        unbreakable();
        name(" ");
        clearLore();
        return this;
    }

    /**
     * Builds a {@link MenuItem} with no click action.
     *
     * @return the built {@link MenuItem}
     */
    public MenuItem buildGuiItem() {
        return buildGuiItem(null, null);
    }

    /**
     * Builds a {@link MenuItem} with the specified click action.
     *
     * @param action the click action to associate with the MenuItem
     * @return the built {@link MenuItem}
     */
    public MenuItem buildGuiItem(MenuAction<InventoryClickEvent> action) {
        return buildGuiItem(null, action);
    }

    /**
     * Builds a {@link MenuItem} with the specified click type and click action.
     *
     * @param clickType the click type required to execute the action
     * @param action    the action to execute
     * @return the built {@link MenuItem}
     */
    public MenuItem buildGuiItem(ClickType clickType, MenuAction<InventoryClickEvent> action) {
        return new MenuItem(getItemStack(), clickType, action);
    }

}