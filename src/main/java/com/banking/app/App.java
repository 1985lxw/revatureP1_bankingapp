package com.banking.app;

import java.sql.Connection;

import com.banking.app.config.MongoDBConnection;
import com.banking.app.config.MongoDBSchemaInitializer;
import com.banking.app.config.PostgresConnection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println("Starting Banking Application...");
        try {

            // test psql conn
            try (Connection connection = PostgresConnection.getConnection()) {
                System.out.println("Successfully connected to PostgreSQL!");
            }

            // test mongo
            MongoDBConnection.getDatabase();
            MongoDBSchemaInitializer.initialize();

            System.out.println("Banking Application initialized successfully!");

        } catch (Exception e) {

            System.err.println("Application startup failed: " + e.getMessage() );
            e.printStackTrace();

        } finally {
            MongoDBConnection.closeConnection();
        }
    }
}
