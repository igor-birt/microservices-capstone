package com.example.catalog.service;

import com.example.catalog.config.CatalogProperties;
import com.example.catalog.model.Product;
import com.example.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final CatalogProperties catalogProperties;

    public Optional<Product> getByUniqId(final String uniqId) {
        addDelay();
        return catalogRepository.findByUniqId(uniqId);
    }

    public List<Product> getBySku(final String sku) {
        addDelay();
        return catalogRepository.findBySku(sku);
    }

    private void addDelay() {
        final int minDelay = catalogProperties.getDelayMs().getMin();
        final int maxDelay = catalogProperties.getDelayMs().getMax();
        if (minDelay > 0 && maxDelay >= minDelay) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1));
            } catch (final InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }
}