
package st10487215.nb.ndlovu.prog5121.part1;

import java.util.Scanner;

public class ST10487215NBNDLOVUPROG5121PART1 {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // ----- Registration -----
        System.out.println("=== Registration ===");

        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter username: ");
        String username = input.nextLine();

        if (login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter password: ");
        String password = input.nextLine();

        if (login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.");
        }

        System.out.print("Enter cell phone number (e.g. +27831234567): ");
        String cellNumber = input.nextLine();

        if (login.checkCellPhoneNumber(cellNumber)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        String registrationMessage = login.registerUser(username, password, cellNumber,
                firstName, lastName);
        System.out.println(registrationMessage);

        // ----- Login -----
        System.out.println("\n=== Login ===");

        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loggedIn));

    }
    
}
