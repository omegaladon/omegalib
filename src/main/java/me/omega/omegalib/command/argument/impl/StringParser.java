package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;

public class StringParser extends ArgumentParser<String> {

    @Override
    public String parse(String input) {
        return input;
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

}
