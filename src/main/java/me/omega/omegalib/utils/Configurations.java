package me.omega.omegalib.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import me.omega.omegalib.plugin.AbstractJavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Configurations {

    private final AbstractJavaPlugin plugin;
    private final Map<String, YamlDocument> configMap;

    public Configurations(AbstractJavaPlugin plugin) {
        this.plugin = plugin;
        this.configMap = new HashMap<>();
    }

    public YamlDocument get(String resourceName) {
        return configMap.get(resourceName);
    }

    public void add(String resourceName) {
        try {
            configMap.put(resourceName, YamlDocument.create(new File(plugin.getDataFolder() +"/" + resourceName + ".yml"),
                                                            plugin.getResource(resourceName + ".yml"),
                                                            GeneralSettings.builder().setKeyFormat(GeneralSettings.KeyFormat.OBJECT).build(),
                                                            LoaderSettings.builder().setAutoUpdate(true).build(),
                                                            DumperSettings.DEFAULT,
                                                            UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build()));
            configMap.get(resourceName).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String resourceName) {
        if (configMap.containsKey(resourceName)) {
            try {
                configMap.get(resourceName).save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void reload(String resourceName) {
        if (configMap.containsKey(resourceName)) {
            try {
                configMap.get(resourceName).reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
