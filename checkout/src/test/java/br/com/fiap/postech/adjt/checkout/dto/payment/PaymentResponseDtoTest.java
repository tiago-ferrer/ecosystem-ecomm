package br.com.fiap.postech.adjt.checkout.dto.payment;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentResponseDtoTest {

    @Test
    void testInstance() {
        PaymentResponseDto dto = TestUtils.buildPaymentResponseDto();
        assertNotNull(dto.getStatus());
        assertNotNull(dto.getPaymentId());
    }

}
