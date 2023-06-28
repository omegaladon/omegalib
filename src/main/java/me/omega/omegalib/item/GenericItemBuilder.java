package me.omega.omegalib.item;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
public abstract class GenericItemBuilder {

    private ItemStack itemStack;
    private ItemMeta meta;

    public GenericItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    public void update() {
        itemStack.setItemMeta(meta);
    }

}

