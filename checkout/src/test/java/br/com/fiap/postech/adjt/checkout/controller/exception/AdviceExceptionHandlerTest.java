package br.com.fiap.postech.adjt.checkout.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdviceExceptionHandlerTest {

	@Mock
	private HttpServletRequest request;

	private AdviceExceptionHandler adviceExceptionHandler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		adviceExceptionHandler = new AdviceExceptionHandler();
		when(request.getRequestURI()).thenReturn("/test-uri");
	}

	@Test
	void testHandleIllegalArgumentException() {
		IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleIllegalArgumentException(exception,
				request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid argument", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}

	@Test
	void testHandleUnsupportedOperationException() {
		UnsupportedOperationException exception = new UnsupportedOperationException("Operation not supported");

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleUnsupportedOperationException(exception,
				request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Operation not supported", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}

	@Test
	void testHandleNotFoundException() {
		NotFoundException exception = new NotFoundException("Resource not found");

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleNotFoundException(exception, request);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource not found", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}

	@Test
	void testHandleNotAuthorizedException() {
		NotAuthorizedException exception = new NotAuthorizedException("Not authorized");

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleNotAuthorizedException(exception,
				request);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("Not authorized", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}

	@Test
	void testHandleStandardErrorException() {
		StandardError standardError = StandardError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error",
				"/test-uri");
		StandardErrorException exception = new StandardErrorException(standardError);

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleStandardErrorException(exception,
				request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Internal error", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}

	@Test
	void testHandleGenericException() {
		Exception exception = new Exception("Generic error");

		ResponseEntity<StandardError> response = adviceExceptionHandler.handleGenericException(exception, request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Generic error", response.getBody().message());
		assertEquals("/test-uri", response.getBody().path());
	}
}