package st10487215.nb.ndlovu.prog5121.part1;

public class Login {

    // Stored user details after successful registration
    String storedUsername;
    String storedPassword;
    String storedCellNumber;
    String firstName;
    String lastName;

    // Checks that the username has an underscore and is at most 5 characters
    public boolean checkUserName(String username) {
        boolean hasUnderscore = username.contains("_");
        boolean correctLength = username.length() <= 5;
        return hasUnderscore && correctLength;
    }

    // Checks password rules: 8+ chars, capital letter, digit, special character
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                hasCapital = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasDigit && hasSpecial;
    }

    /*
     * South African cell phone check adapted from a regex example on Stack Overflow.
     * Matches +27 followed by 9 digits (international code plus 9-digit SA number).
     * Reference:
     * https://stackoverflow.com/questions/28131029/regex-for-south-african-phone-numbers
     * Author: Wiktor Stribiżew
     * Accessed: 2026
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27[0-9]{9}$");
    }

    // Registers the user and returns the correct message
    public String registerUser(String username, String password, String cellNumber,
                               String firstName, String lastName) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
    }

    // Checks if the login details match the stored details
    public boolean loginUser(String username, String password) {
        if (storedUsername == null || storedPassword == null) {
            return false;
        }
        return storedUsername.equals(username) && storedPassword.equals(password);
    }

    // Returns the login status message
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}