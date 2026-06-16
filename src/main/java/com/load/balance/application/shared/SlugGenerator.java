package com.load.balance.application.shared;

import org.springframework.stereotype.Component;

@Component
public class SlugGenerator {
    public String generate(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}

