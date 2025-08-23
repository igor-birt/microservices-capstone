package com.example.product.client;

import com.example.product.model.Availability;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "inventory", path = "/inventory")
public interface InventoryClient {

    @GetMapping("/{id}")
    Availability checkAvailabilityById(@PathVariable("id") String id);

    @PostMapping("/list")
    List<Availability> bulkCheckAvailability(@RequestBody Collection<String> uniqIds);
}