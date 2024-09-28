package br.com.fiap.postech.adjt.checkout.controller.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class StandardErrorExceptionTest {

	@Test
	void testStandardErrorException() {
		StandardError standardError = StandardError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error",
				"/test-uri");
		StandardErrorException exception = new StandardErrorException(standardError);

		assertEquals(standardError, exception.getStandardError());
	}
}