package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;

import java.util.ArrayList;
import java.util.List;

public class IntsCompletion extends TabCompletion {

    public IntsCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        int min;
        int max;
        try {
            min = Integer.parseInt(getValue().split("-")[0]);
            max = Integer.parseInt(getValue().split("-")[1]);
            if (min > max) {
                throw new IllegalArgumentException("The minimum value of an integer range must be smaller than the " +
                                                   "maximum value.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Values on the endpoints of an integer range must be a valid integers.");
        }

        List<String> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

}
