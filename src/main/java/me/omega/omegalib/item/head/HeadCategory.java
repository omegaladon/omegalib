package me.omega.omegalib.item.head;

import java.util.List;

/**
 * A record representing a head category from the minecraft-heads.com API.
 *
 * @param category the category
 * @param entries  a list of head entries
 */
public record HeadCategory(Category category, List<HeadEntry> entries) {

    /**
     * An enum representing all available categories from the minecraft-heads.com API.
     */
    public enum Category {

        ALPHABET,
        ANIMALS,
        BLOCKS,
        DECORATION,
        FOOD_DRINKS,
        HUMANS,
        HUMANOID,
        MISCELLANEOUS,
        MONSTERS,
        PLANTS;

        /**
         * Returns the identifier of the category.
         *
         * @return the identifier
         */
        public String getIdentifier() {
            return name().replace("_", "-").toLowerCase();
        }

    }

}
