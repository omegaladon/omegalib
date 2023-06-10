package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialsCompletion extends TabCompletion {

    public MaterialsCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        return Arrays.stream(org.bukkit.Material.values()).map(org.bukkit.Material::name).collect(Collectors.toList());
    }

}
