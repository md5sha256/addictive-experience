package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugCooldownData;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DrugCooldownDataImpl implements DrugCooldownData {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<UUID, Map<IDrug, Collection<IDrugForm>>> map = new HashMap<>();

    public DrugCooldownDataImpl() {
    }

    @Override
    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.map.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void clear(@NotNull UUID player) {
        this.lock.writeLock().lock();
        try {
            this.map.remove(player);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void clear(@NotNull UUID player, @NotNull IDrug drug) {
        this.lock.writeLock().lock();
        try {
            final Map<IDrug, Collection<IDrugForm>> formMap = this.map.get(player);
            if (formMap != null) {
                formMap.remove(drug);
                if (formMap.isEmpty()) {
                    this.map.remove(player);
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void clear(@NotNull UUID player, @NotNull IDrugForm drugForm) {
        this.lock.writeLock().lock();
        try {
            final Map<IDrug, Collection<IDrugForm>> formMap = this.map.get(player);
            if (formMap == null) {
                return;
            }
            final Iterator<Map.Entry<IDrug, Collection<IDrugForm>>> iterator = formMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<IDrug, Collection<IDrugForm>> entry = iterator.next();
                final Collection<IDrugForm> forms = entry.getValue();
                forms.remove(drugForm);
                if (forms.isEmpty()) {
                    iterator.remove();
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm) {
        this.lock.readLock().lock();
        try {
            final Map<IDrug, Collection<IDrugForm>> formMap = this.map.get(player);
            if (formMap == null) {
                return false;
            }
            final Collection<IDrugForm> blocked = formMap.get(drug);
            return blocked != null && blocked.contains(drugForm);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void setBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm) {
        this.lock.writeLock().lock();
        try {
            final Map<IDrug, Collection<IDrugForm>> formMap = this.map.computeIfAbsent(player, (unused) -> new HashMap<>());
            formMap.computeIfAbsent(drug, (unused) -> new HashSet<>()).add(drugForm);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void setUnblocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm) {
        this.lock.writeLock().lock();
        try {
            final Map<IDrug, Collection<IDrugForm>> formMap = this.map.get(player);
            if (formMap != null) {
                final Collection<IDrugForm> forms = formMap.get(drug);
                if (forms != null) {
                    if (forms.size() == 1) {
                        // Remove the collection if it's empty
                        formMap.remove(drug);
                    } else {
                        forms.remove(drugForm);
                    }
                }
                if (formMap.isEmpty()) {
                    this.map.remove(player);
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }


}
