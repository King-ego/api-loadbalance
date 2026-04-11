package com.load.balance.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class healthController {
    private final DataSource dataSource;

    public healthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                response.put("status", "UP");
                response.put("database", "Connected");
                response.put("message", "Application is healthy");
            } else {
                response.put("status", "DOWN");
                response.put("database", "Connection Invalid");
                response.put("message", "Database connection is not valid");
            }

        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("database", "Disconnected");
            response.put("message", "Failed to connect to database: " + e.getMessage());
        }

        return response;
    }
}