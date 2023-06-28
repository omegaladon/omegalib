package me.omega.omegalib.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import lombok.Getter;
import me.omega.omegalib.plugin.AbstractJavaPlugin;
import org.bson.Document;

import java.util.*;

@Getter
public abstract class DynamicDataManager<K, V extends DynamicData> {

    public abstract void initialize();
    public abstract Class<V> getClazz();
    public abstract void postLoad(K key, V value);
    public abstract V getNewValue(K key);
    public abstract boolean insertValueWhenMissing();

    private final AbstractJavaPlugin plugin;
    private final Map<K, V> data;
    private final MongoCollection<V> collection;

    public DynamicDataManager(AbstractJavaPlugin plugin, String collection, MongoUtil mongo) {
        this.plugin = plugin;
        this.data = new HashMap<>();
        initialize();
        this.collection = mongo.getDatabase().getCollection(
                collection,
                getClazz()
                                                            );
        this.collection.createIndex(new Document("_id", 1));
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(data.get(key));
    }

    public DatabaseLoadCallback<V> load(K key) {
        V value = collection.find(Filters.eq("_id", key)).first();
        if (value == null) {
            // Value is not present in database, player is new
            V newValue = getNewValue(key);
            if (insertValueWhenMissing()) {
                data.put(key, newValue);
                save(key);
                postLoad(key, value);
            }
            return new DatabaseLoadCallback<>(DatabaseLoadResult.NOT_FOUND, newValue);
        } else {
            // Value is present in database, returning player
            data.put(key, value);
            postLoad(key, value);
            return new DatabaseLoadCallback<>(DatabaseLoadResult.FOUND, value);
        }
    }

    public void unload(K key) {
        save(key);
        data.remove(key);
    }

    public void save(K key) {
        collection.replaceOne(Filters.eq("_id", key), data.get(key), Upsert.REPLACE_OPTIONS);
    }

    public void saveAll() {
        if (data.isEmpty()) return;
        List<WriteModel<V>> bulkOperations = new ArrayList<>();
        for (K key : data.keySet()) {
            WriteModel<V> upsertOperation = new UpdateOneModel<>(
                    Filters.eq("_id", key),
                    new Document("$set", data.get(key)),
                    Upsert.UPDATE_OPTIONS
            );
            bulkOperations.add(upsertOperation);
        }
        collection.bulkWrite(bulkOperations);
    }
}