package com.mycompany.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class StoredMessagesManager {
    
  private Message[] sentMessages = new Message[0];
    private Message[] disregardedMessages = new Message[0];
    private Message[] storedMessages = new Message[0];

    private String[] messageHashes = new String[0];
    private String[] messageIDs = new String[0];

    private final Path jsonFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public StoredMessagesManager() {
        this(Path.of("stored_messages.json"));
    }

    // Allows tests to use a separate temporary JSON file
    public StoredMessagesManager(Path jsonFile) {
        this.jsonFile = jsonFile;
        loadStoredMessages();
    }

    public void recordMessage(Message message, int choice) {

        if (choice == 1) {
            sentMessages = addMessage(sentMessages, message);

        } else if (choice == 2) {
            disregardedMessages =
                    addMessage(disregardedMessages, message);

        } else if (choice == 3) {
            storedMessages = addMessage(storedMessages, message);
            saveStoredMessages();
        }

        updateParallelArrays();
    }

    private Message[] addMessage(Message[] oldArray, Message message) {
        Message[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
        newArray[oldArray.length] = message;
        return newArray;
    }

    private void updateParallelArrays() {

        int total = sentMessages.length
                + disregardedMessages.length
                + storedMessages.length;

        messageHashes = new String[total];
        messageIDs = new String[total];

        int index = 0;

        for (Message message : sentMessages) {
            messageHashes[index] = message.getMessageHash();
            messageIDs[index] = message.getMessageID();
            index++;
        }

        for (Message message : disregardedMessages) {
            messageHashes[index] = message.getMessageHash();
            messageIDs[index] = message.getMessageID();
            index++;
        }

        for (Message message : storedMessages) {
            messageHashes[index] = message.getMessageHash();
            messageIDs[index] = message.getMessageID();
            index++;
        }
    }

    public String displaySentMessages() {
        StringBuilder result = new StringBuilder();

        for (Message message : sentMessages) {
            result.append(message.getMessageText()).append("\n");
        }

        return result.toString();
    }

    public String displaySenderAndRecipient() {
        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            result.append("Sender: ")
                    .append(message.getSender())
                    .append("\nRecipient: ")
                    .append(message.getRecipient())
                    .append("\n\n");
        }

        return result.toString();
    }

    public String displayLongestStoredMessage() {

        if (storedMessages.length == 0) {
            return "There are no stored messages.";
        }

        Message longest = storedMessages[0];

        for (Message message : storedMessages) {
            if (message.getMessageText().length()
                    > longest.getMessageText().length()) {
                longest = message;
            }
        }

        return longest.getMessageText();
    }

    public String searchMessageByID(String id) {

        for (Message message : allMessages()) {
            if (message.getMessageID().equals(id)) {
                return "Recipient: " + message.getRecipient()
                        + "\nMessage: " + message.getMessageText();
            }
        }

        return "Message ID not found.";
    }

    public String searchMessagesByRecipient(String recipient) {

        StringBuilder result = new StringBuilder();

        for (Message message : allMessages()) {
            if (message.getRecipient().equals(recipient)) {
                result.append(message.getMessageText()).append("\n");
            }
        }

        if (result.length() == 0) {
            return "No messages found for this recipient.";
        }

        return result.toString();
    }

    public String deleteMessageByHash(String hash) {

        for (int i = 0; i < storedMessages.length; i++) {

            if (storedMessages[i].getMessageHash().equals(hash)) {

                String deletedText = storedMessages[i].getMessageText();

                Message[] newArray =
                        new Message[storedMessages.length - 1];

                int newIndex = 0;

                for (int j = 0; j < storedMessages.length; j++) {
                    if (j != i) {
                        newArray[newIndex++] = storedMessages[j];
                    }
                }

                storedMessages = newArray;
                updateParallelArrays();
                saveStoredMessages();

                return "Message: \"" + deletedText
                        + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    public String displayReport() {

        StringBuilder report = new StringBuilder();

        report.append("===== SENT MESSAGES =====\n");

        for (Message message : sentMessages) {
            appendMessageDetails(report, message);
        }

        report.append("\n===== STORED MESSAGES =====\n");

        for (Message message : storedMessages) {
            appendMessageDetails(report, message);
        }

        report.append("\n===== DISREGARDED MESSAGES =====\n");

        for (Message message : disregardedMessages) {
            appendMessageDetails(report, message);
        }

        return report.toString();
    }

    private void appendMessageDetails(StringBuilder report,
                                      Message message) {

        report.append("Message ID: ")
                .append(message.getMessageID())
                .append("\nMessage Hash: ")
                .append(message.getMessageHash())
                .append("\nSender: ")
                .append(message.getSender())
                .append("\nRecipient: ")
                .append(message.getRecipient())
                .append("\nMessage: ")
                .append(message.getMessageText())
                .append("\n\n");
    }

    private Message[] allMessages() {

        Message[] all = new Message[
                sentMessages.length
                + disregardedMessages.length
                + storedMessages.length];

        int index = 0;

        for (Message message : sentMessages) {
            all[index++] = message;
        }

        for (Message message : disregardedMessages) {
            all[index++] = message;
        }

        for (Message message : storedMessages) {
            all[index++] = message;
        }

        return all;
    }

    private void saveStoredMessages() {

        try {
            Files.writeString(jsonFile, gson.toJson(storedMessages));
        } catch (IOException e) {
            System.out.println("Could not save JSON file: "
                    + e.getMessage());
        }
    }

    private void loadStoredMessages() {

        try {
            if (Files.exists(jsonFile)) {

                String json = Files.readString(jsonFile);

                if (!json.isBlank()) {
                    Message[] loaded = gson.fromJson(
                            json, Message[].class);

                    if (loaded != null) {
                        storedMessages = loaded;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load stored messages: "
                    + e.getMessage());
        }

        updateParallelArrays();
    }

    public int getSentCount() {
        return sentMessages.length;
    }

    public int getDisregardedCount() {
        return disregardedMessages.length;
    }

    public int getStoredCount() {
        return storedMessages.length;
    }

    public Message[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public Message[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages,
                disregardedMessages.length);
    }

    public Message[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageIDs.length);
    }    
}
