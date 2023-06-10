package me.omega.omegalib.menu;

import lombok.NonNull;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item in a {@link Menu}. A menu item consists of an item stack, a click type, and an action.
 */
public record MenuItem(@NonNull ItemStack itemStack, ClickType clickType, MenuAction<InventoryClickEvent> action) {
}
