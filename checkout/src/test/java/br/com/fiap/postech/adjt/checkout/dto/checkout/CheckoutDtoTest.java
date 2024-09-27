package br.com.fiap.postech.adjt.checkout.dto.checkout;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentDto;
import br.com.fiap.postech.adjt.checkout.entity.checkout.Checkout;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CheckoutDtoTest {

    @Test
    void testInstance() {
        CheckoutDto dto = TestUtils.buildCheckoutDto();
        assertEquals(1000, dto.getAmount());
        assertEquals("BRL", dto.getCurrency());
        assertEquals("br_credit_card", dto.getPaymentType());
        assertEquals(TestUtils.genUUID().toString(), dto.getConsumerId());
        assertInstanceOf(PaymentDto.class, dto.getPayment_method());
    }

    @Test
    void testConvertTo() {
        CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto = checkoutDto.convertToDto(TestUtils.buildCheckout());
        assertInstanceOf(CheckoutDto.class, checkoutDto);
    }

    @Test
    void testConvertFromDto() {
        CheckoutDto checkoutDto = new CheckoutDto();
        Checkout checkout = checkoutDto.convertFromDto(TestUtils.buildCheckoutDto());
        assertInstanceOf(Checkout.class, checkout);
    }

}
