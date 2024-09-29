package br.com.fiap.postech.adjt.checkout.clients;

import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentClientTest {

    @Mock
    private PaymentClient paymentClient; // Mock the PaymentClient interface

    @Mock
    private PaymentRequest paymentRequest;

    @Mock
    private CheckoutResponse checkoutResponse;

    @BeforeEach
    void setUp() {
        // No need for @InjectMocks since PaymentClient is an interface
    }

    @Test
    void testProcessPayment_Success() {
        // Arrange
        String apiKey = "test-api-key";
        when(paymentClient.processPayment(apiKey, paymentRequest)).thenReturn(checkoutResponse);

        // Act
        CheckoutResponse response = paymentClient.processPayment(apiKey, paymentRequest);

        // Assert
        assertNotNull(response);
        verify(paymentClient).processPayment(apiKey, paymentRequest); // Verify the method was called
    }

    @Test
    void testProcessPayment_Failure() {
        // Arrange
        String apiKey = "test-api-key";
        when(paymentClient.processPayment(apiKey, paymentRequest)).thenThrow(new RuntimeException("Payment failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> paymentClient.processPayment(apiKey, paymentRequest));
        verify(paymentClient).processPayment(apiKey, paymentRequest); // Verify the method was called
    }
}