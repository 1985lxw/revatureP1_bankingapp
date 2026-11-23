package com.banking.app.dataAccess;

import java.util.Optional;
import java.util.UUID;

import com.banking.app.models.Customer;

public interface CustomerDAO {
    Customer createCustomer(Customer customer);

    // Optional<Customer> getCustomerByEmail(String email);

    // Optional<Customer> getCustomerById(UUID id);

    // boolean update(Customer customer);

    // boolean updatePassword(UUID id, String hashedPassword);
}
