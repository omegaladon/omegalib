package me.omega.omegalib.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Location;

public class LocationCodec implements Codec<Location> {

    @Override
    public Location decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return LocationSerializer.deserialize(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, Location location, EncoderContext encoderContext) {
        bsonWriter.writeString(LocationSerializer.serialize(location));
    }

    @Override
    public Class<Location> getEncoderClass() {
        return Location.class;
    }
}
