package br.com.fiap.postech.adjt.checkout.dto.payment;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldDtoTest {

    @Test
    void testInstance() {
        FieldDto dto = TestUtils.buildFieldDto();
        assertEquals("789", dto.getCvv());
        assertEquals("Josefino", dto.getName());
        assertEquals("2111111111111111", dto.getNumber());
        assertEquals("12", dto.getExpiration_month());
        assertEquals("27", dto.getExpiration_year());
    }

    @Test
    void testInstanceConvertEntity() {
        FieldDto dto = TestUtils.buildFieldDto();
        Field field = dto.convertDto(dto);
        assertEquals("789", field.getCvv());
        assertEquals("Josefino", field.getName());
        assertEquals("2111111111111111", field.getNumber());
        assertEquals("12", field.getExpiration_month());
        assertEquals("27", field.getExpiration_year());
    }


}
