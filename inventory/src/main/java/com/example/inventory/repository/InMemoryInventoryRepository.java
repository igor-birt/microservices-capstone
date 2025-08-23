package com.example.inventory.repository;

import com.example.inventory.model.Availability;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class InMemoryInventoryRepository implements InventoryRepository {

    private static final double DEFAULT_AVAILABILITY_RATIO = 0.75;

    private final Map<String, Availability> byUniq = new ConcurrentHashMap<>();

    public InMemoryInventoryRepository(final Collection<String> uniqIds,
                                       final Random random) {
        uniqIds.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(id -> {
                    boolean available = random.nextDouble() < DEFAULT_AVAILABILITY_RATIO;
                    byUniq.put(id, new Availability(id, available));
                });
    }

    public InMemoryInventoryRepository(final Collection<String> uniqIds) {
        this(uniqIds, ThreadLocalRandom.current());
        System.out.println(this);
    }

    @Override
    public Optional<Availability> getByUniqId(final String uniqId) {
        if (uniqId == null) return Optional.empty();
        return Optional.ofNullable(byUniq.get(uniqId));
    }

    @Override
    public List<Availability> getBulkByIds(final Collection<String> uniqIds) {
        if (uniqIds == null) return Collections.emptyList();
        return uniqIds.stream()
                .map(byUniq::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return byUniq.size();
    }
}
