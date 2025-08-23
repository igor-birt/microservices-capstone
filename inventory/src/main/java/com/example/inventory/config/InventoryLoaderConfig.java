package com.example.inventory.config;

import com.example.inventory.repository.InMemoryInventoryRepository;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.util.CsvUniqIdReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InventoryLoaderConfig {

    @Bean
    public InventoryRepository inventoryRepository(final InventoryProperties props) {
        final List<String> uniqIds = CsvUniqIdReader.loadFromClasspath(props.getClasspath(), props.getMaxRows());
        return new InMemoryInventoryRepository(uniqIds);
    }
}
