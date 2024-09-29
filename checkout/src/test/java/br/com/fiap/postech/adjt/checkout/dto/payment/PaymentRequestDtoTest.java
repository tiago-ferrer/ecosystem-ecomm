package br.com.fiap.postech.adjt.checkout.dto.payment;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;

import static br.com.fiap.postech.adjt.checkout.TestUtils.buildPaymentRequestDto;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestDtoTest {

    @Test
    void testInstance() {
        PaymentRequestDto dto = buildPaymentRequestDto();
        assertEquals(1000, dto.getAmount());
        assertInstanceOf(PaymentDto.class, dto.getPayment_method());
        assertEquals(TestUtils.genUUID(), dto.getOrderId());
        assertNotNull(dto.toString());
    }

}
