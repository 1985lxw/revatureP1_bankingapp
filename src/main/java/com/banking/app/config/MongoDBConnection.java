package com.banking.app.config;


import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBConnection {
    private MongoDBConnection() {
    }
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String URI = getConfig(
            "MONGODB_URI",
            "mongodb://localhost:27017"
    );

    private static final String DATABASE = getConfig(
            "MONGODB_DATABASE",
            "cluster0"
    );

    private static MongoClient mongoClient;

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

    public static synchronized MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
            try {
                mongoClient.getDatabase("admin").runCommand(new Document("ping", 1));
                System.out.println("Successfully connected to MongoDB!");
            } catch (Exception e) {
                mongoClient.close();
                mongoClient = null;
                throw new IllegalStateException(
                        "Failed to connect to MongoDB.", e
                );
            }
        }

        return mongoClient.getDatabase(DATABASE);
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
