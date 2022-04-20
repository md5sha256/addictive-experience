package io.github.md5sha256.addictiveexperience.implementation;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
public final class SlurEffectStateImpl implements SlurEffectState {

    private final Map<UUID, SlurData> expiryMap = new ConcurrentHashMap<>();
    @Inject
    private DrugRegistry drugRegistry;

    @Override
    public void registerSlur(@NotNull UUID player, @NotNull ISlurEffect effect, long duration, @NotNull TimeUnit timeUnit) {
        final long durationMillis = timeUnit.toMillis(duration);
        if (durationMillis == 0) {
            return;
        }
        final long targetEpochMillis = System.currentTimeMillis() + durationMillis;
        this.expiryMap.put(player, new SlurData(effect, targetEpochMillis));
    }

    @Override
    public void registerSlur(@NotNull UUID player, @NotNull IDrug drug) {
        @NotNull Optional<DrugMeta> optionalDrugMeta = this.drugRegistry.metaData(drug, DrugMeta.KEY);
        if (!optionalDrugMeta.isPresent()) {
            throw new IllegalStateException("Failed to get DrugMeta for drug: " + drug.key());
        }
        final DrugMeta meta = optionalDrugMeta.get();
        final Optional<ISlurEffect> optionalEffect = meta.slurEffect();
        if (!optionalEffect.isPresent()) {
            return;
        }
        final ISlurEffect effect = optionalEffect.get();
        registerSlur(player, effect, meta.slurDurationMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void clearSlur(@NotNull UUID player) {
        this.expiryMap.remove(player);
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        this.expiryMap.values().removeIf(slurData -> now >= slurData.expiryEpochMillis);
    }

    @Override
    public void update(@NotNull UUID player) {
        update0(player);
    }

    private @NotNull Optional<@NotNull SlurData> update0(@NotNull UUID player) {
        long now = System.currentTimeMillis();
        final SlurData data = this.expiryMap.get(player);
        if (data != null && now >= data.expiryEpochMillis) {
            this.expiryMap.remove(player);
            return Optional.empty();
        }
        return Optional.ofNullable(data);
    }

    @Override
    public @NotNull Optional<@NotNull ISlurEffect> currentSlurEffect(@NotNull UUID player) {
        return Optional.ofNullable(this.expiryMap.get(player)).map(slurData -> slurData.slurEffect);
    }

    @Override
    public long remainingTimeMillis(@NotNull UUID player) {
        return update0(player).map(data -> System.currentTimeMillis() - data.expiryEpochMillis).orElse(0L);
    }

    @Override
    public long remainingTime(@NotNull UUID player, @NotNull TimeUnit timeUnit) {
        return timeUnit.convert(remainingTimeMillis(player), TimeUnit.MILLISECONDS);
    }

    private static class SlurData {

        final @NotNull ISlurEffect slurEffect;
        final long expiryEpochMillis;

        SlurData(@NotNull ISlurEffect effect, long expiryEpochMillis) {
            this.slurEffect = effect;
            this.expiryEpochMillis = expiryEpochMillis;
        }
    }

}
