package br.com.fiap.postech.adjt.checkout.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.MessageResponse;

@ExtendWith(MockitoExtension.class)
public class CartClientTest {

    @Mock
    private CartClient cartClient;  

    @Mock
    private FindCartByCustomerIdRequest findCartByCustomerIdRequest;

    @Mock
    private ClearCartRequest clearCartRequest;

    @Mock
    private CartResponse cartResponse;

    @Mock
    private MessageResponse messageResponse;

    @Test
    void testConsult() {
        // Arrange
        when(cartClient.consult(findCartByCustomerIdRequest)).thenReturn(cartResponse);

        // Act
        CartResponse response = cartClient.consult(findCartByCustomerIdRequest);

        // Assert
        assertNotNull(response);
        verify(cartClient).consult(findCartByCustomerIdRequest);  // Verify the method was called with the correct request
    }

    @Test
    void testClear() {
        // Arrange
        when(cartClient.clear(clearCartRequest)).thenReturn(messageResponse);

        // Act
        MessageResponse response = cartClient.clear(clearCartRequest);

        // Assert
        assertNotNull(response);
        verify(cartClient).clear(clearCartRequest);  // Verify the method was called with the correct request
    }
}