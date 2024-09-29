package br.com.fiap.postech.adjt.checkout.dto.cart;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartDtoTest {

    @Test
    void testConvertDto() {
        CartDto cartDto = TestUtils.buildCartDto();
        cartDto.convertToCartDto(TestUtils.buildCart());
        assertEquals(1, cartDto.getItems().size());
    }

    @Test
    void testConvertResponseEntityToCartDto() {
        CartDto cartDto = new CartDto();
        cartDto = cartDto.convertResponseEntityToCartDto(TestUtils.buildMapCartItems());
        assertEquals(1, cartDto.getItems().size());
    }

}
