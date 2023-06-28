package me.omega.omegalib;

import com.google.gson.Gson;
import lombok.Getter;
import me.omega.omegalib.item.head.Heads;
import me.omega.omegalib.menu.MenuListener;
import me.omega.omegalib.scheduling.Scheduler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class OmegaLib extends JavaPlugin {

    private static final Gson GSON = new Gson();

    private static OmegaLib INSTANCE;

    @Override
    public void onEnable() {

        Scheduler.init(this);

        INSTANCE = this;

        registerListeners();

        getLogger().info("Loading heads from database...");
        Heads.load();
        getLogger().info("Loaded " + Heads.getCategories().size() + " categories with " + Heads.getHeads().size() + " heads!");
        getLogger().info("omegalib has finished loading!");
    }

    @Override
    public void onDisable() {
        Scheduler.cancelAll();
        getLogger().info("omegalib disabled!");
    }

    public void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new MenuListener(), this);
    }

    public static OmegaLib getInstance() {
        return INSTANCE;
    }

    public static Gson getGson() {
        return GSON;
    }


}
