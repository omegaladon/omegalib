package me.omega.omegalib.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Material;

public class MaterialCodec implements Codec<Material> {
    @Override
    public Material decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return Material.valueOf(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, Material material, EncoderContext encoderContext) {
        bsonWriter.writeString(material.name());
    }

    @Override
    public Class<Material> getEncoderClass() {
        return Material.class;
    }
}
