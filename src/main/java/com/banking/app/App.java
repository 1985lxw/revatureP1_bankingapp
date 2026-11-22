package com.banking.app;

import com.banking.app.dataAccess.CustomerDAOImpl;
import com.banking.app.dataAccess.CustomerDAO;
import com.banking.app.presentations.*;
import com.banking.app.services.CustomerService;

import java.sql.Connection;
import com.banking.app.DatabaseConnection;

public class App 
{
    public static void main(String[] args)
    {
        CustomerDAO customerDao = new CustomerDAOImpl();
        CustomerService customerService = new CustomerService(customerDao);
        Presentation presentation = new Presentation(customerService);
        presentation.start();
    }
}
