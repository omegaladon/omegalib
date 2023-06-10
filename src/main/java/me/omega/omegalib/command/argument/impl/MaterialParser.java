package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;
import org.bukkit.Material;

public class MaterialParser extends ArgumentParser<Material> {

    @Override
    public Material parse(String input) {
        return Material.getMaterial(input.toLowerCase());
    }

    @Override
    public Class<?> getType() {
        return Material.class;
    }

}
