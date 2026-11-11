package quickchatapp;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

// MessageStore class - Part 3
// Holds all the messages in parallel arrays (same index = same message)
// and does the searching, deleting and reporting for the Stored Messages menu.
public class MessageStore {

    public static final String SENT = "Sent";
    public static final String STORED = "Stored";
    public static final String DISREGARDED = "Disregard";

    private String senderNumber;

    // parallel arrays
    private String[] ids;
    private String[] hashes;
    private String[] recipientCells;
    private String[] texts;
    private String[] statuses;
    private int size = 0;

    // filled from the JSON file
    private Message[] jsonMessages = new Message[0];

    public MessageStore(String senderNumber, int capacity) {
        this.senderNumber = senderNumber;
        if (capacity < 1) {
            capacity = 1;
        }
        ids = new String[capacity];
        hashes = new String[capacity];
        recipientCells = new String[capacity];
        texts = new String[capacity];
        statuses = new String[capacity];
    }

    public void addMsg(Message msg, String status) {
        if (size == ids.length) {
            // arrays are full, make them bigger
            int bigger = ids.length * 2;
            ids = Arrays.copyOf(ids, bigger);
            hashes = Arrays.copyOf(hashes, bigger);
            recipientCells = Arrays.copyOf(recipientCells, bigger);
            texts = Arrays.copyOf(texts, bigger);
            statuses = Arrays.copyOf(statuses, bigger);
        }
        ids[size] = msg.getMsgID();
        hashes[size] = msg.getMsgHash();
        recipientCells[size] = msg.getRecipientCell();
        texts[size] = msg.getMsgText();
        statuses[size] = status;
        size++;
    }

    // turns the send / disregard / store choice (1, 2, 3) into a status
    public static String statusFromChoice(int choice) {
        if (choice == 1) {
            return SENT;
        } else if (choice == 3) {
            return STORED;
        }
        return DISREGARDED;
    }

    // disregarded messages are not part of the searches or report
    private boolean notDisregarded(int i) {
        return !statuses[i].equals(DISREGARDED);
    }

    private String[] textsWithStatus(String status) {
        int total = 0;
        for (int i = 0; i < size; i++) {
            if (statuses[i].equals(status)) {
                total++;
            }
        }
        String[] result = new String[total];
        int k = 0;
        for (int i = 0; i < size; i++) {
            if (statuses[i].equals(status)) {
                result[k] = texts[i];
                k++;
            }
        }
        return result;
    }

    public String[] sentMessagesArray() {
        return textsWithStatus(SENT);
    }

    public String[] disregardedMessagesArray() {
        return textsWithStatus(DISREGARDED);
    }

    public String[] hashArray() {
        return Arrays.copyOf(hashes, size);
    }

    public String[] idArray() {
        return Arrays.copyOf(ids, size);
    }

    // Reads the JSON file that storeMessage() wrote back into an array
    // Reference: Google. 2023. Gson User Guide. Available at:
    // https://github.com/google/gson/blob/main/UserGuide.md#array-examples
    public Message[] loadStoredFromJson(String path) {
        try (FileReader fr = new FileReader(path)) {
            Message[] loaded = new Gson().fromJson(fr, Message[].class);
            if (loaded != null) {
                jsonMessages = loaded;
            } else {
                jsonMessages = new Message[0];
            }
        } catch (IOException ex) {
            jsonMessages = new Message[0];
        }
        return jsonMessages;
    }

    public String showJsonMessages() {
        if (jsonMessages.length == 0) {
            return "No stored messages were found in the JSON file.";
        }
        String out = "Stored messages read from JSON:\n";
        for (Message m : jsonMessages) {
            out += "\n" + m.getMessageDetails() + "\n";
        }
        return out.trim();
    }

    // a) sender and recipient of all the messages
    public String senderAndRecipientList() {
        String out = "";
        for (int i = 0; i < size; i++) {
            if (notDisregarded(i)) {
                out += "Sender: " + senderNumber + " | Recipient: " + recipientCells[i] + "\n";
            }
        }
        if (out.isEmpty()) {
            return "There are no messages to display.";
        }
        return out.trim();
    }

    // b) longest message
    public String longestMessage() {
        int longestIndex = -1;
        for (int i = 0; i < size; i++) {
            if (notDisregarded(i) && (longestIndex == -1 || texts[i].length() > texts[longestIndex].length())) {
                longestIndex = i;
            }
        }
        if (longestIndex == -1) {
            return "There are no messages to display.";
        }
        return texts[longestIndex];
    }

    // c) search by message ID
    public String searchMessageID(String id) {
        for (int i = 0; i < size; i++) {
            if (notDisregarded(i) && ids[i].equals(id)) {
                return "Recipient: " + recipientCells[i] + "\nMessage: " + texts[i];
            }
        }
        return "Message ID " + id + " was not found.";
    }

    // d) every message sent or stored for one recipient
    public String[] searchRecipient(String cell) {
        String[] matches = new String[size];
        int found = 0;
        for (int i = 0; i < size; i++) {
            if (notDisregarded(i) && recipientCells[i].equals(cell)) {
                matches[found] = texts[i];
                found++;
            }
        }
        return Arrays.copyOf(matches, found);
    }

    // e) delete using the message hash, moving the later messages up one spot
    public String deleteMessage(String hash) {
        int index = -1;
        for (int i = 0; i < size && index == -1; i++) {
            if (hashes[i].equalsIgnoreCase(hash)) {
                index = i;
            }
        }
        if (index == -1) {
            return "Message hash " + hash + " was not found.";
        }

        String removedText = texts[index];
        for (int i = index; i < size - 1; i++) {
            ids[i] = ids[i + 1];
            hashes[i] = hashes[i + 1];
            recipientCells[i] = recipientCells[i + 1];
            texts[i] = texts[i + 1];
            statuses[i] = statuses[i + 1];
        }
        size--;
        ids[size] = null;
        hashes[size] = null;
        recipientCells[size] = null;
        texts[size] = null;
        statuses[size] = null;

        return "Message: \"" + removedText + "\" successfully deleted.";
    }

    // f) report with the full details of every message
    public String printReport() {
        String report = "========== QuickChat Report ==========\n";
        int listed = 0;
        for (int i = 0; i < size; i++) {
            if (notDisregarded(i)) {
                report += "\nMessage ID: " + ids[i] + "\n"
                        + "Message Hash: " + hashes[i] + "\n"
                        + "Recipient: " + recipientCells[i] + "\n"
                        + "Message: " + texts[i] + "\n"
                        + "Status: " + statuses[i] + "\n";
                listed++;
            }
        }
        if (listed == 0) {
            report += "\nThere are no messages to report.";
        }
        return report.trim();
    }
}
