package com.aplazo.bnpl.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:credit-line-ranges.properties")
@ConfigurationProperties(prefix = "range")
public class CreditLineConfig {

    private List<Map<String, String>> ranges;

    public List<Map<String, String>> getRanges() {
        return ranges;
    }

    public void setRanges(List<Map<String, String>> ranges) {
        this.ranges = ranges;
    }
}
