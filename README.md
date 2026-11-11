# QuickChat – PROG5121 POE

**Student number:** ST10487215
**Module:** Programming 1A (PROG5121)
**Repository:** https://github.com/NjabusiN0081/PRO5121POE

QuickChat is a console chat application written in Java (Maven, JDK 17). It covers all three parts of the POE:

| Part | Feature | Class |
|------|---------|-------|
| 1 | Registration and login | `Login` |
| 2 | Sending messages, message ID / hash, JSON storage | `Message` |
| 3 | Stored messages in arrays, searching, deleting and reporting | `MessageStore` |
| – | Console menus that tie everything together | `QuickChatApp` |

## How to run

```bash
mvn compile exec:java      # runs quickchatapp.QuickChatApp
mvn test                   # runs all unit tests
```

The project can also be opened directly in NetBeans or IntelliJ as a Maven project.

## Part 1 – Registration and login

* **Username** must contain an underscore and be no more than five characters (`checkUserName`).
* **Password** must be at least eight characters with a capital letter, a number and a special character (`checkPasswordComplexity`).
* **Cell number** must start with the +27 international code followed by nine digits, checked with a regular expression (`checkCellPhoneNumber`).
* `registerUser` returns the matching error message or confirms registration; `loginUser` / `returnLoginStatus` verify the stored details and greet the user.

## Part 2 – Sending messages

After logging in the user sees **"Welcome to QuickChat."** and a numeric menu:

1. Send Messages
2. Show recently sent messages ("Coming Soon.")
3. Quit
4. Stored Messages (Part 3)

The user chooses how many messages to enter; a `for` loop collects exactly that many. Each message has:

* a random **ten-digit message ID**,
* the **message number**,
* the **recipient** cell number (validated with the Part 1 method),
* the **message text** (max 250 characters),
* an auto-generated **message hash** – first two digits of the ID, the message number and the first and last words, in capitals (e.g. `00:0:HITONIGHT`).

The user can then **Send**, **Disregard** or **Store** the message. Stored messages are written to `storedMessages.json` using the Gson library, and the total number of sent messages is shown at the end.

## Part 3 – Stored messages and report

`MessageStore` keeps every message in **parallel arrays** (IDs, hashes, recipients, texts and statuses – the same index refers to the same message). The arrays are filled from what the user enters, not hard-coded. The **Stored Messages** menu lets the user:

1. Display the sender and recipient of all messages
2. Display the longest message
3. Search for a message ID and show its recipient and message
4. Search for all messages for a particular recipient
5. Delete a message using its message hash
6. Display a full report (ID, hash, recipient, message, status)
7. Read the stored messages from the JSON file back into an array and display them

## Unit tests

JUnit 5 tests use the test data from the POE brief:

* `LoginTest` – 15 tests (username, password, cell number, registration, login)
* `MessageTest` – 11 tests (message length, recipient, hash `00:0:HITONIGHT` and looped hashes, message ID, send/disregard/store, totals)
* `MessageStoreTest` – 12 tests (sent messages array, longest message, search by ID, search by recipient, delete by hash, report, JSON read)

## Continuous integration

`.github/workflows/TestJava.yml` uses GitHub Actions to run `mvn test` on JDK 17 on every push and pull request.

## References

* Google. 2023. *Gson User Guide*. [Online]. Available at: https://github.com/google/gson/blob/main/UserGuide.md
* W3Schools. 2024. *Java Regular Expressions*. [Online]. Available at: https://www.w3schools.com/java/java_regex.asp
