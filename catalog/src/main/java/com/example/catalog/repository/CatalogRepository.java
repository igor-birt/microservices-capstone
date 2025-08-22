package com.example.catalog.repository;

import com.example.catalog.model.Product;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {

    Optional<Product> findByUniqId(String uniqId);
    List<Product> findBySku(String sku);
    int size();
}
