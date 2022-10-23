package io.github.md5sha256.addictiveexperience.util.configurate;

import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class AdventureKeySerializer extends ScalarSerializer<Key> {

    public AdventureKeySerializer() {
        super(Key.class);
    }

    @Override
    protected Object serialize(Key item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }

    @Override
    public Key deserialize(Type type, Object obj) {
        return Key.key(obj.toString());
    }
}
