package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;

public class DoubleParser extends ArgumentParser<Double> {

    @Override
    public Double parse(String input) throws Exception {
        return Double.valueOf(input);
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }

}
