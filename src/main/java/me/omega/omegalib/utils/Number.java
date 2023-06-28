package me.omega.omegalib.utils;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;

@UtilityClass
public class Number {

    public static String formatted(double number) {
        if (number < 1000) {
            return String.valueOf((int) Math.floor(number));
        }
        String[] suffixes = {" ", "K", "M", "B", "T", "Q", "Qn", "Sx", "Sp", "Oc", "No", "De", "UnD", "DoD", "TrD",
                             "QaD", "QiD", "SxD", "SpD", "OcD", "NoD"};
        int exp = (int) (Math.log10(number) / 3);
        String suffix = suffixes[exp];
        double value = number / Math.pow(10, exp * 3);
        return String.format("%.2f%s", value, suffix);
    }

    public static String pretty(double number) {
        return NumberFormat.getNumberInstance().format(number);
    }

    public static long roundToFive(double value) {
        int counter = 0;
        long temp = Math.round(value);
        while (temp / 10 >= 10) {
            counter++;
            temp /= 10;
        }
        double nearest = 5 * Math.pow(10, counter);
        return (long) (Math.ceil(value / nearest) * nearest);
    }

}
