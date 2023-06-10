package me.omega.omegalib.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.omega.omegalib.Lib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public abstract class Menu implements InventoryHolder {

    private final MenuItem[] slots;
    private Player player;

    private Inventory inventory;
    private final Component inventoryTitle;
    private final int inventorySize;
    private final InventoryType inventoryType;

    @Setter
    private MenuAction<InventoryClickEvent> clickOutsideAction;
    @Setter
    private MenuAction<InventoryCloseEvent> menuCloseAction;
    @Setter
    private MenuAction<InventoryDragEvent> itemDragAction;
    @Setter
    private MenuAction<InventoryOpenEvent> menuOpenAction;
    @Setter
    private MenuAction<InventoryClickEvent> clickBottomAction;

    static {
        Lib.getInstance().registerListeners(new MenuListener());
    }

    @Getter
    @Setter
    private boolean cancelClick = true;


    /**
     * Sets the items in the menu. This method is called once when the menu is opened, and can be called again to update
     * the menu.
     */
    public abstract void draw();

    /**
     * Creates a chest menu with a title and size.
     *
     * @param title the title
     * @param size  the amount of rows
     */
    public Menu(String title, int size) {
        this(title, size, InventoryType.CHEST);
    }

    /**
     * Creates a menu with a title and type. The size is determined by the default size of the {@link InventoryType}.
     *
     * @param title the title
     * @param type  the type of menu
     */
    public Menu(String title, @NonNull InventoryType type) {
        this(title, type.getDefaultSize(), type);
    }

    private Menu(String title, int size, @NonNull InventoryType type) {
        if (title == null) {
            inventoryTitle = InventoryType.CHEST.defaultTitle();
        } else {
            inventoryTitle = Component.text(title);
        }

        if (size < 1 || size > 6) {
            throw new IllegalArgumentException("Menu size must be between 1 and 6.");
        }
        inventorySize = size * 9;
        inventoryType = type;

        slots = new MenuItem[inventorySize];
    }

    /**
     * Opens the menu for a {@link Player}.
     *
     * @param player the {@link Player} to open the menu for
     * @throws IllegalArgumentException if player is null
     */
    public void open(@NonNull Player player) {
        this.player = player;
        if (inventoryType == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(this, inventorySize, inventoryTitle);
        } else {
            inventory = Bukkit.createInventory(this, inventoryType, inventoryTitle);
        }
        draw();
        player.openInventory(inventory);
    }

    /**
     * Sets the {@link MenuItem} at a slot. If the given {@link MenuItem} is null, the slot will be cleared.
     *
     * @param slot the slot
     * @param item the item
     * @throws IllegalArgumentException if slot is outside the bounds of the inventory
     */
    public void setItem(int slot, MenuItem item) {
        validateSlot(slot);
        slots[slot] = item;
        inventory.setItem(slot, item.itemStack());
    }

    /**
     * Adds a {@link MenuItem} to the next available slot in the menu.
     *
     * @param item the {@link MenuItem} to add
     * @return true if the {@link MenuItem} was successfully added, false otherwise (if the menu is full)
     */
    public boolean addItem(@NonNull MenuItem item) {
        int next = getNextEmptySlot();
        if (next == -1) {
            return false;
        }
        setItem(next, item);
        return true;
    }

    /**
     * Adds {@link MenuItem}s to the next available slots in the menu.
     *
     * @param items the {@link MenuItem}s to add
     * @return true if the {@link MenuItem}s were successfully added, false otherwise (if the menu is full)
     */
    public boolean addItems(@NonNull MenuItem... items) {
        boolean success = true;
        for (MenuItem item : items) {
            success &= addItem(item);
        }
        return success;
    }

    /**
     * Removes the {@link MenuItem} at a slot from the menu.
     *
     * @param slot the slot
     * @throws IllegalArgumentException if slot is outside the bounds of the inventory
     */
    public void removeItem(int slot) {
        validateSlot(slot);
        slots[slot] = null;
        inventory.setItem(slot, null);
    }

    /**
     * Checks whether there is a {@link MenuItem} at a slot in the menu.
     *
     * @param slot the slot
     * @return true if there is a {@link MenuItem} at the slot, false otherwise
     * @throws IllegalArgumentException if slot is outside the bounds of the inventory
     */
    public boolean hasItem(int slot) {
        validateSlot(slot);
        return slots[slot] != null;
    }

    /**
     * Returns the {@link MenuItem} at a slot in the menu.
     *
     * @param slot the slot
     * @return the {@link MenuItem} at the slot
     * @throws IllegalArgumentException if slot is outside the bounds of the inventory
     */
    public MenuItem getItem(int slot) {
        validateSlot(slot);
        return slots[slot];
    }

    /**
     * Fills the entire inventory with a {@link MenuItem}.
     *
     * @param item the {@link MenuItem} to fill the menu with
     */
    public void fill(@NonNull MenuItem item) {
        for (int i = 0; i < inventorySize; i++) {
            setItem(i, item);
        }
    }

    /**
     * Adds a {@link MenuItem} to the first available "key" slot in the {@link MenuPattern}. The pattern given must be
     * compatible with the current menu.
     * <p/>
     * This method will only fill the next non-accepted "key" character in the pattern. If you want to fill the entire
     * pattern, use {@link #fillPattern(MenuPattern, MenuItem)}.
     * <p/>
     * If the given item is null, then the next non-accepted "key" character will be cleared.
     *
     * @param pattern the {@link MenuPattern}
     * @param item    the menu item to add
     * @return true if the {@link MenuItem} was successfully added, false otherwise (if the pattern is full)
     * @throws IllegalArgumentException if the pattern is not compatible with the menu
     * @throws IllegalStateException    if all slots in the pattern have already been accepted
     */
    public boolean addInPattern(@NonNull MenuPattern pattern, @NonNull MenuItem item) {
        validatePattern(pattern);
        int[] keys = pattern.getKeyIndexes();
        for (int key : keys) {
            if (pattern.isAccepted(key)) continue;
            setItem(key, item);
            pattern.addAcceptedSlot(key);
            return true;
        }
        return false;
    }

    /**
     * Fills all "key" characters in a {@link MenuPattern} with a {@link MenuItem}. The pattern given must be compatible
     * with the current menu.
     * <p/>
     * All slots in the pattern that are "key" characters will be filled with the given {@link MenuItem}, regardless of
     * their acceptance status.
     * <p/>
     * If the given item is null, then all "key" characters in the pattern will be cleared.
     *
     * @param pattern the {@link MenuPattern}
     * @param item    the menu item to use
     * @throws IllegalArgumentException if the pattern is not compatible with the menu
     */
    public void fillPattern(@NonNull MenuPattern pattern, MenuItem item) {
        validatePattern(pattern);
        pattern.getAcceptedSlots().clear();
        int[] keys = pattern.getKeyIndexes();
        for (int key : keys) {
            setItem(key, item);
            pattern.addAcceptedSlot(key);
        }
    }

    /**
     * Gets the index of the next empty slot in the menu.
     *
     * @return the index of the next empty slot, or -1 if the menu is full
     */
    public int getNextEmptySlot() {
        for (int i = 0; i < inventorySize; i++) {
            if (inventory.getItem(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private void validateSlot(int slot) {
        if (inventoryType == InventoryType.CHEST) {
            if (slot < 0 || slot >= inventorySize) {
                throw new IllegalArgumentException("Invalid slot " + slot + " for chest inventory of size " + inventorySize);
            }
        } else {
            if (slot < 0 || slot >= inventoryType.getDefaultSize()) {
                throw new IllegalArgumentException("Invalid slot " + slot + " for inventory of type " + inventoryType + ". Slot must be between 0 and " + inventoryType.getDefaultSize());
            }
        }
    }

    private void validatePattern(MenuPattern pattern) {
        if (pattern.getPattern().size() * 9 > inventorySize)
            throw new IllegalArgumentException("Pattern is too large for this inventory. Expected " + (inventorySize / 9) + " rows, pattern contains " + pattern.getPattern().size() + " rows.");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the {@link Player} who opened the menu.
     *
     * @return the player
     */
    public Player getPlayer() {
        if (player == null) {
            throw new IllegalStateException("Player is null because the menu has not been opened yet.");
        }
        return player;
    }

}
