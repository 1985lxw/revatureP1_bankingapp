package com.banking.app.presentations;

import java.util.Scanner;
import com.banking.app.services.CustomerService;
import com.banking.app.models.Customer;

// The presentation layer manages all console-based interaction with the user.

// Responsibilities include:

// Displaying menus and available actions appropriate to the logged-in user's role (customer or employee)
// Reading and validating console input
// Displaying results and error messages
// Sending user requests to the service layer
// Avoiding direct database access and business logic

public class Presentation {
    private final Scanner scanner = new Scanner(System.in);
    private CustomerService customerService;

    public Presentation(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void start() {
        System.out.println("Welcome to our banking app!");
        System.out.println("(1) Customer Login");
        System.out.println("(2) Customer Registration");
        System.out.println("(3) Employee Login");
        System.out.println("(4) Exit");
        System.out.print("Enter choice: ");
        int userResponse = scanner.nextInt();
        scanner.nextLine();

        switch (userResponse) {
            case 1:
                break;

            case 2:
                System.out.print("Enter First Name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter Last Name: ");
                String lastName = scanner.nextLine();
                System.out.print("Enter an Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Phone Number: ");
                String phoneNumber = scanner.nextLine();
                System.out.print("Enter a Password: ");
                String password = scanner.nextLine();

                Customer result = customerService.register(firstName, lastName, email, phoneNumber, password);
                if (result != null) {
                    System.out.println("Customer registration success!");
                }
                break;

            case 3:
                break;

            case 4:
                return;

            default:
                return;
        }
    }
}
