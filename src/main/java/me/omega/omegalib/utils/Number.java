package me.omega.omegalib.utils;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;

/**
 * A utility class for working with number-related operations.
 */
@UtilityClass
public class Number {

    /**
     * Returns a formatted string representation of the given number.
     * <p/>
     * If the number is less than 1000, it is returned as a string. Otherwise, the number is abbreviated with a suffix
     * indicating the order of magnitude (e.g. K, M, B, T, etc.).
     *
     * @param number the number to format
     * @return the formatted string representation of the given number
     */
    public static String formatted(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        String[] suffixes = {" ", "K", "M", "B", "T", "Q", "Qn", "Sx", "Sp", "Oc", "No", "De", "UnD", "DoD", "TrD",
                             "QaD", "QiD", "SxD", "SpD", "OcD", "NoD"};
        int exp = (int) (Math.log10(number) / 3);
        String suffix = suffixes[exp];
        double value = number / Math.pow(10, exp * 3);
        return String.format("%.2f%s", value, suffix);
    }

    /**
     * Returns a pretty formatted string representation of the given double number.
     * <p/>
     * Uses the default number format for the current locale to format the number with thousands separators.
     *
     * @param number the double number to format
     * @return the pretty formatted string representation of the given double number
     */
    public static String pretty(double number) {
        return NumberFormat.getNumberInstance().format(number);
    }

}
