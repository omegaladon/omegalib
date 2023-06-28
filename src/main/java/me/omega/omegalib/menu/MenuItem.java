package me.omega.omegalib.menu;

import lombok.NonNull;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public record MenuItem(@NonNull ItemStack itemStack, ClickType clickType, MenuAction<InventoryClickEvent> action) {
}
