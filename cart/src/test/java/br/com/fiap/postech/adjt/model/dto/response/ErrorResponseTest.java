package br.com.fiap.postech.adjt.model.dto.response;

import br.com.fiap.postech.adjt.cart.model.dto.response.ErrorResponse;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void errorResponse_validData_ShouldSetSuccessfully() {
        ErrorResponse errorResponse = new ErrorResponse("Test Error");

        assertEquals("Test Error",errorResponse.error());
    }
}
