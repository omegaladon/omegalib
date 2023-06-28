package me.omega.omegalib.item.head;

import java.util.List;

public record HeadCategory(Category category, List<HeadEntry> entries) {

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

        public String getIdentifier() {
            return name().replace("_", "-").toLowerCase();
        }

    }

}
