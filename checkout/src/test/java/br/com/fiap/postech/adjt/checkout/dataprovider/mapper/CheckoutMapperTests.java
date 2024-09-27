package br.com.fiap.postech.adjt.checkout.dataprovider.mapper;

import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentCheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.mappers.CheckoutMapper;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutMapperTest {

    private CheckoutMapper checkoutMapper;

    @BeforeEach
    void setUp() {
        checkoutMapper = Mappers.getMapper(CheckoutMapper.class);
    }

    @Test
    void testToCheckoutModel() {
        // Given
        FieldDTO fieldDTO = new FieldDTO("4111111111111111", "12", "25", "123", "John Doe");
        PaymentDTO paymentMethod = new PaymentDTO("credit_card", fieldDTO);
        CheckoutDTO checkoutDTO = new CheckoutDTO("consumer-123", 150.0, "USD", paymentMethod);

        // When
        CheckoutModel checkoutModel = checkoutMapper.toCheckoutModel(checkoutDTO);

        // Then
        assertNotNull(checkoutModel, "CheckoutModel should not be null");
        assertEquals(checkoutDTO.consumerId(), checkoutModel.getConsumerId(), "Consumer IDs should match");
        assertEquals(checkoutDTO.amount(), checkoutModel.getAmount(), "Total amounts should match");
    }

    @Test
    void testToCheckoutModel_WithNullFields() {
        // Given
        PaymentDTO paymentMethod = new PaymentDTO("credit_card", new FieldDTO(null, null, null, null, null)); // Passando FieldDTO com campos nulos
        CheckoutDTO checkoutDTO = new CheckoutDTO(null, null, null, paymentMethod);

        // When
        CheckoutModel checkoutModel = checkoutMapper.toCheckoutModel(checkoutDTO);

        // Then
        assertNotNull(checkoutModel, "CheckoutModel should not be null");
        assertNull(checkoutModel.getConsumerId(), "Consumer ID should be null");
        assertNull(checkoutModel.getAmount(), "Total amount should be null");
    }

    @Test
    void testToPaymentCheckoutDTO_WithNullFields() {
        // Given
        CheckoutModel checkoutModel = new CheckoutModel(null, null, null, null, null);

        // When
        PaymentCheckoutDTO paymentCheckoutDTO = checkoutMapper.toPaymentCheckoutDTO(checkoutModel);

        // Then
        assertNotNull(paymentCheckoutDTO, "PaymentCheckoutDTO should not be null");
        assertNull(paymentCheckoutDTO.payment_method(), "Payment method should be null");
        assertNull(paymentCheckoutDTO.amount(), "Total amount should be null");
    }
}
