package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;

public class IntParser extends ArgumentParser<Integer> {

    @Override
    public Integer parse(String input) {
        return Integer.valueOf(input);
    }

    @Override
    public Class<?> getType() {
        return Integer.class;
    }

}
