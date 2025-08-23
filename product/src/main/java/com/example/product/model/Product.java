package com.example.product.model;

public record Product (String uniqId, String sku, String nameTitle, String description,
                       Double listPrice, Double salePrice, String category, String brand, Long totalNumberReviews) {
}