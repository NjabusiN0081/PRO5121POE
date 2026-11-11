package quickchatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageStoreTest {

    MessageStore store;
    Message msg1, msg2, msg3, msg4, msg5;

    // loads the 5 Part 3 test messages into the arrays
    @BeforeEach
    void loadTestData() {
        store = new MessageStore("+27838968976", 5);

        msg1 = new Message(0, "+27834557896", "Did you get the cake?");
        msg2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        msg4 = new Message(3, "0838884567", "It is dinner time!");
        msg5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");

        store.addMsg(msg1, MessageStore.SENT);
        store.addMsg(msg2, MessageStore.STORED);
        store.addMsg(msg3, MessageStore.DISREGARDED);
        store.addMsg(msg4, MessageStore.SENT);
        store.addMsg(msg5, MessageStore.STORED);
    }

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertArrayEquals(new String[]{"Did you get the cake?", "It is dinner time!"}, store.sentMessagesArray());
    }

    @Test
    void disregardedMessagesArrayPopulated() {
        assertArrayEquals(new String[]{"Yohoooo, I am at your gate."}, store.disregardedMessagesArray());
    }

    @Test
    void hashAndIdArraysPopulated() {
        assertEquals(5, store.hashArray().length);
        assertEquals(msg1.getMsgHash(), store.hashArray()[0]);
        assertEquals(msg5.getMsgID(), store.idArray()[4]);
    }

    @Test
    void longestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", store.longestMessage());
    }

    @Test
    void searchForMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!", store.searchMessageID(msg4.getMsgID()));
    }

    @Test
    void searchForMissingMessageID() {
        assertEquals("Message ID 1234567890 was not found.", store.searchMessageID("1234567890"));
    }

    @Test
    void searchAllMessagesForRecipient() {
        String[] expected = {"Where are you? You are late! I have asked you to be on time.", "Ok, I am leaving without you."};
        assertArrayEquals(expected, store.searchRecipient("+27838884567"));
    }

    @Test
    void deleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                store.deleteMessage(msg2.getMsgHash()));
        assertArrayEquals(new String[]{"Ok, I am leaving without you."}, store.searchRecipient("+27838884567"));
        assertEquals("Ok, I am leaving without you.", store.longestMessage());
    }

    @Test
    void deleteMissingHash() {
        assertEquals("Message hash 00:0:NOPE was not found.", store.deleteMessage("00:0:NOPE"));
    }

    @Test
    void senderAndRecipients() {
        String list = store.senderAndRecipientList();
        assertTrue(list.contains("Sender: +27838968976 | Recipient: +27834557896"));
        assertTrue(list.contains("Sender: +27838968976 | Recipient: 0838884567"));
        assertFalse(list.contains("+27834484567"));
    }

    @Test
    void displayReport() {
        String report = store.printReport();
        Message[] sentMsgs = {msg1, msg4};
        for (Message m : sentMsgs) {
            assertTrue(report.contains("Message Hash: " + m.getMsgHash()));
            assertTrue(report.contains("Recipient: " + m.getRecipientCell()));
            assertTrue(report.contains("Message: " + m.getMsgText()));
        }
        assertFalse(report.contains("Yohoooo"));
    }

    @Test
    void readStoredMessagesFromJson() {
        Message stored = new Message(0, "+27838884567", "Checking the JSON file is read");
        stored.sentMessage(3); // saves to the JSON file

        Message[] loaded = store.loadStoredFromJson(Message.JSON_FILE_NAME);
        Message match = null;
        for (Message m : loaded) {
            if (m.getMsgID().equals(stored.getMsgID())) {
                match = m;
            }
        }
        assertNotNull(match);
        assertEquals("Checking the JSON file is read", match.getMsgText());
        assertEquals(stored.getMsgHash(), match.getMsgHash());
    }
}
