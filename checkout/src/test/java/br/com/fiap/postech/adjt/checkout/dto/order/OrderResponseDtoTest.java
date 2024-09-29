package br.com.fiap.postech.adjt.checkout.dto.order;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class OrderResponseDtoTest {

    @Test
    void testInstance() {
        OrderResponseDto dto = TestUtils.buildOrderResponseDto();
        assertEquals(1000, dto.getValue());
        assertEquals(PaymentStatus.pending, dto.getPaymentStatus());
        assertEquals(TestUtils.genUUID(), dto.getOrderId());
        assertInstanceOf(List.class, dto.getItems());
    }

    @Test
    void testConvert() {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setItems(List.of(TestUtils.buildItemDto()));
        dto = dto.convertToDto(TestUtils.buildOrder());
        assertNotNull(dto);
    }

}
