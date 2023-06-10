package me.omega.omegalib.item;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * An abstract class representing a generic item builder.
 */
@Getter
@Setter
public abstract class GenericItemBuilder {

    private ItemStack itemStack;
    private ItemMeta meta;

    /**
     * Creates an item builder with an {@link ItemStack}.
     *
     * @param itemStack the {@link ItemStack} to build
     */
    public GenericItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    /**
     * Updates the itemstack with the current item meta. This method must be called after any changes to the item meta.
     */
    public void update() {
        itemStack.setItemMeta(meta);
    }

}

