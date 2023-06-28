package me.omega.omegalib.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import lombok.Getter;
import me.omega.omegalib.plugin.AbstractJavaPlugin;
import org.bson.Document;

import java.util.*;

@Getter
public abstract class StaticDataManager<K, V> {

    public abstract void initialize();
    public abstract Class<V> getClazz();
    public abstract void postLoad();
    public abstract K getKey(V v);

    private final AbstractJavaPlugin plugin;
    private final Map<K, V> data;
    private final MongoCollection<V> collection;

    public StaticDataManager(AbstractJavaPlugin plugin, String collection, MongoUtil mongo) {
        this.plugin = plugin;
        this.data = new HashMap<>();
        initialize();
        this.collection = mongo.getDatabase().getCollection(
                collection,
                getClazz()
                                                            );
        load();
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(data.get(key));
    }

    public void load() {
        FindIterable<V> findIterable = collection.find();
        Map<K, V> tempData = new HashMap<>();
        for (V v : findIterable) {
            tempData.put(getKey(v), v);
        }
        data.clear();
        data.putAll(tempData);
        postLoad();
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