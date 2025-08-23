package com.example.product.service;

import com.example.product.client.CatalogClient;
import com.example.product.client.InventoryClient;
import com.example.product.model.Availability;
import com.example.product.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
//TODO
@Service
@RequiredArgsConstructor
public class ProductService {

    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    @CircuitBreaker(name = "downstream", fallbackMethod = "unavailableById")
    public Optional<Product> getAvailableByUniqId(final String id) {
        Product p = null;
        try {
            p = catalogClient.getByUniqId(id);
        } catch (final Exception ex) {
            return Optional.empty();
        }

        final Availability a;
        try {
            a = inventoryClient.checkAvailabilityById(id);
        } catch (final Exception ex) {
            throw ex;
        }

        return (a != null && a.available()) ? Optional.of(p) : Optional.empty();
    }

    @CircuitBreaker(name = "downstream", fallbackMethod = "unavailableBySku")
    public List<Product> getAvailableBySku(final String sku) {
        final List<Product> products = catalogClient.getBySku(sku);
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> ids = products.stream()
                .map(Product::uniqId)
                .filter(Objects::nonNull)
                .toList();

        final List<Availability> avs = inventoryClient.bulkCheckAvailability(ids);
        final Set<String> availableIds = avs == null ? Set.of()
                : avs.stream()
                .filter(Availability::available)
                .map(Availability::uniqId)
                .collect(Collectors.toSet());

        return products.stream()
                .filter(p -> availableIds.contains(p.uniqId()))
                .toList();
    }

    private Optional<Product> unavailableById(final String id, final Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Dependent service unavailable", t);
    }

    private List<Product> unavailableBySku(final String sku, final Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Dependent service unavailable", t);
    }
}
