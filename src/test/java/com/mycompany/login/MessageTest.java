package com.mycompany.login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    
   @Test
    public void testValidMessageLength() {
        String text = "A".repeat(250);

        Message message = new Message(
                "1234567890", "+27718693002", text);

        assertEquals("Message ready to send.",
                message.checkMessageLength());
    }

    @Test
    public void testInvalidMessageLength() {
        String text = "A".repeat(251);

        Message message = new Message(
                "1234567890", "+27718693002", text);

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength());
    }

    @Test
    public void testValidRecipientCell() {
        Message message = new Message(
                "1234567890", "+27718693002", "Hello");

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipientCell() {
        Message message = new Message(
                "1234567890", "08575975889", "Hello");

        assertTrue(message.checkRecipientCell()
                .startsWith("Cell phone number is incorrectly formatted"));
    }

    @Test
    public void testValidMessageID() {
        Message message = new Message(
                "1234567890", "+27718693002", "Hello");

        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageHash() {
        Message message = new Message(
                "1234567890",
                "+27718693002",
                "Hi Mike, can you join us tonight?");

        String hash = message.createMessageHash();

        assertTrue(hash.startsWith("12:"));
        assertTrue(hash.endsWith("HITONIGHT"));
        assertEquals(hash.toUpperCase(), hash);
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(
                "+27718693002", "Hello");

        assertEquals("Message successfully sent.",
                message.SentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        Message message = new Message(
                "+27718693002", "Hello");

        assertEquals("Press 0 to delete the message.",
                message.SentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        Message message = new Message(
                "+27718693002", "Hello");

        assertEquals("Message successfully stored.",
                message.SentMessage(3));
    }

    @Test
    public void testPrintMessages() {
        Message message = new Message(
                "1234567890",
                "+27718693002",
                "Hello there");

        String result = message.printMessages();

        assertTrue(result.contains("1234567890"));
        assertTrue(result.contains("+27718693002"));
        assertTrue(result.contains("Hello there"));
    }
} 