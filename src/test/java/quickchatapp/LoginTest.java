package quickchatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    Login login = new Login();

    // registers the test user so login can be checked
    private void registerTestUser() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    @Test
    void usernameCorrectlyFormatted() {
        registerTestUser();
        assertEquals("Username successfully captured.", login.usernameMessage("kyl_1"));
        boolean loggedIn = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus(loggedIn));
    }

    @Test
    void usernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsRequirements() {
        assertEquals("Password successfully captured.", login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void passwordFailsRequirements() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.passwordMessage("password"));
    }

    @Test
    void cellNumberCorrect() {
        assertEquals("Cell number successfully captured.", login.cellNumberMessage("+27838968976"));
    }

    @Test
    void cellNumberIncorrect() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.cellNumberMessage("08966553"));
    }

    @Test
    void registerUserSuccess() {
        assertEquals("User has been registered successfully.",
                login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void loginSuccessful() {
        registerTestUser();
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailed() {
        registerTestUser();
        assertFalse(login.loginUser("kyl_1", "password"));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }

    @Test
    void checkUserNameTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void checkUserNameFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void checkPasswordTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void checkPasswordFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void checkCellTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void checkCellFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
