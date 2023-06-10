package me.omega.omegalib.plugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import lombok.NonNull;
import me.omega.omegalib.Configurations;
import me.omega.omegalib.command.CommandRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class AbstractJavaPlugin extends JavaPlugin {

    private final CommandRegistry commandRegistry = new CommandRegistry(this);

    private final Configurations configs = new Configurations();

    public abstract void enable();

    public abstract void disable();

    /**
     * Registers a new configuration file, accessible via {@link #getConfigs()}.
     *
     * @param versioningName the name of the versioning field in the config
     * @param resourceNames  the name(s) of the configuration file
     * @throws IOException              if the configuration file could not be created
     * @throws IllegalArgumentException if the resource does not exist
     */
    public void registerConfig(@NonNull String versioningName, @NonNull String... resourceNames) throws IllegalArgumentException, IOException {
        for (String name : resourceNames) {
            if (getResource(name + ".yml") == null)
                throw new IllegalArgumentException("The resource " + name + ".yml does not exist.");

            configs.put(name, YamlDocument.create(
                    new File(getDataFolder(), name + ".yml"),
                    getResource(name + ".yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning(versioningName)).build()
            ));
        }
    }

    /**
     * Registers a new listener.
     *
     * @param listeners the listener(s) to register
     */
    public void registerListeners(@NonNull Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        enable();
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        disable();
    }

}
