package br.com.fiap.postech.adjt.checkout.dataprovider.database.gateway.entity.enums;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentStatusTest {

    @Test
    public void testEnumValues() {
        // Verifica se todas as constantes estão presentes
        PaymentStatus[] expectedValues = {PaymentStatus.PENDING, PaymentStatus.APPROVED, PaymentStatus.DECLINED};
        assertArrayEquals(expectedValues, PaymentStatus.values());
    }

    @Test
    public void testEnumNames() {
        // Verifica os nomes das constantes
        assertEquals("PENDING", PaymentStatus.PENDING.name());
        assertEquals("APPROVED", PaymentStatus.APPROVED.name());
        assertEquals("DECLINED", PaymentStatus.DECLINED.name());
    }

    @Test
    public void testEnumOrdinals() {
        // Verifica a ordem das constantes
        assertEquals(0, PaymentStatus.PENDING.ordinal());
        assertEquals(1, PaymentStatus.APPROVED.ordinal());
        assertEquals(2, PaymentStatus.DECLINED.ordinal());
    }
}
