package com.example.inventory.repository;

import com.example.inventory.model.Availability;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Optional<Availability> getByUniqId(String uniqId);

    List<Availability> getBulkByIds(Collection<String> uniqIds);

    int size();
}
