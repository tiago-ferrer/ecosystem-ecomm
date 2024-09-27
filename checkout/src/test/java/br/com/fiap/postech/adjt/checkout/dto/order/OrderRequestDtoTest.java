package br.com.fiap.postech.adjt.checkout.dto.order;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderRequestDtoTest {

    @Test
    void testInstance() {
        OrderRequestDto dto = TestUtils.buildOrderRequestDto();
        assertEquals(TestUtils.genUUID().toString(), dto.getOrderId());
        assertEquals(TestUtils.genUUID().toString(), dto.getConsumerId());
        assertEquals(1000, dto.getValue());
        assertEquals(PaymentStatus.pending, dto.getPaymentStatus());
        assertEquals("paymentType", dto.getPaymentType());
    }

    @Test
    void testInstanceAnyArgumentsConstructor() {
        OrderRequestDto dto = new OrderRequestDto(TestUtils.genUUID().toString(),
                "paymentType", 1000, PaymentStatus.pending);
        assertEquals(TestUtils.genUUID().toString(), dto.getConsumerId());
        assertEquals(1000, dto.getValue());
        assertEquals(PaymentStatus.pending, dto.getPaymentStatus());
        assertEquals("paymentType", dto.getPaymentType());
    }

}
