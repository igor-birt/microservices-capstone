package com.example.catalog.config;

import com.example.catalog.model.Product;
import com.example.catalog.repository.CatalogRepository;
import com.example.catalog.repository.InMemoryCatalogRepository;
import com.example.catalog.util.CsvProductReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CatalogLoaderConfig {

    @Bean
    public CatalogRepository catalogRepository(final CatalogProperties props) {
        final List<Product> products = CsvProductReader.loadFromClasspath(props.getClasspath(), props.getMaxRows());
        return new InMemoryCatalogRepository(products);
    }
}
