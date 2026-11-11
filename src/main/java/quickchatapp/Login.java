package quickchatapp;

// Login class - stores the registered user and checks login details
public class Login {

    private String firstName;
    private String lastName;
    private String userName;
    private String userPassword;
    private String phoneNumber;

    public boolean checkUserName(String userName) {
        // needs an underscore and max 5 characters
        return userName.length() <= 5 && userName.indexOf('_') != -1;
    }

    public boolean checkPasswordComplexity(String password) {
        // at least 8 chars, one capital, one digit, one special char
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    // Regex based on: W3Schools. 2024. Java Regular Expressions.
    // Available at: https://www.w3schools.com/java/java_regex.asp
    public boolean checkCellPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(\\+27)[0-9]{9}$");
    }

    public String usernameMessage(String userName) {
        if (checkUserName(userName)) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    public String passwordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    public String cellNumberMessage(String phoneNumber) {
        if (checkCellPhoneNumber(phoneNumber)) {
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
    }

    public String registerUser(String firstName, String lastName, String userName, String password, String phoneNumber) {
        if (!checkUserName(userName)) {
            return usernameMessage(userName);
        } else if (!checkPasswordComplexity(password)) {
            return passwordMessage(password);
        } else if (!checkCellPhoneNumber(phoneNumber)) {
            return cellNumberMessage(phoneNumber);
        }

        // all good, save the user
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userPassword = password;
        this.phoneNumber = phoneNumber;
        return "User has been registered successfully.";
    }

    public boolean loginUser(String userName, String password) {
        if (this.userName == null) {
            return false; // nobody registered yet
        }
        return this.userName.equals(userName) && this.userPassword.equals(password);
    }

    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
