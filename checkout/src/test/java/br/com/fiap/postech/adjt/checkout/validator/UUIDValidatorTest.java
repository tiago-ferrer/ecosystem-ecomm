package br.com.fiap.postech.adjt.checkout.validator;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UUIDValidatorTest {

    @Test
    void shouldCreateValidUUID() {
        UUIDValidator validator = new UUIDValidator();
        assertThat(validator).isInstanceOf(UUIDValidator.class);
    }

    @Test
    void shouldReturnTrueWhenUUIDIsValid() {
        Boolean isValid = UUIDValidator.isValidUUID(UUID.randomUUID().toString());
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUUIDIsInvalid() {
        Boolean isValid = UUIDValidator.isValidUUID("123");
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalseWhenUUIDIsNull() {
        Boolean isValid = UUIDValidator.isValidUUID(null);
        assertThat(isValid).isFalse();
    }

}
