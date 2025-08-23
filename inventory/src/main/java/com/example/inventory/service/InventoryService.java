package com.example.inventory.service;

import com.example.inventory.config.InventoryProperties;
import com.example.inventory.model.Availability;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryProperties inventoryProperties;

    public Optional<Availability> checkAvailabilityById(final String uniqId) {
        addDelay();
        return inventoryRepository.getByUniqId(uniqId);
    }

    public List<Availability> checkAvailabilityBulkIds(final Collection<String> uniqIds) {
        addDelay();
        return inventoryRepository.getBulkByIds(uniqIds);
    }

    private void addDelay() {
        final int minDelay = inventoryProperties.getDelayMs().getMin();
        final int maxDelay = inventoryProperties.getDelayMs().getMax();
        if (minDelay > 0 && maxDelay >= minDelay) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1));
            } catch (final InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
