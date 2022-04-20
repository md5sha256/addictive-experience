package io.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MessageRegistrySerializer extends ScalarSerializer<MessageRegistry> {

    public MessageRegistrySerializer() {
        super(MessageRegistry.class);
    }

    private static MessageKey resolveMessageKey(@NotNull Object object) {
        if (object instanceof MessageKey) {
            return (MessageKey) object;
        }
        return object::toString;
    }

    @Override
    public MessageRegistry deserialize(Type type, Object obj) {
        final Map<MessageKey, Message> messageMap = new HashMap<>();
        if (obj instanceof ConfigurationNode) {
            final ConfigurationNode root = (ConfigurationNode) obj;
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : root.childrenMap().entrySet()) {
                final MessageKey key = resolveMessageKey(entry.getKey());
                final ConfigurationNode node = entry.getValue();
                try {
                    final Component component = node.get(Component.class);
                    if (component != null) {
                        messageMap.put(key, Message.create(key, component));
                    }
                } catch (SerializationException ignored) {
                    //FIXME log error
                }
            }
        }
        final MessageRegistryImpl registry = new MessageRegistryImpl();
        registry.message(messageMap);
        return registry;
    }

    @Override
    protected Object serialize(MessageRegistry item, Predicate<Class<?>> typeSupported) {
        return null;
    }
}
