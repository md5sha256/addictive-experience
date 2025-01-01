package io.github.md5sha256.addictiveexperience.util.configurate;

import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

public class BlockPositionSerializer implements TypeSerializer<BlockPosition> {

    private static final String KEY_POSITION = "position";
    private static final String KEY_WORLD = "world";

    @Override
    public BlockPosition deserialize(Type type,
                                     ConfigurationNode node) throws SerializationException {
        ConfigurationNode position = node.node(KEY_POSITION);
        ConfigurationNode world = node.node(KEY_WORLD);
        World worldInstance = Objects.requireNonNull(world.get(World.class));
        return new BlockPosition(worldInstance, position.getLong());
    }

    @Override
    public void serialize(Type type,
                          @Nullable BlockPosition obj,
                          ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.removeChild(KEY_POSITION);
            node.removeChild(KEY_WORLD);
            return;
        }
        node.node(KEY_POSITION).set(obj.getPosition());
        node.node(KEY_WORLD).set(obj.getWorld());
    }


}
