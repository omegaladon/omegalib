package me.omega.omegalib.menu;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a menu pattern for a {@link Menu}. The pattern consists of 1-6 rows of 9 characters each. The
 * characters can either be a key character or an empty character.
 * <p/>
 * You can fill the pattern with the method fillPattern(MenuPattern, MenuItem) in {@link Menu}. All key characters will
 * be filled with the given MenuItem, and all empty characters will be skipped.
 */
@Data
public class MenuPattern {

    private final char key;
    private final char empty;
    private final List<char[]> pattern = new ArrayList<>();
    private final List<Integer> acceptedSlots = new ArrayList<>();

    /**
     * Constructs a new MenuPattern with the given key and empty characters.
     *
     * @param key   the key character
     * @param empty the empty character
     * @throws IllegalArgumentException if key and empty characters are the same
     */
    public MenuPattern(char key, char empty) {
        if (key == empty)
            throw new IllegalArgumentException("Key and empty characters must be different.");
        this.key = key;
        this.empty = empty;
    }

    /**
     * Constructs a new MenuPattern with default key and empty characters.
     * <p/>
     * By default, the key character is '1' and the empty character is '0'.
     */
    public MenuPattern() {
        this('1', '0');
    }

    /**
     * Adds an accepted slot to the list of already accepted slots. If a slot is accepted, it means that it has been
     * filled with an item.
     *
     * @param slot the index of the slot to add
     */
    public void addAcceptedSlot(int slot) {
        acceptedSlots.add(slot);
    }

    /**
     * Checks if a slot is an accepted slot. If a slot is accepted, it means that it has been filled with an item.
     *
     * @param slot the index of the slot to check
     * @return true if the slot is an accepted slot, false otherwise
     */
    public boolean isAccepted(int slot) {
        return acceptedSlots.contains(slot);
    }

    /**
     * Adds a row to the pattern.
     *
     * @param input the row to add, represented as a string
     * @return this MenuPattern instance, for chaining
     * @throws IllegalArgumentException if the row is not 9 characters long, contains characters other than the key and
     *                                  empty, or if the pattern already contains 6 rows
     */
    public MenuPattern add(@NonNull String input) {
        if (pattern.size() == 6) {
            throw new IllegalArgumentException("Pattern cannot contain more than 6 rows.");
        }
        char[] chars = input.toCharArray();
        if (chars.length != 9) {
            throw new IllegalArgumentException("Pattern must be 9 characters long.");
        }
        for (char c : chars) {
            if (c != key && c != empty) {
                throw new IllegalArgumentException("Pattern must only contain the key and empty characters.");
            }
        }
        pattern.add(chars);
        return this;
    }

    /**
     * Gets the number of key characters in the pattern.
     *
     * @return the number of key characters
     */
    public int getKeyAmount() {
        int count = 0;
        for (char[] chars : pattern) {
            for (char c : chars) {
                if (c == key) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets the indexes of the key characters in the pattern.
     *
     * @return an array of indexes of the key characters
     */
    public int[] getKeyIndexes() {
        int[] indexes = new int[getKeyAmount()];
        int index = 0;
        for (int j = 0; j < pattern.size(); j++) {
            char[] chars = pattern.get(j);
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == key) {
                    indexes[index] = j * 9 + i;
                    index++;
                }
            }
        }
        return indexes;
    }

}
