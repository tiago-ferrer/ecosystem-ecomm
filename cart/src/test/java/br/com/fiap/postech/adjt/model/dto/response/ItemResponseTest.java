package br.com.fiap.postech.adjt.model.dto.response;

import br.com.fiap.postech.adjt.cart.model.dto.response.ItemResponse;
import org.junit.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemResponseTest {

    @Test
    public void ItemResponseTest_validData_ShouldSetSuccessfully() {
        ItemResponse itemResponse = new ItemResponse(1L,"chocolate", BigDecimal.ONE,"dark chocolate","candy","candy.img");

        assertEquals(1L,itemResponse.id());
        assertEquals("chocolate",itemResponse.title());
        assertEquals(BigDecimal.ONE,itemResponse.price());
        assertEquals("dark chocolate",itemResponse.description());
        assertEquals("candy",itemResponse.category());
        assertEquals("candy.img",itemResponse.image());
    }
}
