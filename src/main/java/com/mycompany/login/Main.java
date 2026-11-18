package com.mycompany.login;

import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        StoredMessagesManager manager =
                new StoredMessagesManager();

        System.out.println("===== QUICKCHAT =====");
        System.out.println("CREATE YOUR ACCOUNT");

        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cellphone number (+27...): ");
        String cellphone = input.nextLine();

        Login login = new Login(username, password,
                cellphone, firstName, lastName);

        if (!login.checkUserName()) {
            System.out.println("Username is not correctly formatted. "
                    + "Please ensure that your username contains "
                    + "an underscore and is no more than five characters.");
            return;
        }

        if (!login.checkPasswordComplexity()) {
            System.out.println("Password is not correctly formatted. "
                    + "It must contain at least eight characters, "
                    + "a capital letter, a number and a special character.");
            return;
        }

        if (!login.checkCellPhoneNumber()) {
            System.out.println("Cellphone number is incorrectly formatted.");
            return;
        }

        System.out.println("Registration successful!");

        System.out.println("\n===== LOGIN =====");

        System.out.print("Enter username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = input.nextLine();

        if (!login.loginUser(enteredUsername, enteredPassword)) {
            System.out.println("Incorrect username or password.");
            return;
        }

        System.out.println(login.returnLoginStatus());

        boolean running = true;

        while (running) {

            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            System.out.print("Choose an option: ");
            String option = input.nextLine();

            switch (option) {

                case "1":
                    sendMessages(input, manager);
                    break;

                case "2":
                    System.out.println(manager.displaySentMessages());
                    break;

                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat.");
                    break;

                case "4":
                    storedMessagesMenu(input, manager);
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }

        input.close();
    }

    private static void sendMessages(Scanner input,
                                     StoredMessagesManager manager) {

        System.out.print("How many messages would you like to send? ");

        int numberOfMessages;

        try {
            numberOfMessages = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return;
        }

        for (int i = 0; i < numberOfMessages; i++) {

            System.out.println("\nMessage " + (i + 1));

            System.out.print("Enter recipient cellphone number (+27...): ");
            String recipient = input.nextLine();

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            Message message = new Message(recipient, messageText);

            if (!message.checkMessageID()) {
                System.out.println("Invalid message ID.");
                continue;
            }

            if (!message.checkRecipientCell().startsWith(
                    "Cell phone number successfully")) {
                System.out.println(message.checkRecipientCell());
                continue;
            }

            if (!message.checkMessageLength().equals(
                    "Message ready to send.")) {
                System.out.println(message.checkMessageLength());
                continue;
            }

            System.out.println("\n1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");

            System.out.print("Choose an option: ");

            int choice;

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
                continue;
            }

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid option.");
                continue;
            }

            System.out.println(message.SentMessage(choice));

            manager.recordMessage(message, choice);

            System.out.println("\n" + message.printMessages());
        }

        System.out.println("\nTotal messages sent: "
                + manager.getSentCount());
    }

    private static void storedMessagesMenu(
            Scanner input, StoredMessagesManager manager) {

        boolean menuRunning = true;

        while (menuRunning) {

            System.out.println("\n===== STORED MESSAGES MENU =====");
            System.out.println("1. Display sender and recipient");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search for a message using Message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message using Message Hash");
            System.out.println("6. Display message report");
            System.out.println("7. Return to main menu");

            System.out.print("Choose an option: ");
            String option = input.nextLine();

            switch (option) {

                case "1":
                    System.out.println(manager.displaySenderAndRecipient());
                    break;

                case "2":
                    System.out.println(manager.displayLongestStoredMessage());
                    break;

                case "3":
                    System.out.print("Enter Message ID: ");
                    String id = input.nextLine();

                    System.out.println(manager.searchMessageByID(id));
                    break;

                case "4":
                    System.out.print("Enter recipient cellphone number: ");
                    String recipient = input.nextLine();

                    System.out.println(
                            manager.searchMessagesByRecipient(recipient));
                    break;

                case "5":
                    System.out.print("Enter Message Hash: ");
                    String hash = input.nextLine();

                    System.out.println(
                            manager.deleteMessageByHash(hash));
                    break;

                case "6":
                    System.out.println(manager.displayReport());
                    break;

                case "7":
                    menuRunning = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }   
}
