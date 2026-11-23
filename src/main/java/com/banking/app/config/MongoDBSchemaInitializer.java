
package com.banking.app.config;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.client.model.ValidationOptions;

public class MongoDBSchemaInitializer {

    private MongoDBSchemaInitializer() {
    }

    public static void initialize() {

        MongoDatabase database = MongoDBConnection.getDatabase();

        createAccountNotes(database);
        createActivityLogs(database);

        System.out.println("MongoDB schema initialization complete.");
    }

    private static boolean collectionExists(MongoDatabase database, String collectionName) {
        List<String> collections = database.listCollectionNames().into(new ArrayList<>());
        return collections.contains(collectionName);
    }

    private static void createAccountNotes(MongoDatabase database) {

        String collectionName = "account_notes";

        if (collectionExists(database, collectionName)) {
            System.out.println(collectionName + " already exists.");
            return;
        }

        Document validator = new Document("$jsonSchema", new Document("bsonType", "object")
            .append("required", List.of(
                    "account_id",
                    "author_employee_id",
                    "category",
                    "note_text",
                    "created_at",
                    "tags"
            ))
            .append("properties", new Document()
                .append("account_id", new Document("bsonType", "string"))
                .append("author_employee_id", new Document("bsonType", "string"))
                .append("category", new Document("bsonType", "string")
                .append("enum", List.of(
                        "GENERAL",
                        "WIRE_TRANSFER",
                        "SUSPICIOUS_ACTIVITY",
                        "CUSTOMER_CONTACT"
                )))
                .append("note_text", new Document("bsonType", "string"))
                .append("created_at", new Document("bsonType", "date"))
                .append("related_transaction_id", new Document("bsonType", "string"))
                .append("tags", new Document("bsonType", "array")
                    .append("items", new Document("bsonType", "string"))))
        );

        createValidatedCollection(database, collectionName, validator);

        database.getCollection(collectionName).createIndex(
                Indexes.ascending("account_id")
        );

        database.getCollection(collectionName).createIndex(
                Indexes.ascending("author_employee_id")
        );

        database.getCollection(collectionName).createIndex(
                Indexes.descending("created_at")
        );

        System.out.println("Created collection: " + collectionName);
    }

    private static void createActivityLogs(MongoDatabase database) {

        String collectionName = "activity_logs";

        if (collectionExists(database, collectionName)) {
            System.out.println(collectionName + " already exists.");
            return;
        }

        Document validator = new Document("$jsonSchema", new Document("bsonType", "object")
            .append("required", List.of(
                    "employee_id",
                    "action_type",
                    "account_id",
                    "occurred_at",
                    "details"))
            .append("properties", new Document()
                .append("employee_id", new Document("bsonType", "string"))
                .append("action_type", new Document("bsonType", "string")
                .append("enum", List.of(
                    "VIEW_ACCOUNT",
                    "WIRE_TRANSFER",
                    "SUSPICIOUS_ACTIVITY",
                    "FREEZE_ACCOUNT",
                    "UNFREEZE_ACCOUNT",
                    "CREATE_NOTE")))
                .append("account_id", new Document("bsonType", "string"))
                .append("customer_id", new Document("bsonType", "string"))
                .append("occurred_at", new Document("bsonType", "date"))
                .append("details", new Document("bsonType", "object")))
        );

        createValidatedCollection(database, collectionName, validator);

        database.getCollection(collectionName).createIndex(
                Indexes.ascending("employee_id")
        );

        database.getCollection(collectionName).createIndex(
                Indexes.ascending("account_id")
        );

        database.getCollection(collectionName).createIndex(
                Indexes.descending("occurred_at")
        );

        System.out.println("Created collection: " + collectionName);
    }

    private static void createValidatedCollection(
            MongoDatabase database,
            String collectionName,
            Document validator) {

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(validator)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        database.createCollection(collectionName, options);
    }
}
