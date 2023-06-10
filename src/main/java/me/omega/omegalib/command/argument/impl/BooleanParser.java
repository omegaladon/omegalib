package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;

public class BooleanParser extends ArgumentParser<Boolean> {

    @Override
    public Boolean parse(String input) {
        return Boolean.parseBoolean(input.toLowerCase());
    }

    @Override
    public Class<?> getType() {
        return Boolean.class;
    }

}
