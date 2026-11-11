package quickchatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Message class - holds one message and the send / store logic
public class Message {

    public static final String JSON_FILE_NAME = "storedMessages.json";

    private static List<Message> sentList = new ArrayList<>();
    private static List<Message> storedList = new ArrayList<>();
    private static int totalSent = 0;

    private String msgID;
    private int msgNumber;
    private String recipientCell;
    private String msgText;
    private String msgHash;

    public Message(int msgNumber, String recipientCell, String msgText) {
        this(newMessageID(), msgNumber, recipientCell, msgText);
    }

    public Message(String msgID, int msgNumber, String recipientCell, String msgText) {
        this.msgID = msgID;
        this.msgNumber = msgNumber;
        this.recipientCell = recipientCell;
        this.msgText = msgText;
        this.msgHash = createMessageHash();
    }

    // random number padded to 10 digits
    private static String newMessageID() {
        long number = (long) (new Random().nextDouble() * 10_000_000_000L);
        return String.format("%010d", number);
    }

    public boolean checkMessageID() {
        return msgID.length() <= 10;
    }

    public String printMessageID() {
        return "Message ID generated: " + msgID;
    }

    public String checkRecipientCell() {
        // same check as the registration cell number
        if (new Login().checkCellPhoneNumber(recipientCell)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        int length = msgText.length();
        if (length > 250) {
            return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    public String createMessageHash() {
        String[] words = msgText.trim().split(" ");
        // strip punctuation like "," or "?" off the words
        String first = words[0].replaceAll("\\W", "");
        String last = words[words.length - 1].replaceAll("\\W", "");

        return (msgID.substring(0, 2) + ":" + msgNumber + ":" + first + last).toUpperCase();
    }

    public String sentMessage(int choice) {
        if (choice == 1) {
            sentList.add(this);
            totalSent++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid option.";
    }

    public String getMessageDetails() {
        return "Message ID: " + msgID + "\n"
                + "Message Hash: " + msgHash + "\n"
                + "Recipient: " + recipientCell + "\n"
                + "Message: " + msgText;
    }

    public static String printMessages() {
        String allMessages = "";
        for (Message m : sentList) {
            allMessages += m.getMessageDetails() + "\n\n";
        }
        return allMessages.isEmpty() ? "No messages have been sent yet." : allMessages.trim();
    }

    public static int returnTotalMessages() {
        return totalSent;
    }

    // Saves stored messages to a JSON file using Gson
    // Reference: Google. 2023. Gson User Guide. Available at:
    // https://github.com/google/gson/blob/main/UserGuide.md
    public void storeMessage() {
        storedList.add(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fw = new FileWriter(JSON_FILE_NAME)) {
            gson.toJson(storedList, fw);
        } catch (IOException ex) {
            System.out.println("Error writing JSON file: " + ex.getMessage());
        }
    }

    public String getMsgID() {
        return msgID;
    }

    public String getMsgHash() {
        return msgHash;
    }

    public String getRecipientCell() {
        return recipientCell;
    }

    public String getMsgText() {
        return msgText;
    }
}
