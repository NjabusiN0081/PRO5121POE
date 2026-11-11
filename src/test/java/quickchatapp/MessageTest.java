package quickchatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // fixed IDs so the hashes can be checked
    Message testMessage1 = new Message("0011223344", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
    Message testMessage2 = new Message("0055667788", 1, "08575975889", "Hi Keegan, did you receive the payment?");

    @Test
    void messageLengthSuccess() {
        assertEquals("Message ready to send.", testMessage1.checkMessageLength());
    }

    @Test
    void messageLengthFailure() {
        String text = "x".repeat(255);
        Message tooLong = new Message("1111111111", 0, "+27718693002", text);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", tooLong.checkMessageLength());
    }

    @Test
    void recipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", testMessage1.checkRecipientCell());
    }

    @Test
    void recipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                testMessage2.checkRecipientCell());
    }

    @Test
    void messageHashCorrect() {
        assertEquals("00:0:HITONIGHT", testMessage1.createMessageHash());
    }

    @Test
    void messageHashLoop() {
        Message[] msgs = {
            testMessage1,
            testMessage2,
            new Message("3300000000", 2, "+27838884567", "Ok, I am leaving without you.")
        };
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "33:2:OKYOU"};

        for (int i = 0; i < msgs.length; i++) {
            assertEquals(expectedHashes[i], msgs[i].createMessageHash());
        }
    }

    @Test
    void messageIDCreated() {
        Message m = new Message(0, "+27718693002", "Testing the ID");
        assertTrue(m.checkMessageID());
        assertEquals("Message ID generated: " + m.getMsgID(), m.printMessageID());
    }

    @Test
    void userSelectedSend() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    void userSelectedDisregard() {
        assertEquals("Press 0 to delete the message.", testMessage2.sentMessage(2));
    }

    @Test
    void userSelectedStore() {
        Message m = new Message(0, "+27718693002", "Remember to buy bread");
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    @Test
    void totalMessagesSent() {
        int startTotal = Message.returnTotalMessages();

        Message m1 = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        Message m2 = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        m1.sentMessage(1);
        m2.sentMessage(2);

        assertEquals(startTotal + 1, Message.returnTotalMessages());
        assertTrue(Message.printMessages().contains(m1.getMessageDetails()));
    }
}
