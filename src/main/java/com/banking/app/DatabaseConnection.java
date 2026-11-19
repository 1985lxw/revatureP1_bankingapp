package com.banking.app;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String URL = getConfig(
            "POSTGRES_URL",
            "jdbc:postgresql://localhost:5432/banking_db"
    );

    private static final String USER = getConfig(
            "POSTGRES_USERNAME",
            "postgres"
    );

    private static final String PASSWORD = getConfig(
            "POSTGRES_PASSWORD",
            null
    );

    private static String getConfig(String key, String fallback) {
        String envValue = System.getenv(key);

        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }

        String dotenvValue = dotenv.get(key);

        if (dotenvValue != null && !dotenvValue.trim().isEmpty()) {
            return dotenvValue;
        }

        return fallback;
    }

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing POSTGRES_PASSWORD in .env file."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
