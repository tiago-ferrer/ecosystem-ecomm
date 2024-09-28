package br.com.fiap.postech.adjt.checkout.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void shouldCreateCard_WithCorrectValues() {
        // Arrange
        UUID expectedId = UUID.randomUUID();
        UUID expectedConsumerId = UUID.randomUUID();
        String expectedNumber = "1234567812345678";
        String expectedExpirationMonth = "12";
        String expectedExpirationYear = "2025";
        String expectedCvv = "123";
        String expectedName = "John Doe";

        // Act
        Card card = new Card(expectedId, expectedConsumerId, expectedNumber, expectedExpirationMonth, expectedExpirationYear, expectedCvv, expectedName);

        // Assert
        assertEquals(expectedId, card.getId(), "Id should match the expected value");
        assertEquals(expectedConsumerId, card.getConsumerId(), "ConsumerId should match the expected value");
        assertEquals(expectedNumber, card.getNumber(), "Number should match the expected value");
        assertEquals(expectedExpirationMonth, card.getExpiration_month(), "Expiration month should match the expected value");
        assertEquals(expectedExpirationYear, card.getExpiration_year(), "Expiration year should match the expected value");
        assertEquals(expectedCvv, card.getCvv(), "CVV should match the expected value");
        assertEquals(expectedName, card.getName(), "Name should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualCards() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID consumerId = UUID.randomUUID();
        String number = "1234567812345678";
        String expirationMonth = "12";
        String expirationYear = "2025";
        String cvv = "123";
        String name = "John Doe";

        Card card1 = new Card(id, consumerId, number, expirationMonth, expirationYear, cvv, name);
        Card card2 = new Card(id, consumerId, number, expirationMonth, expirationYear, cvv, name);

        // Act & Assert
        assertEquals(card1, card2, "Cards with the same values should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentCards() {
        // Arrange
        Card card1 = new Card(UUID.randomUUID(), UUID.randomUUID(), "1234567812345678", "12", "2025", "123", "John Doe");
        Card card2 = new Card(UUID.randomUUID(), UUID.randomUUID(), "8765432187654321", "11", "2024", "321", "Jane Doe");

        // Act & Assert
        assertNotEquals(card1, card2, "Cards with different values should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID consumerId = UUID.randomUUID();
        String number = "1234567812345678";
        String expirationMonth = "12";
        String expirationYear = "2025";
        String cvv = "123";
        String name = "John Doe";

        Card card1 = new Card(id, consumerId, number, expirationMonth, expirationYear, cvv, name);
        Card card2 = new Card(id, consumerId, number, expirationMonth, expirationYear, cvv, name);

        // Act & Assert
        assertEquals(card1.hashCode(), card2.hashCode(), "Hash codes should be equal for equal Cards");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID consumerId = UUID.randomUUID();
        String number = "1234567812345678";
        String expirationMonth = "12";
        String expirationYear = "2025";
        String cvv = "123";
        String name = "John Doe";

        Card card = new Card(id, consumerId, number, expirationMonth, expirationYear, cvv, name);
        String expectedToString = "Card(id=" + id + ", consumerId=" + consumerId + ", number=1234567812345678, expiration_month=12, expiration_year=2025, cvv=123, name=John Doe)";

        // Act & Assert
        assertEquals(expectedToString, card.toString(), "toString() should return the correct string representation");
    }
}