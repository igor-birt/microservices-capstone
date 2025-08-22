package com.example.catalog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.delay-ms")
public class DelayProperties {

    private int min;
    private int max;
}
