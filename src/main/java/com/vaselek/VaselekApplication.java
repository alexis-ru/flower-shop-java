package com.vaselek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class VaselekApplication {

    public static void main(String[] args) {
        loadConfigIni();
        SpringApplication.run(VaselekApplication.class, args);
    }

    private static void loadConfigIni() {
        // Ищем config.ini в нескольких возможных местах
        Path[] candidates = {
            Paths.get("config.ini"),
            Paths.get("/app/config.ini"),
            Paths.get(System.getProperty("user.dir"), "config.ini")
        };

        Path configPath = null;
        for (Path p : candidates) {
            if (Files.exists(p)) {
                configPath = p;
                break;
            }
        }
        if (configPath == null) {
            System.err.println("WARNING: config.ini не найден, используются значения по умолчанию");
            return;
        }

        System.out.println("Loading config from: " + configPath.toAbsolutePath());

        try {
            List<String> lines = Files.readAllLines(configPath);
            String section = "";
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith(";"))
                    continue;
                if (line.startsWith("[") && line.endsWith("]")) {
                    section = line.substring(1, line.length() - 1);
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) continue;

                String key = line.substring(0, eq).trim();
                String val = line.substring(eq + 1).trim();
                String fullKey = section.isEmpty() ? key : section + "." + key;

                // Маппинг ini-ключей на свойства Spring Boot
                switch (fullKey) {
                    case "database.url"      -> System.setProperty("spring.datasource.url", val);
                    case "database.username" -> System.setProperty("spring.datasource.username", val);
                    case "database.password" -> System.setProperty("spring.datasource.password", val);
                    case "server.address"    -> System.setProperty("server.address", val);
                    case "server.port"       -> System.setProperty("server.port", val);
                    default -> System.setProperty(fullKey, val);
                }

                System.out.println("  " + fullKey + " = " + val);
            }
        } catch (IOException e) {
            System.err.println("ERROR reading config.ini: " + e.getMessage());
        }
    }
}
