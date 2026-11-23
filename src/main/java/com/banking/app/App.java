package com.banking.app;

import java.sql.Connection;
import com.banking.app.config.MongoDBConnection;
import com.banking.app.config.MongoDBSchemaInitializer;
import com.banking.app.config.PostgresConnection;

import com.banking.app.dataAccess.CustomerDAOImpl;
import com.banking.app.dataAccess.CustomerDAO;
import com.banking.app.presentations.*;
import com.banking.app.services.CustomerService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        
        presentation.start();
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
        CustomerDAO customerDao = new CustomerDAOImpl();
        CustomerService customerService = new CustomerService(customerDao);
        Presentation presentation = new Presentation(customerService);
    }
}
