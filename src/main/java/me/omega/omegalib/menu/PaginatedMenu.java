package me.omega.omegalib.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.omega.omegalib.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedMenu<T> extends Menu {

    private List<T> pageItems = new ArrayList<>();
    @Setter
    @Getter
    private int page;

    private Pair<Integer, MenuItem> pageUpItem;
    private Pair<Integer, MenuItem> pageDownItem;
    @Setter
    private boolean alwaysDisplayPageItems = true;

    /**
     * Creates a paginated menu starting at page 0 (first page).
     */
    public PaginatedMenu(String title, int size) {
        this(title, size, 0);
    }

    public PaginatedMenu(String title, int size, int page) {
        super(title, size);
        this.page = page;
    }

    /**
     * Gets the desired amount of items per page.
     */
    public abstract int getItemsPerPage();

    /**
     * Converts the parameterized type to a menu item suitable for being displayed in the menu.
     */
    public abstract MenuItem typeToItem(@NonNull T type);

    /**
     * Sets the items to be displayed in the menu. This should be a list of objects of the specified type.
     */
    public void setPageItems(@NonNull List<T> pageItems) {
        this.pageItems = pageItems;
    }

    /**
     * Sets the menu item for the page up action to the given item and slot.
     */
    public void setPageUpItem(int slot, @NonNull MenuItem item) {
        pageUpItem = new Pair<>(slot, item);
    }

    /**
     * Sets the menu item for the page down action to the given item and slot.
     */
    public void setPageDownItem(int slot, @NonNull MenuItem item) {
        pageDownItem = new Pair<>(slot, item);
    }

    /**
     * Returns the maximum number of pages that can be accessed in the paginated menu.
     */
    public int getMaxPages() {
        return (int) Math.ceil((double) pageItems.size() / getItemsPerPage());
    }

    /**
     * Fills the paginated menu with {@link MenuItem}s obtained from {@link #typeToItem(T)}, using the given pattern.
     * <p/>
     * This method should be placed in the {@link #draw()} method of the menu.
     *
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
            if (page + 1 < getMaxPages()) {
                setItem(pageUpItem.key(), pageUpItem.value());
            }
        } else {
            setItem(pageUpItem.key(), pageUpItem.value());
            setItem(pageDownItem.key(), pageDownItem.value());
        }
    }

}