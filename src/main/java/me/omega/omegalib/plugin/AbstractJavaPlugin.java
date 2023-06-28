package me.omega.omegalib.plugin;

import co.aikar.commands.PaperCommandManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import me.omega.omegalib.utils.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class AbstractJavaPlugin extends JavaPlugin {

    private PaperCommandManager commandManager;
    private Configurations configurations;

    /**
     * Initialize the database codec, pojo providers, and anything else needed to be run before database initialization.
     */
    public abstract void initializeDatabase();
    /**
     * Is called after abstract plugin runs enable code. Put stuff like accessing DB here.
     */
    public abstract void enable();
    public abstract void disable();

    /**
     * Register ACF commands and tab completions here.
     */
    public abstract void registerCommands();

    public YamlDocument getConfig(String resourceName) {
        return configurations.get(resourceName);
    }

    @Override
    public void onEnable() {
        configurations = new Configurations(this);
        initializeDatabase();
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.enableUnstableAPI("help");
        enable();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        disable();
    }

}
