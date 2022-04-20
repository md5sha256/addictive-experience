package io.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageRegistryImpl implements MessageRegistry {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<MessageKey, Message> messages = new HashMap<>();

    MessageRegistryImpl() {

    }

    @Override
    public @NotNull Message message(@NotNull MessageKey key) {
        this.lock.readLock().lock();
        try {
            return this.messages.getOrDefault(key, Message.empty(key));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @NotNull Message message(@NotNull MessageKey key, @NotNull Component value) {
        this.lock.writeLock().lock();
        try {
            return this.messages.computeIfAbsent(key, (unused) -> Message.create(key, value));
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void message(@NotNull Map<MessageKey, Message> messages) {
        this.lock.writeLock().lock();
        try {
            this.messages.clear();
            this.messages.putAll(messages);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void message(@NotNull Message message) {
        this.lock.writeLock().lock();
        try {
            this.messages.put(message.key(), message);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
