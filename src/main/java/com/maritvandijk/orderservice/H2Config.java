package com.maritvandijk.orderservice;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class H2Config {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @PreDestroy
    public void onDestroy() {
        if (dbUrl != null && dbUrl.startsWith("jdbc:h2:file:")) {
            try {
                String fileName = dbUrl.substring("jdbc:h2:file:".length());
                Files.deleteIfExists(Paths.get(fileName + ".mv.db")); // H2 database filename extension is .mv.db
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete H2 database file", e);
            }
        }
    }
}
