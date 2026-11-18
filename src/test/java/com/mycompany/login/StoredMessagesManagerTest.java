package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class StoredMessagesManagerTest {
    
    @TempDir
    Path tempDir;

    private StoredMessagesManager manager;

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() {

        manager = new StoredMessagesManager(
                tempDir.resolve("test_messages.json"));

        message1 = new Message(
                "0000000001",
                "+27834557896",
                "Did you get the cake?");

        message2 = new Message(
                "0000000002",
                "+27838884567",
                "Where are you? You are late! "
                + "I have asked you to be on time.");

        message3 = new Message(
                "0000000003",
                "+27834484567",
                "Yohoooo, I am at your gate.");

        message4 = new Message(
                "0838884567",
                "+27834557896",
                "It is dinner time !");

        message5 = new Message(
                "0000000005",
                "+27838884567",
                "Ok, I am leaving without you.");

        manager.recordMessage(message1, 1);
        manager.recordMessage(message2, 3);
        manager.recordMessage(message3, 2);
        manager.recordMessage(message4, 1);
        manager.recordMessage(message5, 3);
    }

    @Test
    public void testSentMessagesArray() {

        assertEquals(2, manager.getSentCount());

        String sent = manager.displaySentMessages();

        assertTrue(sent.contains("Did you get the cake?"));
        assertTrue(sent.contains("It is dinner time !"));
    }

    @Test
    public void testDisregardedMessagesArray() {

        assertEquals(1, manager.getDisregardedCount());

        assertEquals(
                "Yohoooo, I am at your gate.",
                manager.getDisregardedMessages()[0].getMessageText());
    }

    @Test
    public void testStoredMessagesArrayAndJSON() {

        assertEquals(2, manager.getStoredCount());

        Path jsonFile = tempDir.resolve("test_messages.json");

        assertTrue(java.nio.file.Files.exists(jsonFile));

        try {
            String json = java.nio.file.Files.readString(jsonFile);

            assertTrue(json.trim().startsWith("["));
            assertTrue(json.trim().endsWith("]"));
            assertTrue(json.contains("Where are you?"));
            assertTrue(json.contains("Ok, I am leaving without you."));

        } catch (Exception e) {
            fail("Could not read the JSON file.");
        }
    }

    @Test
    public void testDisplayLongestStoredMessage() {

        assertEquals(
                "Where are you? You are late! "
                + "I have asked you to be on time.",
                manager.displayLongestStoredMessage());
    }

    @Test
    public void testSearchMessageByID() {

        String result = manager.searchMessageByID("0838884567");

        assertTrue(result.contains("It is dinner time !"));
        assertTrue(result.contains("+27834557896"));
    }

    @Test
    public void testSearchMessagesByRecipient() {

        String result = manager.searchMessagesByRecipient(
                "+27838884567");

        assertTrue(result.contains("Where are you?"));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {

        String hash = message2.getMessageHash();

        String result = manager.deleteMessageByHash(hash);

        assertTrue(result.contains("successfully deleted."));
        assertEquals(1, manager.getStoredCount());

        assertFalse(manager.displayReport().contains(
                "Where are you?"));
    }

    @Test
    public void testDisplayReport() {

        String report = manager.displayReport();

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("Where are you?"));
        assertTrue(report.contains("It is dinner time !"));
    }
 }