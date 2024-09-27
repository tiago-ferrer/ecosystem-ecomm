package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.CartItemDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
<<<<<<< HEAD
=======
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
import br.com.fiap.postech.adjt.checkout.dto.PaymentFieldsDTO;
import br.com.fiap.postech.adjt.checkout.dto.PaymentMethodDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID consumerId;

    @BeforeEach
    public void setUp() {
<<<<<<< HEAD
=======
        // Inicializa o consumerId antes de cada teste
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
        consumerId = UUID.randomUUID();
    }

    @Test
    public void testProcessCheckout() throws Exception {
        // Arrange
        CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(
                consumerId,
                100.0,
                "BRL",
                new PaymentMethodDTO("credit_card", new PaymentFieldsDTO("123456789", "12", "2025", "123", "John Doe")),
<<<<<<< HEAD
                List.of(new CartItemDTO(consumerId, 1L, 2))
=======
                List.of(new CartItemDTO(consumerId, 1L, 2)) // Exemplo de item no carrinho
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
        );

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testProcessCheckout_InvalidConsumerId() throws Exception {
        // Arrange
<<<<<<< HEAD
        UUID invalidConsumerId = UUID.randomUUID();
=======
        UUID invalidConsumerId = UUID.randomUUID(); // ID inválido
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
        CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(
                invalidConsumerId,
                100.0,
                "BRL",
                new PaymentMethodDTO("credit_card", new PaymentFieldsDTO("123456789", "12", "2025", "123", "John Doe")),
                List.of(new CartItemDTO(invalidConsumerId, 1L, 2))
<<<<<<< HEAD
=======
// Exemplo de item no carrinho
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
        );

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutRequest)))
<<<<<<< HEAD
                .andExpect(status().isBadRequest())
=======
                .andExpect(status().isBadRequest()) // Verificar se o status retornado é 400
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
                .andExpect(jsonPath("$.error").value("Invalid consumer ID"));
    }

}
