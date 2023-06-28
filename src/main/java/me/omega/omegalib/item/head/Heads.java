package me.omega.omegalib.item.head;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.omega.omegalib.OmegaLib;
import me.omega.omegalib.utils.Http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Heads {

    private static final String API_URL = "https://minecraft-heads.com/scripts/api.php";

    private static final List<HeadCategory> CATEGORIES = new ArrayList<>();
    private static final Map<Integer, HeadEntry> HEADS = new HashMap<>();

    private static boolean LOADED = false;
    private static boolean DEBUG = false;

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * Loads all head entries from the minecraft-heads.com API. If already loaded, this method does nothing.
     */
    @SneakyThrows
    public static void load() {

        if (LOADED) {
            return;
        }

        for (HeadCategory.Category headCategory : HeadCategory.Category.values()) {
            HttpURLConnection connection =
                    (HttpURLConnection) new URL(API_URL + "?cat=" + headCategory.getIdentifier() + "&tags=true&ids" +
                                                "=true").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                Optional<Object> values = Http.getUrlContent(API_URL + "?cat=" + headCategory.getIdentifier() +
                                                             "&tags=true&ids=true", true);
                if (values.isEmpty()) {
                    continue;
                }
                List<JsonObject> headValues = (List<JsonObject>) values.get();
                List<HeadEntry> headEntries = new ArrayList<>();
                for (JsonObject value : headValues) {
                    if (DEBUG) OmegaLib.getInstance().getLogger().info(value.toString());
                    HeadEntry headEntry = new HeadEntry(
                            value.get("name").getAsString(),
                            UUID.fromString(value.get("uuid").getAsString()),
                            value.get("value").getAsString(),
                            List.of(value.get("tags").getAsString().split(",")),
                            value.get("id").getAsInt()
                    );
                    headEntries.add(headEntry);
                    HEADS.put(headEntry.id(), headEntry);
                }
                CATEGORIES.add(new HeadCategory(headCategory, headEntries));
            }

            connection.disconnect();
        }
        LOADED = true;

    }

    public static Optional<HeadEntry> getHeadEntry(int id) {
        return Optional.ofNullable(HEADS.get(id));
    }

    public static List<HeadCategory> getCategories() {
        return CATEGORIES;
    }

    public static List<HeadEntry> getHeads() {
        return new ArrayList<>(HEADS.values());
    }

    public static boolean isLoaded() {
        return LOADED;
    }

}
