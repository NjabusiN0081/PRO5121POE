package st10487215.nb.ndlovu.prog5121.part1;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testCorrectUsername() {
        Login login = new Login();
        login.checkUserName("kyl_1");

        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testIncorrectUsername() {
        Login login = new Login();
        login.checkUserName("kyle!!!!!!!");

        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testCorrectPassword() {
        Login login = new Login();
        login.checkPasswordComplexity("Ch&&sec@ke99!");

        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testIncorrectPassword() {
        Login login = new Login();
        login.checkPasswordComplexity("password");

        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCorrectCellPhoneNumber() {
        Login login = new Login();
        login.checkCellPhoneNumber("+27838968976");

        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testIncorrectCellPhoneNumber() {
        Login login = new Login();
        login.checkCellPhoneNumber("08966553");

        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.checkUserName("kyl_1");
        login.checkPasswordComplexity("Ch&&sec@ke99!");

        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.checkUserName("kyl_1");
        login.checkPasswordComplexity("Ch&&sec@ke99!");

        assertFalse(login.loginUser("wrong", "password"));
    }
}