package com.example.catalog.repository;

import com.example.catalog.model.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCatalogRepository implements CatalogRepository {

    private final Map<String, Product> byUniq = new ConcurrentHashMap<>();
    private final Map<String, List<Product>> bySku = new ConcurrentHashMap<>();

    public InMemoryCatalogRepository(final Collection<Product> seed) {
        seed.forEach(this::index);
    }

    private void index(final Product p) {
        byUniq.put(p.uniqId(), p);
        bySku.computeIfAbsent(p.sku(), k -> new ArrayList<>()).add(p);
    }

    @Override
    public Optional<Product> findByUniqId(final String uniqId) {
        return Optional.ofNullable(byUniq.get(uniqId));
    }

    @Override
    public List<Product> findBySku(final String sku) {
        return bySku.getOrDefault(sku, List.of());
    }

    @Override
    public int size() {
        return byUniq.size();
    }
}
