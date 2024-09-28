package br.com.fiap.postech.adjt.checkout.controller.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotAuthorizedExceptionTest {

	@Test
	void testNotAuthorizedException() {
		NotAuthorizedException exception = new NotAuthorizedException("Not authorized");

		assertEquals("Not authorized", exception.getMessage());
	}
}