package me.omega.omegalib.menu;

import lombok.NonNull;
import lombok.Setter;
import me.omega.omegalib.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class PaginatedMenu<T> extends Menu {

    private final List<T> pageItems = new ArrayList<>();
    private final int page;

    private Pair<Integer, MenuItem> pageUpItem;
    private Pair<Integer, MenuItem> pageDownItem;
    @Setter
    private boolean alwaysDisplayPageItems = true;

    /**
     * Creates a paginated menu with a title and size. Paginated menus are only compatible chest inventory types.
     * <p/>
     * Using this constructor will start the menu on page 0.
     *
     * @param title the title
     * @param size  the size
     */
    public PaginatedMenu(String title, int size) {
        this(title, size, 0);
    }

    /**
     * Creates a paginated menu with a title, size, and page. Paginated menus are only compatible chest inventory
     * types.
     *
     * @param title the title
     * @param size  the size
     * @param page  the page to start on
     */
    public PaginatedMenu(String title, int size, int page) {
        super(title, size);
        this.page = page;
    }

    /**
     * Gets the desired amount of items per page.
     *
     * @return the amount of items
     */
    public abstract int getItemsPerPage();

    /**
     * Converts the parameterized type to a menu item suitable for being displayed in the menu.
     *
     * @param type the type
     * @return a menu item
     */
    public abstract MenuItem typeToItem(@NonNull T type);

    /**
     * Sets the items to be displayed on the current page of the paginated menu to the given list of items.
     *
     * @param pageItems the list of items to set as the page items
     * @throws IllegalArgumentException if pageItems is greater than the maximum items per page
     */
    public void setPageItems(@NonNull List<T> pageItems) {
        if (pageItems.size() > getItemsPerPage())
            throw new IllegalArgumentException("Page items must be less than or equal to the maximum items per page. " +
                                               "(" + getItemsPerPage() + ")");
        this.pageItems.clear();
        this.pageItems.addAll(pageItems);
    }

    /**
     * Sets the menu item for the page up action to the given item and slot.
     *
     * @param slot the slot
     * @param item the menu item
     */
    public void setPageUpItem(int slot, @NonNull MenuItem item) {
        pageUpItem = new Pair<>(slot, item);
    }

    /**
     * Sets the menu item for the page down action to the given item and slot.
     *
     * @param slot the slot
     * @param item the menu item
     */
    public void setPageDownItem(int slot, @NonNull MenuItem item) {
        pageDownItem = new Pair<>(slot, item);
    }

    /**
     * Returns the maximum number of pages that can be accessed in the paginated menu.
     *
     * @return the maximum number of pages
     */
    public int getMaxPages() {
        return (int) Math.ceil((double) pageItems.size() / getItemsPerPage());
    }

    /**
     * Fills the paginated menu with {@link MenuItem}s obtained from {@link #typeToItem(T)}, using the given pattern.
     * <p/>
     * This method should be placed in the {@link #draw()} method of the menu.
     *
     * @param paginatedItemPattern the pattern to use for filling the menu
     * @throws NullPointerException     if the page up or page down items are null
     * @throws IllegalStateException    if the page items have not been set
     * @throws IllegalArgumentException if the amount of key slots in the pattern is less than the items per page, or if
     *                                  the pattern contains the page up or page down items
     */
    public void drawItems(@NonNull MenuPattern paginatedItemPattern) {

        if (pageItems.isEmpty()) {
            throw new IllegalStateException("Page items must be set.");
        }

        if (paginatedItemPattern.getKeyAmount() < getItemsPerPage()) {
            throw new IllegalArgumentException("Amount of key slots in pattern must be greater than or equal to the " +
                                               "items per page.");
        }

        if (pageUpItem == null || pageDownItem == null) {
            throw new NullPointerException("Page up and page down items must not be null.");
        }

        if ((Objects.equals(paginatedItemPattern.getKeyIndexes(), pageUpItem.key()))
            || Objects.equals(paginatedItemPattern.getKeyIndexes(), pageDownItem.key())) {
            throw new IllegalArgumentException("Page up and page down items must not be in the pattern.");
        }

        int itemsPerPage = getItemsPerPage();
        int startIndex = page * itemsPerPage;
        int endIndex = startIndex + itemsPerPage;
        if (endIndex > pageItems.size()) {
            endIndex = pageItems.size();
        }

        pageItems.subList(startIndex, endIndex).forEach(type -> {
            MenuItem item = typeToItem(type);
            int slot = paginatedItemPattern.getKeyIndexes()[pageItems.indexOf(type)];
            setItem(slot, item);
        });

        if (!alwaysDisplayPageItems) {
            if (page > 0) {
                setItem(pageDownItem.key(), pageDownItem.value());
            }
            if (page < getMaxPages()) {
                setItem(pageUpItem.key(), pageUpItem.value());
            }
        } else {
            setItem(pageUpItem.key(), pageUpItem.value());
            setItem(pageDownItem.key(), pageDownItem.value());
        }
    }

}