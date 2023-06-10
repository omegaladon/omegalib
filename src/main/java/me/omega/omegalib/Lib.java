package me.omega.omegalib;

import com.google.gson.Gson;
import lombok.Getter;
import me.omega.omegalib.item.head.Heads;
import me.omega.omegalib.plugin.AbstractJavaPlugin;
import me.omega.omegalib.scheduling.Scheduler;

@Getter
public class Lib extends AbstractJavaPlugin {

    private static final Gson GSON = new Gson();

    private static Lib INSTANCE;
    private static boolean finishedLoading;

    @Override
    public void enable() {

        getLogger().info("Initializing scheduler...");
        Scheduler.init(this);

        finishedLoading = false;
        INSTANCE = this;

        Scheduler.async().run(() -> {
            getLogger().info("Loading heads from database...");
            Heads.load();
            getLogger().info("Loaded " + Heads.getCategories().size() + " categories with " + Heads.getHeads().size() + " heads!");
        }).whenComplete((result, error) -> {
            finishedLoading = true;
            getLogger().info("omegalib has finished loading!");
        });
    }

    @Override
    public void disable() {
        Scheduler.cancelAll();
        getLogger().info("omegalib disabled!");
    }

    /**
     * Returns the instance of the current omegalib plugin, which provides a set of utility classes and methods for use
     * in developing Minecraft server plugins. The method should only be used in the omegalib plugin itself.
     *
     * @return the instance of the current omegalib plugin
     */
    public static Lib getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the current Gson instance being used to deserialize JSON responses.
     *
     * @return the Gson instance
     */
    public static Gson getGson() {
        return GSON;
    }


}
