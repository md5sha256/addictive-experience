package io.github.md5sha256.addictiveexperience.util.configurate;

import org.bukkit.Server;
import org.bukkit.World;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Predicate;

public class WorldSerializer extends ScalarSerializer<World> {

    private final Server server;

    public WorldSerializer(Server server) {
        super(World.class);
        this.server = server;
    }

    private World getOrThrow(UUID uuid) throws SerializationException {
        World world = this.server.getWorld(uuid);
        if (world == null) {
            throw new SerializationException("World not found: " + uuid);
        }
        return world;
    }

    @Override
    public World deserialize(Type type, Object obj) throws SerializationException {
        if (obj instanceof UUID uuid) {
            return getOrThrow(uuid);
        } else if (obj instanceof String s) {
            try {
                return getOrThrow(UUID.fromString(s));
            } catch (IllegalArgumentException ex) {
                throw new SerializationException(ex);
            }
        }
        throw new SerializationException("Unsupported type: " + type);
    }

    @Override
    protected Object serialize(World item, Predicate<Class<?>> typeSupported) {
        if (typeSupported.test(UUID.class)) {
            return item.getUID();
        } else {
            return item.getUID().toString();
        }
    }
}
