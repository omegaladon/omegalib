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

    public MenuPattern(char key, char empty) {
        if (key == empty)
            throw new IllegalArgumentException("Key and empty characters must be different.");
        this.key = key;
        this.empty = empty;
    }

    public MenuPattern() {
        this('1', '0');
    }

    public void addAcceptedSlot(int slot) {
        acceptedSlots.add(slot);
    }

    public boolean isAccepted(int slot) {
        return acceptedSlots.contains(slot);
    }

    /**
     * Adds a row to the pattern.
     *
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
     * Adds a full line of empty characters.
     */
    public MenuPattern empty() {
        if (pattern.size() == 6) {
            throw new IllegalArgumentException("Pattern cannot contain more than 6 rows.");
        }
        char[] chars = new char[9];
        for (int i = 0; i < 9; i++) {
            chars[i] = empty;
        }
        pattern.add(chars);
        return this;
    }

    /**
     * Adds a full line of key characters.
     */
    public MenuPattern full() {
        if (pattern.size() == 6) {
            throw new IllegalArgumentException("Pattern cannot contain more than 6 rows.");
        }
        char[] chars = new char[9];
        for (int i = 0; i < 9; i++) {
            chars[i] = key;
        }
        pattern.add(chars);
        return this;
    }

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
