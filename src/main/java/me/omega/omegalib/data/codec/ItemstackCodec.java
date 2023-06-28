package me.omega.omegalib.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.inventory.ItemStack;

public class ItemstackCodec implements Codec<ItemStack> {

    @Override
    public ItemStack decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return ItemstackSerializer.deserialize(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, ItemStack itemstack, EncoderContext encoderContext) {
        bsonWriter.writeString(ItemstackSerializer.serialize(itemstack));
    }

    @Override
    public Class<ItemStack> getEncoderClass() {
        return ItemStack.class;
    }
}
