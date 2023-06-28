package me.omega.omegalib.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDMapPropertyCodecProvider implements PropertyCodecProvider {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> Codec<T> get(final TypeWithTypeParameters<T> type, final PropertyCodecRegistry registry) {
        if (Map.class.isAssignableFrom(type.getType()) && type.getTypeParameters().size() == 2
            && UUID.class.isAssignableFrom(type.getTypeParameters().get(0).getType())) {
            Codec<?> valueCodec = registry.get(type.getTypeParameters().get(1));
            return new UUIDMapCodec(type.getType(), valueCodec);
        } else {
            return null;
        }
    }

    private static final class UUIDMapCodec<T> implements Codec<Map<UUID, T>> {
        private final Class<Map<UUID, T>> encoderClass;
        private final Codec<T> codec;

        private UUIDMapCodec(final Class<Map<UUID, T>> encoderClass, final Codec<T> codec) {
            this.encoderClass = encoderClass;
            this.codec = codec;
        }

        @Override
        public void encode(final BsonWriter writer, final Map<UUID, T> map, final EncoderContext encoderContext) {
            writer.writeStartDocument();
            writer.writeInt32("size", map.size());

            writer.writeStartArray("entries");
            for (Map.Entry<UUID, T> entry : map.entrySet()) {
                writer.writeStartDocument();
                writer.writeString("key", entry.getKey().toString());
                writer.writeName("value");
                codec.encode(writer, entry.getValue(), encoderContext);
                writer.writeEndDocument();
            }
            writer.writeEndArray();
            writer.writeEndDocument();
        }

        @Override
        public Map<UUID, T> decode(final BsonReader reader, final DecoderContext context) {
            reader.readStartDocument();
            int size = reader.readInt32("size");

            Map<UUID, T> resultMap = new HashMap<>(size);
            reader.readStartArray();
            for (int i = 0; i < size; i++) {
                reader.readStartDocument();
                UUID key = UUID.fromString(reader.readString("key"));
                reader.readName("value");
                T value = codec.decode(reader, context);
                resultMap.put(key, value);
                reader.readEndDocument();
            }
            reader.readEndArray();
            reader.readEndDocument();

            return resultMap;
        }

        @Override
        public Class<Map<UUID, T>> getEncoderClass() {
            return encoderClass;
        }
    }

}
