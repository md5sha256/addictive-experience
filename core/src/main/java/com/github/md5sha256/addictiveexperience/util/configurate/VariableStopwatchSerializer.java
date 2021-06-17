package com.github.md5sha256.addictiveexperience.util.configurate;

import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class VariableStopwatchSerializer implements TypeSerializer<VariableStopwatch> {

    private static final String KEY_NANOS = "elapsed-nanoseconds";
    private static final String KEY_RUNNING = "running";

    @Override
    public VariableStopwatch deserialize(final Type type,
                                         final ConfigurationNode node)
            throws SerializationException {
        final ConfigurationNode nodeNanos = node.node(KEY_NANOS);
        final ConfigurationNode nodeRunning = node.node(KEY_RUNNING);
        if (nodeNanos == null) {
            throw new SerializationException("Missing nanos");
        }
        if (nodeRunning == null) {
            throw new SerializationException("Missing running");
        }
        final long nanos = nodeNanos.getLong();
        final boolean isRunning = nodeRunning.getBoolean();
        final VariableStopwatch stopwatch;
        if (isRunning) {
            stopwatch = Stopwatches.variableStopwatch(GuavaAdapter.ofStarted());
        } else {
            stopwatch = Stopwatches.variableStopwatch(GuavaAdapter.ofUnstarted());
        }
        return stopwatch.setElapsedTime(nanos, TimeUnit.NANOSECONDS);
    }

    @Override
    public void serialize(final Type type,
                          @Nullable final VariableStopwatch stopwatch,
                          final ConfigurationNode node) throws SerializationException {
        if (stopwatch == null) {
            node.removeChild(KEY_NANOS);
            node.removeChild(KEY_RUNNING);
            return;
        }
        final long nanos = stopwatch.elapsedNanos();
        final boolean running = stopwatch.isRunning();
        node.node(KEY_NANOS, nanos);
        node.node(KEY_RUNNING, running);
    }
}
