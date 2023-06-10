package me.omega.omegalib;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains a collection of {@link YamlDocument} associated with a name.
 */
public class Configurations implements Map<String, YamlDocument> {

    private final Map<String, YamlDocument> map = new HashMap<>();

    /**
     * Saves all the YAML documents in this collection to their respective files on disk.
     *
     * @return true if all documents were successfully saved, false otherwise
     */
    public boolean saveAll() {
        boolean success = true;
        for (YamlDocument document : map.values()) {
            try {
                document.save();
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * Saves a single YAML document with the specified name to its file on disk.
     *
     * @param name the name of the document to save
     * @return true if the document was successfully saved, false otherwise
     */
    public boolean save(@NonNull String name) {
        try {
            return map.get(name).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(@NonNull Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(@NonNull Object value) {
        return map.containsValue(value);
    }

    @Override
    public YamlDocument get(@NonNull Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public YamlDocument put(@NonNull String key, @NonNull YamlDocument value) {
        return map.put(key, value);
    }

    @Override
    public YamlDocument remove(@NonNull Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends YamlDocument> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<YamlDocument> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, YamlDocument>> entrySet() {
        return map.entrySet();
    }

}

