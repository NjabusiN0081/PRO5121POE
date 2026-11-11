package quickchatapp;

import java.util.Scanner;

// Main class - runs registration, login and the QuickChat menu
public class QuickChatApp {

    static Scanner sc = new Scanner(System.in);
    static Login user = new Login();
    static MessageStore store;

    public static void main(String[] args) {
        register();

        if (login()) {
            quickChatMenu();
        } else {
            System.out.println("Login failed 3 times, closing the app.");
        }
    }

    static void register() {
        System.out.println("----- REGISTRATION -----");
        System.out.print("First name: ");
        String firstName = sc.nextLine();
        System.out.print("Last name: ");
        String lastName = sc.nextLine();

        String userName = "";
        boolean valid = false;
        while (!valid) {
            System.out.print("Username: ");
            userName = sc.nextLine();
            System.out.println(user.usernameMessage(userName));
            valid = user.checkUserName(userName);
        }

        String password = "";
        valid = false;
        while (!valid) {
            System.out.print("Password: ");
            password = sc.nextLine();
            System.out.println(user.passwordMessage(password));
            valid = user.checkPasswordComplexity(password);
        }

        String phone = "";
        valid = false;
        while (!valid) {
            System.out.print("Cell phone number (+27...): ");
            phone = sc.nextLine();
            System.out.println(user.cellNumberMessage(phone));
            valid = user.checkCellPhoneNumber(phone);
        }

        System.out.println(user.registerUser(firstName, lastName, userName, password, phone));
    }

    // user gets 3 tries to log in
    static boolean login() {
        System.out.println("\n----- LOGIN -----");
        for (int tries = 1; tries <= 3; tries++) {
            System.out.print("Username: ");
            String userName = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            boolean success = user.loginUser(userName, password);
            System.out.println(user.returnLoginStatus(success));
            if (success) {
                return true;
            }
        }
        return false;
    }

    static void quickChatMenu() {
        System.out.println("\nWelcome to QuickChat.");
        int numMessages = getInt("How many messages do you want to send? ");
        while (numMessages < 1) {
            numMessages = getInt("Enter at least 1: ");
        }

        store = new MessageStore(user.getPhoneNumber(), numMessages);
        int count = 0; // messages entered so far
        int option = 0;

        while (option != 3) {
            System.out.println("\nOption 1) Send Messages");
            System.out.println("Option 2) Show recently sent messages");
            System.out.println("Option 3) Quit");
            System.out.println("Option 4) Stored Messages");
            option = getInt("Select an option: ");

            switch (option) {
                case 1 -> {
                    if (count == numMessages) {
                        System.out.println("Message limit of " + numMessages + " reached.");
                    } else {
                        for (int i = count; i < numMessages; i++) {
                            System.out.println("\nMessage " + (i + 1) + ":");
                            createMessage(i);
                            count++;
                        }
                        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
                    }
                }
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> System.out.println("Thank you for using QuickChat. Bye!");
                case 4 -> storedMessagesMenu();
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    // Part 3 - stored messages menu, uses the arrays in MessageStore
    static void storedMessagesMenu() {
        int pick = -1;
        while (pick != 0) {
            System.out.println("\n----- STORED MESSAGES -----");
            System.out.println("1. Display sender and recipient of all messages");
            System.out.println("2. Display the longest message");
            System.out.println("3. Search by message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message by message hash");
            System.out.println("6. Display message report");
            System.out.println("7. Load stored messages from JSON file");
            System.out.println("0. Back");
            pick = getInt("Select an option: ");

            switch (pick) {
                case 1 -> System.out.println(store.senderAndRecipientList());
                case 2 -> System.out.println("Longest message: " + store.longestMessage());
                case 3 -> {
                    System.out.print("Message ID: ");
                    System.out.println(store.searchMessageID(sc.nextLine().trim()));
                }
                case 4 -> {
                    System.out.print("Recipient cell number: ");
                    String cell = sc.nextLine().trim();
                    String[] results = store.searchRecipient(cell);
                    if (results.length == 0) {
                        System.out.println("No messages found for " + cell + ".");
                    }
                    for (String text : results) {
                        System.out.println("\"" + text + "\"");
                    }
                }
                case 5 -> {
                    System.out.print("Message hash: ");
                    System.out.println(store.deleteMessage(sc.nextLine().trim()));
                }
                case 6 -> System.out.println(store.printReport());
                case 7 -> {
                    store.loadStoredFromJson(Message.JSON_FILE_NAME);
                    System.out.println(store.showJsonMessages());
                }
                case 0 -> System.out.println("Returning to main menu.");
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    static void createMessage(int msgNumber) {
        String recipient;
        while (true) {
            System.out.print("Recipient cell number: ");
            recipient = sc.nextLine();
            if (user.checkCellPhoneNumber(recipient)) {
                break;
            }
            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
        }

        String text;
        while (true) {
            System.out.print("Message: ");
            text = sc.nextLine();
            if (!text.isBlank() && text.length() <= 250) {
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
        }

        Message msg = new Message(msgNumber, recipient, text);
        System.out.println(msg.checkRecipientCell());
        System.out.println(msg.checkMessageLength());

        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        int choice = getInt("Your choice: ");
        while (choice < 1 || choice > 3) {
            choice = getInt("Choose 1, 2 or 3: ");
        }

        System.out.println(msg.sentMessage(choice));
        store.addMsg(msg, MessageStore.statusFromChoice(choice));

        if (choice == 1) {
            System.out.println(msg.getMessageDetails());
        } else if (choice == 2) {
            int del = getInt("");
            while (del != 0) {
                del = getInt("Press 0 to delete the message. ");
            }
            System.out.println("Message deleted.");
        }
    }

    // reads a number, asks again if it's not one
    static int getInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("That is not a number, try again: ");
            sc.nextLine();
        }
        int num = sc.nextInt();
        sc.nextLine();
        return num;
    }
}
