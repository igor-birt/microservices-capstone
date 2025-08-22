package com.example.catalog.util;

import com.example.catalog.model.Product;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class CsvProductReader {

    private static final String H_UNIQ_ID = "uniq_id";
    private static final String H_SKU = "sku";
    private static final String H_NAME_TITLE = "name_title";
    private static final String H_DESCRIPTION = "description";
    private static final String H_LIST_PRICE = "list_price";
    private static final String H_SALE_PRICE = "sale_price";
    private static final String H_CATEGORY = "category";
    private static final String H_BRAND = "brand";
    private static final String H_TOTAL_REVIEWS = "total_number_reviews";

    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setIgnoreSurroundingSpaces(true)
            .build();

    public static List<Product> loadFromClasspath(final String classpathResource, final int maxRows) {
        try (InputStream in = CsvProductReader.class.getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + classpathResource);
            }
            return parse(in, maxRows);
        } catch (IOException e) {
            throw new RuntimeException("Failed reading CSV from classpath: " + classpathResource, e);
        }
    }

    public static List<Product> loadFromFile(Path path, int maxRows) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return parse(reader, maxRows);
        } catch (IOException e) {
            throw new RuntimeException("Failed reading CSV file: " + path, e);
        }
    }

    private static List<Product> parse(InputStream in, int maxRows) throws IOException {
        return parse(new java.io.InputStreamReader(in, StandardCharsets.UTF_8), maxRows);
    }

    private static List<Product> parse(Reader reader, int maxRows) throws IOException {
        List<Product> out = new ArrayList<>(Math.max(16, Math.min(maxRows, 10_000)));
        try (CSVParser parser = new CSVParser(reader, FORMAT)) {
            for (CSVRecord r : parser) {
                String uniqId = val(r, H_UNIQ_ID);
                String sku = val(r, H_SKU);
                String nameTitle = val(r, H_NAME_TITLE);
                String description = val(r, H_DESCRIPTION);
                Double listPrice = toDouble(val(r, H_LIST_PRICE));
                Double salePrice = toDouble(val(r, H_SALE_PRICE));
                String category = val(r, H_CATEGORY);
                String brand = val(r, H_BRAND);
                Long totalReviews = toLong(val(r, H_TOTAL_REVIEWS));

                if (uniqId == null || sku == null) {
                    continue;
                }

                out.add(new Product(
                        uniqId,
                        sku,
                        nameTitle,
                        description,
                        listPrice,
                        salePrice,
                        category,
                        brand,
                        totalReviews
                ));

                if (out.size() >= maxRows) break;
            }
        }
        return out;
    }

    private static String val(CSVRecord r, String header) {
        try {
            String v = r.get(header);
            if (v == null) return null;
            v = v.trim();
            if (v.isEmpty() || "null".equalsIgnoreCase(v) || "NULL".equals(v)) return null;
            return v;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Double toDouble(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Long toLong(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}