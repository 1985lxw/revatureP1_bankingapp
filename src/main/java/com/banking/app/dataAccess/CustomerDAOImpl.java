package com.banking.app.dataAccess;

import com.banking.app.models.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.banking.app.DatabaseConnection;

public class CustomerDAOImpl implements CustomerDAO {

    @Override 
    public Customer createCustomer(Customer customer) {
        String sql = """
            insert into customers (first_name, last_name, email, phone, password_hash)
            values (?, ?, ?, ?, ?)
            returning *
        """;

        // Both the database connection and prepared statement leaves resources open
        // so enclosing them in try-with-resources accounts for that
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement prepStatement = conn.prepareStatement(sql)) {
            prepStatement.setString(1, customer.getFirstName());
            prepStatement.setString(2, customer.getLastName());
            prepStatement.setString(3, customer.getEmail());
            prepStatement.setString(4, customer.getPhoneNumber());
            prepStatement.setString(5, customer.getHashedPassword());

            ResultSet rs = prepStatement.executeQuery();
            if (rs.next()) {
                customer.setCustomerId(rs.getObject("customer_id", UUID.class));
                customer.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                customer.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

}
