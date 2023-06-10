package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;

import java.util.Arrays;
import java.util.List;

public class StringsCompletion extends TabCompletion {

    public StringsCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        return Arrays.asList(getValue().split(","));
    }

}
