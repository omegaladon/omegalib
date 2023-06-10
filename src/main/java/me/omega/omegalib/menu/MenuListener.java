package me.omega.omegalib.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {

            if (menu.isCancelClick()) {
                event.setCancelled(true);
            }

            if (event.getClickedInventory() == null) {
                if (menu.getClickOutsideAction() != null) {
                    menu.getClickOutsideAction().execute(event);
                }
                return;
            }

            if (event.getClickedInventory().getHolder() != menu) {
                if (menu.getClickBottomAction() != null) {
                    menu.getClickBottomAction().execute(event);
                }
                return;
            }

            int clickedSlot = event.getRawSlot();
            MenuItem clickedItem = menu.getSlots()[clickedSlot];
            if (clickedItem == null) {
                return;
            }

            if (clickedItem.action() == null) {
                event.setCancelled(true);
                return;
            }

            if (clickedItem.clickType() == null) {
                clickedItem.action().execute(event);
                return;
            }

            if (clickedItem.clickType() == event.getClick()) {
                clickedItem.action().execute(event);
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {
            if (menu.getMenuCloseAction() != null) {
                menu.getMenuCloseAction().execute(event);
            }
        }
    }

    @EventHandler
    public void onMenuOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {
            if (menu.getMenuOpenAction() != null) {
                menu.getMenuOpenAction().execute(event);
            }
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {
            if (menu.getItemDragAction() != null) {
                menu.getItemDragAction().execute(event);
            }
        }
    }

}
