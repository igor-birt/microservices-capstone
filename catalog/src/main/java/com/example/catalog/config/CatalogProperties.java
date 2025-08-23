package com.example.catalog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.catalog")
public class CatalogProperties {

    private String classpath;
    private int maxRows;

    private Delay delayMs;

    @Getter
    @Setter
    public static class Delay {
        private int min;
        private int max;
    }
}
