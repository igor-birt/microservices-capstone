package com.example.inventory.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.inventory")
public class InventoryProperties {

    private String classpath;
    private int maxRows;

    private Delay delayMs = new Delay();

    @Getter
    @Setter
    public static class Delay {
        private int min;
        private int max;
    }
}
