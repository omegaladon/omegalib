package me.omega.omegalib.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.omega.omegalib.data.codec.ItemstackCodec;
import me.omega.omegalib.data.codec.LocationCodec;
import me.omega.omegalib.data.codec.MaterialCodec;
import me.omega.omegalib.data.codec.UUIDMapPropertyCodecProvider;
import org.bson.UuidRepresentation;
import org.bson.codecs.Codec;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MongoUtil {

    private MongoClient mongo;
    private MongoDatabase database;
    private final List<Codec<?>> codecList;
    private final ArrayList<CodecProvider> codecProviderList;
    private final List<PropertyCodecProvider> propertyCodecProviderList;

    public MongoUtil() {
        this.codecList = new ArrayList<>();
        this.codecProviderList = new ArrayList<>();
        this.propertyCodecProviderList = new ArrayList<>();
    }

    public void connect(String connectionString, String databaseName) {
        PojoCodecProvider.Builder builder = PojoCodecProvider.builder().automatic(true);
        propertyCodecProviderList.add(new UUIDMapPropertyCodecProvider());
        for (PropertyCodecProvider codec : propertyCodecProviderList) {
            builder.register(codec);
        }
        CodecProvider pojoCodecProvider = builder.automatic(true).build();

        codecProviderList.add(new UuidCodecProvider(UuidRepresentation.STANDARD));

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(codecList),
                CodecRegistries.fromCodecs(new LocationCodec(), new ItemstackCodec(), new MaterialCodec()),
                CodecRegistries.fromProviders(pojoCodecProvider),
                CodecRegistries.fromProviders(codecProviderList));
        MongoClientSettings settings = MongoClientSettings.builder()
                                               .applyConnectionString(new ConnectionString(connectionString))
                                               .codecRegistry(pojoCodecRegistry)
                                               .uuidRepresentation(UuidRepresentation.STANDARD)
                                               .build();
        mongo = MongoClients.create(settings);
        database = mongo.getDatabase(databaseName);
    }

    public void registerCodec(Codec<?> codec) {
        codecList.add(codec);
    }

    public void registerCodecProvider(CodecProvider codec) {
        codecProviderList.add(codec);
    }

    public void registerPropertyCodecProvider(PropertyCodecProvider codec) {
        propertyCodecProviderList.add(codec);
    }

}
