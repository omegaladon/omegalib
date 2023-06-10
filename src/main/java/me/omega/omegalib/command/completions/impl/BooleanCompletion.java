package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;

import java.util.Arrays;
import java.util.List;

public class BooleanCompletion extends TabCompletion {

    public BooleanCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        return Arrays.asList("true", "false");
    }

}
