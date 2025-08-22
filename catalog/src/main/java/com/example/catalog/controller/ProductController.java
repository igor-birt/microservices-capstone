package com.example.catalog.controller;

import com.example.catalog.model.Product;
import com.example.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CatalogService catalogService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getByUniqId(@PathVariable("id") final String id) {
        return catalogService.getByUniqId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }

    @GetMapping("/by-sku/{sku}")
    public List<Product> getBySku(@PathVariable("sku") final String sku) {
        return catalogService.getBySku(sku);
    }
}
