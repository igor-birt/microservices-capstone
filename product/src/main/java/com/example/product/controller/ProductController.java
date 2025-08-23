package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> byUniqId(@PathVariable("id") final String id) {
        return productService.getAvailableByUniqId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NO_CONTENT).build());
    }

    @GetMapping("/by-sku/{sku}")
    public List<Product> bySku(@PathVariable("sku") final String sku) {
        return productService.getAvailableBySku(sku);
    }
}
