package com.example.product.client;

import com.example.product.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "catalog", path = "/products")
public interface CatalogClient {

    @GetMapping("/{id}")
    Product getByUniqId(@PathVariable("id") String id);

    @GetMapping("/by-sku/{sku}")
    List<Product> getBySku(@PathVariable("sku") String sku);
}
