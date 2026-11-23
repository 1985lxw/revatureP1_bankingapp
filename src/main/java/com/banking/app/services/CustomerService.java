package com.banking.app.services;

import com.banking.app.dataAccess.CustomerDAO;
import com.banking.app.models.Customer;
import com.banking.app.PasswordHasher;

// Handles the input validation, trimming, lowercasing, email existence checking,
// password hashing etc..

public class CustomerService {
    private CustomerDAO customerDao;

    public CustomerService(CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }
    
    public Customer register(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password
    )
    {   
        // customerId, createdAt, and updatedAt will be handled
        // by postgresql so we don't need to include them here

        Customer customer = new Customer();

        customer.setFirstName(firstName.trim());

        customer.setLastName(lastName.trim());

        // Ensure email doesn't already exist in database (do it later)
        customer.setEmail(email.trim());

        customer.setPhoneNumber(phoneNumber.trim());

        customer.setHashedPassword(PasswordHasher.hash(password));

        Customer result = customerDao.createCustomer(customer);

        return result;
    }

}
