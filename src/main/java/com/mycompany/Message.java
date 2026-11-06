package com.mycompany;

import java.util.Random;

public class Message {
     private String messageID;
    private String messageHash;
    private String sender;
    private String recipient;
    private String messageText;

    private static int messageNumber = 0;
    private static int totalMessages = 0;

    // Constructor for a new message
    public Message(String recipient, String messageText) {
        this.sender = "Developer";
        this.recipient = recipient;
        this.messageText = messageText;
        messageNumber++;
        generateMessageID();
        createMessageHash();
    }

    // Constructor with a specified message ID
    public Message(String messageID, String recipient,
                   String messageText) {
        this.sender = "Developer";
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        messageNumber++;
        createMessageHash();
    }

    // Constructor used when loading a message from JSON
    public Message(String sender, String messageID,
                   String recipient, String messageText) {
        this.sender = sender;
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        messageNumber++;
        createMessageHash();
    }

    private void generateMessageID() {
        Random random = new Random();
        long number = 1000000000L
                + random.nextLong(9000000000L);
        messageID = String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("[0-9]{1,10}");
    }

    public String checkRecipientCell() {
        if (recipient != null
                && recipient.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted "
                + "or does not contain international code. "
                + "Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int length = messageText == null ? 0 : messageText.length();

        return "Message exceeds 250 characters by "
                + (length - 250) + "; please reduce the size.";
    }

    public String createMessageHash() {
        String firstWord = "";
        String lastWord = "";

        if (messageText != null && !messageText.trim().isEmpty()) {
            String[] words = messageText.trim().split("\\s+");
            firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
            lastWord = words[words.length - 1]
                    .replaceAll("[^a-zA-Z0-9]", "");
        }

        String idStart = messageID.length() >= 2
                ? messageID.substring(0, 2) : messageID;

        messageHash = (idStart + ":"
                + (messageNumber - 1) + ":"
                + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}