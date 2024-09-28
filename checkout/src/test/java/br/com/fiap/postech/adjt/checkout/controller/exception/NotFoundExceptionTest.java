package br.com.fiap.postech.adjt.checkout.controller.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

	@Test
	void testNotFoundException() {
		NotFoundException exception = new NotFoundException("Resource not found");

		assertEquals("Resource not found", exception.getMessage());
	}
}